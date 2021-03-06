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

<display:table pagesize="${displayTagPageSize}" name="publishers" requestURI="${currentRequestUri}" id="publisher" sort="list">
	<display:column property="name" titleKey="publishers.name" sortable="true" href="publishers/show.do" paramId="id" paramProperty="id" escapeXml="true" />
	<display:column property="foundationDate" titleKey="publishers.foundationDate" sortable="true" format="{0,date,dd/MM/yyyy}"/>
	<display:column property="description" titleKey="publishers.description" sortable="true" escapeXml="true" />

	<c:if test="${principal != null and principal.administrator or principal.trusted}">
	    <display:column titleKey="misc.actions">
	        <app:redir-button code="misc.actions.edit" action="publishers/edit.do?id=${publisher.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
	        <app:delete-button code="misc.actions.delete" action="publishers/delete.do?id=${publisher.id}&returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'publisher'))}" warnCascade="true" />
    	</display:column>
    </c:if>

</display:table>

<c:if test="${principal != null and principal.administrator or principal.trusted}">
    <app:redir-button code="misc.actions.new" action="publishers/new.do?returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
</c:if>