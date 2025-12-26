<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="com.aditya.*;" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Using session attributes set by LoginServlet; values are escaped via c:out -->

<HTML>
<HEAD>
<STYLE> 
	ul{list-style-type: none; padding: 2%;} 
	li{display: inline; padding: 2%;}
</STYLE>
</HEAD>
<BODY>
<h4 ALIGN='center'>Welcome to your link page</h4>
<FORM NAME=\"LoginPage\" ACTION=\"/OnlineLoginServlet\" METHOD =\"POST\">
<TR bgcolor='#ECFAEB'>
<div id='navigation'>
<ul>
	<c:url var="overviewUrl" value="/CSCI6810/AccountOverview">
		<c:param name="UserID" value="${sessionScope.Username}" />
		<c:param name="CustomerName" value="${sessionScope.CustomerName}" />
	</c:url>
	<c:url var="openUrl" value="/CSCI6810/PreOpenBankAccount.jsp">
		<c:param name="UserID" value="${sessionScope.Username}" />
		<c:param name="CustomerName" value="${sessionScope.CustomerName}" />
	</c:url>
	<c:url var="withdrawUrl" value="/CSCI6810/Withdraw.jsp">
		<c:param name="UserID" value="${sessionScope.Username}" />
		<c:param name="CustomerName" value="${sessionScope.CustomerName}" />
	</c:url>
	<c:url var="depositUrl" value="/CSCI6810/Deposit.jsp">
		<c:param name="UserID" value="${sessionScope.Username}" />
		<c:param name="CustomerName" value="${sessionScope.CustomerName}" />
	</c:url>
	<c:url var="transferUrl" value="/CSCI6810/Transfer.jsp">
		<c:param name="UserID" value="${sessionScope.Username}" />
		<c:param name="CustomerName" value="${sessionScope.CustomerName}" />
	</c:url>
	<c:url var="inquireUrl" value="/CSCI6810/InquireTransactions">
		<c:param name="UserID" value="${sessionScope.Username}" />
		<c:param name="CustomerName" value="${sessionScope.CustomerName}" />
	</c:url>

<li><a href="${overviewUrl}" target="display">Account Overview</a></li>
<li><a href="${openUrl}" target="display">Open Bank Account</a></li>
<li><a href="${withdrawUrl}" target="display">Withdraw</a></li>
<li><a href="${depositUrl}" target="display">Deposit</a></li>
<li><a href="${transferUrl}" target="display">Transfer</a></li>
<li><a href="${inquireUrl}" target="display">Inquire Transaction</a></li>
<li><A HREF='/CSCI6810/' TARGET='display'>Logout</A></li>
</ul>
</div>
</TR>
</FORM>
</BODY>
<SCRIPT LANGUAGE=\"JavaScript\">
function checkInputs()
{
	var Prompts = \"\";
	Username = window.document.LoginPage.UserName.value;
	Password = window.document.LoginPage.PassWord.value;
	if (Username == \"\" || Password == \"\") 
	{
		if (Username == "")
		Prompts +=\"Please enter your username!\n";
		if (Password == "")
		Prompts +=\"Please enter your password!\n";
		if (Prompts != "")
		window.alert(Prompts);
	}
	else 
	{	
		document.LoginPage.submit();
	}
}
</SCRIPT>
</html>