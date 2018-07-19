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

<form:form action="author/edit.do" modelAttribute="autor">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<form:label path="name">
		<spring:message code="author.name" />:
	</form:label>
	<form:input path="nombre" />
	<form:errors cssClass="error" path="name" />
	<br />

	<form:label path="birthDate">
		<spring:message code="author.birthDate" />:
	</form:label>
	<form:input path="birthDate" />
	<form:errors cssClass="error" path="birthDate" />
	<br />
	
	<form:label path="birthPlace">
		<spring:message code="author.birthPlace" />:
	</form:label>
	<form:input path="birthPlace" />
	<form:errors cssClass="error" path="birthPlace" />
	<br />

	<form:label path="image">
		<spring:message code="author.image" />:
	</form:label>
	<form:input path="image" />
	<form:errors cssClass="error" path="image" />
	<br />

	<input type="submit" name="save"
		value="<spring:message code="author.save" />" />&nbsp; 
	<jstl:if test="${author.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="author.delete" />"
			onclick="return confirm('<spring:message code="author.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="author.cancel" />"
		onclick="javascript: relativeRedir('author/list.do');" />
	<br />


</form:form>