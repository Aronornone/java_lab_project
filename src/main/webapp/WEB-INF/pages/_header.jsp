<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<div class="header">

    <div class="logged">
        <div class="flagesLogged">
            <a href="login"><img src="resources/images/flag_en_small.png" alt="en"></a>
            <a href="login"><img src="resources/images/flag_ru_small.png" alt="ru"></a>
        </div>
        <p>Вы/You: ${user.name}</p>
        <a href="bucket"><input class="bucketBut" type="submit" value="Корзина/Bucket"></a>
        <form action="logout" method="post">
            <input class="logoutBut" type="submit" value="Logout">
        </form>

    </div>
    </div>


