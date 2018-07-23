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

<form:form action="comment/edit.do" modelAttribute="comment">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="comic" />
	<form:hidden path="user" />

	<form:label path="text">
		<spring:message code="comment.text" />:
	</form:label>
	<form:input path="text" />
	<form:errors cssClass="error" path="text" />
	<br />


	<input type="submit" name="save"
		value="<spring:message code="comment.send" />" />&nbsp; 
	<jstl:if test="${comment.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="comment.delete" />"
			onclick="return confirm('<spring:message code="comment.confirm.delete" />')" />&nbsp;
	</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="comment.cancel" />"
		onclick="javascript: relativeRedir('comic/display.do?comicId=${comment.comic.id}');" />
	<br />


</form:form>