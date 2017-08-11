<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="foot" uri="/WEB-INF/tld/footer.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error page</title>
    <link rel="stylesheet"
          type="text/css"
          href="<c:url value='resources/css/style.css'/>">
</head>
<body>
<div class="body">
    <div class="wrapper">
        <div class="blockError">
            <div id="errorDiv1">Error!</div>
            <div id="errorDiv2">Page not found!</div>
        </div>
    </div>
</div>
</body>
</html>
