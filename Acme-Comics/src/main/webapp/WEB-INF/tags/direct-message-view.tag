<%@tag language="java" body-content="empty" %>

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

<%@ attribute name="directMessage" required="true" type="domain.DirectMessage" %>
<%@ attribute name="showActions" required="false" %>

<div class="message-outer">
    <div class="message-inner">
        <div class="message-header">
            <div class="message-subject">
                <c:if test="${directMessage.administrationNotice}">
                    <img class="iconColumnIcon" src="images/exclamation.png" />
                </c:if>
                <span class="${directMessage.administrationNotice ? 'administrationNoticeSubject' : ''}">
                    <c:out value="${directMessage.subject}" />
                </a>
            </div>
            <div class="message-meta">
                <span class="message-sender">
                    <spring:message code="direct_messages.sender" />:
                    <c:out value="${directMessage.sender.nickname}" />
                </span>
                -
                <span class="message-recipient">
                    <spring:message code="direct_messages.recipient" />:
                    <c:out value="${directMessage.recipient.nickname}" />
                </span>
                <br/>
                <span class="message-creationTime">
                    <fmt:formatDate value="${directMessage.creationTime}" pattern="dd/MM/yyyy HH:mm:ss" />
                </span>
            </div>
        </div>
        <div class="message-body multiline"><c:out value="${directMessage.body}" /></div>

        <c:if test="${showActions != null and showActions}">
            <app:redir-button code="direct_messages.reply" action="direct_messages/reply.do?id=${directMessage.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
            <app:redir-button code="direct_messages.move" action="direct_messages/move.do?id=${directMessage.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
            <c:if test="${directMessage.messageFolder.type != 'SYSTEM_TRASH'}">
                <app:delete-button code="misc.actions.delete" action="direct_messages/delete.do?id=${directMessage.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
            </c:if>
        </c:if>
    </div>
</div>