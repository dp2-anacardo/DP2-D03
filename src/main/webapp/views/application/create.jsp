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

<security:authorize access="hasRole('BROTHERHOOD')">
<form:form action="parade/brotherhood/edit.do" modelAttribute="parade">
	<form:hidden path="id" />
	<acme:textbox code="parade.title" path="title"/>
	<acme:textarea code="parade.description" path="description"/>
	<acme:textbox code="parade.maxRow" path="maxRow"/>
	<acme:textbox code="parade.maxColumn" path="maxColumn"/>
	<acme:textbox code="parade.date" path="moment"/>
	<form:label path="floats">
		<spring:message code="parade.floats"/>
	</form:label>
	<form:select path="floats">	
		<form:options items="${floats}" itemValue="id" itemLabel="title"
			/>
	</form:select>
	<form:errors cssClass="error" path="floats" />
	<br />
	
	
	
	<jstl:if test="${parade.isFinal == false }">
		<acme:submit name="saveFinal" code="parade.saveFinal"/>
		<acme:submit name="saveDraft" code="parade.saveDraft"/>
	</jstl:if>
	<jstl:if test="${parade.isFinal == true }">
		<acme:submit name="saveFinal" code="parade.save"/>
	</jstl:if>
	<jstl:if test="${parade.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="parade.delete" />" />
	</jstl:if>
	<acme:cancel url="parade/brotherhood/list.do" code="parade.cancel"/>
	<br />
	
	
	
</form:form>
</security:authorize>