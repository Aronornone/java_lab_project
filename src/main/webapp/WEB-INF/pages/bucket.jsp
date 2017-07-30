<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bucket</title>
    <link rel="stylesheet"
          type="text/css"
          href="<c:url value='resources/style.css'/>">
</head>


<body>
<div class="body">
    <jsp:include page="/WEB-INF/pages/_header.jsp"/>
    <div class="wrapper">
        <div class="bucket">

            <div class="bucketTable">
                <div class="flightInfo">
                    <p class="error"> ${bucketEmpty}</p>
                    Рейс/Flight: <b>${flightNumber}</b> из/from: <b>${departureAirport.name}</b> в/to:
                    <b>${arrivalAirport.name}</b>.
                    Дата и время/ DateTime: <b>${dateTime}</b> Cамолет/Airplane: <b>${airplanename}</b>
                </div>

                <c:forEach items="${tickets}" var="tickets">
                    <div class="passenger">
                        <p class="passengerNum">Пассажир/Passenger</p>
                        <table class="buckettable">
                            <tr>
                                <th>Фамилия и имя/First and Last Name</th>
                                <th>Паспорт/ID card</th>
                                <th class="tableButDel"></th>
                            </tr>
                            <tr>
                                <td><input class="passengerfield" type="text" name="passengerName" value=""></td>
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
                                <p>Бизнес-класс/Business-class <input class="checkbox" name="business" type="checkbox"
                                                                      value="0">
                                    Багаж/Luggage <input class="checkbox" name="luggage" type="checkbox" value="0"></p>
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

                <div class="butPay">
                    <div class="pCostTotal">Итого/Total:</div>
                    <div class="costTotal">${totalSum}</div>
                    <!--TODO: Добавить логику на кнопку сохранения внесенных в билеты данных-->
                    <a href="save"><input class="buttonBucketSave" type="submit" value="Сохранить/Save"></a>
                    <form name="ticket" action="ticketPay" method="post">
                        <input class="buttonBucketPay" type="submit" value="Оплатить/Pay">
                    </form>
                </div>

            </div>
        </div>
    </div>
    <jsp:include page="/WEB-INF/pages/_footer.jsp"/>
</div>
</body>
</html>
