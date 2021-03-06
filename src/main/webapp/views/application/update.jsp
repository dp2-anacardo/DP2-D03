<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('HACKER')">
<form:form action="application/hacker/update.do" modelAttribute="application">
	<form:hidden path="id" readOnly = "true"/>
	
	<acme:textarea code="application.explanation" path="explanation"/>
	<acme:textbox code="application.link" path="link"/>
	
	<input type="submit" name="update"
			value="<spring:message code="application.update" />" />
			
	<acme:cancel url="application/hacker/list.do" code="application.cancel"/>
</form:form>
</security:authorize>