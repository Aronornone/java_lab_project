<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Index</title>
    <link rel="stylesheet"
          type="text/css"
          href="<c:url value='resources/style.css'/>">

</head>
<body>
    <h1>Pages:</h1>
    <form action="bucket" method="post">
        <input type="submit" value="To Cookie Page">
    </form>

    <form action="hello" method="post">
        <input type="submit" value="To Hello Page">
    </form>
</body>
</html>
