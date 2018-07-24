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

<form:form action="volume/edit.do" modelAttribute="volume">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="users"/>
	<form:hidden path="comments"/>

	<form:label path="name">
		<spring:message code="volume.name" />:
	</form:label>
	<form:input path="name" />
	<form:errors cssClass="error" path="name" />
	<br />
	
	<form:label path="description">
		<spring:message code="volume.description" />:
	</form:label>
	<form:input path="description" />
	<form:errors cssClass="error" path="description" />
	<br />
	
	<form:label path="orderNumber">
		<spring:message code="volume.orderNumber" />:
	</form:label>
	<form:input path="orderNumber" />
	<form:errors cssClass="error" path="orderNumber" />
	<br />
	
	<form:label path="releaseDate">
		<spring:message code="volume.releaseDate" />:
	</form:label>
	<form:input path="releaseDate" />
	<form:errors cssClass="error" path="releaseDate" />
	<br />
	
	<form:label path="chapterCount">
		<spring:message code="volume.chapterCount" />:
	</form:label>
	<form:input path="chapterCount" />
	<form:errors cssClass="error" path="chapterCount" />
	<br />

	<form:label path="comic">
		<spring:message code="volume.comic" />:
	</form:label>
	<form:select id="comics" path="comic" >
		<form:option value="0" label="----" />		
		<form:options items="${comics}" itemValue="id"
			itemLabel="name" />
	</form:select>
	<form:errors cssClass="error" path="comic" />
	<br />

	<form:label path="author">
		<spring:message code="volume.author" />:
	</form:label>
	<form:select id="authors" path="author" >
		<form:option value="0" label="----" />		
		<form:options items="${authors}" itemValue="id"
			itemLabel="name" />
	</form:select>
	<form:errors cssClass="error" path="author" />
	<br />


	<form:label path="image">
		<spring:message code="volume.image" />:
	</form:label>
	<form:input path="image" />
	<form:errors cssClass="error" path="image" />
	<br />
	<br />

	<input type="submit" name="save"
		value="<spring:message code="volume.save" />" />&nbsp; 
	<jstl:if test="${volume.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="volume.delete" />"
			onclick="return confirm('<spring:message code="volume.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="volume.cancel" />"
		onclick="javascript: relativeRedir('volume/list.do');" />
	<br />


</form:form>