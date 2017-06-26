<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
<script type="text/javascript">
	$(document).ready(function() {
		monitoringStream();
		getTimeNow();
	});
</script>
<style type="text/css">
.transparent {
	background-size: cover;
	-webkit-filter: blur(4px);
	-moz-filter: blur(4px);
	-ms-filter: blur(4px);
	-o-filter: blur(4px);
	filter: blur(4px);
}

table#tableMain>tbody>tr>td {
	height: .1em;
	border-top: 1px solid;
	border-left: 1px solid;
	border-bottom: 1px solid;
	padding: 0.1em;
	font-size: 11pt;
}

table#tableMain>thead>tr>th {
	background-color: #ccc;
	border-right: 1px solid;
}

.ui-table-columntoggle-btn {
	display: none !important;
}
</style>
<%
	if (request.getSession().getAttribute("loginSession") == null) {
		response.sendRedirect("index.jsp");
	}
%>
</head>
<body>
	<div data-role="page" class="jqm-demos jqm-home">
		<input type="hidden" id="lineId">
		<div data-role="header" class="jqm-header">
			<br> <a href="#"
				class="jqm-navmenu-link ui-btn ui-btn-icon-notext ui-corner-all ui-icon-bars ui-nodisc-icon ui-alt-icon ui-btn-left">Menu</a>
		</div>
		<div data-role="main" class="ui-content jqm-content" id="RaceForm">
			<input type="hidden" name="isAppend" id="isAppend"
				value="<%=request.getParameter("isAppend")%>">
			<div class='ui-grid-solo'>
				<div class="ui-grid-a">
					<div class="ui-block-a" data-inline="true">
						Checkpoint <span style="font-style: italic;"><%=request.getParameter("checkpointNo")%>&nbsp;</span>
						Race <span style="font-style: italic;"><%=request.getParameter("raceNumber")%>&nbsp;</span>
						(<%=request.getParameter("checkPointType")%>)
					</div>
					<div class="ui-block-b">
						<input type="number" id="riderGroup" value="1" disabled="disabled"
							data-inline="true" data-mini="true">
					</div>
				</div>
			</div>
			<input type="hidden" id="ipadd"
				value="<%=request.getParameter("ipAddress")%>"> <input
				type="hidden" id="raceNo" name="raceNo"
				value="<%=request.getParameter("raceNumber")%>">
			<%
				String chkT = request.getParameter("checkPointType");
				if (chkT.contains("tanda"))
					chkT = "Normal";
			%>
			<input type=hidden id="checkPointNo" name="checkPointNo"
				value="<%=request.getParameter("checkpointNo")%>"> <input
				type="hidden" id="checkPointType" name="checkPointType"
				value="<%=chkT%>"> <input type="hidden" id="scannerdelay"
				value="<%=request.getParameter("delay")%>">
			<div style="height: 30em; overflow: scroll;" id="tableContainer"
				class='ui-grid-solo'>
				<table data-role="table" id="tableMain" data-mode="columntoggle">
					<thead>
						<tr>
							<td data-priority="5"
								class="ui-table-priority-5 ui-table-cell-visible" id="rowId"></td>
							<th>Rider No</th>
							<th data-priority="1"
								class="ui-table-priority-1 ui-table-cell-visible">Time</th>
							<%
								if (request.getParameter("checkPointType").contains("tag")) {
							%>
							<th data-priority="4"
								class="ui-table-priority-4 ui-table-cell-visible">Group</th>
							<%
								}
							%>
							<th data-priority="3"
								class="ui-table-priority-3 ui-table-cell-visible">Tag No</th>
							<th data-priority="2"
								class="ui-table-priority-3 ui-table-cell-visible">Edit</th>
							<th data-priority="2"
								class="ui-table-priority-3 ui-table-cell-visible">Remove</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="ui-block-solo">
				<a style="cursor: pointer;" data-role="button" data-role="button"
					href="#" data-inline="true" onclick="startTheRace();" id="startBTN"
					data-mini="true"><img src="css/jquery-mobile/images/play.png" />START</a>
				<span id="timer"
					style="font-size: 14pt; font-family: Orbitron, sans-serif;"></span>
				<a style="cursor: pointer;" data-inline="true" data-mini="true"
					href="#" data-role="button" data-role="button"
					onclick="openPopupM();">Manual Submit</a> <a
					style="cursor: pointer; 				<%if (request.getParameter("autocommit") != null) {%>
				display: none;
				<%}%>
				"
					data-inline="true" data-role="button" href="#" data-role="button"
					data-mini="true" onclick="commit();" id="autoCommitBTN">Commit</a>
				<a data-mini="true" data-role="button" onclick="finishTheRace();"
					href="#"
					style="cursor: pointer; float: right; position: relative; top: 0; right: 0;"><img
					src="css/jquery-mobile/images/stop.png" />FINISH</a> <span id="stime"
					style="float: right; position: relative; font-size: 16px; font-family: Orbitron, sans-serif;"></span>
				<span
					style="float: right; font-size: 14pt; font-family: Orbitron, sans-serif;"
					id="currentTime"></span>
			</div>
		</div>
		<div data-role="panel" class="jqm-navmenu-panel" data-position="left"
			data-display="overlay" data-theme="a">
			<ul class="jqm-list ui-alt-icon ui-nodisc-icon">
				<li data-role="collapsible" data-enhanced="true"
					data-collapsed-icon="carat-d" data-expanded-icon="carat-u"
					data-iconpos="right" data-inset="false" data-mini="true"
					class="ui-collapsible ui-collapsible-themed-content ui-collapsible-collapsed">
					<div class="ui-field-contain">
						<div class="ui-block-a">
							<label for="autocommit">Auto Commit</label> <select
								name="autocommit" id="autocommit" data-role="slider"
								onchange="autoCommitChange();">
								<option value="Off"
									<%if (request.getParameter("autocommit") == null) {%>
									selected="selected" <%}%>>Off</option>
								<option value="on"
									<%if (request.getParameter("autocommit") != null
					&& request.getParameter("autocommit").equals("on")) {%>
									selected="selected" <%}%>>On</option>
							</select>
						</div>
						<div class="ui-block-b" style="float: right; width: 66%;">
							<label for="commitdelay">Delay</label> <input type="number"
								name="commitdelay" id="commitdelay"
								value="<%=request.getParameter("commitdelay")%>" />
						</div>
					</div>
				</li>
				<li data-role="collapsible" data-enhanced="true" data-mini="true"
					data-collapsed-icon="carat-d" data-expanded-icon="carat-u"
					data-iconpos="right" data-inset="false"
					class="ui-collapsible ui-collapsible-themed-content ui-collapsible-collapsed">
					<h3 class="ui-collapsible-heading ui-collapsible-heading-collapsed">
						<a href="#"
							onclick="$('#readerSettingPopup').popup('open').trigger('create');"
							class="ui-collapsible-heading-toggle ui-btn ui-btn-icon-right ui-btn-inherit ui-icon-carat-d">
							Adjust Scanner Power </a>
					</h3>
				</li>
				<li data-role="collapsible" data-enhanced="true" data-mini="true"
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
							<li data-filtertext="Restart Scanning"><a href="#"
								onclick="restartReading()">Restart Reading</a></li>
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
					<a href="LogoutServlet" data-ajax="false">Logout</a>
				</li>
			</ul>
		</div>
		<div data-role="popup" id="manualpopup" data-position-to="window"
			data-transition="turn" style="background-color: #f3f3f3;">
			<a href="#" data-role="button" data-theme="a" data-icon="delete"
				data-iconpos="notext" class="ui-btn-right"
				onclick="$('#manualpopup').popup('close'); $('#RaceForm').removeClass('transparent');">Close</a>
			<div class="ui-block-solo">
				<input type="text" placeholder="Rider Number" name="riderNo"
					id="riderNo">
			</div>
			<%
				if (!request.getParameter("checkPointType").contains("tag")) {
			%>
			<div class="ui-grid-b">
				<div class="ui-block-a">
					<input placeholder="HH" type="text" name="hh" id="hh" value="" />
				</div>
				<div class="ui-block-b">
					<input placeholder="MM" type="text" name="mm" id="mm" value="" />
				</div>
				<div class="ui-block-c">
					<input placeholder="SS" type="text" name="ss" id="ss" value="" />
				</div>
			</div>
			<%
				}
			%>
			<div class="ui-block-solo">
				<a style="cursor: pointer;" data-role="button" href="#"
					class="ui-btn ui-shadow ui-corner-all" id="submitRider"
					onclick="manualEntry();" accesskey=" ">Submit</a>
			</div>
		</div>
		<div data-role="popup" id="readerSettingPopup"
			data-position-to="window" data-transition="turn"
			style="background-color: #f3f3f3;">
			<a href="#" data-role="button" data-theme="a" data-icon="delete"
				data-iconpos="notext" class="ui-btn-right"
				onclick="$('#readerSettingPopup').popup('close');">Close</a>
			<div data-role="rangeslider"
				style="min-width: 100%; display: inline;">
				<label for="range-1a">Received Signal Strength Indication
					Filter</label> <input name="range-1a" id="range-1a" min="0" max="10000"
					value="0" type="range" /> <label for="range-1b">Rangeslider:</label>
				<input name="range-1b" id="range-1b" min="0" max="10000"
					value="10000" type="range" />
			</div>
			<div data-role="rangeslider" style="min-width: 100%;">
				<label for="range-1a">RF Attenuation (dB)</label> <input
					name="rangerf" id="rangerf" min="0" max="120" value="0"
					type="range" />
			</div>
		</div>

	</div>
</body>
</html>