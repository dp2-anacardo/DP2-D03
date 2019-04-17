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
<form:form action="application/hacker/create.do" modelAttribute="application">
	<form:hidden path="id" />
	<form:hidden path="moment"/>
	<form:hidden path="status"/>
	<form:hidden path="hacker"/>
	<form:hidden path="problem"/>
	
	<form:label path="curricula">
		<spring:message code="application.selectCurricula"/>
	</form:label>
	<form:select path="curricula">
		<form:options items="${curricula}" itemValue="id" itemLabel="id"/>
	</form:select>
	<form:errors cssClass="error" path="curricula" />
	
	<input type="submit" name="save"
			value="<spring:message code="application.save" />" />
			
	<acme:cancel url="position/hacker/list.do" code="application.cancel"/>
</form:form>
</security:authorize>