<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="JSPBundle"/>
<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="flightsTitle"/></title>
    <link rel="stylesheet"
          type="text/css"
          href="<c:url value='resources/style.css'/>">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script>
        $(document).ready(function () {
            $.get(document.URL + "&pageNum=0",function(data) {
                $data.find(" #appendFlights tbody");
                alert(data);
            });
            //var divLoad = $('#hiddenDiv').load(document.URL + "&pageNum=0 #appendFlights tbody").html();
            //$('#appendFlights').html('<tbody>' + divLoad + '</tbody>');

            var pageNum = 1;
            if (pageNum >${requestScope.numPages}) document.getElementById("appendButton").remove();
            $("#appendButton").click(function () {
                alert("on click");
                var divLoad = $('#hiddenDiv').load(document.URL + "&pageNum=" + pageNum + " #appendFlights tbody").html();
                $('#appendFlights').append('<tbody>' + divLoad + '</tbody>').html();
                if (pageNum >=${requestScope.numPages}) document.getElementById("appendButton").remove();
                pageNum++;
            });
        });

    </script>
</head>
<body>
<div class="body">
    <jsp:include page="/WEB-INF/pages/_header.jsp"/>
    <div class="wrapper">
        <div class="filters">
            <form name="form1" class="search" action="doSearch" method="get">

                <div class="pdates">
                    <p class="filter"><fmt:message key="dateFrom"/></p>
                    <p class="filter"><fmt:message key="dateTo"/></p>
                </div>
                <div class="fdates">
                    <input class="calendar" type="date" name="dateFrom" value="${sessionScope.dateFrom}" required
                           pattern="[0-9]{4}-[0-9]{2}-[0-9]{2}">
                    <input class="calendar" type="date" name="dateTo" value="${sessionScope.dateTo}" required
                           pattern="[0-9]{4}-[0-9]{2}-[0-9]{2}">
                </div>

                <div class="pairports">
                    <p class="filter"><fmt:message key="depAirp"/></p>
                    <p class="filter"><fmt:message key="arrAirp"/></p>
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
                    <p class="filter"><fmt:message key="numPassengers"/>
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
                    <p class="filter"><fmt:message key="busClass"/>
                        <input class="fieldFilters" type="checkbox"
                        <c:if test="${sessionScope.business !=null}"> checked="checked" </c:if>
                               value="business" name="box">
                    </p>
                </div>
                <p class="error">${requestScope.nothingFound}</p>
                <p class="error">${requestScope.setFilters}</p>
                <p class="error">${requestScope.notEnoughPlaces}</p>
                <p class="error">${ticketsAdd}</p>
                <div class="psearchBut">
                    <p>
                        <input class="buttonSearch" type="submit" value="<fmt:message key="searchButton"/>"/>
                    </p>
                </div>
            </form>

            <hr class="headerLine">
        </div>

        <div class="flightTable">
            <table id="appendFlights">
                <thead>
                <tr>
                    <th><fmt:message key="tabFrom"/></th>
                    <th><fmt:message key="tabTo"/></th>
                    <th><fmt:message key="tabDateTime"/></th>
                    <th><fmt:message key="tabFlight"/></th>
                    <th><fmt:message key="tabCost"/></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${flights}" var="flight">
                    <form name="form2" id="form2${flight.flightId}" class="addTickets" action="addFlightToInvoice"
                          method="post">
                        <tr>
                            <td>${flight.departureAir.code} (${flight.departureAir.city})</td>
                            <td>${flight.arrivalAir.code} (${flight.arrivalAir.city})</td>
                            <td>${flight.dateTime}</td>
                            <td>${flight.flightNumber}</td>
                            <td>${flight.baseCost}</td>
                            <td>
                                <input id="num" class="fieldFilters" type="number" min="1"
                                       max="${sessionScope.numberTicketsFilter}" step="1"
                                       value="${sessionScope.numberTicketsFilter}" form="form2${flight.flightId}"
                                       name="numberTicketsFlight">
                                <input type="hidden" name="flightId" form="form2${flight.flightId}"
                                       value="${flight.flightId}">
                            </td>
                            <td>
                                <input class="buttonBucket" form="form2${flight.flightId}" type="submit"
                                       value="<fmt:message key="buyButton"/>"/>
                            </td>
                        </tr>
                    </form>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="appendButton">
            <c:if test="${requestScope.numPages!=null}">
                <button id="appendButton"><fmt:message key="showMore"/>
                </button>
            </c:if>
        </div>
        <div id="hiddenDiv"></div>
    </div>

    <jsp:include page="/WEB-INF/pages/_footer.jsp"/>
</div>

</body>
</html>
