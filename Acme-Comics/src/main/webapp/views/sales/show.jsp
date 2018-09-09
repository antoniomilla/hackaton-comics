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
	<p><spring:message code="sales.name"/>: <c:out value="${sale.name}"/></p>
	<p><spring:message code="sales.user"/>: <app:actor-name actor="${sale.user}" /></p>
	<p><spring:message code="sales.status"/>: <spring:message code="sales.status.${sale.status}"/></p>
	<c:if test="${sale.userSoldTo != null}">
	    <p><spring:message code="sales.userSoldTo"/>: <app:actor-name actor="${sale.userSoldTo}" /></p>
	</c:if>
	<p><spring:message code="sales.creationTime"/>: <fmt:formatDate value="${sale.creationTime}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
	<p><spring:message code="sales.price"/>: <c:out value="${sale.price}"/></p>
	<p><spring:message code="sales.description"/>: <c:out value="${sale.description}"/></p>

	<c:if test="${!empty sale.images}">
        <br/>

        <div class="sale-images">
            <c:forEach items="${sale.images}" var="image">
                <a href="<c:out value="${image}" />"><img class="sale-image" src="<c:out value="${image}" />" /></a>
            </c:forEach>
        </div>
    </c:if>
</div>

<c:if test="${principal.user and sale.user != principal}">
    <br/>
    <div>
        <c:choose>
            <c:when test="${not sale.interestedUsers.contains(principal)}">
                <c:if test="${sale.status == 'SELLING'}">
                    <app:post-button code="sales.interested" action="sales/set_interested.do?id=${sale.id}&interested=1&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                </c:if>
            </c:when>
            <c:otherwise>
                <c:if test="${sale.status == 'SELLING'}">
                    <app:post-button code="sales.notInterested" action="sales/set_interested.do?id=${sale.id}&interested=0&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
                    <app:redir-button code="sales.talkWithSeller" action="sales/conversation.do?id=${sale.id}&user=${principal.id}" />
                </c:if>
                <c:if test="${sale.status != 'SELLING'}">
                    <app:redir-button code="sales.viewConversation" action="sales/conversation.do?id=${sale.id}&user=${principal.id}" />
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<c:if test="${sale.user == principal and sale.status == 'SELLING'}">
    <br/>
    <div>
        <app:redir-button code="misc.actions.edit" action="sales/edit.do?id=${sale.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
        <app:post-button code="sales.cancel" action="sales/cancel.do?id=${sale.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" confirmCode="misc.areYouSureIrreversible" />
    </div>
</c:if>

<c:if test="${principal.administrator or sale.user == principal}">
    <h3><spring:message code="sales.interestedUsers" /></h3>
    <div>
        <display:table pagesize="${displayTagPageSize}" name="sale.interestedUsers" id="user" requestURI="${currentRequestUri}" sort="list">
           <display:column property="nickname" titleKey="sales.interestedUser" sortable="true" escapeXml="true" />

            <c:if test="${sale.user == principal}">
                <display:column titleKey="misc.actions">
                    <app:redir-button code="sales.viewConversation" action="sales/conversation.do?id=${sale.id}&user=${user.id}" />
                    <c:if test="${sale.status == 'SELLING'}">
                        <app:post-button code="sales.complete" action="sales/complete.do?id=${sale.id}&user=${user.id}&returnAction=${appfn:escapeUrlParam(returnActionForHere)}" confirmCode="misc.areYouSureIrreversible" />
                    </c:if>
                </display:column>
            </c:if>
        </display:table>
    </div>
</c:if>

<h3><spring:message code="sales.comments" /></h3>
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
		    <form:hidden path="sale" />
		    <app:textarea code="comments.text" path="text" />
		    <app:submit />
		</form:form>
	</div>
</security:authorize>