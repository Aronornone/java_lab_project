<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Flights</title>
    <link rel="stylesheet"
          type="text/css"
          href="<c:url value='resources/style.css'/>">
</head>
<body>
<div class="body">
    <jsp:include page="/WEB-INF/pages/_header.jsp"/>
    <div class="wrapper">
        <div class="filters">
            <form name="form1" class="search" action="doSearch" method="get">

                <div class="pdates">
                    <p class="filter">Дата с </p>
                    <p class="filter">Дата по </p>
                </div>
                <div class="fdates">
                    <input class="calendar" type="date" name="dateFrom" value="${sessionScope.dateFrom}">
                    <input class="calendar" type="date" name="dateTo" value="${sessionScope.dateTo}">
                </div>

                <div class="pairports">
                    <p class="filter">Аэропорт вылета</p>
                    <p class="filter">Аэропорт прилета</p>
                </div>
                <div class="fairports">
                    <input name="selectedDeparture" pattern="[A-Z]{3}" list="DA-filter" class="checkFilter"
                           value="${sessionScope.departureF}">
                    <datalist id="DA-filter">
                        <c:forEach items="${departures}" var="departure">
                            <option value="${departure.code}">${departure.city} (${departure.airportName})
                            </option>
                        </c:forEach>
                    </datalist>

                    <input name="selectedArrival" pattern="[A-Z]{3}" list="AA-filter" class="checkFilter"
                           value="${sessionScope.arrivalF}">
                    <datalist id="AA-filter">
                        <c:forEach items="${arrivals}" var="arrival">
                            <option value="${arrival.code}">${arrival.city} (${arrival.airportName})</option>
                        </c:forEach>
                    </datalist>
                </div>

                <div class="pnumberTickets">
                    <p class="filter">Количество пассажиров
                        <c:if test="${sessionScope.numberTicketsFilter == null}">
                            <input class="fieldFilters" type="number" min="1" max="80" step="1"
                                   value="0" name="numberTicketsFilter">
                        </c:if>
                        <c:if test="${sessionScope.numberTicketsFilter != null}">
                            <input class="fieldFilters" type="number" min="1" max="80" step="1"
                                   value="${sessionScope.numberTicketsFilter}"
                                   name="numberTicketsFilter">
                        </c:if>
                    </p>
                </div>
                <p class="error">${requestScope.nothingFound}</p>
                <p class="error">${requestScope.setFilters}</p>
                <p class="error">${requestScope.notEnoughPlaces}</p>
                <div class="psearchBut">
                    <p>
                        <input class="buttonSearch" type="submit" value="Найти/Search">
                    </p>
                </div>
            </form>

            <hr class="headerLine">
        </div>
        <div class="flightTable">

            <table>
                <tr>
                    <th>From</th>
                    <th>To</th>
                    <th>Date and Time</th>
                    <th>Flight</th>
                    <th>Airplane</th>
                    <th>Cost</th>
                    <th></th>
                    <th></th>
                </tr>

                <c:forEach items="${flights}" var="flight">
                    <form name="form2" class="addTickets" action="addFlightToInvoice" method="post">
                        <tr>
                            <td>${flight.departureAirport.code} (${flight.departureAirport.city})</td>
                            <td>${flight.arrivalAirport.code} (${flight.arrivalAirport.city})</td>
                            <td>${flight.dateTime}</td>
                            <td>${flight.flightNumber}</td>
                            <td>${flight.airplane.name}</td>
                            <td>${flight.baseCost}</td>
                            <td>
                                <input id="num" class="fieldFilters" type="number" min="1"
                                       max="${sessionScope.numberTicketsFilter}" step="1"
                                       value="${sessionScope.numberTicketsFilter}" name="numberTicketsFlight">
                                <input type="hidden" name="flightId" value="${flight.flightId}">
                            </td>
                            <td>
                                <input class="buttonBucket" type="submit" value="Купить/Buy">
                            </td>
                        </tr>
                    </form>
                </c:forEach>

            </table>
        </div>

    </div>
    <jsp:include page="/WEB-INF/pages/_footer.jsp"/>
</div>

</body>
</html>
