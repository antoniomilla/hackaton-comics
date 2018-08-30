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

<div>
    <form:form method="GET" action="${currentRequestUri}" modelAttribute="searchForm">
        <app:textbox path="terms" code="misc.searchTerms" />
        <app:submit code="misc.actions.search" />
    </form:form>
</div>


<div>
    <display:table pagesize="${displayTagPageSize}" name="comicPairs" requestURI="${currentRequestUri}" id="pair">
        <security:authorize access="hasRole('USER')">
            <display:column class="iconColumn" title="&#8203;" sortProperty="right.starred" sortable="${empty searchForm.terms}">
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
        </security:authorize>
        <display:column property="left.name" titleKey="comics.name" sortable="${empty searchForm.terms}" href="comics/show.do" paramId="id" paramProperty="left.id" />
        <display:column property="left.publisher.name" titleKey="comics.publisher" sortable="${empty searchForm.terms}" />
        <display:column property="left.author.name" titleKey="comics.author" sortable="${empty searchForm.terms}" />
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


<c:if test="${principal != null and principal.administrator or principal.trusted}">
    <div>
        <app:redir-button code="misc.actions.new" action="comics/new.do?returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
    </div>
</c:if>