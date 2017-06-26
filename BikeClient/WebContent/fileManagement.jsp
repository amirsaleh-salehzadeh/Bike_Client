<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile-1.4.5.min.css">
<link rel="stylesheet" href="css/jquery-mobile/jqm-demos.css">
<script src="js/jquery.min.js"></script>
<script src="js/index.js"></script>
<script src="js/jquery.mobile-1.4.5.min.js"></script>
<script src="js/race.js"></script>
<link rel="stylesheet"
	href="css/jquery-mobile/jquery.mobile.datepicker.css" />
<link rel="stylesheet"
	href="css/jquery-mobile/jquery.mobile.datepicker.theme.css" />
<script src="js/jquery.mobile.datepicker.js"></script>
<script src="js/datepicker.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth() + 1;
		var yyyy = today.getFullYear();
		$('#sdate').val("01/01/2017").trigger("create");
		$('#edate').val(mm + "/" + dd + "/" + yyyy);
		fetchTheRaceList();
	});
<%if (request.getSession().getAttribute("loginSession") == null) {
				response.sendRedirect("index.jsp");
			}%>
	
</script>
<style type="text/css">
th , td {
	border-bottom: 1px solid #d6d6d6;
}
th{
	border: 1px solid #d6d6d6;
	background-color: #363636;
	color: white;
}

tr:nth-child(even) {
	background: #e9e9e9;
}
</style>
</head>
<body>
	<div data-role="page" class="jqm-demos jqm-home">
		<div data-role="header" class="jqm-header">
			<br> <a href="#"
				class="jqm-navmenu-link ui-btn ui-btn-icon-notext ui-corner-all ui-icon-bars ui-nodisc-icon ui-alt-icon ui-btn-left">Menu</a>
		</div>
		<div data-role="main" class="ui-content jqm-content" id="RaceForm">
			<div class="ui-block-solo">
				<div class="ui-grid-a">
					<div class="ui-block-a">
						<input type="text" data-role="date" data-mini="true" id="sdate"
							onchange="fetchTheRaceList();">
					</div>
					<div class="ui-block-b">
						<input type="text" data-role="date" data-mini="true" id="edate"
							onchange="fetchTheRaceList();">
					</div>
				</div>
			</div>
			<div class="ui-block-solo">
				<div class="ui-grid-a">
					<div class="ui-block-a">
						<input id="name" data-mini="true" type="text"
							placeholder="Race Name" onkeyup="fetchTheRaceList();">
					</div>
					<div class="ui-block-b">
						<input id="checkpoint" data-mini="true" type="text"
							placeholder="Checkpoint" onkeyup="fetchTheRaceList();">
					</div>
				</div>
			</div>
			<div class="ui-block-solo">
				<select id="chkType" data-mini="true" onchange="fetchTheRaceList();">
					<option value="0">All</option>
					<option value="1">Standard</option>
					<option value="2">Staggered</option>
				</select>
			</div>
			<div class="ui-block-solo">
				<table data-role="table" class="ui-responsive">
					<thead>
						<tr>
							<th data-priority='1'>#</th>
							<th data-priority='persist'>Race Name</th>
							<th data-priority='1'>Checkpoint</th>
							<th data-priority='5'>Date</th>
							<th data-priority='persist'>Export</th>
						</tr>
					</thead>
					<tbody id="rmtablebody">
					</tbody>
				</table>
			</div>
		</div>






		<div data-role="panel" class="jqm-navmenu-panel" data-position="left"
			data-display="overlay" data-theme="a">
			<ul class="jqm-list ui-alt-icon ui-nodisc-icon">
				<li data-role="collapsible" data-enhanced="true"
					data-collapsed-icon="carat-d" data-expanded-icon="carat-u"
					data-iconpos="right" data-inset="false"
					class="ui-collapsible-themed-content ui-collapsible-collapsed">
					<a href="CheckPointConfigServlet" data-ajax="false">Configuration</a>
				</li>
				<li data-role="collapsible" data-enhanced="true"
					data-collapsed-icon="carat-d" data-expanded-icon="carat-u"
					data-iconpos="right" data-inset="false"
					class="ui-collapsible ui-collapsible-themed-content ui-collapsible-collapsed">
					<a href="LogoutServlet" data-ajax="false">Logout</a>
				</li>
			</ul>
		</div>
	</div>
</body>
</html>