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
    <display:table pagesize="${displayTagPageSize}" name="sales" id="sale" requestURI="${currentRequestUri}" sort="list">
        <display:column property="name" titleKey="sales.name" href="sales/show.do" paramId="id" paramProperty="id" sortable="true" escapeXml="true" />
        <display:column property="price" titleKey="sales.price" sortable="true" escapeXml="true" />
        <display:column titleKey="sales.user" sortable="true">
            <app:actor-name actor="${sale.user}" />
        </display:column>
        <c:if test="${showStatus}">
            <display:column titleKey="sales.status" sortProperty="status" sortable="true">
                <spring:message code="sales.status.${sale.status}" />
            </display:column>
        </c:if>
        <display:column property="creationTime" titleKey="sales.creationTime" format="{0,date,dd/MM/yyyy HH:mm:ss}" sortable="true" />
    </display:table>
</div>

<security:authorize access="hasRole('USER')">
    <div>
        <app:redir-button code="misc.actions.new" action="sales/new.do?returnAction=${appfn:escapeUrlParam(returnActionForHere)}" />
    </div>
</security:authorize>