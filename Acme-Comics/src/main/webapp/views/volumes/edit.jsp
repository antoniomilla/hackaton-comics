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

<form:form action="${formAction}" modelAttribute="volume">
    <app:preserve-return-action />
    <app:entity-editor />

    <form:hidden path="comic" />
	<app:textbox path="orderNumber" code="volume.orderNumber" />
	<app:textbox path="name" code="volume.name" />
	<app:select path="author" code="volume.author" items="${authors}" itemLabel="name" />
	<app:datepicker path="releaseDate" code="volume.releaseDate" />
	<app:textbox path="chapterCount" code="volume.chapterCount" />
	<app:textbox path="image" code="volume.image" />
	<app:textarea path="description" code="volume.description" />

	<div>
        <app:submit entity="${volume}" />
        <app:cancel-button />
    </div>
</form:form>
