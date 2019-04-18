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

<security:authorize access="hasRole('COMPANY')">

<acme:showtext fieldset="true" code="hacker.name" value="${hacker.name}"/>
<acme:showtext fieldset="true" code="hacker.surname" value="${hacker.surname}"/>

<fieldset><legend><spring:message code="hacker.photo"/></legend>
	<a href='${hacker.photo}'>${hacker.photo}</a>
</fieldset>

<acme:showtext fieldset="true" code="hacker.email" value="${hacker.email}"/>
<acme:showtext fieldset="true" code="hacker.address" value="${hacker.address}"/>
		
</security:authorize>
