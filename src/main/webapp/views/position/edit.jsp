<%@page language="java" contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="position/company/create.do" modelAttribute="position">

    <security:authorize
            access="hasAnyRole('COMPANY')">


        <acme:textbox code="position.title" path="title" />
        <br

        <acme:textbox code="position.description" path="description" />
        <br>

        <acme:textbox code="position.deadline" path="deadline" />
        <br>

        <acme:textbox code="position.profile" path="profile" />
        <br>

        <acme:textbox code="position.salary" path="salary" />
        <br>

        <acme:submit name="save" code="curricula.save"/>

        <acme:cancel url="curricula/hacker/list.do" code="curricula.cancel"/>

    </security:authorize>
</form:form>