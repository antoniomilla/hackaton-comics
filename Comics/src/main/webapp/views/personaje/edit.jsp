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

<form:form action="personaje/edit.do" modelAttribute="personaje">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<form:label path="nombre">
		<spring:message code="personaje.nombre" />:
	</form:label>
	<form:input path="nombre" />
	<form:errors cssClass="error" path="nombre" />
	<br />

	<form:label path="alias">
		<spring:message code="personaje.alias" />:
	</form:label>
	<form:input path="alias" />
	<form:errors cssClass="error" path="alias" />
	<br />
	
	<form:label path="ciudad">
		<spring:message code="personaje.ciudad" />:
	</form:label>
	<form:input path="ciudad" />
	<form:errors cssClass="error" path="ciudad" />
	<br />

	<form:label path="editorial">
		<spring:message code="comic.editorial" />:
	</form:label>
	<form:select id="editoriales" path="editorial">
		<form:option value="0" label="----" />		
		<form:options items="${editoriales}" itemValue="id" itemLabel="nombre" />
	</form:select>
	<form:errors cssClass="error" path="editorial" />
	<br />

	<form:label path="imagen">
		<spring:message code="personaje.imagen" />:
	</form:label>
	<form:input path="imagen" />
	<form:errors cssClass="error" path="imagen" />
	<br />

	<input type="submit" name="save"
		value="<spring:message code="personaje.save" />" />&nbsp; 
	<jstl:if test="${personaje.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="personaje.delete" />"
			onclick="return confirm('<spring:message code="personaje.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="personaje.cancel" />"
		onclick="javascript: relativeRedir('personaje/list.do');" />
	<br />


</form:form>