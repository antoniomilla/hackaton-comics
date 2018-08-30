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

<%@ attribute name="actor" required="true" type="domain.Actor" %>

<c:if test="${actor.user}">
    <c:if test="${actor.trusted}">
        <img class="iconColumnIcon" src="images/trusted.png" title="<spring:message code="users.trusted" />" />
    </c:if>
    <c:if test="${actor.blocked}">
        <img class="iconColumnIcon" src="images/blocked.png" title="<spring:message code="users.blocked" />" />
    </c:if>
</c:if>
<c:if test="${actor.administrator}">
    <img class="iconColumnIcon" src="images/admin.png" title="<spring:message code="actors.administrator" />" />
</c:if>
<c:if test="${principal != null and principal.user and principal.friends.contains(actor)}">
    <img class="iconColumnIcon" src="images/friend.png" title="<spring:message code="users.friendNoun" />" />
</c:if>
<c:if test="${actor.user}">
    <a href="users/show.do?id=${actor.id}">
        <c:if test="${actor.blocked}">
            <span style="text-decoration: line-through;">
                <c:out value="${actor.nickname}" />
            </span>
        </c:if>
        <c:if test="${not actor.blocked}">
            <c:out value="${actor.nickname}" />
        </c:if>
    </a>
</c:if>
<c:if test="${not actor.user}">
    <c:out value="${actor.nickname}" />
</c:if>