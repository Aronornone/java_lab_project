<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="JSPBundle"/>
<!DOCTYPE html>
<html>
<head>
    <title><fmt:message key="bucketTitle"/> </title>
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
                <div class="flightInfo">
                    <p class="error"> ${cartEmpty}</p>
                    <fmt:message key="tabFlight"/> <b>${flightNumber}</b> <fmt:message key="tabFrom"/> : <b>${departureAirport.name}</b> <fmt:message key="tabTo"/> :
                    <b>${arrivalAirport.name}</b>.
                    <fmt:message key="tabDateTime"/> : <b>${dateTime}</b><fmt:message key="tabAirplane"/> : <b>${airplanename}</b>
                </div>

                <c:forEach items="${tickets}" var="tickets">
                    <div class="passenger">
                        <p class="passengerNum"><fmt:message key="passenger"/> </p>
                        <table class="buckettable">
                            <tr>
                                <th><fmt:message key="name"/> </th>
                                <th><fmt:message key="passport"/> </th>
                                <th class="tableButDel"></th>
                            </tr>
                            <tr>
                                <td><input class="passengerfield" type="text" name="passengerName" value=""></td>
                                <td><input type="text" name="passport" value=""></td>
                                <td>
                                    <form name="ticketDelete" action="ticketDelete" method="post">
                                        <input class="buttonDelete" type="submit" value="<fmt:message key="delete"/> ">
                                    </form>
                                </td>
                            </tr>
                        </table>
                        <div class="wrapOptions">
                            <div>
                                <p><fmt:message key="busClass"/> <input class="checkbox" name="business" type="checkbox"
                                                                      value="0">
                                    <fmt:message key="lugg"/> <input class="checkbox" name="luggage" type="checkbox" value="0"></p>
                            </div>
                            <div class="pasCost">
                                <div class="pplace"><fmt:message key="place"/> :</div>
                                <div class="place">${tickets.sittingPlace}</div>
                                <div class="pcost"><fmt:message key="price"/> :</div>
                                <div class="cost">${tickets.price}</div>
                            </div>
                        </div>
                        <hr class="headerLine">
                    </div>

                </c:forEach>
                <div>
                    <c:forEach items="${requestScope.flights}" var="flight">
                        <div class="flightInfo">
                            Рейс/Flight: <b>${flight.flightNumber}</b> из/from: <b>${flight.departureAirport.name}</b>
                            в/to:
                            <b>${flight.arrivalAirport.name}</b>.
                            Дата и время/ DateTime: <b>${flight.dateTime}</b> Cамолет/Airplane:
                            <b>${flight.airplanename}</b>
                        </div>
                        <c:forEach items="${flightTickets}" var="tickets">
                            <div class="passenger">
                                <p class="passengerNum">Пассажир/Passenger</p>
                                <table class="buckettable">
                                    <tr>
                                        <th>Фамилия и имя/First and Last Name</th>
                                        <th>Паспорт/ID card</th>
                                        <th class="tableButDel"></th>
                                    </tr>
                                    <tr>
                                        <td><input class="passengerfield" type="text" name="passengerName" value="">
                                        </td>
                                        <td><input type="text" name="passport" value=""></td>
                                        <td>
                                            <form name="ticketDelete" action="ticketDelete" method="post">
                                                <input class="buttonDelete" type="submit" value="Удалить/Delete">
                                            </form>
                                        </td>
                                    </tr>
                                </table>
                                <div class="wrapOptions">
                                    <div>
                                        <p>Бизнес-класс/Business-class <input class="checkbox" name="business"
                                                                              type="checkbox"
                                                                              value="0">
                                            Багаж/Luggage <input class="checkbox" name="luggage" type="checkbox"
                                                                 value="0">
                                        </p>
                                    </div>
                                    <div class="pasCost">
                                        <div class="pplace">Ваше место/Your seat:</div>
                                        <div class="place">${tickets.sittingPlace}</div>
                                        <div class="pcost">Стоимость/Cost:</div>
                                        <div class="cost">${tickets.price}</div>
                                    </div>
                                </div>
                                <hr class="headerLine">
                            </div>
                        </c:forEach>
                    </c:forEach>
                </div>

                <div class="butPay">
                    <div class="pCostTotal"><fmt:message key="total"/> :</div>
                    <div class="costTotal">${totalSum}</div>
                    <!--TODO: Добавить логику на кнопку сохранения внесенных в билеты данных-->
                    <a href="save"><input class="buttonBucketSave" type="submit" value="<fmt:message key="saveButton"/> "/></a>
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
