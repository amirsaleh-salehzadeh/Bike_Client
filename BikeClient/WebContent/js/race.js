var address = "/BikeClient";
var now;
var timer;
var monitoting;
var initTime;
var lastTime;
var counter;
var percentColors = [ {
	pct : 0.0,
	color : {
		r : 0xff,
		g : 0x00,
		b : 0
	}
}, {
	pct : 0.5,
	color : {
		r : 0xff,
		g : 0xff,
		b : 0
	}
}, {
	pct : 1.0,
	color : {
		r : 0x00,
		g : 0xff,
		b : 0
	}
} ];

function exportMe(x) {
	window.location.href = 'ExportCSVServlet?fileName='
			+ $(x).attr("title");
}

function pingMe() {
	$
			.ajax({
				url : address + "/REST/GetWS/PingMe?ipAdd="
						+ $("#ipAddress").val(),
				dataType : "json",
				cache : false,
				async : false,
				beforeSend : function() {
					$('#readerStat').html("");
					document.getElementById("loader").style.display = "block";
				},
				success : function(data) {
					setTimeout(
							function() {
								if (data)
									$('#readerStat')
											.html(
													'<label style="color: green;">Connected</label>');
								else
									$('#readerStat')
											.html(
													'<label style="color: red;">NOT Connected</label>');
								// $('#RaceForm').removeClass('transparent');
								document.getElementById("loader").style.display = "none";
							}, 2000);
				},
				error : function(xhr, settings, exception) {
					// alert(xhr);
				}
			});
}

function manualEntry() {
	if ($("#checkPointType").val() == "Normal") {
		// $('#RaceForm').removeClass('transparent');
		$('#manualpopup').popup('close');
		alert("Please start the race first");
		return;
	}
	var timeli = $("#hh").val() + ":" + $("#mm").val() + ":" + $("#ss").val();
	var urltmp = "";
	if ($("#lineId").val() == "")
		urltmp = address + "/REST/GetWS/AddLine?";
	else
		urltmp = address + "/REST/GetWS/EditALine?rowId=" + $('#lineId').val()
				+ "&";
	urltmp += "checkPointNo=" + $("#checkPointNo").val() + "&raceNo="
			+ $("#raceNo").val() + "&riderNo=" + $("#riderNo").val()
			+ "&checkPointType=" + $("#checkPointType").val();
	urltmp += "&riderGroup=" + $("#riderGroup").val();
	if ($("#checkPointType").val() == "Staggered") {
		var time = Date.parse(new Date());
		timeli = convertTime(time);
		timeli = "Waiting";
	}
	urltmp += "&time=" + timeli;
	$.ajax({
		url : urltmp,
		dataType : "json",
		cache : false,
		async : false,
		success : function(data) {
			$('#manualpopup').popup('close');
		},
		error : function(xhr, settings, exception) {
			$('#manualpopup').popup('close');
		}
	});
	$('#manualpopup').popup('close');
}

function playSound() {
	var aud = new Audio('bip.wav');
	navigator.vibrate = navigator.vibrate || navigator.webkitVibrate
			|| navigator.mozVibrate || navigator.msVibrate;
	if ("vibrate" in navigator) {
		navigator.vibrate(300);
	}
	aud.play();
}

function raceMonitoring(u) {
	var first = false;
	counter = 0;
	var init = parseInt($("#rowId").html());
	$
			.ajax({
				url : u,
				dataType : "json",
				cache : false,
				async : false,
				success : function(data) {
					var ht = "";
					$
							.each(
									data,
									function(k, l) {
										counter++;
										if (first == false) {
											first = true;
											initTime = l.time;
										}
										ht += "<tr id='"
												+ l.id
												+ "'><td id='colorC' class='ui-table-priority-4 ui-table-cell-visible'>"
												+ counter + "</td>"
												+ "<td class='riderNoTD' >"
												+ l.riderNo + "</td>";
										ht += "<td class='timeTD ui-table-priority-1 ui-table-cell-visible' id='"
												+ l.time
												+ "'>"
												+ l.timePresentation;
										if ($("#checkPointType").val() == "Staggered")
											ht += "</td><td class='ui-table-priority-3 ui-table-cell-visible' >"
													+ l.group;
										ht += "</td><td class='ui-table-priority-2 ui-table-cell-visible' >"
												+ l.tag
												+ "</td>"
												+ "<td class='ui-table-priority-3 ui-table-cell-visible' >"
												+ "<img src='css/jquery-mobile/images/update.png' style='cursor: pointer;' onclick='editMe(this);'></td><td class='ui-table-priority-3 ui-table-cell-visible' >"
												+ "<img src='css/jquery-mobile/images/remove.png' style='cursor: pointer;' onclick='removeMe("
												+ l.id + ");'></td></tr>";
										lastTime = l.time;
										$("#rowId").html(counter);
									});
					if (counter > init) {
						playSound();
					}
					$("table#tableMain tbody").html(ht).trigger("create");
//					modifyColors();
					$("table#tableMain tbody").closest("table#tableMain")
							.trigger("create");
				}
			});
	$("#isAppend").val("true");
}

function fetchTheRaceList() {
	var trz = "";
	var counter = 1;
	$.ajax({
				url : address + "/REST/GetWS/GetAllFiles?name=" + $("#name").val() + "&sdate="
				+ $("#sdate").val() + "&edate=" + $("#edate").val()+ "&chktype="
				+ $("#chkType").val()+ "&chkName="
				+ $("#checkpoint").val(),
		cache : false,
		success : function(data) {
			trz = "";
			$.each(data, function(k, l) {
				trz += "<tr><th>" + counter + "</th><td>" + l.race + "</td><td>"
				+ "{" + l.checkpointType + "} "+ l.checkpointName + "</td><td>" + l.dateTime + "</td>" +
						"<td> <img src='css/jquery-mobile/images/excel.png' title='"
						+ l.fileName + "' id='" + l.id
						+ "' onclick='exportMe(this);' style='cursor: pointer;'/></td></tr>";
				counter++;
			});
			$("tbody#rmtablebody").html(trz).trigger("updatelayout");
		}
	});
}

function editMe(x) {
	// alert($(x).parent().parent().html());
	var tr = $(x).parent().parent();
	var timer = "";
	$(tr).on('click', 'td', function() {
		$(this).siblings().each(function() {
			if ($(this).attr('class') == "riderNoTD") {
				$("#riderNo").val($(this).html());
			} else if ($(this).attr('class').indexOf("timeTD") >= 0) {
				timer = $(this).attr('id');
				$('#ss').val(Math.floor((timer / 1000) % 60));
				$('#mm').val(Math.floor((timer / 1000 / 60) % 60));
				$('#hh').val(2 + Math.floor((timer / (1000 * 60 * 60)) % 24));
			}
		});
	});
	$("#lineId").val(tr.attr('id'));
	$('#manualpopup').popup().trigger('create');
	$('#manualpopup').popup('open').trigger('create');
}

function removeMe(x) {
	if (confirm("REMOVE?"))
		$.ajax({
			url : address + "/REST/GetWS/RemoveALine?raceNo="
					+ $("#raceNo").val() + "&checkPointType="
					+ $("#checkPointType").val() + "&checkPointNo="
					+ $("#checkPointNo").val() + "&rowId=" + x,
			dataType : "json",
			cache : false,
			async : false,
			success : function(data) {
			}
		});
}

function getColorForPercentage(pct) {
	for ( var i = 1; i < percentColors.length - 1; i++) {
		if (pct < percentColors[i].pct) {
			break;
		}
	}
	var lower = percentColors[i - 1];
	var upper = percentColors[i];
	var range = upper.pct - lower.pct;
	var rangePct = (pct - lower.pct) / range;
	var pctLower = 1 - rangePct;
	var pctUpper = rangePct;
	var color = {
		r : Math.floor(lower.color.r * pctLower + upper.color.r * pctUpper),
		g : Math.floor(lower.color.g * pctLower + upper.color.g * pctUpper),
		b : Math.floor(lower.color.b * pctLower + upper.color.b * pctUpper)
	};
	return 'rgb(' + [ color.r, color.g, color.b ].join(',') + ')';
}

function modifyColors() {
	var tmpCounter = counter;
	var index = 1;
	$("table#tableMain > tbody > tr > td#colorC")
			.each(
					function() {
						if (parseInt($(this).attr("id"))
								+ ((parseInt($("#commitdelay").val()) * 1000)) >= new Date()
								.getTime()) {
							$(this).css("background-color", "#A9BCF5");
						} else
							$(this)
									.css(
											"background-color",
											getColorForPercentage(tmpCounter
													/ counter));
						tmpCounter--;
						index++;
					});
}

function convertTime(ti) {
	var seconds = Math.floor(Math.floor(ti / 1000) % 60);
	var minutes = Math.floor(Math.floor(ti / 1000 / 60) % 60);
	var hours = Math.floor(Math.floor(ti / (1000 * 60 * 60)) % 24);
	return hours + ":" + minutes + ":" + seconds;
}

function getTimeRemaining() {
	var t = Date.parse(new Date()) - now;
	$("#timer").html(convertTime(t));
	timer = setTimeout(getTimeRemaining, 50);
}

function getTimeNow() {
	$("#currentTime").html(convertTime(Date.parse(new Date())));
	timer = setTimeout(getTimeNow, 1000);
}

function startTheRace() {
	var stag = false;
	var cht = $("#checkPointType").val();
	var rgp = $("#riderGroup").val();
	if ($("#checkPointType").val() == "Staggered")
		stag = true;
	if (!stag) {
		$("#startBTN").attr("onclick", "");
		cht = "Standard";
		$("#checkPointType").val("Standard");
	} else {
		newgroup();
	}
	now = Date.parse(new Date());
	getTimeRemaining();
	$.ajax({
		url : address + "/REST/GetWS/StartRace?raceNo=" + $("#raceNo").val()
				+ "&staggered=" + stag + "&riderGroup=" + rgp
				+ "&checkPointType=" + cht + "&checkPointNo="
				+ $("#checkPointNo").val(),
		dataType : "json",
		cache : false,
		async : false,
		success : function(data) {
		}
	});

}

function monitoringStream() {
	url = address + "/REST/GetWS/GetAllTags?commitDelay="
			+ $("#commitdelay").val() + "&raceNo=" + $("#raceNo").val()
			+ "&autoCommit=" + $("#autocommit").val() + "&commitDelay="
			+ $("#commitDelay").val() + "&scannerDelay="
			+ $("#scannerdelay").val() + "&checkPointNo="
			+ $("#checkPointNo").val() + "&checkPointType="
			+ $("#checkPointType").val() + "&rssi1="
			+ $("#range-1a").val() + "&rssi2="
			+ $("#range-1b").val() + "&strength="
			+ $("#rangerf").val() + "&riderGroup="
			+ $("#riderGroup").val() + "&isAppend=" + $("#isAppend").val();
	raceMonitoring(url);
	monitoting = setTimeout(monitoringStream, 500);
}

function autoCommitChange() {
	if ($("#autocommit").val() == "on")
		$("#autoCommitBTN").css("display", "none");
	else
		$("#autoCommitBTN").css("display", "inline-block");
}

function finishTheRace() {
	if (confirm('Are you sure you want to finish the race?')) {
		$.ajax({
			url : address + "/REST/GetWS/Finish?raceNo=" + $("#raceNo").val()
					+ "&checkPointNo=" + $("#checkPointNo").val()
					+ "&checkPointType=" + $("#checkPointType").val(),
			dataType : "json",
			cache : false,
			async : false,
			success : function(data) {
			}
		});
		window.location.replace("CheckPointConfigServlet");
	} else
		return;
}

function openPopupM() {
	var time = Date.parse(new Date());
	// $("#RaceForm").addClass("transparent").trigger('create');
	$('#manualpopup').popup().trigger('create');
	$('#riderNo').val("");
	$("#lineId").val("");
	$('#manualpopup').popup('open').trigger('create');
	$('#ss').val(Math.floor((time / 1000) % 60));
	$('#mm').val(Math.floor((time / 1000 / 60) % 60));
	$('#hh').val(2 + Math.floor((time / (1000 * 60 * 60)) % 24));
	$('#riderNo').focus();
}

function commit() {
	$.ajax({
		url : address + "/REST/GetWS/Commit?raceNo=" + $("#raceNo").val()
				+ "&checkPointNo=" + $("#checkPointNo").val()
				+ "&checkPointType=" + $("#checkPointType").val(),
		dataType : "json",
		cache : false,
		async : false,
		success : function(data) {
		}
	});
}

function restartDevice() {
	$.ajax({
		url : address + "/REST/GetWS/RebootPi",
		dataType : "json",
		cache : false,
		async : false,
		success : function(data) {
		}
	});
}

function shutdown() {
	$.ajax({
		url : address + "/REST/GetWS/ShutdownPi",
		dataType : "json",
		cache : false,
		async : false,
		success : function(data) {
		}
	});
}

function restartApp() {
	$.ajax({
		url : address + "/REST/GetWS/RestartTomcat",
		dataType : "json",
		cache : false,
		async : false,
		success : function(data) {
		}
	});
}

function restartReading() {
	$.ajax({
		url : address + "/REST/GetWS/RestartReading?commitDelay="
				+ $("#commitdelay").val() + "&raceNo=" + $("#raceNo").val()
				+ "&autoCommit=" + $("#autocommit").val() + "&scannerDelay="
				+ $("#commitdelay").val() + "&checkPointType="
				+ $("#checkPointType").val() + "&checkPointNo="
				+ $("#checkPointNo").val(),
		dataType : "json",
		cache : false,
		async : false,
		success : function(data) {
		}
	});
}

function newgroup() {
	$('#riderGroup').val(parseInt($('#riderGroup').val()) + parseInt(1));
	$.ajax({
		url : address + "/REST/GetWS/NewGroup?groupId="
				+ $('#riderGroup').val(),
		dataType : "json",
		cache : false,
		async : false,
		success : function(data) {
		}
	});
}