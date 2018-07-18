<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="personajes" requestURI="${requestURI}" id="row">
	
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="personaje/edit.do?personajeId=${row.id}">
				<spring:message	code="personaje.edit" />
			</a>
		</display:column>		
	</security:authorize>
	
	<spring:message code="personaje.nombre" var="nombreHeader" />
	<display:column property="nombre" title="${nombreHeader}" sortable="true" />

	<spring:message code="personaje.alias" var="aliasHeader" />
	<display:column property="alias" title="${aliasHeader}" sortable="true" />

	<spring:message code="personaje.ciudad" var="ciudadHeader" />
	<display:column property="ciudad" title="${ciudadHeader}" sortable="false" />
	
	<spring:message code="personaje.editorial" var="editorialHeader" />
	<display:column property="editorial.nombre" title="${editorialHeader}" sortable="true" />

	<display:column>
		<a href="personaje/display.do?personajeId=${row.id }">
		<spring:message code="personaje.ver"/>
		</a>
	</display:column>

</display:table>


<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="personaje/create.do"> <spring:message
				code="personaje.create" />
		</a>
	</div>
</security:authorize>