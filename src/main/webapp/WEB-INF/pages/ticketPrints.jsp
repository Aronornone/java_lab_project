<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="foot" uri="/WEB-INF/tld/footer.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="JSPBundle"/>
<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="ticketPrintTitle"/></title>
    <link rel="stylesheet"
          type="text/css"
          href="<c:url value='resources/style.css'/>">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="resources/js/ticketPrint.js"></script>
</head>

<body>
<div class="body">
    <jsp:include page="/WEB-INF/pages/_header.jsp"/>
    <div class="wrapper">
        <div>
            <p class="invoices"><fmt:message key="payedInvoices"/></p>
            <p class="error"> ${noPayedInvoices}</p>
            <c:forEach items="${requestScope.invoices}" var="invoice">
                <div class="InvoiceInfo">
                    <button class="invoiceToggle"><fmt:message key="invoice"/> №:<b>${invoice.invoiceId}</b>
                        <fmt:message key="timeInvoice"/>:
                        <b>${invoice.timestamp}</b></button>

                    <c:forEach items="${invoice.tickets}" var="ticket">
                        <div class="ticketInfo">
                            <p><u><fmt:message key="ticket"/> <b>№${ticket.ticketId}</b></u></p>
                            <div class="printTickets">
                                <div class="firstBlock">
                                    <p><fmt:message key="tabFlight"/> <b>${ticket.flight.flightNumber}</b>
                                    <fmt:message key="tabDateTime"/> <b>${ticket.flight.dateTime}</b></p>
                                    <p><fmt:message key="depAirp"/> <b>${ticket.flight.departureAirport.code}</b>
                                    <fmt:message key="arrAirp"/> <b>${ticket.flight.arrivalAirport.code}</b>
                                    <fmt:message key="tabAirplane"/> <b>${ticket.flight.airplane.name}</b></p>
                                </div>
                                <div class="secondBlock">
                                    <p><fmt:message key="name"/> <b>${ticket.passengerName}</b>
                                    <fmt:message key="passport"/> <b>${ticket.passport}</b></p>
                                    <p><fmt:message key="place"/> <b>${ticket.sittingPlace}</b>
                                    <fmt:message key="lugg"/> <b>
                                            <c:if test="${ticket.luggage==true}"><fmt:message key="yes"/></c:if>
                                            <c:if test="${ticket.luggage==false}"><fmt:message key="no"/></c:if>
                                        </b>
                                    <fmt:message key="busClass"/> <b>
                                            <c:if test="${ticket.businessClass==true}"><fmt:message key="yes"/></c:if>
                                            <c:if test="${ticket.businessClass==false}"><fmt:message key="no"/></c:if>
                                        </b>
                                    <fmt:message key="price"/> <b>${ticket.price}</b></p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:forEach>
        </div>
    </div>
    <foot:show/>
</div>
</body>
</html>
