<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<style type="text/css">
.ACCEPTED{
  background-color: green;
}
.REJECTED{
  background-color: red;
}
.SUBMITTED{
  background-color: grey;
}
</style>

<security:authorize access="hasRole('BROTHERHOOD')">
<display:table name="parade" id="row" requestURI="${requestURI}" pagesize="5" class="displaytag">
	
		<jstl:if test="${row.status == 'ACCEPTED'}">
				<jstl:set var="css" value="ACCEPTED"></jstl:set>
			</jstl:if>
		<jstl:if test="${row.status == 'SUBMITTED'}">
		<jstl:set var="css" value="SUBMITTED"></jstl:set>
		</jstl:if>
		<jstl:if test="${row.status == 'REJECTED'}">
			<jstl:set var="css" value="REJECTED"></jstl:set>
		</jstl:if>
	
	<spring:message code="parade.title" var="columnTitle"/>
	<display:column title="${columnTitle}" class="${css}">
		<jstl:out value="${row.title }"></jstl:out>
	</display:column>
	
	<spring:message code="parade.status" var="status"/>
	<display:column title="${status}" class="${css}" sortable="true">
			<jstl:if test="${row.status == 'ACCEPTED' }">
				<spring:message code="parade.accepted"/>
			</jstl:if>
			
			<jstl:if test="${row.status == 'SUBMITTED' }">
				<spring:message code="parade.submitted"/>
			</jstl:if>
			
			<jstl:if test="${row.status == 'REJECTED' }">
				<spring:message code="parade.rejected"/>
			</jstl:if>
	</display:column>
	
	<spring:message code="parade.rejectComment" var="rejectComment"/>
		<display:column title="${rejectComment}" class="${css}">
		<jstl:if test="${row.status == 'REJECTED'}">
			<jstl:out value="${row.rejectComment }"/>
		</jstl:if>
		</display:column>	
	<display:column class="${css}">
		<a href="parade/brotherhood/edit.do?paradeId=${row.id}">
			<spring:message code="parade.edit"/>
		</a>
	</display:column>
	
	<display:column class="${css}">
		<a href="parade/show.do?paradeId=${row.id}">
			<spring:message code="parade.show"/>
		</a>
	</display:column>
	<display:column class="${css}">
		<a href="request/brotherhood/list.do?paradeId=${row.id}">
			<spring:message code="parade.request"/>
		</a>
	</display:column>
	
	<display:column class="${css}">
		<a href="parade/brotherhood/copy.do?paradeId=${row.id}">
			<spring:message code="parade.copy"/>
		</a>
	</display:column>
	
	
</display:table>
<div>
	<a href="parade/brotherhood/create.do"> <spring:message
				code="parade.create" />
	</a>
</div>
</security:authorize>