<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head><meta charset="utf-8"></head>
<body>
  <h3>Inquire Transactions</h3>
  <c:if test="${not empty requestScope.message}">
    <div style="color:red;"><c:out value="${requestScope.message}"/></div>
  </c:if>
  <form method="post" action="/CSCI6810/InquireTransactions">
    <input type="hidden" name="_csrf" value="${csrfToken}" />
    <input type="hidden" name="UserID" value="${sessionScope.Username}" />
    <label>Start Date (YYYY-MM-DD): <input name="StartDate" type="text" required></label><br/>
    <label>End Date (YYYY-MM-DD): <input name="EndDate" type="text" required></label><br/>
    <button type="submit">Search</button>
  </form>

  <c:if test="${not empty requestScope.transactions}">
    <h4>Results</h4>
    <table border="1">
      <tr><th>Trans#</th><th>Amount</th><th>Type</th><th>Time</th><th>Date</th><th>From</th><th>To</th></tr>
      <c:forEach var="row" items="${requestScope.transactions}">
        <tr>
          <c:forEach var="col" items="${row}">
            <td><c:out value="${col}"/></td>
          </c:forEach>
        </tr>
      </c:forEach>
    </table>
  </c:if>
</body>
</html>
