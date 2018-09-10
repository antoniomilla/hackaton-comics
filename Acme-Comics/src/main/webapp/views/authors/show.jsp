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

<c:if test="${author.image != null}">
    <div>
        <img src="${author.image}" width="200" height="200"/>
    </div>
</c:if>

<div>
	<p><spring:message code="authors.name"/>: <c:out value="${author.name}" /></p>
	<c:if test="${author.birthDate != null}">
	    <p><spring:message code="authors.birthDate"/>: <fmt:formatDate value="${author.birthDate}" pattern="dd/MM/yyyy" /></p>
	</c:if>
	<c:if test="${author.birthPlace != null}">
	    <p><spring:message code="authors.birthPlace"/>: <c:out value="${author.birthPlace}" /></p>
	</c:if>
	<c:if test="${author.description != null}">
	    <p><spring:message code="authors.description"/>: <c:out value="${author.description}" /></p>
	</c:if>
</div>

<c:if test="${principal != null and principal.administrator or principal.trusted}">
    <div>
        <app:redir-button code="misc.actions.edit" action="authors/edit.do?id=${author.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
        <app:delete-button code="misc.actions.delete" action="authors/delete.do?id=${author.id}&returnAction=authors/list.do" warnCascade="true" />
    </div>
</c:if>


<h3><spring:message code="authors.comics" /></h3>
<div>
	<display:table pagesize="${displayTagPageSize}" name="comicPairs" id="comicPair" requestURI="${currentRequestUri}" sort="list">
	    <c:if test="${pair.right != null}">
            <display:column class="iconColumn" title="&#8203;" sortProperty="right.starred" sortable="true">
                <c:if test="${pair.right.starred}">
                    <app:post-link action="comics/unstar.do?id=${comicPair.left.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}">
                        <img class="iconColumnIcon iconButton" src="images/star_yes.png" />
                    </app:post-link>
                </c:if>
                <c:if test="${not pair.right.starred}">
                    <app:post-link action="comics/star.do?id=${comicPair.left.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}">
                        <img class="iconColumnIcon iconButton" src="images/star_no.png" />
                    </app:post-link>
                </c:if>
            </display:column>
        </c:if>
	    <display:column property="left.name" titleKey="comics.name" sortable="true" href="comics/show.do" paramId="id" paramProperty="left.id" escapeXml="true" />
	    <display:column property="left.publisher.name" titleKey="comics.publisher" href="publishers/show.do" paramId="id" paramProperty="left.publisher.id" escapeXml="true" />
        <display:column titleKey="comics.tags">
            <c:forEach var="tag" items="${comicPair.left.tags}">
                <span class="searchTag"><c:out value="${tag}"/></span>
            </c:forEach>
        </display:column>

        <c:if test="${principal != null and principal.administrator or principal.trusted}">
            <display:column titleKey="misc.actions">
                <app:redir-button code="misc.actions.edit" action="comics/edit.do?id=${comicPair.left.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
            </display:column>
        </c:if>
	</display:table>
</div>

<h3><spring:message code="authors.volumes" /></h3>
<div>
	<display:table pagesize="${displayTagPageSize}" name="volumePairs" id="volumePair" requestURI="${currentRequestUri}" sort="list">
        <display:column sortProperty="left.name" titleKey="volume.name" sortable="true">
            <c:if test="${volumePair.right != null and volumePair.right.readVolumes.contains(volumePair.left)}">
                <img class="iconColumnIcon" src="images/read.png" />
            </c:if>
            <a href="volumes/show.do?id=${volumePair.left.id}">
                <c:out value="${volumePair.left.name}" />
            </a>
        </display:column>
        <display:column property="left.comic.name" titleKey="volume.comic" sortable="true" escapeXml="true" />
        <display:column property="left.releaseDate" titleKey="volume.releaseDate" format="{0,date,dd/MM/yyyy}" sortable="true" />
        <security:authorize access="isAuthenticated()">
            <display:column titleKey="misc.actions">
                <c:if test="${volumePair.right != null}">
                    <c:choose>
                        <c:when test="${volumePair.right.readVolumes.contains(volumePair.left)}">
                            <app:post-button code="volume.unread" action="volumes/unread.do?id=${volumePair.left.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                        </c:when>
                        <c:otherwise>
                            <app:post-button code="volume.read" action="volumes/read.do?id=${volumePair.left.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${principal != null and principal.administrator or principal.trusted}">
                    <app:redir-button code="misc.actions.edit" action="volumes/edit.do?id=${volumePair.left.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                    <app:delete-button action="volumes/delete.do?id=${volumePair.left.id}&returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'volumePair'))}" />
                </c:if>
            </display:column>
        </security:authorize>
    </display:table>
</div>

<h3><spring:message code="authors.comments" /></h3>
<div>
	<display:table pagesize="${displayTagPageSize}" name="comments" id="comment" requestURI="${currentRequestUri}" sort="list">
        <display:column property="text" titleKey="comments.text" sortable="true" escapeXml="true" />
        <display:column property="user.nickname" titleKey="comments.user" sortable="true" escapeXml="true" />
        <display:column property="creationTime" titleKey="comments.creationTime" sortable="true" format="{0,date,dd/MM/yyyy HH:mm:ss}"  />

        <security:authorize access="hasRole('ADMINISTRATOR')">
            <display:column titleKey="misc.actions">
                <app:delete-button action="comments/delete.do?id=${comment.id}&returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'comment'))}" />
            </display:column>
        </security:authorize>
	</display:table>
</div>

<security:authorize access="hasRole('USER')">
    <h3><spring:message code="comments.writeAComment" /></h3>
	<div>
		<form:form modelAttribute="comment" action="comments/create.do?returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'comment'))}">
		    <form:hidden path="creationTime" />
		    <form:hidden path="user" />
		    <form:hidden path="author" />
		    <app:textarea code="comments.text" path="text" />
		    <app:submit />
		</form:form>
	</div>
</security:authorize>