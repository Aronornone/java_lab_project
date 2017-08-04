<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.currentLocale}"/>
<fmt:setBundle basename="JSPBundle"/>
<!DOCTYPE html>

<div class="invoiceSuccess">
        <p><fmt:message key="paid"/> </p>
        <p><fmt:message key="getBack"/> <a href="flights"><fmt:message key="flightsList"/></p>
</div>
