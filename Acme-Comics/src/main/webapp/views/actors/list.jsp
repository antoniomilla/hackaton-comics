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
    <display:table pagesize="${displayTagPageSize}" name="actors" requestURI="${currentRequestUri}" id="actor" sort="list">
        <display:column titleKey="users.nickname" sortProperty="nickname" sortable="true">
            <app:actor-name actor="${actor}" />
        </display:column>
        <display:column titleKey="users.level">
            <c:if test="${actor.user}">
                <c:out value="${actor.level}" />
            </c:if>
        </display:column>
        <display:column property="description" titleKey="users.description" sortable="true" />

        <security:authorize access="hasRole('ADMINISTRATOR')">
            <display:column titleKey="users.blockReason">
                <c:if test="${actor.user}">
                    <c:out value="${actor.blockReason}" />
                </c:if>
            </display:column>
        </security:authorize>

        <security:authorize access="isAuthenticated()">
            <display:column titleKey="misc.actions">
                <c:if test="${actor != principal}">
                    <c:if test="${principal.administrator or not actor.user or not actor.onlyFriendsCanSendDms or actor.friends.contains(principal)}">
                        <app:redir-button code="users.sendMessage" action="direct_messages/new.do?recipient=${actor.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                    </c:if>
                </c:if>

                <security:authorize access="hasRole('ADMINISTRATOR')">
                    <c:if test="${actor.user}">
                        <c:if test="${not actor.trusted}">
                            <app:post-button code="users.trust" action="users/trust.do?id=${actor.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                        </c:if>
                        <c:if test="${actor.trusted}">
                            <app:post-button code="users.untrust" action="users/untrust.do?id=${actor.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                        </c:if>
                        <c:if test="${not actor.blocked}">
                            <app:redir-button code="users.block" action="users/block.do?id=${actor.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                        </c:if>
                        <c:if test="${actor.blocked}">
                            <app:post-button code="users.unblock" action="users/unblock.do?id=${actor.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                        </c:if>
                    </c:if>
                </security:authorize>

                <security:authorize access="hasRole('USER')">
                    <c:if test="${actor.user and actor != principal}">
                        <c:choose>
                            <c:when test="${not principal.friends.contains(actor)}">
                                <app:post-button code="users.friend" action="users/friend.do?id=${actor.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                            </c:when>
                            <c:otherwise>
                                <app:post-button code="users.unfriend" action="users/unfriend.do?id=${actor.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </security:authorize>
            </display:column>
        </security:authorize>
    </display:table>
</div>