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
		
		<security:authorize access="hasRole('CLIENTE')">
			<li><a class="fNiv"><spring:message	code="master.page.customer" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="cliente/comic/listLeidos.do"><spring:message code="master.page.customer.leidos" /></a></li>
					<li><a href="cliente/comic/listNoLeidos.do"><spring:message code="master.page.customer.no.leidos" /></a></li>					
				</ul>
			</li>
			<li><a class="fNiv" href="cliente/comic/list.do"><spring:message code="master.page.comics" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="editorial/comic/listDC.do"><spring:message code="master.page.DC" /></a></li>
					<li><a href="editorial/comic/listMarvel.do"><spring:message code="master.page.marvel" /></a></li>					
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMIN')">
		<li><a class="fNiv"><spring:message code="master.page.actores" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="cliente/list.do"><spring:message code="master.page.customers" /></a></li>					
					<li><a href="administrador/list.do"><spring:message code="master.page.administrators" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv"><spring:message code="master.page.admin" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="comic/create.do"><spring:message code="master.page.registrar.comic" /></a></li>					
					<li><a href="editorial/create.do"><spring:message code="master.page.registrar.editorial" /></a></li>
					<li><a href="autor/create.do"><spring:message code="master.page.registrar.autor" /></a></li>
					<li><a href="personaje/create.do"><spring:message code="master.page.registrar.personaje" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="comic/list.do"><spring:message code="master.page.comics" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="editorial/comic/listDC.do"><spring:message code="master.page.DC" /></a></li>
					<li><a href="editorial/comic/listMarvel.do"><spring:message code="master.page.marvel" /></a></li>					
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="comic/list.do"><spring:message code="master.page.comics" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="editorial/comic/listDC.do"><spring:message code="master.page.DC" /></a></li>
					<li><a href="editorial/comic/listMarvel.do"><spring:message code="master.page.marvel" /></a></li>					
				</ul>
			</li>
			<li><a class="fNiv" href="editorial/list.do"><spring:message code="master.page.editoriales" /></a></li>
			<li><a class="fNiv" href="autor/list.do"><spring:message code="master.page.autores" /></a></li>
			<li><a class="fNiv" href="personaje/list.do"><spring:message code="master.page.personajes" /></a></li>
			<li><a class="fNiv" href="cliente/create.do"><spring:message code="master.page.registrar" /></a></li>
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv" href="editorial/list.do"><spring:message code="master.page.editoriales" /></a></li>
			<li><a class="fNiv" href="autor/list.do"><spring:message code="master.page.autores" /></a></li>
			<li><a class="fNiv" href="personaje/list.do"><spring:message code="master.page.personajes" /></a></li>
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

