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

<form:form action="autor/edit.do" modelAttribute="autor">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<form:label path="nombre">
		<spring:message code="autor.nombre" />:
	</form:label>
	<form:input path="nombre" />
	<form:errors cssClass="error" path="nombre" />
	<br />

	<form:label path="fechaNacimiento">
		<spring:message code="autor.fechaNacimiento" />:
	</form:label>
	<form:input path="fechaNacimiento" />
	<form:errors cssClass="error" path="fechaNacimiento" />
	<br />
	
	<form:label path="lugarNacimiento">
		<spring:message code="autor.lugarNacimiento" />:
	</form:label>
	<form:input path="lugarNacimiento" />
	<form:errors cssClass="error" path="lugarNacimiento" />
	<br />

	<form:label path="imagen">
		<spring:message code="autor.imagen" />:
	</form:label>
	<form:input path="imagen" />
	<form:errors cssClass="error" path="imagen" />
	<br />

	<input type="submit" name="save"
		value="<spring:message code="autor.save" />" />&nbsp; 
	<jstl:if test="${autor.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="autor.delete" />"
			onclick="return confirm('<spring:message code="autor.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="autor.cancel" />"
		onclick="javascript: relativeRedir('autor/list.do');" />
	<br />


</form:form>