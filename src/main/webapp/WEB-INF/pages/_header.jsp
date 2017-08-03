<%@ page import="java.net.URL" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="JSPBundle"/>
<!DOCTYPE html>
<div class="header">
    <div class="logged">
        <div class="flagesLogged">
            <%
                String pageName = (String) session.getAttribute("lastServletPath");
            %>
            <form action="language" method="post">
                <a href="javascript:" onclick="parentNode.submit();"><img src="resources/images/flag_en_small.png"
                                                                          alt="en"></a>
                <input type="hidden" name="locale" value="enLocale">
                <input type="hidden" name="backPage" value="<%=pageName%>">
            </form>
            <form action="language" method="post">
                <a href="javascript:" onclick="parentNode.submit();"><img src="resources/images/flag_ru_small.png"
                                                                          alt="ru"></a>
                <input type="hidden" name="locale" value="ruLocale">
                <input type="hidden" name="backPage" value="<%=pageName%>">
            </form>

        </div>
        <c:if test="${sessionScope.user != null}">
            <p><fmt:message key="welcomeMessage1"/> ${sessionScope.user.name}</p>
            <p class="error"><fmt:message key="ticketInCart"/> : ${ticketsInBucket}</p>
            <a href="bucket"><input class="buttonBucket" type="submit" value="<fmt:message key="cartButton"/>"/></a>
            <form action="logout" method="post">
                <input class="buttonLogout" type="submit" value="<fmt:message key="logoutButton"/>">
            </form>
        </c:if>
        <c:if test="${sessionScope.user == null}">
            <p><fmt:message key="welcomeMessage2"/>: </p>
            <form action="loginPage" method="post">
                <input class="buttonLogin" type="submit" value="<fmt:message key="loginButton"/>"/>
            </form>
        </c:if>

    </div>
</div>


