<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Example with Cookies and Session</title>
    <link rel="stylesheet"
          type="text/css"
          href="<c:url value='resources/style.css'/>">

</head>
<body>
<h2>Some text JSP</h2>
<form action="index" method="post">
    <input type="submit" value="To Index">
</form>
Cookies List:
<table>
<c:forEach items="${cookie}" var="cookies">
    <tr>
        <td>Name: ${cookies.value.name}</td>
        <td>Value: ${cookies.value.value}</td>
        <td>Version: ${cookies.value.version}</td>
    </tr>
</c:forEach>
</table>
</br>
<p>Some parameters from Servlet Class:</p>
<c:forEach items="${list}" var="string">
    <tr>
        <td>${string}</td> </br>
    </tr>
</c:forEach>

Id session: ${sessionId}
Last Time session: ${lastTime}

</body>
</html>
