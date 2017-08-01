<%@ page import="java.net.URL" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>

<div class="header">

    <div class="logged">
        <div class="flagesLogged">
            <%String path = new URL(request.getRequestURL().toString()).getPath();%>
            <a href="language?locale=${"enLocale"}&backPage=<%=path%>"><img src="resources/images/flag_en_small.png"
                                                                            alt="en"></a>
            <a href="language?locale=${"ruLocale"}&backPage=<%=path%>"><img src="resources/images/flag_ru_small.png"
                                                                            alt="ru"></a>
        </div>
        <c:if test="${user != null}">
            <p>Вы/You: ${user.name}</p>
            <a href="bucket"><input class="buttonBucket" type="submit" value="Корзина/Bucket"></a>
            <form action="logout" method="post">
                <input class="buttonLogout" type="submit" value="Logout">
            </form>
        </c:if>
        <c:if test="${user == null}">
            <p>Войдите, чтобы получить возможность купить билет: </p>
            <form action="loginPage" method="post">
                <input class="buttonLogin" type="submit" value="Login">
            </form>
        </c:if>

    </div>
</div>


