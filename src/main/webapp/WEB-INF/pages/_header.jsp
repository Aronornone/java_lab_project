<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="JSPBundle"/>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="resources/js/_header.js"></script>
</head>
<!DOCTYPE html>
<div class="header">
    <div class="logged">
        <div class="flagesLogged">
            <form action="language" method="post">
                <a href="javascript:" onclick="parentNode.submit();"><img src="resources/images/flag_en_small.png"
                                                                          alt="en"></a>
                <input type="hidden" name="locale" value="enLocale">
                <input type="hidden" name="backPage" value="${sessionScope.lastServletPath}">
            </form>
            <form action="language" method="post">
                <a href="javascript:" onclick="parentNode.submit();"><img src="resources/images/flag_ru_small.png"
                                                                          alt="ru"></a>
                <input type="hidden" name="locale" value="ruLocale">
                <input type="hidden" name="backPage" value="${sessionScope.lastServletPath}">
            </form>
        </div>

        <div class="rightHeader">
            <c:if test="${sessionScope.user != null}">
                <fmt:message key="welcomeMessage1"/> ${sessionScope.user.name}!
                <a href="bucket">
                    <button class="buttonBucketMenu"><fmt:message key="cartButton"/>: &nbsp;<div id="ticketBucket">${ticketsInBucket}</div>
                        &nbsp;<fmt:message key="cartTickets"/></button>
                </a>

                <ul class="menu">
                    <li class="menuButton"><fmt:message key="go"/>
                        <ul>
                            <div class="menuBlock">
                                <li>
                                    <form action="flights" method="post">
                                        <input class="buttonMenu" type="submit" value="<fmt:message key="flightsBut"/>">
                                    </form>
                                </li>
                                <li>
                                    <form action="ticketsPrint" method="post">
                                        <input class="buttonMenu" type="submit"
                                               value="<fmt:message key="ticketPrintBut"/>">
                                    </form>
                                </li>
                                <li class="last">
                                    <form action="logout" method="post">
                                        <input class="buttonMenu" type="submit"
                                               value="<fmt:message key="logoutButton"/>">
                                    </form>
                                </li>
                            </div>
                        </ul>
                    </li>
                </ul>

            </c:if>
            <c:if test="${sessionScope.user == null}">
                <fmt:message key="welcomeMessage2"/>! <fmt:message key="welcomeMessage2.1"/>.
                <form action="loginPage" method="post">
                    <input class="buttonLogin" type="submit" value="<fmt:message key="loginButton"/>"/>
                </form>
            </c:if>
        </div>
    </div>
</div>





