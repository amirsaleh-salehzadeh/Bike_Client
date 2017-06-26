<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile-1.4.5.min.css">
<script src="js/jquery.min.js"></script>
<script src="js/jquery.mobile-1.4.5.min.js"></script>
<script src="js/race.js"></script>
<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<script type="text/javascript">
	function submitForm() {
		if ($('#password').val() == "" || $('#password').val() == "") {
			alert("Please fill the username and password feilds");
			return;
		} else
			$("#RaceForm").submit();
	}
</script>
</head>
<body>
	<%
		if (request.getSession().getAttribute("loginSession") != null) {
			response.sendRedirect("CheckPointConfigServlet");
		}
	%>
	<div data-role="page">
		<div role="main" class="ui-content" id="mainBodyContent">
			<form action="LoginServlet" method="post" id="RaceForm" data-ajax="false">
				<%
					if (request.getParameter("message") != null) {
				%>
				<div>
					<label style="color: red"><%=request.getParameter("message")%></label>
				</div>
				<%
					}
				%>
				<h3>Sign In</h3>
				<input type="text" name="username" id="username" value=""
					placeholder="Username" data-mini="true"> <input placeholder="Password"
					type="password" name="password" id="password" value="" data-mini="true"> <a
					id="btn-submit" 
					class="ui-btn ui-btn-b ui-corner-all"
					onclick="submitForm();" data-mini="true">Login</a>
			</form>
		</div>
	</div>
</body>
</html>