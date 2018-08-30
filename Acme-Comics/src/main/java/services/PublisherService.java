
package services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import exceptions.ResourceNotFoundException;
import repositories.PublisherRepository;
import domain.Publisher;
import utilities.CheckUtils;
import utilities.PolicyCheckUtils;

@Service
@Transactional
public class PublisherService {
	@Autowired private PublisherRepository repository;
	@Autowired private ActorService actorService;
	@Autowired private Validator validator;
	@PersistenceContext private EntityManager entityManager;

	public List<Publisher> findAll() 
	{
		return repository.findAll();
	}

	public Publisher getById(int id)
	{
		Publisher result = findById(id);
		if (result == null) throw new ResourceNotFoundException();
		return result;
	}

	public Publisher findById(int id)
	{
		return repository.findOne(id);
	}

	public Publisher create(Publisher publisher)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		CheckUtils.checkNotExists(publisher);
		return repository.save(publisher);
	}

	public Publisher getByIdForEdit(int id)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		return getById(id);
	}

	public Publisher bindForUpdate(Publisher publisher, BindingResult binding)
	{
		// Hibernate is in the dirty habit of automatically persisting any managed entities
		// at the end of the transaction, even if it was never saved. An attacker can force
		// the system to load a managed entity using a parameter (which gets parsed by the converter which loads the entity),
		// which can later be modified by the bound model attributes before our code is even called.
		// We're not going to save this entity, so detach it just in case.
		entityManager.detach(publisher);

		Publisher oldPublisher = getById(publisher.getId());
		CheckUtils.checkSameVersion(publisher, oldPublisher);

		oldPublisher.setFoundationDate(publisher.getFoundationDate());
		oldPublisher.setDescription(publisher.getDescription());
		oldPublisher.setImage(publisher.getImage());
		oldPublisher.setName(publisher.getName());

		validator.validate(oldPublisher, binding);
		if (binding.hasErrors()) entityManager.detach(oldPublisher);

		return oldPublisher;
	}

	public Publisher update(Publisher publisher)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		CheckUtils.checkExists(publisher);
		return repository.save(publisher);
	}

	public void delete(int id)
	{
		PolicyCheckUtils.checkCanEditContent(actorService.findPrincipal());
		repository.delete(id);
	}
}
