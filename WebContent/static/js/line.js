"use strict";

//显示列表
function toggle_list() {
	$("#tab-tables, span.hide-list, span.show-list" ).toggleClass("hide");
}

$(function(){
	SetCurrentMenu(3);
	$("#ztree-main").hide();
	$('#switchs-mode').show();
	$('#switchs-mode').click(switch_mode_show);
});

function initialize(evt) {
    var svgdoc = evt.target.ownerDocument;
    var root = svgdoc.documentElement;
    $('#svgholder embed').getSVG().fitToBox(1400, 1400);
    setTimeout(function () { loadData(root, svgdoc); }, 5000);
    $('#svgholder embed').getSVG().click(function(e) {
    	hidemenu(e);
        
    });
}

function loadData(root, doc) {
    $.ajax({
        url: "GetAllStationEvents",
        data:{"stationlist": 2},
        type: "GET",
        cache: false,
        dataType: "json",
        success: function (stations) {
            $.each(stations, function (index, station) {
                handleNodeStatus(root, station);
            });
           setTimeout(function () { loadData(root, doc); }, 6000);
        },
        error: function () {
           setTimeout(function () { loadData(root, doc); }, 500);
        }
    });
}

function username_show() {
	$("#user_name").val("");
	$("#pass_word").val("");
	$("#user_msg").val("");
	$("#username-dialog").dialog({
		modal: true,
		buttons : [ {
			text : "确认",
			click : username_upload,
		}, {
			text : "取消",
			click : function() {
				$("#user_msg").html("");
				$(this).dialog("close");
			}
		} ],
	});
}

function username_upload(){
	var userID = $("#user_name").val();
	var password = $("#pass_word").val();
	var command ='INSERV';
	var mode = $('#mode-list input:checked');
	var value = mode.parents('p').find('select').val(); 
	value = '["'+value+'"]';
	
	 $.ajax({
	        url: "login", 
	        data: {userID: userID, password: password,command : command,value:JSON.stringify(value)},
	        type: "GET",
	        cache: false,
	        dataType: "json",
	        success: function (data) {	
	        	if(data.islogin==0){
	        		$("#user_msg").html(data.msg);
	        		return;
	        	}

        		submit_mode();
	        	
	        	$("#username-dialog").dialog("close");
	        	$("#user_msg").html("");
	        }
	    });
}

function showTooltip(root, station, doc) {
    var test = new RegExp(/[+|-]?\d+(\.\d+)?/g);
    var objStation = root.getElementById("Station." + station.stationid);
    var objLine = root.getElementById("Line." + station.lineid);
    var objToolTip = root.getElementById("ToolTip");
    var srcToolTip = objToolTip.cloneNode(true);
    var sta = [0, 0];
    var stationTransform = objStation.getAttribute("transform");
    if (stationTransform != null) {
        var stationCoords = stationTransform.match(test);
        sta[0] = parseFloat(stationCoords[0]);
        sta[1] = parseFloat(stationCoords[1]);
    }
    var objStatus = root.getElementById("Station." + station.stationid + ".Status");
    var statusTransform = objStatus.getAttribute("transform");
    var statusCoords = statusTransform.match(test);
    var posX = sta[0] + parseFloat(statusCoords[0]) + 30;
    var posY = sta[1] + parseFloat(statusCoords[1]) + 30;
    srcToolTip.setAttribute("id", "ToolTip." + station.stationid);
    srcToolTip.setAttribute("transform", "translate(" + posX + "," + posY + ")");
    srcToolTip.setAttribute("style", "display:inline;");
    objLine.appendChild(srcToolTip);
    var srcText = srcToolTip.childNodes[3];
    var descriptions = [];
    $.each(['METSER', 'MODSTA', 'STWRNCNT', 'STALMCNT', 'STEVTCNT'], function(i, v){
        if (v in station.status) {
        	descriptions.push({name: station.status[v].tagname,
        		value: station.status[v].value,
        		description: station.status[v].valDesc.valDesc});
        }
    });
    var i = 0;
    $.each(descriptions, function (index, value) {
        var t = doc.createElementNS("http://www.w3.org/2000/svg", "tspan");
        var content = value.name;
        if (value.description != "") {
            content += "(" + value.description + ")";
        }
        content += ":" + value.value;
        t.textContent = content;
        t.setAttribute("x", 10);
        t.setAttribute("y", 24 * i + 20);
        t.setAttribute("style", "font-size:24px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;letter-spacing:0px;word-spacing:0px;writing-mode:lr-tb;text-anchor:start;fill:#000000;fill-opacity:1;stroke:none;display:inline;font-family:'汉仪中黑简';-inkscape-font-specification:'汉仪中黑简'");
        srcText.appendChild(t);
        i++;
    });
}

function hideTooltip(root, station) {
    var objLine = root.getElementById("Line." + station.lineid);
    var objToolTip = root.getElementById("ToolTip." + station.stationid);
    if (objToolTip != null) {
        objLine.removeChild(objToolTip);
    }
}

function handleNodeStatus(root, station) {
    var value = station.stationid;
    var objStatus = root.getElementById("Station." + value + ".Status");
    var objOuter = root.getElementById("Station." + value + ".Status.Outer");
    var objInner = root.getElementById("Station." + value + ".Status.Inner");
    var colors = {
        0: { type: "normal", outer: "#004c00", inner: "#00ff00" },
        1: { type: "outofservice", outer: "#4c5219", inner: "#fff100" },
        2: { type: "warning", outer: "#4c5219", inner: "#fff100" },
        3: { type: "alarm", outer: "#4c0b19", inner: "#ff0000" },
        4: { type: "offline", outer: "#3d4755", inner: "#cccccc" }
    };
    
    var objPath = root.getElementById("Station." + value + ".Status.Path");
    objPath && objStatus.removeChild(objPath);
    var status = station.status;
    var _metser;
    var icolor = colors[0];
    $.each(status, function(index, obj) {
        obj.METSER && (_metser = obj.METSER);
    });
    
    if(_metser) {
        icolor = colors[_metser.value];
    }
    
    $(objOuter).css("fill", icolor.outer);
    $(objInner).css("fill", icolor.inner);
    _metser && splash(root, value);
}

function splash(root, id) {
    var obj = $(root.getElementById("Station." + id + ".Status"));
    obj.css("cursor","pointer");
    var repeat = 6;
	(function __splash() {
		if (--repeat < 0)
			return;
		obj.css("display", ["inline","none"][repeat % 2]);
		
		setTimeout(function () { __splash(); }, 300 - (repeat & 1) * 100);
	})();
}

function loadStation(element, line, station) {
    var url = "../station.html?l=" + line + "&s=" + station;
    window.location.href = url;
}