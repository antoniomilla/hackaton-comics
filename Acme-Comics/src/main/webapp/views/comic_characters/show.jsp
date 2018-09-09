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

<c:if test="${comicCharacter.image != null}">
    <div>
        <img src="${comicCharacter.image}" width="200" height="200"/>
    </div>
</c:if>

<div>
	<p><spring:message code="comic_characters.alias"/>: <c:out value="${comicCharacter.alias}" />
	<p><spring:message code="comic_characters.name"/>: <c:out value="${comicCharacter.name}" /></p>
    <c:if test="${not empty comicCharacter.otherAliases}">
        <p>
            <spring:message code="comic_characters.otherAliases"/>:
            <c:forEach var="otherAlias" items="${comicCharacter.otherAliases}" varStatus="vs">
                <c:if test="${vs.count > 1}">
                    <c:out value=", "/>
                </c:if>
                <c:out value="${otherAlias}"/>
            </c:forEach>
        </p>
	</c:if>
	<c:if test="${comicCharacter.city != null}">
	    <p><spring:message code="comic_characters.city"/>: <c:out value="${comicCharacter.city}" />
	</c:if>
	<c:if test="${comicCharacter.firstAppearance != null}">
	    <p><spring:message code="comic_characters.firstAppearance"/>: <c:out value="${comicCharacter.firstAppearance}" />
	</c:if>
	<c:if test="${comicCharacter.publisher != null}">
	    <p><spring:message code="comic_characters.publisher"/>: <a href="publishers/show.do?id=${comicCharacter.publisher.id}"><c:out value="${comicCharacter.publisher.name}" /></a>
	</c:if>
	<c:if test="${comicCharacter.description != null}">
	    <p><spring:message code="comic_characters.description"/>: <c:out value="${comicCharacter.description}" />
	</c:if>
</div>

<c:if test="${principal != null and principal.administrator or principal.trusted}">
    <div>
        <app:redir-button code="misc.actions.edit" action="comic_characters/edit.do?id=${comicCharacter.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
        <app:delete-button code="misc.actions.delete" action="comic_characters/delete.do?id=${comicCharacter.id}&returnAction=comic_characters/list.do" />
    </div>
</c:if>


<h3><spring:message code="comic_characters.comics" /></h3>
<div>
	<display:table pagesize="${displayTagPageSize}" name="comicPairs" id="pair" requestURI="${currentRequestUri}" sort="list">
	    <c:if test="${pair.right != null}">
            <display:column class="iconColumn" title="&#8203;" sortProperty="right.starred" sortable="true">
                <c:if test="${pair.right.starred}">
                    <app:post-link action="comics/unstar.do?id=${pair.left.comic.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}">
                        <img class="iconColumnIcon iconButton" src="images/star_yes.png" />
                    </app:post-link>
                </c:if>
                <c:if test="${not pair.right.starred}">
                    <app:post-link action="comics/star.do?id=${pair.left.comic.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}">
                        <img class="iconColumnIcon iconButton" src="images/star_no.png" />
                    </app:post-link>
                </c:if>
            </display:column>
        </c:if>
	    <display:column property="left.comic.name" titleKey="comics.name" sortable="true" href="comics/show.do" paramId="id" paramProperty="left.comic.id" escapeXml="true" />
	    <display:column property="left.role" titleKey="comics.name" sortable="true" escapeXml="true" />
	    <display:column property="left.comic.author.name" titleKey="comics.author" href="authors/show.do" paramId="id" paramProperty="left.comic.author.id" escapeXml="true" />
	    <display:column property="left.comic.publisher.name" titleKey="comics.publisher" href="publishers/show.do" paramId="id" paramProperty="left.comic.publisher.id" escapeXml="true" />
        <display:column titleKey="comics.tags">
            <c:forEach var="tag" items="${pair.left.comic.tags}">
                <span class="searchTag"><c:out value="${tag}"/></span>
            </c:forEach>
        </display:column>

        <c:if test="${principal != null and principal.administrator or principal.trusted}">
            <display:column titleKey="misc.actions">
                <app:redir-button code="misc.actions.edit" action="comic_comic_characters/edit.do?id=${pair.left.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                <app:delete-button code="misc.actions.remove" action="comic_comic_characters/delete.do?id=${pair.left.id}&returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'pair'))}" />
            </display:column>
        </c:if>
	</display:table>
</div>

<h3><spring:message code="comic_characters.comments" /></h3>
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
		    <form:hidden path="comicCharacter" />
		    <app:textarea code="comments.text" path="text" />
		    <app:submit />
		</form:form>
	</div>
</security:authorize>