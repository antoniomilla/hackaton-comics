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

<c:if test="${volume.image != null}">
    <div>
        <img src="${volume.image}" width="200" height="200"/>
    </div>
</c:if>

<div>
    <p>
        <spring:message code="volume.name" />:
        <c:if test="${userComic != null and userComic.readVolumes.contains(volume)}">
            <img class="iconColumnIcon" src="images/read.png" />
        </c:if>
        <c:out value="${volume.name}" />
    </p>
    <p><spring:message code="volume.author" />: <a href="authors/show.do?id=${volume.author.id}"><c:out value="${volume.author.name}" /></a>
	<p><spring:message code="volume.releaseDate" />: <fmt:formatDate value="${volume.releaseDate}" pattern="dd/MM/yyyy" />
	<c:if test="${volume.chapterCount != null}">
        <p><spring:message code="volume.chapterCount" />: <c:out value="${volume.chapterCount}" />
    </c:if>
    <p><spring:message code="volume.comic" />: <a href="comics/show.do?id=${volume.comic.id}"><c:out value="${volume.comic.name}" /></a>
    <c:if test="${volume.description != null}">
	    <p><spring:message code="volume.description" />: <c:out value="${volume.description}" />
	</c:if>

	<c:if test="${principal != null and principal.administrator or principal.trusted}">
        <p><spring:message code="volume.orderNumber" />: <c:out value="${volume.orderNumber}" /></p>
    </c:if>
</div>

<c:if test="${userComic != null}">
    <c:choose>
        <c:when test="${userComic.readVolumes.contains(volume)}">
            <app:post-button code="volume.unread" action="volumes/unread.do?id=${volume.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
        </c:when>
        <c:otherwise>
            <app:post-button code="volume.read" action="volumes/read.do?id=${volume.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
        </c:otherwise>
    </c:choose>
</c:if>

<c:if test="${principal != null and principal.administrator or principal.trusted}">
    <app:redir-button code="misc.actions.edit" action="volumes/edit.do?id=${volume.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
    <app:delete-button action="volumes/delete.do?id=${volume.id}&returnAction=volumes/list.do" />
</c:if>

<h3><spring:message code="volume.comments" /></h3>
<div>
	<display:table pagesize="${displayTagPageSize}" name="comments" id="comment" requestURI="${currentRequestUri}">
        <display:column property="text" titleKey="comments.text" sortable="true"/>
        <display:column property="user.nickname" titleKey="comments.user" sortable="true"/>
        <display:column property="creationTime" titleKey="comments.creationTime" sortable="true" format="{0,date,dd/MM/yyyy HH:mm:ss}"  />

        <security:authorize access="hasRole('ADMINISTRATOR')">
            <display:column titleKey="misc.actions">
                <app:delete-button action="comments/delete.do?id=${comment.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
            </display:column>
        </security:authorize>
	</display:table>
</div>

<security:authorize access="hasRole('USER')">
    <h3><spring:message code="comments.writeAComment" /></h3>
	<div>
		<form:form modelAttribute="comment" action="comments/create.do?returnAction=${appfn:escapeUrlParam(returnActionForHere)}">
		    <form:hidden path="creationTime" />
		    <form:hidden path="user" />
		    <form:hidden path="volume" />
		    <app:textarea code="comments.text" path="text" />
		    <app:submit />
		</form:form>
	</div>
</security:authorize>