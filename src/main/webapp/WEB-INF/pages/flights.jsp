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
            <form class="search" action="doSearch" method="post">
                <div class="pdates">
                    <p class="filter">Дата с </p>
                    <p class="filter">Дата по </p>
                </div>
                <div class="fdates">
                    <input class="calendar" type="date" name="dateFrom">
                    <input class="calendar" type="date" name="dateTo">
                </div>
                <div class="pairports">

                    <p class="filter">Аэропорт вылета</p>
                    <p class="filter">Аэропорт прилета</p>
                </div>
                <div class="fairports">
                    <input name="selectedDeparture" list="DA-filter" class="checkFilter">
                    <datalist id="DA-filter">
                        <c:forEach items="${departures}" var="departure">
                            <option value="${departure.name}">${departure.name} (${departure.city})</option>
                        </c:forEach>
                    </datalist>
                    <input name="selectedArrival" list="AA-filter" class="checkFilter">
                    <datalist id="AA-filter">
                        <c:forEach items="${arrivals}" var="arrival">
                            <option value="${arrival.name}">${arrival.name} (${arrival.city})</option>
                        </c:forEach>
                    </datalist>
                </div>
                <div class="pnumberTickets">
                    <p class="filter">Количество пассажиров
                        <input class="fieldFilters" type="number" min="1" max="80" step="1" value="1"
                               name="numberTickets">
                    </p>
                </div>
                <p class="error">${nothingFound}</p>
                <p class="error">${insertFilters}</p>
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
                </tr>

                <c:forEach items="${flights}" var="flight">
                    <tr>
                        <td>${flight.departureAirport.name} (${flight.departureAirport.city}) </td>
                        <td>${flight.arrivalAirport.name} (${flight.arrivalAirport.city})</td>
                        <td>${flight.dateTime}</td>
                        <td>${flight.flightNumber}</td>
                        <td>${flight.airplane.name}</td>
                        <td>${flight.baseCost}</td>
                        <td>
                            <a href="bucket?flightId=${flight.flightId}"><input class="buttonBucket" type="submit"
                                                                                value="Купить/Buy"></a>
                        </td>
                    </tr>
                </c:forEach>

            </table>
        </div>

    </div>
    <jsp:include page="/WEB-INF/pages/_footer.jsp"/>
</div>

</body>
</html>
