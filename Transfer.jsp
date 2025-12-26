<!--
/******************************************************************************
*	Program Author: Dr. Yongming Tang for CSCI 6810 Java and the Internet	  *
*	Date: September, 2012													  *
*******************************************************************************/
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><meta charset="utf-8"></head>
<body>
    <h3>Transfer</h3>
    <c:if test="${not empty requestScope.message}">
        <div><c:out value="${requestScope.message}"/></div>
    </c:if>
    <form name="TransferForm" action="/CSCI6810/Transfer" method="post">
        <input type="hidden" name="_csrf" value="${csrfToken}" />
        <input type="hidden" name="UserID" value="${sessionScope.Username}" />
        <label>From:
            <select name="FromCheckingOrSavings">
                <option value="Checking">Checking</option>
                <option value="Savings">Savings</option>
            </select>
        </label>
        <label>To:
            <select name="ToCheckingOrSavings">
                <option value="Checking">Checking</option>
                <option value="Savings">Savings</option>
            </select>
        </label>
        <br/>
        <label>Amount: <input name="AmountField" type="text" required></label>
        <br/>
        <button type="submit">Make Transfer</button>
    </form>
</body>
</html>