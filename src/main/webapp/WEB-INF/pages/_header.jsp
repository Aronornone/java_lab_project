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
                <fmt:message key="welcomeMessage1"/>
                <%--NEEDS REPOSITION--%>
                <div id="toggleOut">
                    <div id="usernameOut"> ${sessionScope.user.name}</div>
                    <div id="panOut">
                        <form action="logout" method="post">
                            <input class="buttonLogout" type="submit" value="<fmt:message key="logoutButton"/>">
                        </form>
                    </div>
                </div>
                <%----%>
                <c:if test="${sessionScope.lastServletPath =='/bucket' }">
                    <form action="flights" method="post">
                        <input class="goBackBut" type="submit" value="<fmt:message key="goBackBut"/>">
                    </form>
                </c:if>

                <form action="ticketsPrint" method="post">
                    <input class="goBackBut" type="submit" value="<fmt:message key="ticketPrintBut"/>">
                </form>

                <a href="bucket">
                    <c:if test="${sessionScope.ticketsInBucket != null}">
                        <input class="buttonBucket" type="submit"
                               value="<fmt:message key="cartButton"/>: ${ticketsInBucket} <fmt:message key="cartTickets"/>"/>
                    </c:if>
                    <c:if test="${sessionScope.ticketsInBucket == null}">
                        <input class="buttonBucket" type="submit"
                               value="<fmt:message key="cartButton"/>: 0 <fmt:message key="cartTickets"/>"/></c:if>
                </a>
            </c:if>
            <c:if test="${sessionScope.user == null}">
                <fmt:message key="welcomeMessage2"/>
                <%--NEEDS REPOSITION--%>
                <div id="toggleIn">
                    <div id="usernameIn">
                        Guest!
                    </div>
                    <div id="panIn">
                        <form action="loginPage" method="post">
                            <input class="buttonLogin" type="submit" value="<fmt:message key="loginButton"/>"/>
                        </form>
                    </div>
                </div>
                <%----%>

                <fmt:message key="welcomeMessage2.1"/>
            </c:if>
        </div>
    </div>
</div>

<%--Script to perform toggling--%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script>
    $(document).ready(function () {
        $("#usernameIn").click(function () {
            $("#panIn").slideToggle("fast");
        });
    });
</script>

<script>
    $(document).ready(function () {
        $("#usernameOut").click(function () {
            $("#panOut").slideToggle("fast");
        });
    });
</script>



