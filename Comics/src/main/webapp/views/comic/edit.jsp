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

<form:form action="comic/edit.do" modelAttribute="comic">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<form:label path="nombre">
		<spring:message code="comic.nombre" />:
	</form:label>
	<form:input path="nombre" />
	<form:errors cssClass="error" path="nombre" />
	<br />

	<form:label path="numPaginas">
		<spring:message code="comic.numPaginas" />:
	</form:label>
	<form:input path="numPaginas" />
	<form:errors cssClass="error" path="numPaginas" />
	<br />


	<form:label path="autor">
		<spring:message code="comic.autor" />:
	</form:label>
	<form:select id="autores" path="autor" >
		<form:option value="0" label="----" />		
		<form:options items="${autores}" itemValue="id"
			itemLabel="nombre" />
	</form:select>
	<form:errors cssClass="error" path="autor" />

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
		<spring:message code="comic.imagen" />:
	</form:label>
	<form:input path="imagen" />
	<form:errors cssClass="error" path="imagen" />
	<br />

	<input type="submit" name="save"
		value="<spring:message code="comic.save" />" />&nbsp; 
	<jstl:if test="${comic.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="comic.delete" />"
			onclick="return confirm('<spring:message code="comic.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="comic.cancel" />"
		onclick="javascript: relativeRedir('comic/list.do');" />
	<br />


</form:form>