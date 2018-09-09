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

<display:table pagesize="${displayTagPageSize}" name="comicCharacters" requestURI="${currentRequestUri}" id="comicCharacter" sort="list">
	<display:column property="alias" titleKey="comic_characters.alias" sortable="true" href="comic_characters/show.do" paramId="id" paramProperty="id" escapeXml="true" />
	<display:column property="name" titleKey="comic_characters.name" sortable="true" escapeXml="true" />
	<display:column property="city" titleKey="comic_characters.city" sortable="true" escapeXml="true" />
	<display:column property="publisher.name" titleKey="comic_characters.publisher" sortable="true" escapeXml="true" />

	<c:if test="${principal != null and principal.administrator or principal.trusted}">
        <display:column titleKey="misc.actions">
            <app:redir-button code="misc.actions.edit" action="comic_characters/edit.do?id=${comicCharacter.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
            <app:delete-button code="misc.actions.delete" action="comic_characters/delete.do?id=${comicCharacter.id}&returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'comicCharacter'))}" />
        </display:column>
    </c:if>
</display:table>

<c:if test="${principal != null and principal.administrator or principal.trusted}">
    <app:redir-button code="misc.actions.new" action="comic_characters/new.do?returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
</c:if>