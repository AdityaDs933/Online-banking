<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title>Sign Up</title>
</head>
<body>
	<h3>Create an account</h3>
	<c:if test="${not empty requestScope.signupError}">
		<div style="color: red;"><c:out value="${requestScope.signupError}"/></div>
	</c:if>
	<form action="/SignUpServlet" method="post">
        <input type="hidden" name="_csrf" value="${csrfToken}" />
		<label>Username: <input name="UsernameField" type="text" required></label><br/>
		<label>Password: <input name="PasswordField" type="password" required></label><br/>
		<label>Re-enter Password: <input name="RePasswordField" type="password" required></label><br/>
		<label>Full name: <input name="NameField" type="text"></label><br/>
		<button type="submit">Create account</button>
	</form>
</body>
</html>
 
