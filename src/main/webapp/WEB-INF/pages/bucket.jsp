<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="JSPBundle"/>
<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="bucketTitle"/></title>
    <link rel="stylesheet"
          type="text/css"
          href="<c:url value='resources/style.css'/>">
</head>

<body>
<div class="body">
    <jsp:include page="/WEB-INF/pages/_header.jsp"/>
    <p class="error"> ${cartEmpty}</p>
    <div class="wrapper">
        <div class="bucket">
            <div class="bucketTable">
                <div class="flightList">
                    <c:forEach items="${requestScope.ticketsFlights}" var="flight">
                        <div class="flightInfo">
                            <fmt:message key="tabFlight"/>: <b>${flight.key.flightNumber}</b><fmt:message key="tabFrom"/>:
                            <b>${flight.key.departureAirport.code} (${flight.key.departureAirport.airportName})</b>
                            <fmt:message key="tabTo"/>:<b>${flight.key.arrivalAirport.code} (${flight.key.arrivalAirport.airportName})</b>.
                            <fmt:message key="tabDateTime"/>: <b>${flight.key.dateTime}</b> <fmt:message key="tabAirplane"/>:
                            <b>${flight.key.airplane.name}</b>
                        </div>
                        <!--<%--
                        <c:forEach items="${flight.value}" var="tickets">
                            <div class="passenger">
                                <p class="passengerNum"><fmt:message key="passenger"/></p>
                                <table class="buckettable">
                                    <tr>
                                        <th><fmt:message key="name"/></th>
                                        <th><fmt:message key="passport"/></th>
                                        <th class="tableButDel"></th>
                                    </tr>
                                    <tr>
                                        <td><input class="passengerfield" type="text" name="passengerName" value="">
                                        </td>
                                        <td><input type="text" name="passport" value=""></td>
                                        <td>
                                            <input type="hidden" name="ticketId" value="${tickets.ticketId}">
                                            <form name="ticketDelete" action="ticketDelete" method="post">
                                                <input class="buttonDelete" type="submit"
                                                       value="<fmt:message key="delete"/>">
                                            </form>
                                        </td>
                                    </tr>
                                </table>
                                <div class="wrapOptions">
                                    <div>
                                        <p><fmt:message key="busClass"/>
                                            <input class="checkbox" name="business" type="checkbox" value="0">
                                            <fmt:message key="lugg"/>
                                            <input class="checkbox" name="luggage" type="checkbox" value="0">
                                        </p>
                                    </div>
                                    <div class="pasCost">
                                        <div class="pplace"><fmt:message key="place"/></div>
                                        <div class="place">${tickets.sittingPlace}</div>
                                        <div class="pcost"><fmt:message key="price"/>:</div>
                                        <div class="cost">${tickets.price}</div>
                                    </div>
                                </div>
                                <hr class="headerLine">
                            </div>
                        </c:forEach>--%>
                        -->
                    </c:forEach>
                </div>

                <div class="butPay">
                    <div class="pCostTotal"><fmt:message key="total"/> :</div>
                    <div class="costTotal">${totalSum}</div>
                    <!--TODO: Добавить логику на кнопку сохранения внесенных в билеты данных-->
                    <a href="save">
                        <input class="buttonBucketSave" type="submit" value="<fmt:message key="saveButton"/> "/>
                    </a>
                    <form name="ticket" action="ticketPay" method="post">
                        <input class="buttonBucketPay" type="submit" value="<fmt:message key="pay"/> "/>
                    </form>
                </div>

            </div>
        </div>
    </div>
    <jsp:include page="/WEB-INF/pages/_footer.jsp"/>
</div>
</body>
</html>
