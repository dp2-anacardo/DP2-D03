<%--
 * index.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<p>${welcome}</p>

<p><spring:message code="welcome.greeting.prefix" /> ${name}<spring:message code="welcome.greeting.suffix" /></p>

<p><spring:message code="welcome.greeting.current.time" /> ${moment}</p>
 
<security:authorize access="isAnonymous()">
<input type="button" name="registerHacker"
		value="<spring:message code="welcome.register.hacker" />"
		onclick="javascript: relativeRedir('hacker/create.do');" />&nbsp;
		
<input type="button" name="registerCompany"
		value="<spring:message code="welcome.register.company" />"
		onclick="javascript: relativeRedir('company/create.do');" />&nbsp;
		
</security:authorize>

