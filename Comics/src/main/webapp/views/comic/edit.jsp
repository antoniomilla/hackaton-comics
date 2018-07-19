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

	<form:label path="name">
		<spring:message code="comic.name" />:
	</form:label>
	<form:input path="name" />
	<form:errors cssClass="error" path="name" />
	<br />

	<form:label path="author">
		<spring:message code="comic.author" />:
	</form:label>
	<form:select id="authors" path="author" >
		<form:option value="0" label="----" />		
		<form:options items="${authors}" itemValue="id"
			itemLabel="name" />
	</form:select>
	<form:errors cssClass="error" path="author" />

	<form:label path="publisher">
		<spring:message code="comic.publisher" />:
	</form:label>
	<form:select id="publishers" path="publisher">
		<form:option value="0" label="----" />		
		<form:options items="${publishers}" itemValue="id" itemLabel="name" />
	</form:select>
	<form:errors cssClass="error" path="publisher" />
	<br />

	<form:label path="image">
		<spring:message code="comic.image" />:
	</form:label>
	<form:input path="image" />
	<form:errors cssClass="error" path="image" />
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