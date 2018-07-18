<%--
 * edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="cliente/edit.do" modelAttribute="cliente">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<form:label path="cliente">
		<spring:message code="cliente.nombre" />:
	</form:label>
	<form:input path="nombre" />
	<form:errors cssClass="error" path="nombre" />
	<br />

	<form:label path="fechaNacimiento">
		<spring:message code="cliente.numPaginas" />:
	</form:label>
	<form:input path="numPaginas" />
	<form:errors cssClass="error" path="numPaginas" />
	<br />
	
	<form:label path="numPaginas">
		<spring:message code="cliente.numPaginas" />:
	</form:label>
	<form:input path="numPaginas" />
	<form:errors cssClass="error" path="numPaginas" />
	<br />
	
	<form:label path="numPaginas">
		<spring:message code="cliente.numPaginas" />:
	</form:label>
	<form:input path="numPaginas" />
	<form:errors cssClass="error" path="numPaginas" />
	<br />
	
	<form:label path="numPaginas">
		<spring:message code="cliente.numPaginas" />:
	</form:label>
	<form:input path="numPaginas" />
	<form:errors cssClass="error" path="numPaginas" />
	<br />
	
	<form:label path="numPaginas">
		<spring:message code="cliente.numPaginas" />:
	</form:label>
	<form:input path="numPaginas" />
	<form:errors cssClass="error" path="numPaginas" />
	<br />


	<input type="submit" name="save"
		value="<spring:message code="cliente" />" />&nbsp; 
	<input type="button" name="cancel"
		value="<spring:message code="cliente.cancel" />"
		onclick="javascript: relativeRedir('cliente/list.do');" />
	<br />


</form:form>