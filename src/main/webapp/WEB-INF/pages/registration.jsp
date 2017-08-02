<%@ page import="java.net.URL" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="JSPBundle"/>
<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="register"/> </title>
    <link rel="stylesheet"
          type="text/css"
          href="<c:url value='resources/style.css'/>">
</head>
<body>
<div class="body">
    <div class="formLogReg">
        <div class="flagesLogReg">
            <%String path = new URL(request.getRequestURL().toString()).getPath();%>
            <a href="language?locale=${"enLocale"}&backPage=<%=path%>"><img src="resources/images/flag_en.png" alt="en"></a>
            <a href="language?locale=${"ruLocale"}&backPage=<%=path%>"><img src="resources/images/flag_ru.png" alt="ru"></a>
        </div>
        <p class="error">${passMismatch}</p>
        <p class="error">${fieldEmpty}</p>
        <p class="error">${userAlreadyExists}</p>

        <form action="doReg" method="post">
            <p><fmt:message key="loginLable"/> :<input class="fieldLogReg" type="email" name="email" value="${email}"></p>
            <p><fmt:message key="dispName"/> :<input class="fieldLogReg" type="text" name="username"
                                                       value="${username}"></p>
            <p><fmt:message key="passLable"/> :<input class="fieldLogReg" type="password" name="password1"></p>
            <p><fmt:message key="repPass"/> :<input class="fieldLogReg" type="password" name="password2"></p>
            <input class="buttonLogReg" type="submit" value="<fmt:message key="regButton"/>"/>
            <p><a href="loginPage"><fmt:message key="loginButton"/> </a></p>
        </form>
    </div>
</div>
</body>
</html>
