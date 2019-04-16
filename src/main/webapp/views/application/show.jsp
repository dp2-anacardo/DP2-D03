<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

    
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('HACKER')">
<jstl:if test="${application.status != 'PENDING'}">
<acme:showtext fieldset="true" code="application.submitMoment" value="${application.submitMoment}"/>
<acme:showtext fieldset="true" code="application.explanation" value="${application.explanation}"/>
<acme:showtext fieldset="true" code="application.link" value="<a href="${application.link}">Link"/>
</jstl:if>

<jstl:if test="${application.status == 'ACCEPTED' }">
		<acme:showtext fieldset="true" code="application.status" 
		value="<spring:message code="application.accepted"/>"/>
</jstl:if>
			
<jstl:if test="${application.status == 'SUBMITTED' }">
	<acme:showtext fieldset="true" code="application.status" 
	value="<spring:message code="application.submitted"/>"/>
</jstl:if>
			
<jstl:if test="${application.status == 'PENDING' }">
	<acme:showtext fieldset="true" code="application.status" 
	value="<spring:message code="application.pending"/>"/>
</jstl:if>
			
<jstl:if test="${application.status == 'REJECTED' }">
	<acme:showtext fieldset="true" code="application.status" 
	value="<spring:message code="application.rejected"/>"/>
	<acme:showtext fieldset="true" code="application.rejectComment" 
	value="${application.rejectComment}"/>
</jstl:if>

<acme:showtext fieldset="true" code="application.problem" 
value="<a href="problem/show.do?problemId=${row.problem.id}">
		<spring:message code="application.problem"/>"/>
		
</security:authorize>
