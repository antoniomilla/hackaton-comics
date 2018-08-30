<%--
 * submit.tag
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

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

<%-- Attributes --%> 

<%@ attribute name="name" required="false" %>
<%@ attribute name="code" required="false" %>
<%@ attribute name="entity" type="domain.DomainEntity" required="false" %>

<c:if test="${name == null}">
    <c:set var="name" value="submit" />
</c:if>

<c:if test="${code == null}">
    <c:if test="${entity == null}">
        <c:set var="code" value="misc.actions.submit" />
    </c:if>
    <c:if test="${entity != null && entity.id == 0}">
        <c:set var="code" value="misc.actions.create" />
    </c:if>
    <c:if test="${entity != null && entity.id != 0}">
        <c:set var="code" value="misc.actions.update" />
    </c:if>
</c:if>

<%-- Definition --%>

<button type="submit" name="${name}" class="btn btn-primary">
	<spring:message code="${code}" />
</button>
