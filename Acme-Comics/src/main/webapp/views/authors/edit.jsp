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

<form:form action="${formAction}" modelAttribute="author">
    <app:preserve-return-action />
    <app:entity-editor />

	<app:textbox path="name" code="authors.name" />
	<app:datepicker path="birthDate" code="authors.birthDate" />
	<app:textbox path="birthPlace" code="authors.birthPlace" />
	<app:textbox path="image" code="publishers.image" />
	<app:textarea path="description" code="authors.description" />

	<div>
        <app:submit entity="${author}" />
        <app:cancel-button />
    </div>
</form:form>
