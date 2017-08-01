<%@ page import="java.net.URL" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Registration</title>
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
            <p>Логин/Login (email): <input class="fieldLogReg" type="email" name="email" value="${email}"></p>
            <p>Отображаемое Имя/Displayed Name: <input class="fieldLogReg" type="text" name="username"
                                                       value="${username}"></p>
            <p>Пароль/Password: <input class="fieldLogReg" type="password" name="password1"></p>
            <p>Повторите пароль/Repeat password: <input class="fieldLogReg" type="password" name="password2"></p>
            <input class="buttonLogReg" type="submit" value="Register/Регистрация">
            <p><a href="loginPage">Вход\Login</a></p>
        </form>
    </div>
</div>
</body>
</html>
