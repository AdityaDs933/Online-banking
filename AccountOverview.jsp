
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- AccountOverview now follows MVC: data is prepared by AccountOverviewServlet -->

<HTML><HEAD></HEAD>
<BODY>
	<FORM NAME="AccountOverviewForm" ACTION="" METHOD ="POST">
		<input type="hidden" name="_csrf" value="${csrfToken}" />
		<input type="hidden" name="UserID" value="${fn:escapeXml(param.UserID)}" />

        <TABLE cellPadding=3 ALIGN='center'>

            <TR bgcolor='#F1F1FD'>
                <td> Account Type</td>
		<td> Account Number</td>
          	<td> Balance</td>
            </TR>
				<TR bgcolor='#F1F1FD'>
			<td>Checking Account</td>
			<td><c:out value="${checkingLast4}"/></td>
			<td><c:out value="${checkingBalance}"/></td>
				</TR>
				<TR bgcolor='#F1F1FD'>
				<td>Saving Account</td>
				<td><c:out value="${savingsLast4}"/></td>
				<td><c:out value="${savingsBalance}"/></td>
				</TR>
            </TABLE><BR>
		<CENTER><INPUT TYPE="SUBMIT" NAME='submitBNTN' VALUE='Refresh'></CENTER><BR>
		
	</FORM>

	</BODY>
	</HTML>
<SCRIPT LANGUAGE='JavaScript'>
</SCRIPT>