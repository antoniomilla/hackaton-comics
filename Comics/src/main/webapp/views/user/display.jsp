<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<p><spring:message code="user.nickname"/>: <jstl:out value="${user.nickname }"/></p>
<p><spring:message code="user.creationTime"/>: <jstl:out value="${user.creationTime }"/></p>
<p><spring:message code="user.description"/>: <jstl:out value="${user.description }"/></p>
<p><spring:message code="user.level"/>: <jstl:out value="${user.level }"/></p>




<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="comics" id="row">
	
	<spring:message code="comic.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true"/>

	<spring:message code="comic.publisher" var="publisherHeader" />
	<display:column property="publisher.name" title="${publisherHeader}" sortable="true" />

	<spring:message code="comic.author" var="authorHeader" />
	<display:column property="author.name" title="${authorHeader}" sortable="true" />
	
	<jstl:if test="${user.id!=null }">
		<spring:message code="comic.status" var="statusHeader"/>
		<display:column title="${statusHeader }" >>
			
				<jstl:forEach var="uc1" items="${row.userComics }">
					<jstl:forEach var="uc2" items="${user.userComics }">
						<jstl:if test="${uc1.id==uc2.id }">
							<jstl:out value="${uc1.status }"/>
						</jstl:if>
					</jstl:forEach>
				</jstl:forEach>
			
		</display:column>
	</jstl:if>
	
	<display:column>
		<a href="comic/display.do?comicId=${row.id }">
		<spring:message code="comic.display"/>
		</a>
	</display:column>
	
	</display:table>
</div>

<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="volumes" id="row">
	
	<spring:message code="volume.name" var="nameHeader" />
	<display:column property="name" title="${nameHeader}" sortable="true" />

	<spring:message code="volume.releaseDate" var="releaseDateHeader" />
	<display:column property="releaseDate" title="${releaseDateHeader}" sortable="true" />
	
	<spring:message code="volume.chapterCount" var="chapterCountHeader" />
	<display:column property="chapterCount" title="${chapterCountHeader}" sortable="true" />
	
	<spring:message code="volume.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" sortable="true" />
	
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
	
	<jstl:if test="${row.comic.id!=null }">
		<spring:message code="comment.comic" var="comicHeader" />
		<display:column>{
			<jstl:out value="${row.comic.name }"/>
		</display:column>
	</jstl:if>
	
	<jstl:if test="${row.publisher.id!=null }">
		<spring:message code="comment.publisher" var="publisherHeader" />
		<display:column>{
			<jstl:out value="${row.publisher.name }"/>
		</display:column>
	</jstl:if>
	
	<jstl:if test="${row.comicCharacter.id!=null }">
		<spring:message code="comment.comicCharacter" var="comicCharacterHeader" />
		<display:column>{
			<jstl:out value="${row.comicCharacter.alias }"/>
		</display:column>
	</jstl:if>
	
	<jstl:if test="${row.author.id!=null }">
		<spring:message code="comment.author" var="authorHeader" />
		<display:column>{
			<jstl:out value="${row.author.name }"/>
		</display:column>
	</jstl:if>
	
	<jstl:if test="${row.volume.id!=null }">
		<spring:message code="comment.volume" var="volumeHeader" />
		<display:column>{
			<jstl:out value="${row.volume.name }"/>
		</display:column>
	</jstl:if>
	
	<spring:message code="comment.creationTime" var="creationTimeHeader" />
	<display:column property="creationTime" title="${creationTimeHeader}" sortable="true"/>
	
	</display:table>
</div>

<div>
	<display:table pagesize="5" class="displaytag" keepStatus="true" name="friends" id="row">
	
	<spring:message code="user.nickname" var="nicknameHeader" />
	<display:column property="nickname" title="${nicknameHeader}" sortable="true" />

	<spring:message code="user.level" var="levelHeader" />
	<display:column property="level" title="${levelHeader}" sortable="true" />
	
	
	<display:column>
		<a href="user/displayUser.do?userId=${row.id }">
		<spring:message code="comic.display"/>
		</a>
	</display:column>
	
	</display:table>
</div>