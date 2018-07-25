<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<div>
	<img src="images/comicslogo.png" alt="Comics" />
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		
		<security:authorize access="hasRole('USER')">
			<li><a class="fNiv"><spring:message	code="master.page.user" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="user/displayProfile.do"><spring:message code="master.page.profile" /></a></li>
					<li><a href="messageFolder/list.do"><spring:message code="master.page.user.messageFolders" /></a></li>					
				</ul>
			</li>
			<li><a class="fNiv" href="user/comic/list.do"><spring:message code="master.page.comics" /></a></li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
		<li><a class="fNiv"><spring:message code="master.page.actors" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="user/list.do"><spring:message code="master.page.users" /></a></li>					
					<li><a href="administrator/list.do"><spring:message code="master.page.administrators" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.admin" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="comic/create.do"><spring:message code="master.page.register.comic" /></a></li>					
					<li><a href="publisher/create.do"><spring:message code="master.page.register.publisher" /></a></li>
					<li><a href="author/create.do"><spring:message code="master.page.register.author" /></a></li>
					<li><a href="comicCharacter/create.do"><spring:message code="master.page.register.comicCharacter" /></a></li>
					<li><a href="volume/create.do"><spring:message code="master.page.register.volume" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="comic/list.do"><spring:message code="master.page.comics" /></a>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="comic/list.do"><spring:message code="master.page.comics" /></a></li>
			<li><a class="fNiv" href="publisher/list.do"><spring:message code="master.page.publishers" /></a></li>
			<li><a class="fNiv" href="author/list.do"><spring:message code="master.page.authors" /></a></li>
			<li><a class="fNiv" href="comicCharacter/list.do"><spring:message code="master.page.comicCharacters" /></a></li>
			<li><a class="fNiv" href="volume/list.do"><spring:message code="master.page.volume" /></a></li>
			<li><a class="fNiv" href="user/list.do"><spring:message code="master.page.users" /></a>
			<li><a class="fNiv" href="user/create.do"><spring:message code="master.page.sign.up" /></a></li>
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv" href="comic/list.do"><spring:message code="master.page.comics" /></a></li>
			<li><a class="fNiv" href="publisher/list.do"><spring:message code="master.page.publishers" /></a></li>
			<li><a class="fNiv" href="author/list.do"><spring:message code="master.page.authors" /></a></li>
			<li><a class="fNiv" href="comicCharacter/list.do"><spring:message code="master.page.comicCharacters" /></a></li>
			<li><a class="fNiv" href="volume/list.do"><spring:message code="master.page.volume" /></a></li>
			<li><a class="fNiv" href="user/list.do"><spring:message code="master.page.users" /></a>
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
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

