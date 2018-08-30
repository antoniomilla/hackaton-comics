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

<c:if test="${publisher.image != null}">
    <div>
        <img src="${publisher.image}" width="200" height="200"/>
    </div>
</c:if>

<div>
	<p><spring:message code="publishers.name"/>: <c:out value="${publisher.name}" /></p>
	<c:if test="${publisher.foundationDate != null}">
	    <p><spring:message code="publishers.foundationDate"/>: <fmt:formatDate value="${publisher.foundationDate}" pattern="dd/MM/yyyy"/>
	</c:if>
	<c:if test="${publisher.description != null}">
	    <p><spring:message code="publishers.description"/>: <c:out value="${publisher.description}" />
	</c:if>
</div>

<c:if test="${principal != null and principal.administrator or principal.trusted}">
    <div>
        <app:redir-button code="misc.actions.edit" action="publishers/edit.do?id=${publisher.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
        <app:delete-button code="misc.actions.delete" action="publishers/delete.do?id=${publisher.id}&returnAction=publishers/list.do" warnCascade="true" />
    </div>
</c:if>
<h3><spring:message code="publishers.characters" /></h3>
<div>
	<display:table pagesize="${displayTagPageSize}" name="comicCharacters" id="comicCharacter" requestURI="${currentRequestUri}">
        <display:column property="alias" titleKey="comic_characters.alias" href="comic_characters/show.do" paramId="id" paramProperty="id" />
	</display:table>
</div>

<h3><spring:message code="publishers.comics" /></h3>
<div>
	<display:table pagesize="${displayTagPageSize}" name="comicPairs" id="pair" requestURI="${currentRequestUri}">
	    <c:if test="${pair.right != null}">
            <display:column class="iconColumn" title="&#8203;" sortProperty="right.starred" sortable="true">
                <c:if test="${pair.right.starred}">
                    <app:post-link action="comics/unstar.do?id=${pair.left.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}">
                        <img class="iconColumnIcon iconButton" src="images/star_yes.png" />
                    </app:post-link>
                </c:if>
                <c:if test="${not pair.right.starred}">
                    <app:post-link action="comics/star.do?id=${pair.left.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}">
                        <img class="iconColumnIcon iconButton" src="images/star_no.png" />
                    </app:post-link>
                </c:if>
            </display:column>
        </c:if>
	    <display:column property="left.name" titleKey="comics.name" sortable="true" href="comics/show.do" paramId="id" paramProperty="left.id"/>
	    <display:column property="left.author.name" titleKey="comics.author" href="authors/show.do" paramId="id" paramProperty="left.author.id" />
        <display:column titleKey="comics.tags">
            <c:forEach var="tag" items="${pair.left.tags}">
                <span class="searchTag"><c:out value="${tag}"/></span>
            </c:forEach>
        </display:column>

        <c:if test="${principal != null and principal.administrator or principal.trusted}">
            <display:column titleKey="misc.actions">
                <app:redir-button code="misc.actions.edit" action="comics/edit.do?id=${pair.left.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                <app:delete-button action="comics/delete.do?id=${pair.left.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
            </display:column>
        </c:if>
	</display:table>
</div>

<h3><spring:message code="publishers.comments" /></h3>
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
		    <form:hidden path="publisher" />
		    <app:textarea code="comments.text" path="text" />
		    <app:submit />
		</form:form>
	</div>
</security:authorize>