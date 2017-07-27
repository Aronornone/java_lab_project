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
            <form class="search" action="search" method="post">
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
                    <select class="checkFilter">
                        <option>Пункт 1</option>
                        <option>Пункт 2</option>
                    </select>
                    <select class="checkFilter">
                        <option>Пункт 1</option>
                        <option>Пункт 2</option>
                    </select>
                </div>
                <div class="pnumberTickets">
                    <p class="filter">Количество пассажиров
                        <input class="fieldFilters" type="number" min="1" max="80" step="1" value="1"
                               name="numberTickets">
                    </p>
                </div>
                <div class="psearchBut">
                    <p>
                        <input class="searchBut" type="submit" value="Найти/Search">
                    </p>
                </div>
            </form>
            <hr class="headerLine">
        </div>
        <div class="flightTable">

            <table>
                <tr>
                    <th>
                        From
                    </th>
                    <th>
                        To
                    </th>
                    <th>
                        Date and Time
                    </th>
                    <th>
                        Flight
                    </th>
                    <th>
                        Airplane
                    </th>
                    <th>
                        Cost
                    </th>
                    <th>

                    </th>
                </tr>
                <c:forEach items="${flights}" var="flights">
                    <tr>
                        <td>
                                ${flights.departureAirport}
                        </td>
                        <td>
                                ${flights.arrivalAirport}
                        </td>
                        <td>
                                ${flights.dateTime}
                        </td>
                        <td>
                                ${flights.flightNumber}
                        </td>
                        <td>
                                ${flights.airplane.name}
                        </td>
                        <td>
                                ${flights.baseCost}
                        </td>
                        <td>
                            <form class="buttonBuy" action="bucket" method="post">
                                <input class="bucketBut" type="submit" value="Купить/Buy">
                            </form>
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
