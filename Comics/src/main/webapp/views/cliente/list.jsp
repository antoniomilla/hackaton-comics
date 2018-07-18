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
	name="clientes" requestURI="${requestURI}" id="row">

	
	<spring:message code="cliente.nombre" var="nombreHeader" />
	<display:column property="nombre" title="${nombreHeader}" sortable="true" />

	<spring:message code="cliente.fechaNacimiento" var="fechaNacimientoHeader" />
	<display:column property="fechaNacimiento" title="${fechaNacimientoHeader}" sortable="true" />

	<spring:message code="cliente.email" var="emailHeader" />
	<display:column property="email" title="${emailHeader}" sortable="true" />

	<spring:message code="cliente.dni" var="dniHeader" />
	<display:column property="dni" title="${dniHeader}" sortable="true" />
	
	<spring:message code="cliente.fechaAlta" var="fechaAltaHeader" />
	<display:column property="fechaAlta" title="${fechaAltaHeader}" sortable="true" />
	
	<spring:message code="cliente.nivel" var="nivelHeader" />
	<display:column property="nivel" title="${nivelHeader}" sortable="true" />

</display:table>
