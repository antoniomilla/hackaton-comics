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

<p>
    <spring:message code="users.nickname"/>:
    <app:actor-name actor="${user}" />
</p>
<p><spring:message code="users.creationTime"/>: <fmt:formatDate value="${user.creationTime }" pattern="dd/MM/yyyy HH:mm"/></p>
<c:if test="${user.description != null}">
    <p><spring:message code="users.description"/>: <c:out value="${user.description }"/></p>
</c:if>
<p>
    <spring:message code="users.level"/>:
    <c:out value="${user.level }"/>
    <security:authorize access="hasRole('ADMINISTRATOR')">
        <app:post-button code="misc.actions.update" action="users/update_level.do?id=${user.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
    </security:authorize>
</p>
<security:authorize access="hasRole('ADMINISTRATOR')">
    <c:if test="${user.blocked and user.blockReason != null}">
        <p><spring:message code="users.blockReason"/>: <c:out value="${user.blockReason}"/></p>
    </c:if>
</security:authorize>

<security:authorize access="isAuthenticated()">
    <c:if test="${user != principal}">
        <c:if test="${principal.administrator or not user.onlyFriendsCanSendDms or user.friends.contains(principal)}">
            <app:redir-button code="users.sendMessage" action="direct_messages/new.do?recipient=${user.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
        </c:if>
    </c:if>
</security:authorize>

<security:authorize access="hasRole('ADMINISTRATOR')">
    <c:if test="${not user.trusted}">
        <app:post-button code="users.trust" action="users/trust.do?id=${user.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
    </c:if>
    <c:if test="${user.trusted}">
        <app:post-button code="users.untrust" action="users/untrust.do?id=${user.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
    </c:if>
    <c:if test="${not user.blocked}">
        <app:redir-button code="users.block" action="users/block.do?id=${user.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
    </c:if>
    <c:if test="${user.blocked}">
        <app:post-button code="users.unblock" action="users/unblock.do?id=${user.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
    </c:if>
</security:authorize>

<security:authorize access="hasRole('USER')">
    <c:if test="${principal != user}">
        <c:choose>
            <c:when test="${not principal.friends.contains(user)}">
                <app:post-button code="users.friend" action="users/friend.do?id=${user.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
            </c:when>
            <c:otherwise>
                <app:post-button code="users.unfriend" action="users/unfriend.do?id=${user.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
            </c:otherwise>
        </c:choose>
    </c:if>
</security:authorize>

<h3><spring:message code="users.comics" /></h3>
<div>
<c:forEach var="status" items="${statuses}">
    <c:if test="${currentStatus == status}">
        <a href="#" onclick="return false" class="statusButton currentStatusButton">
            <spring:message code="comics.user.status.${status}"/>
        </a>
    </c:if>
    <c:if test="${currentStatus != status}">
        <a href="${appfn:withUrlParam(appfn:withoutDisplayTagParams(currentRequestUriAndParams, 'userComic'), 'current_status', status)}" class="statusButton">
            <spring:message code="comics.user.status.${status}"/>
        </a>
    </c:if>
</c:forEach>
</div>
<br/>
<div>
	<display:table pagesize="${displayTagPageSize}" name="userComics" id="userComic" sort="list" requestURI="${currentRequestUri}">
	    <display:column class="iconColumn" title="&#8203;" sortProperty="starred" sortable="true">
            <c:if test="${userComic.starred}">
                <img class="iconColumnIcon" src="images/star_yes.png" />
            </c:if>
            <c:if test="${not userComic.starred}">
                <img class="iconColumnIcon" src="images/star_no.png" />
            </c:if>
	    </display:column>
        <display:column property="comic.name" titleKey="comics.name" sortable="true" href="comics/show.do" paramProperty="comic.id" paramId="id" />
        <display:column property="comic.publisher.name" titleKey="comics.publisher" sortable="true"/>
        <display:column property="comic.author.name" titleKey="comics.author" sortable="true"/>
        <display:column titleKey="comics.user.status" sortable="true">
            <spring:message code="comics.user.status.${userComic.status}" />
        </display:column>
        <display:column titleKey="comics.user.volumesRead">
            <c:out value="${userComic.readVolumeCount}" />/<c:out value="${userComic.comic.volumeCount}" />
        </display:column>
        <display:column property="score" titleKey="comics.user.score" sortable="true">
            <c:out value="${score != null ? score : ''}" />
        </display:column>
	</display:table>
</div>

<h3><spring:message code="users.recentComments" /></h3>

<div>
	<display:table pagesize="${displayTagPageSize}" name="comments" id="comment" requestURI="${currentRequestUri}" sort="list">
	
        <display:column titleKey="comments.type">
            <c:if test="${comment.comic != null}">
                <spring:message code="comments.comic" />
            </c:if>

            <c:if test="${comment.publisher != null}">
                <spring:message code="comments.publisher" />
            </c:if>

            <c:if test="${comment.comicCharacter != null}">
                <spring:message code="comments.comicCharacter" />
            </c:if>

            <c:if test="${comment.author != null}">
                <spring:message code="comments.author" />
            </c:if>

            <c:if test="${comment.volume != null}">
                <spring:message code="comments.volume" />
            </c:if>
        </display:column>

        <display:column titleKey="comments.name">
            <c:if test="${comment.comic != null}">
                <c:out value="${comment.comic.name }"/>
            </c:if>

            <c:if test="${comment.publisher != null}">
                <c:out value="${comment.publisher.name }"/>
            </c:if>

            <c:if test="${comment.comicCharacter != null}">
                <c:out value="${comment.comicCharacter.alias }"/>
            </c:if>

            <c:if test="${comment.author != null}">
                <c:out value="${comment.author.name }"/>
            </c:if>

            <c:if test="${comment.volume != null}">
                <c:out value="${comment.volume.name }"/>
            </c:if>
        </display:column>

        <display:column property="text" titleKey="comments.text" />

        <display:column property="creationTime" titleKey="comments.creationTime" format="{0,date,dd/MM/yyyy HH:mm:ss}" sortable="true"/>

	    <security:authorize access="hasRole('ADMINISTRATOR')">
	        <display:column titleKey="misc.actions">
	            <app:delete-button action="comments/delete.do?id=${comment.id}&returnAction=${appfn:escapeUrlParam(appfn:withoutDisplayTagParams(returnActionForHere, 'comment'))}" />
	        </display:column>
	    </security:authorize>
	</display:table>
</div>

<h3><spring:message code="users.friends" /></h3>

<div>
	<display:table pagesize="${displayTagPageSize}" name="friends" id="friend" requestURI="${currentRequestUri}" sort="list">
        <display:column sortProperty="nickname" titleKey="users.nickname" sortable="true">
            <c:if test="${friend.trusted}">
                <img class="iconColumnIcon" src="images/trusted.png" title="<spring:message code="users.trusted" />" />
            </c:if>
            <c:if test="${friend.blocked}">
                <img class="iconColumnIcon" src="images/blocked.png" title="<spring:message code="users.blocked" />" />
            </c:if>
            <security:authorize access="hasRole('USER')">
                <c:if test="${principal.friends.contains(user)}">
                    <img class="iconColumnIcon" src="images/friend.png" title="<spring:message code="users.friendNoun" />" />
                </c:if>
            </security:authorize>
            <c:if test="${friend.blocked}">
                <span style="text-decoration: line-through;"><c:out value="${friend.nickname }"/></span>
            </c:if>
            <c:if test="${not friend.blocked}">
                <c:out value="${friend.nickname }"/>
            </c:if>
        </display:column>
        <display:column property="level" titleKey="users.level" sortable="true" />
	</display:table>
</div>