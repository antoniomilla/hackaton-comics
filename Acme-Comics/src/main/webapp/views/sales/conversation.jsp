<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="appfn" uri="/WEB-INF/appfn.tld" %>

<form:form action="${formAction}" modelAttribute="directMessage">
    <input type="hidden" value="<c:out value="${page}" />" name="page" />
    <form:hidden path="sender" />
    <p><spring:message code="direct_messages.recipient"/>: <c:out value="${directMessage.recipient.nickname}" /></p>
    <form:hidden path="recipient" />

    <form:hidden path="sale" />

    <app:textbox path="subject" code="direct_messages.subject" />
	<app:textarea path="body" code="direct_messages.body" />

	<div>
        <app:submit />
    </div>
</form:form>

<br/>

<c:forEach items="${directMessages.content}" var="item">
    <app:direct-message-view directMessage="${item}" />
</c:forEach>

<app:paginator page="${directMessages}" />