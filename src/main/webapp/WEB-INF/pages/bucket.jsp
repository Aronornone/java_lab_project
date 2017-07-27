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
            <form name="ticket" action="ticket" method="post">
                <div class="bucketTable">

                    <div class="passenger">
                        <p class="passengerNum">Passenger 1</p>
                        <table class="buckettable">
                            <tr>
                                <th>
                                    First Name
                                </th>
                                <th>
                                    Last Name
                                </th>
                                <th>
                                    Passport
                                </th>
                            </tr>
                            <tr>
                                <td>
                                    <input type="text" value="">
                                </td>
                                <td>
                                    <input type="text" value="">
                                </td>
                                <td>
                                    <input type="text" value="">
                                </td>
                                <td>
                                </td>
                            </tr>
                        </table>
                        <div class="wrapOptions">
                            <div class="options">
                                <p>
                                    Бизнес-класс/Business-class <input class="checkbox" type="checkbox" value="0">
                                    Багаж/Luggage <input class="checkbox" type="checkbox" value="0">
                                </p>
                            </div>
                            <div class="pasCost">
                                <div class="pcost">Стоимость:</div>
                                <div class="cost">10000 р.</div>
                            </div>
                        </div>
                        <hr class="headerLine">
                    </div>


                    <div class="passenger">
                        <p class="passengerNum">Passenger 2</p>
                        <table class="buckettable">
                            <tr>
                                <th>
                                    First Name
                                </th>
                                <th>
                                    Last Name
                                </th>
                                <th>
                                    Passport
                                </th>
                            </tr>
                            <tr>
                                <td>
                                    <input type="text" value="">
                                </td>
                                <td>
                                    <input type="text" value="">
                                </td>
                                <td>
                                    <input type="text" value="">
                                </td>
                                <td>
                                </td>
                            </tr>
                        </table>
                        <div class="wrapOptions">
                            <div class="options">
                                <p>
                                    Бизнес-класс/Business-class <input class="checkbox" type="checkbox" value="0">
                                    Багаж/Luggage <input class="checkbox" type="checkbox" value="0">
                                </p>
                            </div>
                            <div class="pasCost">
                                <div class="pcost">Стоимость:</div>
                                <div class="cost">10000 р.</div>
                            </div>
                        </div>
                        <hr class="headerLine">
                    </div>

                    <div class="butPay">
                        <div class="pCostTotal">Итого/Total:</div>
                        <div class="costTotal">20000 р.</div>

                        <input class="bucketPay" type="submit" value="Оплатить/Pay">
                    </div>
                </div>
            </form>
        </div>
    </div>
    <jsp:include page="/WEB-INF/pages/_footer.jsp"/>
</div>
</body>
</html>
