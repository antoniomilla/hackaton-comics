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

<form:form action="${formAction}" modelAttribute="comicCharacter">
    <app:preserve-return-action />
    <app:entity-editor />

	<app:textbox path="alias" code="comic_characters.alias" />
	<app:textbox path="name" code="comic_characters.name" />
    <app:stringlist path="otherAliases" items="${comicCharacter.otherAliases}" code="comic_characters.otherAliases" />
	<app:textbox path="city" code="comic_characters.city" />
	<app:textbox path="firstAppearance" code="comic_characters.firstAppearance" />
	<app:textbox path="image" code="comic_characters.image" />
	<app:select path="publisher" code="comic_characters.publisher" items="${publishers}" itemLabel="name" />
	<app:textarea path="description" code="comic_characters.description" />

	<div>
        <app:submit entity="${author}" />
        <app:cancel-button />
    </div>
</form:form>
