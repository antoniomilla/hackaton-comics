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

<form:form action="${formAction}" modelAttribute="comic">
    <app:preserve-return-action />
    <app:entity-editor />
    <app:textbox path="name" code="comics.name" />
    <app:textarea path="description" code="comics.description" />
    <app:textbox path="image" code="comics.image" />
    <app:select path="publisher" items="${publishers}" itemLabel="name" code="comics.publisher" />
    <app:select path="author" items="${authors}" itemLabel="name" code="comics.author" />
    <app:stringlist path="tags" items="${comic.tags}" code="comics.tags" />

    <div>
        <app:submit entity="${comic}" />
        <app:cancel-button />
    </div>
</form:form>

