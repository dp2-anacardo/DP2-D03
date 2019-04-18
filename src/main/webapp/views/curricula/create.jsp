<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="curricula/hacker/create.do" modelAttribute="personalD">

    <security:authorize
            access="hasAnyRole('HACKER')">


        <acme:textbox code="curricula.personal.name" path="fullName" />
        <br

        <acme:textbox code="curricula.personal.statement" path="statement" />
        <br>

        <acme:textbox code="curricula.personal.phoneNumber" path="phoneNumber" />
        <br>

        <acme:textbox code="curricula.personal.githubProfile" path="githubProfile" />
        <br>

        <acme:textbox code="curricula.personal.linkedInProfile" path="linkedInProfile" />
        <br>

        <acme:submit name="save" code="curricula.save"/>

        <acme:cancel url="curricula/hacker/list.do" code="curricula.cancel"/>

    </security:authorize>
</form:form>