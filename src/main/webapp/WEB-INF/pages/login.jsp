<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="JSPBundle"/>
<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="loginTitle"/> </title>
    <link rel="stylesheet"
          type="text/css"
          href="<c:url value='resources/css/style.css'/>">
</head>

<body>
<div class="body">
    <div class="formLogReg">
        <div class="flagesLogged">
            <%
                String pageName = (String) session.getAttribute("lastServletPath");
            %>
            <form action="language" method="post">
                <a href="javascript:" onclick="parentNode.submit();"><img src="resources/images/flag_en.png"
                                                                          alt="en"></a>
                <input type="hidden" name="locale" value="enLocale">
                <input type="hidden" name="backPage" value="<%=pageName%>">
            </form>
            <form action="language" method="post">
                <a href="javascript:" onclick="parentNode.submit();"><img src="resources/images/flag_ru.png"
                                                                          alt="ru"></a>
                <input type="hidden" name="locale" value="ruLocale">
                <input type="hidden" name="backPage" value="<%=pageName%>">
            </form>

        </div>
        <p class="error">${nonexistentLogin}</p>
        <p class="error">${regSuccess}</p>
        <p class="error">${loginFailed}</p>
        <p class="error">${fieldEmpty}</p>
        <form action="doLogin" method="post">
            <p><fmt:message key="loginLable"/> <input class="fieldLogReg" type="email" name="email" value="${requestScope.email}"></p>
            <p><fmt:message key="passLable"/> <input class="fieldLogReg" type="password" name="password"></p>
            <input class="buttonLogReg" type="submit" value="<fmt:message key="loginButton"/>"/>
            <p><a href="regPage"><fmt:message key="register"/> </a></p>
        </form>
    </div>
</div>
</body>
</html>
