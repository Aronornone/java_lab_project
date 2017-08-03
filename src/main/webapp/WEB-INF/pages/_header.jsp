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
        <div class="rightHeader">
            <c:if test="${sessionScope.user != null}">
                <p><fmt:message key="welcomeMessage1"/> ${sessionScope.user.name}</p>
                <a href="bucket">
                    <c:if test="${sessionScope.ticketsInBucket != null}">
                        <input class="buttonBucket" type="submit"
                               value="<fmt:message key="cartButton"/>: ${ticketsInBucket} <fmt:message key="cartTickets"/>"/>
                    </c:if>
                    <c:if test="${sessionScope.ticketsInBucket == null}">
                        <input class="buttonBucket" type="submit"
                               value="<fmt:message key="cartButton"/>: 0 <fmt:message key="cartTickets"/>"/></c:if>
                </a>
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
</div>


