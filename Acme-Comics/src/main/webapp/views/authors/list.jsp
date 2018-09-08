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

<display:table pagesize="${displayTagPageSize}" name="authors" requestURI="${currentRequestUri}" id="author" sort="list">
	<display:column property="name" titleKey="authors.name" sortable="true" href="authors/show.do" paramId="id" paramProperty="id" />
	<display:column property="birthDate" titleKey="authors.birthDate" sortable="true" format="{0,date,dd/MM/yyyy}"/>
	<display:column property="birthPlace" titleKey="authors.birthPlace" sortable="true" />

	<c:if test="${principal != null and principal.administrator or principal.trusted}">
        <display:column titleKey="misc.actions">
            <app:redir-button code="misc.actions.edit" action="authors/edit.do?id=${author.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
            <app:delete-button code="misc.actions.delete" action="authors/delete.do?id=${author.id}&returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'author'))}" warnCascade="true" />
        </display:column>
    </c:if>
</display:table>

<c:if test="${principal != null and principal.administrator or principal.trusted}">
    <app:redir-button code="misc.actions.new" action="authors/new.do?returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
</c:if>