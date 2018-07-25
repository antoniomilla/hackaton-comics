<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<div>
	<img src="${comic.image }" width="200" height="200"/>
</div>

<div>
	<p><spring:message code="comic.name"/>: <jstl:out value="${comic.name }"></jstl:out></p>
	<p><spring:message code="comic.publisher"/>: <a href="publisher/display.do?publisherId=${comic.publisher.id }"><jstl:out value="${comic.publisher.name }"></jstl:out></a></p>
	<p><spring:message code="comic.author"/>: <a href="author/display.do?authorId=${comic.author.id }"><jstl:out value="${comic.author.name }"></jstl:out></a></p>
	<p><spring:message code="comic.description"/>: <jstl:out value="${comic.description }"></jstl:out></p>
	<jstl:if test="${userComic.id!=null }">
	<p><spring:message code="comic.user.rating"/>: <jstl:out value="${userComic.score }"></jstl:out></p>
	<p><spring:message code="comic.user.status"/>: <jstl:out value="${userComic.status }"></jstl:out></p>
	</jstl:if>
	
</div>


<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comicCharacters" id="row">
	
	<spring:message code="comicCharacter.alias" var="aliasHeader"/>
	<display:column property="alias" title="${aliasHeader }" sortable="true"/>
	
	<spring:message code="comicCharacter.role" var="roleHeader"/>
	<display:column title="${roleHeader }" >>
		
			<jstl:forEach var="ccc1" items="${row.comicComicCharacter }">
				<jstl:forEach var="ccc2" items="${comic.comicComicCharacter }">
					<jstl:if test="${ccc1.id==ccc2.id }">
						<jstl:out value="${ccc1.role }"/>
					</jstl:if>
				</jstl:forEach>
			</jstl:forEach>
		
	</display:column>
	
	<display:column>
		<a href="comicCharacter/display.do?comicCharacterId=${row.id }">
		<spring:message code="comic.display"/>
		</a>
	</display:column>
	
	</display:table>
</div>

<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="volumes" id="row">
	
	<display:column property="name"/>
	
	<display:column>
		<a href="volume/display.do?volumeId=${row.id }">
		<spring:message code="comic.display"/>
		</a>
	</display:column>
	
	</display:table>
	
</div>

<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comments" id="row">
	
	<spring:message code="comment.text" var="textHeader" />
	<display:column property="text" title="${textHeader}" sortable="true"/>
	
	<spring:message code="comment.creationTime" var="creationTimeHeader" />
	<display:column property="creationTime" title="${creationTimeHeader}" sortable="true"/>
	
	<spring:message code="comment.user" var="userHeader" />
	<display:column property="user.nickname" title="${userHeader}" sortable="true"/>
	
	</display:table>
</div>

<security:authorize access="hasRole('USER')">
	<div>
		<a href="comment/comic/create.do?comicId=${comic.id }"><spring:message code="comment.create"/></a>
	</div>
</security:authorize>
