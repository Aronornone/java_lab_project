<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
         pageEncoding="US-ASCII" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
    <title>Login Success Page</title>
</head>
<body>

<c:set var = "username" scope = "session" value = "null"/>
<c:forEach items="${cookie}" var="cookies">
    <c:if test="${cookies.value.name=='user'}">
        <c:set var = "username" scope = "session" value = "${cookies.value.value}"/>
    </c:if>
    <c:if test="${username==null}">
        <c:redirect url = "/WEB-INF/pages/login.jsp"/>
    </c:if>
</c:forEach>

<h3>Hi ${username}, Login successful.</h3>
<br>
<form action="LogoutServlet" method="post">
    <input type="submit" value="Logout">
</form>
</body>
</html>