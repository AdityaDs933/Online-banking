<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <title>Login</title>
</head>
<body bgcolor="#F1F1FD">

<form name="LoginPage" action="/OnlineLoginServlet" method="post">
  <input type="hidden" name="_csrf" value="${csrfToken}" />
  <table cellPadding="3" align="center">
    <tr bgcolor="#ECFAEB">
      <td>USERNAME:</td>
      <td>
        <input type="text" name="UserName" value="" size="15" autofocus>
      </td>
    </tr>
    <tr bgcolor="#ECFAEB">
      <td>PASSWORD:</td>
      <td>
        <input type="password" name="PassWord" value="" size="15">
        <input type="button" name="submitBTN" value="Login" onClick="checkInputs()">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        First time user? Click <a href="/CSCI6810/SignUp.jsp" target="display">Sign Up</a>
      </td>
    </tr>
  </table>
</form>

<script language="JavaScript">
function checkInputs() {
  var Prompts = "";
  var Username = window.document.LoginPage.UserName.value;
  var Password = window.document.LoginPage.PassWord.value;
  if (Username === "" || Password === "") {
    if (Username === "") Prompts += "Please enter your username!\n";
    if (Password === "") Prompts += "Please enter your password!\n";
    if (Prompts !== "") window.alert(Prompts);
  } else {
    document.LoginPage.submit();
  }
}
</script>

</body>
</html>
