<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet"
	href="css/themes/default/jquery.mobile-1.4.5.min.css">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/jquery-mobile/jqm-demos.css">
<script src="js/jquery.min.js"></script>
<script src="js/index.js"></script>
<script src="js/jquery.mobile-1.4.5.min.js"></script>
<script src="js/race.js"></script>
<script type="text/javascript">
	function submitFormConf() {
		if ($('#raceNumber').val() == "") {
			alert("Please select the race name");
			return false;
		} else if ($('#checkpointNo').val() == "") {
			alert("Please select the checkpoint number");
			return false;
		}
		$
				.ajax({
					url : address + "/REST/GetWS/FileExist?raceNo="
							+ $("#raceNumber").val() + "&checkPointNo="
							+ $("#checkpointNo").val() + "&checkPointType="
							+ $("#checkPointType").val(),
					dataType : "json",
					cache : false,
					async : true,
					success : function(data) {
						if (data.res == "true"
								&& confirm('(Yes) To CONTINUE the race/ (No) Towards a NEW race')) {
							$("#isAppend").val("true");
						}
						$('#chconf').submit();
					},
					error : function(xhr, settings, exception) {
						// alert(xhr);
					}
				});
	}
	// 	$(document).ajaxStart(function() {
	// 		document.getElementById("loader").style.display = "block";
	// 	}).ajaxStop(function() {
	// 		document.getElementById("loader").style.display = "none";
	// 	});
</script>
<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
<meta http-equiv="pragma" content="no-cache" />

<%
	if (request.getSession().getAttribute("loginSession") == null) {
		response.sendRedirect("index.jsp");
	}
%>
</head>
<body>
	<div data-role="page" class="jqm-demos jqm-home">
		<div data-role="header" class="jqm-header">
			<br> <a href="#"
				class="jqm-navmenu-link ui-btn ui-btn-icon-notext ui-corner-all ui-icon-bars ui-nodisc-icon ui-alt-icon ui-btn-left">Menu</a>
		</div>
		<div id="loader"></div>
		<div data-role="main" class="ui-content jqm-content" id="RaceForm">
			<form action="CheckPointRunServlet" class="ui-filterable"
				method="post" id="chconf" data-ajax="false">
				<input type="hidden" name="isAppend" id="isAppend" value="false">
				<%
					if (request.getParameter("message") != null) {
				%>
				<div>
					<label style="color: red"><%=request.getParameter("message")%></label>
				</div>
				<%
					}
				%>
				<div class="ui-field-contain">
					<!-- 					<label for="raceNumber">Race Number</label>      -->
					<input name="raceNumber" id="raceNumber" data-type="search"
						placeholder="Race Name" data-mini="true">
					<ul id="autocomplete" data-role="listview" data-inset="true"
						data-filter="true" data-input="#raceNumber"></ul>
				</div>
				<div class="ui-field-contain">
					<!-- 					<label for="checkpointName">Checkpoint Number</label>      -->
					<input name="checkpointNo" id="checkpointNo" data-mini="true"
						data-type="search" placeholder="Checkpoint Number">
					<ul id="autocomplete" data-role="listview" data-inset="true"
						data-filter="true" data-input="checkpointNo"></ul>
				</div>
				<div class="ui-field-contain">
					<label for="checkPointType">Checkpoint Type</label> <select
						name="checkPointType" id="checkPointType" data-iconpos="left"
						data-mini="true">
						<option value="Standard">Standard</option>
						<option value="Staggered">Staggered</option>
					</select>
				</div>
				<div class="ui-field-contain">
					<div class="ui-grid-b">
						<div class="ui-block-a">
							<label for="ipAddress">IP Address</label> <input data-mini="true"
								placeholder="IP Address" type="text" name="ipAddress"
								id="ipAddress" value="<%=request.getParameter("ipadd")%>" />
						</div>
						<div class="ui-block-c" id="readerStat">
							<%
								if (request.getParameter("readerStat").equals("true")) {
							%>
							<label style="color: green;">Connected</label>
							<%
								} else {
							%>
							<label style="color: red;">NOT Connected</label>
							<%
								}
							%>
						</div>
						<div class="ui-block-c">
							<input type="button" value="Ping" onclick="pingMe();"
								data-mini="true" />
						</div>
					</div>
				</div>
				<div class="ui-field-contain">
					<label for="delay">Scanner Delay Time (Sec)</label> <input
						data-mini="true" type="number" name="delay" id="delay" value="5"
						placeholder="Scanner Delay Time" />
				</div>
				<div class="ui-field-contain">
					<div class="ui-grid-a">
						<div class="ui-block-a">
							<label for="autocommit">Auto Commit</label> <input
								data-mini="true" type="checkbox" data-role="flipswitch"
								name="autocommit" id="autocommit" data-on-text="On"
								data-off-text="Off" checked=""
								data-wrapper-class="custom-label-flipswitch">
						</div>
						<div class="ui-block-b">
							<label for="commitdelay">Commit Every (Seconds)</label> <input
								data-mini="true" type="number" name="commitdelay"
								id="commitdelay" value="30" />
						</div>
					</div>
				</div>
				<a href="#" id="btn-submit" class="ui-btn ui-btn-b ui-corner-all "
					onclick="submitFormConf();" data-mini="true">Save</a>
			</form>
		</div>
		<div data-role="panel" class="jqm-navmenu-panel" data-position="left"
			data-display="overlay" data-theme="a">
			<ul class="jqm-list ui-alt-icon ui-nodisc-icon">
				<li data-role="collapsible" data-enhanced="true"
					data-collapsed-icon="carat-d" data-expanded-icon="carat-u"
					data-iconpos="right" data-inset="false"
					class="ui-collapsible ui-collapsible-themed-content ui-collapsible-collapsed">
					<h3 class="ui-collapsible-heading ui-collapsible-heading-collapsed">
						<a href="#"
							class="ui-collapsible-heading-toggle ui-btn ui-btn-icon-right ui-btn-inherit ui-icon-carat-d">
							Scanner Management<span class="ui-collapsible-heading-status">
								click to expand contents</span>
						</a>
					</h3>
					<div
						class="ui-collapsible-content ui-body-inherit ui-collapsible-content-collapsed"
						aria-hidden="true">
						<ul>
							<li data-filtertext="Open Controlpanel"><a href="#"
								onclick="window.open('http://'+$('#ipadd').val());">Open
									Control Panel</a></li>
						</ul>
					</div>
				</li>
				<li data-role="collapsible" data-enhanced="true"
					data-collapsed-icon="carat-d" data-expanded-icon="carat-u"
					data-iconpos="right" data-inset="false"
					class="ui-collapsible ui-collapsible-themed-content ui-collapsible-collapsed">
					<h3 class="ui-collapsible-heading ui-collapsible-heading-collapsed">
						<a href="#"
							class="ui-collapsible-heading-toggle ui-btn ui-btn-icon-right ui-btn-inherit ui-icon-carat-d">
							App Management<span class="ui-collapsible-heading-status">
								click to expand contents</span>
						</a>
					</h3>
					<div
						class="ui-collapsible-content ui-body-inherit ui-collapsible-content-collapsed"
						aria-hidden="true">
						<ul>
							<li data-filtertext="Restart Scanner"><a href="#"
								onclick="restartApp()">Restart App</a></li>
						</ul>
					</div>
				</li>
				<li data-role="collapsible" data-enhanced="true"
					data-collapsed-icon="carat-d" data-expanded-icon="carat-u"
					data-iconpos="right" data-inset="false"
					class="ui-collapsible ui-collapsible-themed-content ui-collapsible-collapsed">
					<h3 class="ui-collapsible-heading ui-collapsible-heading-collapsed">
						<a href="#"
							class="ui-collapsible-heading-toggle ui-btn ui-btn-icon-right ui-btn-inherit ui-icon-carat-d">
							Device Management<span class="ui-collapsible-heading-status">
								click to expand contents</span>
						</a>
					</h3>
					<div
						class="ui-collapsible-content ui-body-inherit ui-collapsible-content-collapsed"
						aria-hidden="true">
						<ul>
							<li data-filtertext="Restart Scanner"><a href="#"
								onclick="restartDevice()">Restart Device</a></li>
							<li data-filtertext="Restart Scanner"><a href="#"
								onclick="shutdown()">Shut Down</a></li>
						</ul>
					</div>
				</li>
				<li data-role="collapsible" data-enhanced="true"
					data-collapsed-icon="carat-d" data-expanded-icon="carat-u"
					data-iconpos="right" data-inset="false"
					class="ui-collapsible-themed-content ui-collapsible-collapsed">
					<a href="fileManagement.jsp" data-ajax="false">File Management</a>
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