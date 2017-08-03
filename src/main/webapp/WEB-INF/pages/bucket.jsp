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
                    <c:forEach items="${requestScope.flights}" var="flight">
                        <div class="flightInfo">
                            <fmt:message key="tabFlight"/>: <b>${flight.flightNumber}</b> <fmt:message key="tabFrom"/>:
                            <b>${flight.departureAirport.code} (${flight.departureAirport.airportName})</b>
                            <fmt:message key="tabTo"/>: <b>${flight.arrivalAirport.code}
                            (${flight.arrivalAirport.airportName})</b>.
                            <fmt:message key="tabDateTime"/>: <b>${flight.dateTime}</b> <fmt:message key="tabAirplane"/>:
                            <b>${flight.airplane.name}</b>
                        </div>
                        <c:forEach items="${flight.tickets}" var="ticket">
                            <div class="passenger">
                                <p class="passengerNum"><fmt:message key="passenger"/></p>
                                <table class="buckettable">
                                    <tr>
                                        <th><fmt:message key="name"/></th>
                                        <th><fmt:message key="passport"/></th>
                                        <th><fmt:message key="lugg"/></th>
                                        <th><fmt:message key="place"/></th>
                                        <th><fmt:message key="price"/></th>
                                        <th class="tableButDel"></th>
                                    </tr>
                                    <tr>
                                        <td><input class="passengerfield" type="text" name="passengerName" value="">
                                        </td>
                                        <td><input type="text" name="passport" value=""></td>
                                        <td>
                                            <div class="tableBucketField">
                                                <input class="checkbox" name="luggage" type="checkbox" value="0">
                                            </div>
                                        </td>
                                        <td>
                                            <div class="tableBucketField">${ticket.sittingPlace}</div>
                                        </td>
                                        <td>
                                            <div class="tableBucketField">${ticket.price}</div>
                                        </td>
                                        <td>
                                            <form name="ticketDelete" action="ticketDelete" method="post">
                                            <input type="hidden" name="ticketId" value="${ticket.ticketId}">
                                                <input class="buttonDelete" type="submit"
                                                       value="<fmt:message key="delete"/>">
                                            </form>
                                        </td>
                                    </tr>
                                </table>
                                <hr class="headerLineTicket">
                            </div>
                        </c:forEach>
                        <hr class="headerLineFlight">
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
