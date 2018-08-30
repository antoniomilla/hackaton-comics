package utilities;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.spi.EventSource;
import org.hibernate.jdbc.Work;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.mapping.Table;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.beans.Transient;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.spi.PersistenceProvider;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import domain.Actor;
import domain.DomainEntity;
import security.UserAccount;
import utilities.internal.EclipseConsole;

public class ExportPopulateDatabase {
	private static final String XMLNS_ROOT = "http://www.springframework.org/schema/beans";
	private static final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	private static final String SCHEMA_LOCATION = "http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd";

	private static final SimpleDateFormat XML_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
	private static final SimpleDateFormat XML_TIME_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	PersistenceProvider	persistenceProvider;
	EntityManagerFactory entityManagerFactory;
	EntityManager entityManager;

	Document xmlDoc;
	Element xmlRoot;
	List<DomainEntity> entities;
	Map<Class<?>, Integer> entityNumMap;
	Map<DomainEntity, String> entityIdMap;
	Map<String, DomainEntity> idEntityMap;
	Configuration configuration;


	public static void main(String[] args)
	{
		try {
			(new ExportPopulateDatabase()).run();
			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	@SuppressWarnings("unchecked")
	public void run() throws Exception
	{
		EclipseConsole.fix();
		LogManager.getLogger("org.hibernate").setLevel(Level.ERROR);
		LogManager.getLogger("cz.jirutka.validator").setLevel(Level.ERROR);

		initializeXml();
		initializeDatabase();

		// Pull entities from DB.
		entities = new ArrayList<>();
		entityNumMap = new HashMap<>();
		for (Class<?> klazz : findAllDomainClasses()) {
			Assert.isAssignable(DomainEntity.class, klazz);
			for (DomainEntity e: getAllInstancesOfEntity((Class<? extends DomainEntity>) klazz)) {
				if (!entities.contains(e)) entities.add(e);
			}
			entityNumMap.put(klazz, 1);
		}

		// FULLY pull entities from the DB.
		for (DomainEntity entity : entities) {
			unlazifyRelationships(entity, entity.getClass());
		}

		// Validate all entities.
		for (DomainEntity entity: entities) {
			ValidationUtils.validateBean(entity);
		}

		// Detach all entities.
		entityManager.clear();

		// Clean all entities.
		for (DomainEntity entity: entities) {
			entity.setId(0);
			entity.setVersion(0);
		}

		// Do the rest in a transaction, since we don't want to touch the DB for real.
		try {
			entityManager.getTransaction().begin();

			deleteAllEntitiesFromDb();

			// Since PopulateDatabase's topological sort doesn't always work, do it ourselves here.
			// We use the same trial-and-error method but this one actually works fine.
			// If entities are already sorted, PopulateDatabase's sort will do nothing.
			entities = sortEntitiesTopologicallyBySaving(entities);

			// Assign a bean ID to all of them.
			entityIdMap = new HashMap<>();
			idEntityMap = new HashMap<>();
			for (DomainEntity entity : entities) {
				int num = entityNumMap.get(entity.getClass());

				String beanId = entity.getClass().getSimpleName();
				beanId = beanId.substring(0, 1).toLowerCase() + beanId.substring(1);

				if (entity instanceof UserAccount) {
					beanId += "_" + ((UserAccount) entity).getUsername();
				} else if (entity instanceof Actor) {
					beanId += "_" + ((Actor) entity).getUserAccount().getUsername();
				} else {
					beanId += "_" + num;
				}

				entityNumMap.put(entity.getClass(), num + 1);

				entityIdMap.put(entity, beanId);
				idEntityMap.put(beanId, entity);
			}

			// Serialize all entities.
			for (DomainEntity entity : entities) {
				xmlRoot.appendChild(serializeBean(entityIdMap.get(entity), entity));
			}
		} finally {
			entityManager.getTransaction().rollback();
		}

		printDocument(System.out);
	}

	@SuppressWarnings("unchecked")
	private void unlazifyRelationships(DomainEntity entity, Class<? extends DomainEntity> klazz)
	{
		Hibernate.initialize(entity);

		for (Method method : klazz.getDeclaredMethods()) {
			if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
				if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
					if (method.isAnnotationPresent(ManyToMany.class) || method.isAnnotationPresent(OneToMany.class) || method.isAnnotationPresent(ManyToOne.class) || method.isAnnotationPresent(OneToOne.class) || method.isAnnotationPresent(ElementCollection.class)) {
						try {
							Hibernate.initialize(method.invoke(entity));
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
		}

		if (!klazz.equals(DomainEntity.class)) {
			unlazifyRelationships(entity, (Class<? extends DomainEntity>) klazz.getSuperclass());
		}
	}

	private List<DomainEntity> sortEntitiesTopologicallyBySaving(List<DomainEntity> entities) throws Exception
	{
		List<DomainEntity> unsaved = new ArrayList<>(entities);
		List<DomainEntity> result = new ArrayList<>();

		// Now, go through all of the entities, and attempt to save them.
		// If it cannot be saved, push it to the end of the queue.
		// If we reach the point where the unsaved list is empty, we're done.
		// If the unsaved list doesnt change after a run, we cannot find a solution and throw.
		while (true) {
			int lastUnsavedSize = unsaved.size();

			List<DomainEntity> unsaved2 = new ArrayList<>();
			for (DomainEntity entity: unsaved) {
				try {
					entityManager.persist(entity);
					result.add(entity);
				} catch (Exception oops) {
					unsaved2.add(entity);
					entity.setId(0);
					entity.setVersion(0);
					entityManager.detach(entity);
					((EventSource) entityManager.unwrap(Session.class)).getActionQueue().clear();
				}
			}
			unsaved = unsaved2;

			if (unsaved.size() == 0) {
				return result;
			}
			if (unsaved.size() == lastUnsavedSize) {
				throw new RuntimeException("Cannot find a topological order");
			}
		}
	}

	private void deleteAllEntitiesFromDb() throws SQLException
	{
		// First, delete everyhing from the DB.
		Iterator<Table> iter = configuration.getTableMappings();
		while (iter.hasNext()) {
			final Table table = iter.next();
			entityManager.unwrap(Session.class).doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException
				{
					Statement stmt = null;
					try {
						stmt = connection.createStatement();
						stmt.execute("DELETE FROM " + table.getQuotedName());
					} finally {
						try {
							if (stmt != null) stmt.close();
						} catch(Exception ignored) {}
					}
				}
			});
		}
	}

	private Configuration buildConfiguration() {
		Configuration result;
		Metamodel metamodel;
		Collection<EntityType<?>> entities;
		Collection<EmbeddableType<?>> embeddables;

		result = new Configuration();
		metamodel = this.entityManagerFactory.getMetamodel();

		entities = metamodel.getEntities();
		for (final EntityType<?> entity : entities)
			result.addAnnotatedClass(entity.getJavaType());

		embeddables = metamodel.getEmbeddables();
		for (final EmbeddableType<?> embeddable : embeddables)
			result.addAnnotatedClass(embeddable.getJavaType());

		return result;
	}

	public void printDocument(OutputStream out) throws IOException, TransformerException
	{
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		transformer.transform(new DOMSource(xmlDoc),
							  new StreamResult(new OutputStreamWriter(out, "UTF-8")));
	}

	public static List<Field> getAllFields(Class<?> type)
	{
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> c = type; c != null; c = c.getSuperclass()) {
			fields.addAll(Arrays.asList(c.getDeclaredFields()));
		}
		return fields;
	}

	@SuppressWarnings("unchecked")
	private Element serializeBean(String id, Object bean) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Element beanElem = xmlDoc.createElement("bean");
		if (id != null) beanElem.setAttribute("id", id);
		beanElem.setAttribute("class", bean.getClass().getName());
		for (Field field: getAllFields(bean.getClass())) {
			if (Modifier.isStatic(field.getModifiers())) continue;
			if (field.getAnnotation(Transient.class) != null) continue;
			if (field.getName().equals("id")) continue;
			if (field.getName().equals("version")) continue;

			Method getter = bean.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
			if (getter == null) continue;
			if (getter.getAnnotation(Transient.class) != null) continue;

			Element property = xmlDoc.createElement("property");
			beanElem.appendChild(property);
			property.setAttribute("name", field.getName());

			Object value = getter.invoke(bean);

			Class<?> fieldType = field.getType();
			if (fieldType != getter.getReturnType()) {
				Assert.isTrue(false);
			}

			if (value == null) {
				property.appendChild(xmlDoc.createElement("null"));
				continue;
			}

			boolean isEntity = false;
			isEntity = isEntity || getter.getAnnotation(OneToOne.class) != null;
			isEntity = isEntity || getter.getAnnotation(ManyToOne.class) != null;

			boolean isCollection = Iterable.class.isAssignableFrom(fieldType);

			boolean isEnum = getter.getAnnotation(Enumerated.class) != null;

			boolean isEntityCollection = false;
			isEntityCollection = isEntityCollection || getter.getAnnotation(OneToMany.class) != null;
			isEntityCollection = isEntityCollection || getter.getAnnotation(ManyToMany.class) != null;
			isEntityCollection = isCollection && isEntityCollection;

			boolean isDatatype = false;
			isDatatype = isDatatype || getter.getReturnType().getAnnotation(Embeddable.class) != null;

			boolean isPrimitive = !isEntity && !isEnum && !isEntityCollection && !isDatatype && !isCollection;

			String collectionName = null;
			if (isCollection) {
				if (Set.class.isAssignableFrom(fieldType)) {
					collectionName = "set";
				} else {
					collectionName = "list";
				}
			}

			if (isPrimitive) {
				String primitiveValue = serializePrimitive(getter, value);
				if (primitiveValue == null) throw new IllegalStateException();
				property.setAttribute("value", primitiveValue);
			} else if (isEnum) {
				property.setAttribute("value", value.toString());
			} else if (isEntity) {
				Element ref = xmlDoc.createElement("ref");
				property.appendChild(ref);
				ref.setAttribute("bean", entityIdMap.get(value));
			} else if (isEntityCollection) {
				Element list = xmlDoc.createElement(collectionName);
				property.appendChild(list);
				for (DomainEntity child: (Iterable<DomainEntity>) value) {
					Element ref = xmlDoc.createElement("ref");
					list.appendChild(ref);
					ref.setAttribute("bean", entityIdMap.get(child));
				}
			} else if (isDatatype) {
				property.appendChild(serializeBean(null, value));
			} else if (isCollection) {
				Element list = xmlDoc.createElement(collectionName);
				property.appendChild(list);
				for (Object child: (Iterable) value) {
					list.setAttribute("value-type", child.getClass().getName());
					String primitiveValue = serializePrimitive(getter, child);
					if (primitiveValue != null) {
						Element valueElem = xmlDoc.createElement("value");
						list.appendChild(valueElem);
						valueElem.setTextContent(primitiveValue);
					} else {
						list.appendChild(serializeBean(null, child));
					}
				}
			}
		}

		return beanElem;
	}

	private String serializePrimitive(Method getter, Object value) {
		String result = null;
		Class<?> type = value.getClass();

		if (type == String.class || type == Integer.class || type == Long.class || type == Double.class || type == Float.class || type == Boolean.class) {
			result = value.toString();
		} else if (Date.class.isAssignableFrom(type)) {
			Date date = ((Date) value);
			Assert.isTrue(getter.getAnnotation(Temporal.class) != null, getter.getDeclaringClass().getSimpleName() + "." + getter.getName() + " not using Temporal annotation");

			if (getter.getAnnotation(Temporal.class).value() == TemporalType.DATE) {
				result = XML_DATE_FORMAT.format(date);
			} else {
				result = XML_TIME_FORMAT.format(date);
			}
		}

		return result;
	}

	private void initializeDatabase() throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		this.persistenceProvider = new HibernatePersistenceProvider();
		this.entityManagerFactory = this.persistenceProvider.createEntityManagerFactory(DatabaseConfig.PersistenceUnit, null);
		if (this.entityManagerFactory == null)
			throw new RuntimeException(String.format("Couldn't create an entity manager factory for persistence unit `%s'", DatabaseConfig.PersistenceUnit));

		this.entityManager = this.entityManagerFactory.createEntityManager();
		if (this.entityManager == null)
			throw new RuntimeException(String.format("Couldn't create an entity manager for persistence unit `%s'", DatabaseConfig.PersistenceUnit));


		this.configuration = new Configuration();
		Metamodel metamodel = this.entityManagerFactory.getMetamodel();

		Set<EntityType<?>> entities = metamodel.getEntities();
		for (final EntityType<?> entity : entities)
			this.configuration.addAnnotatedClass(entity.getJavaType());

		Set<EmbeddableType<?>> embeddables = metamodel.getEmbeddables();
		for (final EmbeddableType<?> embeddable : embeddables)
			this.configuration.addAnnotatedClass(embeddable.getJavaType());

	}

	@SuppressWarnings("unchecked")
	private Collection<? extends DomainEntity> getAllInstancesOfEntity(Class<? extends DomainEntity> klazz)
	{
		Query query = entityManager.createQuery("FROM " + klazz.getSimpleName() + " ORDER BY id ASC");
		return (Collection<? extends DomainEntity>) query.getResultList();
	}

	private Collection<? extends Class<?>> findAllDomainClasses() throws Exception
	{
		Set<Class<?>> result = new HashSet<Class<?>>();

		for (String pkgName: new String[] {"domain", "security"}) {
			result.addAll(findCandidatesByAnnotation(javax.persistence.Entity.class, pkgName));
		}
		return result;
	}

	private Collection<? extends Class<?>> findCandidatesByAnnotation(Class<? extends Annotation> aAnnotation, String aRoot) throws Exception
	{
		List<Class<?>> result = new ArrayList<Class<?>>();

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
			@Override
			protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition)
			{
				return true;
			}
		};

		scanner.addIncludeFilter(new AnnotationTypeFilter(aAnnotation));
		Set<BeanDefinition> canditates = scanner.findCandidateComponents(aRoot);
		for (BeanDefinition beanDefinition : canditates) {
			String classname = beanDefinition.getBeanClassName();
			Class<?> clazz = Class.forName(classname);
			result.add(clazz);
		}

		return result;
	}

	private void initializeXml() throws ParserConfigurationException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		xmlDoc = dbf.newDocumentBuilder().newDocument();
		xmlRoot = xmlDoc.createElement("beans");
		xmlDoc.appendChild(xmlRoot);

		xmlRoot.setAttribute("xmlns", XMLNS_ROOT);
		xmlRoot.setAttributeNS(XMLNS_XSI, "schemaLocation", SCHEMA_LOCATION);
	}

}

