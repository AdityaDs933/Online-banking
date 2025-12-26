<!--
/******************************************************************************
*	Program Author: Dr. Yongming Tang for CSCI 6810 Java and the Internet	  *
*	Date: September, 2012													  *
*******************************************************************************/
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head><meta charset="utf-8"></head>
<body>
    <h3>Deposit</h3>
    <c:if test="${not empty requestScope.message}">
        <div><c:out value="${requestScope.message}"/></div>
    </c:if>
    <form name="DepositForm" action="/CSCI6810/Deposit" method="post">
        <input type="hidden" name="_csrf" value="${csrfToken}" />
        <input type="hidden" name="UserID" value="${sessionScope.Username}" />
        <label>Deposit From:
            <select name="CheckingOrSavings">
                <option value="Checking">Checking</option>
                <option value="Savings">Savings</option>
            </select>
        </label>
        <br/>
        <label>Amount: <input name="AmountField" type="text" required></label>
        <br/>
        <button type="submit">Deposit</button>
    </form>
</body>
</html>