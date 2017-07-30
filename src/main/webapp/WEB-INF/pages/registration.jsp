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
            <a href="registration"><img src="resources/images/flag_en.png" alt="en"></a>
            <a href="registration"><img src="resources/images/flag_ru.png" alt="ru"></a>
        </div>
        <p class="error">${passwordsNotEqual}</p>
        <p class="error">${passwordsEmpty}</p>
        <p class="error">${userAlreadyExist}</p>

        <form action="doReg" method="post">
            <p>Логин/Login (email): <input class="fieldLogReg" type="email" name="email" value="${email}"></p>
            <p>Отображаемое Имя/Displayed Name: <input class="fieldLogReg" type="text" name="username" value="${username}"></p>
            <p>Пароль/Password: <input class="fieldLogReg" type="password" name="password1"></p>
            <p>Повторите пароль/Repeat password: <input class="fieldLogReg" type="password" name="password2"></p>
            <input class="buttonLogReg" type="submit" value="Register/Регистрация">
            <p><a href="loginPage">Вход\Login</a></p>
        </form>
    </div>
</div>
</body>
</html>
