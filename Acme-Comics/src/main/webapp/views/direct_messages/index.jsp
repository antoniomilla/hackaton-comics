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

<div class="row">
    <div class="column messageFolderList">
        <display:table pagesize="${displayTagPageSize}" name="messageFolders" id="messageFolder" requestURI="${currentRequestUri}">
            <display:column property="nameForDisplay" titleKey="message_folders.name" href="${appfn:withoutDisplayTagParams(currentRequestUriAndParams, 'directMessage')}" paramId="folder" paramProperty="id" sortable="true" />
            <display:column titleKey="misc.actions">
                <c:if test="${messageFolder.type == 'USER'}">
                    <app:redir-button code="message_folders.rename" action="message_folders/edit.do?id=${messageFolder.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                    <app:delete-button code="misc.actions.delete" action="message_folders/delete.do?id=${messageFolder.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                </c:if>
            </display:column>
        </display:table>

        <app:redir-button code="misc.actions.new" action="message_folders/new.do?returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
    </div>
    <div class="column messageList">
        <display:table pagesize="${displayTagPageSize}" name="directMessages" id="directMessage" requestURI="${currentRequestUri}">
            <display:column sortProperty="subject" titleKey="direct_messages.subject" sortable="true">
                <c:if test="${directMessage.administrationNotice}">
                    <img class="iconColumnIcon" src="images/exclamation.png" />
                </c:if>
                <a class="${directMessage.administrationNotice ? 'administrationNoticeSubject' : ''} ${directMessage.readByUser ? '' : 'unreadMessageSubject'}" href="direct_messages/show.do?id=${directMessage.id}">
                    <c:out value="${directMessage.subject}" />
                </a>
            </display:column>
            <display:column titleKey="direct_messages.interlocutor">
                <c:choose>
                    <c:when test="${principal == directMessage.sender}">
                        <app:actor-name actor="${directMessage.recipient}" />
                    </c:when>
                    <c:otherwise>
                        <app:actor-name actor="${directMessage.sender}" />
                    </c:otherwise>
                </c:choose>
            </display:column>
            <display:column property="creationTime" titleKey="direct_messages.creationTime" sortable="true" format="{0,date,dd/MM/yyyy HH:mm:ss}" />
            <display:column titleKey="misc.actions">
                <c:choose>
                    <c:when test="${principal == directMessage.sender}">
                        <c:set var="actor" value="${directMessage.recipient}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="actor" value="${directMessage.sender}" />
                    </c:otherwise>
                </c:choose>
                <c:if test="${principal.administrator or not actor.user or not actor.onlyFriendsCanSendDms or actor.friends.contains(principal)}">
                    <app:redir-button code="direct_messages.reply" action="direct_messages/reply.do?id=${directMessage.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                </c:if>
                <app:redir-button code="direct_messages.move" action="direct_messages/move.do?id=${directMessage.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                <c:if test="${directMessage.messageFolder.type != 'SYSTEM_TRASH'}">
                    <app:delete-button code="misc.actions.delete" action="direct_messages/delete.do?id=${directMessage.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                </c:if>
            </display:column>
        </display:table>
    </div>
</div>
