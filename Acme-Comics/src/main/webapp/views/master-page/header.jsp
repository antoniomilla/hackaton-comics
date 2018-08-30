<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

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
	<img src="images/logo.png" alt="Comics" />
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="comics/list.do"><spring:message code="master.page.comics" /></a></li>
			<li><a class="fNiv" href="publishers/list.do"><spring:message code="master.page.publishers" /></a></li>
			<li><a class="fNiv" href="authors/list.do"><spring:message code="master.page.authors" /></a></li>
			<li><a class="fNiv" href="comic_characters/list.do"><spring:message code="master.page.comicCharacters" /></a></li>
			<li><a class="fNiv" href="actors/list.do"><spring:message code="master.page.users" /></a>
			<li><a class="fNiv" href="sales/list.do"><spring:message code="master.page.sales" /></a></li>
			<li><a class="fNiv" href="users/new.do?returnAction=${appfn:escapeUrlParam(returnActionForHere)}"><spring:message code="master.page.sign.up" /></a></li>
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv" href="comics/list.do"><spring:message code="master.page.comics" /></a></li>
			<li><a class="fNiv" href="publishers/list.do"><spring:message code="master.page.publishers" /></a></li>
			<li><a class="fNiv" href="authors/list.do"><spring:message code="master.page.authors" /></a></li>
			<li><a class="fNiv" href="comic_characters/list.do"><spring:message code="master.page.comicCharacters" /></a></li>
			<li><a class="fNiv" href="actors/list.do"><spring:message code="master.page.users" /></a>
			<li><a class="fNiv" href="direct_messages/index.do"><spring:message code="master.page.mail" /></a></li>
			<li>
                <a class="fNiv"><spring:message code="master.page.sales" /></a>
                <ul>
                    <li class="arrow"></li>
                    <li><a href="sales/list.do"><spring:message code="master.page.sales" /> </a></li>
                    <li><a href="sales/list.do?mine=1"><spring:message code="master.page.involvingMe" /> </a></li>
                </ul>
            </li>
			<li>
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow"></li>					
					<li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>

		<security:authorize access="hasRole('ADMINISTRATOR')">
            <li><a class="fNiv" href="comics/list.do"><spring:message code="master.page.comics" /></a></li>
            <li><a class="fNiv" href="publishers/list.do"><spring:message code="master.page.publishers" /></a></li>
            <li><a class="fNiv" href="authors/list.do"><spring:message code="master.page.authors" /></a></li>
            <li><a class="fNiv" href="comic_characters/list.do"><spring:message code="master.page.comicCharacters" /></a></li>
            <li><a class="fNiv" href="actors/list.do"><spring:message code="master.page.users" /></a>
            <li><a class="fNiv" href="direct_messages/index.do"><spring:message code="master.page.mail" /></a></li>
            <li><a class="fNiv" href="sales/list.do"><spring:message code="master.page.sales" /></a></li>
            <li>
                <a class="fNiv">
                    <spring:message code="master.page.profile" />
                    (<security:authentication property="principal.username" />)
                </a>
                <ul>
                    <li class="arrow"></li>
                    <li><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
                </ul>
            </li>
        </security:authorize>
	</ul>
</div>

<div>
	<a href="<c:out value="${appfn:withUrlParam(returnActionForHere, 'language', 'en')}" />">en</a> | <a href="<c:out value="${appfn:withUrlParam(returnActionForHere, 'language', 'es')}" />">es</a>
</div>

