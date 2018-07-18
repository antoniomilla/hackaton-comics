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
	name="autores" requestURI="${requestURI}" id="row">
	
	
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<a href="autor/edit.do?autorId=${row.id}">
				<spring:message	code="autor.edit" />
			</a>
		</display:column>		
	</security:authorize>
	
	<spring:message code="autor.nombre" var="nombreHeader" />
	<display:column property="nombre" title="${nombreHeader}" sortable="true" />

	<spring:message code="autor.fechaNacimiento" var="fechaNacimientoHeader" />
	<display:column property="fechaNacimiento" title="${fechaNacimientoHeader}" sortable="true" />

	<spring:message code="autor.lugarNacimiento" var="lugarNacimientoHeader" />
	<display:column property="lugarNacimiento" title="${lugarNacimientoHeader}" sortable="false" />
	
	<display:column>
		<a href="autor/display.do?autorId=${row.id }">
		<spring:message code="autor.ver"/>
		</a>
	</display:column>

</display:table>


<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="autor/create.do"> <spring:message
				code="autor.create" />
		</a>
	</div>
</security:authorize>