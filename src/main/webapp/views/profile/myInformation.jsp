
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('ADMIN')">

<acme:showtext code="actor.name" value="${administrator.name}" fieldset="true"/>
<br>
<acme:showtext code="actor.surname" value="${administrator.surname}" fieldset="true"/>
<br>
<acme:showtext code="actor.email" value="${administrator.email}" fieldset="true"/>
<br>
<acme:showtext code="actor.phoneNumber" value="${administrator.phoneNumber}" fieldset="true"/>
<br>
<acme:showtext code="actor.vatNumber" value="${administrator.vatNumeber}" fieldset="true"/>
<br>
<acme:showtext code="actor.address" value="${administrator.address}" fieldset="true"/>
<br>

<input type="button" name="Edit PD" value="<spring:message code="edit.PD" />"
			onclick="javascript: relativeRedir('/administrator/administrator/edit.do');" />
<input type="button" name="socialProfiles" value="<spring:message code="socialProfile" />"
			onclick="javascript: relativeRedir('/socialProfile/brotherhood,member,admin/list.do');" />
			
</security:authorize>
	
<security:authorize access="hasRole('HACKER')">

<acme:showtext code="actor.name" value="${hacker.name}" fieldset="true"/>
<br>
<acme:showtext code="actor.surname" value="${hacker.surname}" fieldset="true"/>
<br>
<acme:showtext code="actor.email" value="${hacker.email}" fieldset="true"/>
<br>
<acme:showtext code="actor.phoneNumber" value="${hacker.phoneNumber}" fieldset="true"/>
<br>
<acme:showtext code="actor.vatNumber" value="${hacker.vatNumeber}" fieldset="true"/>
<br>
<acme:showtext code="actor.address" value="${hacker.address}" fieldset="true"/>
<br>

<input type="button" name="Edit PD" value="<spring:message code="edit.PD" />"
			onclick="javascript: relativeRedir('hacker/hacker/edit.do');" />
<input type="button" name="socialProfiles" value="<spring:message code="socialProfile" />"
			onclick="javascript: relativeRedir('/socialProfile/brotherhood,member,admin/list.do');" />
			
</security:authorize>



<security:authorize access="hasRole('COMPANY')">

<acme:showtext code="actor.name" value="${company.name}" fieldset="true"/>
<br>
<acme:showtext code="actor.commercialName" value="${company.commercialName}" fieldset="true"/>
<br>
<acme:showtext code="actor.surname" value="${company.surname}" fieldset="true"/>
<br>
<acme:showtext code="actor.email" value="${company.email}" fieldset="true"/>
<br>
<acme:showtext code="actor.phoneNumber" value="${company.phoneNumber}" fieldset="true"/>
<br>
<acme:showtext code="actor.vatNumber" value="${company.vatNumeber}" fieldset="true"/>
<br>
<acme:showtext code="actor.address" value="${company.address}" fieldset="true"/>
<br>

<input type="button" name="Edit PD" value="<spring:message code="edit.PD" />"
			onclick="javascript: relativeRedir('/company/company/edit.do');" />
<input type="button" name="socialProfiles" value="<spring:message code="socialProfile" />"
			onclick="javascript: relativeRedir('/socialProfile/brotherhood,member,admin/list.do');" />

</security:authorize>

<acme:cancel url="/" code="messageBox.goBack"/>
