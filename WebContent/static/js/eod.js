var type_list="event";
var station_id = 0;
$(function(){
	SetCurrentMenu(6);
	 $.ajax({
	        url: "getCurrentStationId", 
	        type: "GET",
	        cache: false,
	        dataType: "json",
	        success: function (data) {	
	        		station_id = data;
	        		if(station_id == 0){
	        			$("input[name='cmd']").click(function(){
	        				 var check = $("input[name='cmd']:checked").val();
	        				 if(check == "INIEOD"){
	        					 $('.ui-dialog-buttonpane button:contains("测试下发")').show();
	        					 $('#furtur_eod_version').hide();
	        					 resetFutureVersionParam();
	        				 }else if(check == "SETEOD"){
	        					 $('.ui-dialog-buttonpane button:contains("测试下发")').hide();
	        					 $('#furtur_eod_version').show();
	        				 }else{
        						$('.ui-dialog-buttonpane button:contains("测试下发")').hide();
        						$('#furtur_eod_version').hide();
        						resetFutureVersionParam();
	        				 }
	        			});
	        		}
	        }
	 });
	 
	jQuery('#effective_start_date,#effective_end_date').datepicker(jQuery.extend({showMonthAfterYear:false}, jQuery.datepicker.regional['zh_cn'], {'showAnim':'fold','showMonthAfterYear':true,'dateFormat':'yy-mm-dd'}));
	$("#effective_start_date").datepicker("setDate", new Date());
	
	var setting = {
			view: {
				fontCss : {color:"white"}
			},
			check: {
				enable: true
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			
			async: {
				enable: true,
				type: "get",
				url:"../AFCSUP/getAllLinesDevices",
				autoParam:["id", "name=n", "level=lv"],
				otherParam:{"otherParam":"zTreeAsyncTest"}
			}
		};

			$.fn.zTree.init($("#eod-lis"), setting);
			
			
	
});

function selectSingleDevice(obj){
	var chkBoxGroupName = $(obj).attr("name");
	var $chkBoxGroup = $("input[name = '"+chkBoxGroupName+"']");
	var $chkBoxStation = $("tr[data-tt-id='"+chkBoxGroupName+"'] input");
	
	if($(obj).prop("checked")==true){
		if ($chkBoxStation.prop("checked")==false) {
			$chkBoxStation.prop("checked", true);
		}
	} else {
		var flag = true;
		$chkBoxGroup.each(function(index,o){
			if ($(this).prop("checked")==true) {
				flag = false;
				return false;
			}
		});
		if (flag) {
			$chkBoxStation.prop("checked", false);
		}
	}
}

function selectStationDevice(obj){
	var chkBoxChildName = $(obj).parent().parent().attr("data-tt-id");
	if($(obj).prop("checked")==true){
		$("input[name='"+chkBoxChildName+"']").prop("checked", true);//全选
	} else {
		$("input[name='"+chkBoxChildName+"']").prop("checked",false);//取消全选
	}
}
	
	function parseToDeviceJson(deviceArray){
		var deviceIdJsonPrex = "[";
		var deviceIdJsonSuffix = "]";
		var deviceIdJson = "";
		var device = "";
		for (var i = 0; i < deviceArray.length; i+=2) {
			var devicePrex = "{";
			var deviceSuffix =	"},";
			var deviceKey = "\"" + deviceArray[i] + "\"";
			var deviceVal = deviceArray[i+1];
			var deviceJson = "";
			for (var j = 0; j < deviceVal.length; j++) {
				var deviceId = deviceVal[j];
				deviceJson = deviceJson + "\"" + deviceId + "\",";
			}
			device = device + (devicePrex + deviceKey + ":[" + deviceJson.substring(0, deviceJson.length - 1) + "]" + deviceSuffix);
		}
		deviceIdJson = deviceIdJsonPrex + device.substring(0, device.length - 1) + deviceIdJsonSuffix;
		return deviceIdJson;
	}
	
	function parseToStationJson(stationArray){
		var stationJson = "";
		for (var i = 0; i < stationArray.length; i++) {
			var stationId = stationArray[i];
			stationJson = stationJson + "\"" + stationId + "\",";
		}
		return stationJson.substring(0, stationJson.length - 1);
	}
	
	function username_show(obj) {
		$("#user_name").val("");
		$("#pass_word").val("");
		$("#user_msg").val("");
		
		var eodSendChoose = obj;
		send = $("input[name='cmd']:checked").val();
		if (eodSendChoose == "normalSend" && send == "SETEOD") {
			if (!checkSendFutureVersionParam()) {
				return;
			}
			if (!checkSendFutureEndDateParam()) {
				return;
			}
		}
		
		$("#username-dialog").dialog({
			modal: true,
			buttons : [ {
				text : "确认",
				click : function() {username_upload(obj);} ,
			}, {
				text : "取消",
				click : function() {
					$("#user_msg").html("");
					$(this).dialog("close");
				}
			} ],
		});
	}
	
	function username_upload(obj){
		var userID = $("#user_name").val();
		var password = $("#pass_word").val();
		var sendArray = [];
		var sendCommand = "";
		var eodSendChoose = obj;
		send = $("input[name='cmd']:checked").val();
		
		if (eodSendChoose == "testSend") {
			if(send == "INIACC"){
				sendCommand="TSTEOD";
				sendArray.push(0);
			} else if(send == "INIEOD") {
				sendCommand="TSTEOD";
				sendArray.push(1);
			}
		}else{
			sendCommand=send;
			if(send == "INIACC"){
				sendCommand="INIEOD";
				sendArray.push(0);
			} else if(send == "INIEOD") {
				sendArray.push(1);
			}
		}

		//var deviceIdJson = parseToDeviceJson(deviceArray);
		var sendParamJson = sendArray.length == 0 ? "["+ sendArray +"]" : "[\""+ sendArray +"\"]";
		
		 $.ajax({
		        url: "login", 
		        data: {userID: userID, password: password,command : sendCommand,value:sendParamJson},
		        type: "GET",
		        cache: false,
		        dataType: "json",
		        success: function (data) {	
		        	if(data.islogin==0){
		        		$("#user_msg").html(data.msg);
		        		return;
		        	}
		        	$("#username-dialog").dialog("close");
		        	$("#user_msg").html("");
		        	myShow();
		        	setTimeout( "button_click('"+obj+"')",1000);
		        	
		        }
		    });
		
	}
	
	function button_click(obj){
		
		var stationArray = [];
		var deviceArray = [];
		var stationlist={};
		//拼装json数据传至后台
		var treeObj = $.fn.zTree.getZTreeObj("eod-lis");
		treeObj.refresh();
		var nodes = treeObj.getNodes();
			for (var i=0; i < nodes.length; i++) {
				nodes[i].iconSkin ='';
		    	for(var j=0;j < nodes[i].children.length;j++){
		    		nodes[i].children[j].iconSkin='';
		    		stationlist[nodes[i].children[j].id] = 0;
		    		if(nodes[i].children[j].children != null){
		    			stationlist[nodes[i].children[j].id]=nodes[i].children[j].children.length;
		    			for(var k=0;k < nodes[i].children[j].children.length;k++){
		    				nodes[i].children[j].children[k].iconSkin='';
		    			}
		    		}
			    }
			}
			treeObj.refresh();
			var nodes = treeObj.getCheckedNodes(true);
			var objDes = new Object();
			 for (var i=0; i < nodes.length; i++) {
				 if(nodes[i].pId!=null){
					 var nodes_array = nodes[i].pId.split("_");
					 if(nodes_array[0] != "L"){
						 if(objDes[nodes[i].pId] == null)
						 {
							 objDes[nodes[i].pId] = new Array();
						 }
						 objDes[nodes[i].pId].push(nodes[i].id);
					 }
				}
			} 

			deviceArray.push(objDes);
			for(var i in deviceArray[0]) {
				if(deviceArray[0][i].length == stationlist[i]){
					deviceArray[0][i]=["0"];
				}
			}
			
			deviceArray=JSON.stringify(deviceArray); 
			
		var sendArray = [];
		var sendCommand = "";
		var eodSendChoose = obj;
		send = $("input[name='cmd']:checked").val();

		if (eodSendChoose == "testSend") {
			if(send == "INIACC"){
				sendCommand="TSTEOD";
				sendArray.push(0);
			} else if(send == "INIEOD") {
				sendCommand="TSTEOD";
				sendArray.push(1);
			}
		}else{
			sendCommand=send;
			if(send == "INIACC"){
				sendCommand="INIEOD";
				sendArray.push(0);
			} else if(send == "INIEOD") {
				sendArray.push(1);
			} else if(send == "SETEOD") {
				var accZlcFlag = $("#furtur_eod_version input[name='acc_zlc']:checked").val();
				var effectiveVersion = $("#furtur_eod_version input[name='eff_version']").val();
				var effectiveStartTime = $("#effective_start_date").val().replace("-","").replace("-","");
				var effectiveEndTime = $("#effective_end_date").val().replace("-","").replace("-","");
				sendArray=[accZlcFlag,effectiveVersion,effectiveStartTime,effectiveEndTime];
			}
		}

		var stationIdJson = "["+ parseToStationJson(stationArray) +"]";
		//var deviceIdJson = parseToDeviceJson(deviceArray);
		var sendParamJson = sendArray.length == 0 ? "["+ sendArray +"]" : "["+ sendArray +"]";

		
		$.ajax({
			type : "post",
			url : "../AFCSUP/SendCommand",
			dataType : "json",
			data: {stationId:stationIdJson,deviceId:deviceArray,sendCommand:sendCommand,sendParam:sendParamJson},
			async : true,
			success : function(msg) {
				
				if (JSON.stringify(msg) != "[]") {
					$("#myShow").dialog("close");
					$("#dialog").html("已经发送");
					$("#dialog").dialog( "open" );
					for (var i = 0; i < msg.length; i++) {
						var stationId = msg[i].stationId;
						var statusObj = msg[i].status;
						$.each(statusObj,function(key,val){
							if (key == 0) {
								var node = treeObj.getNodeByParam("id", stationId, null); 
								if(val==-1){
									node.iconSkin ='diy03';
			        				treeObj.updateNode(node);
								}else if(val==0){
									node.iconSkin ='diy01';
			        				treeObj.updateNode(node);
								}
								return false;
							} else {
								var node = treeObj.getNodeByParam("id", key, null); 
								if(val==-1){
									node.iconSkin ='diy03';
			        				treeObj.updateNode(node);
								}else if(val==0){
									node.iconSkin ='diy01';
			        				treeObj.updateNode(node);
								}
							}	
						});
					}
				}
			}
		}); 
 
	    
	}
		
function eod_is(){
	var node =[];
	var treeObj = $.fn.zTree.getZTreeObj("eod-lis");
	var station_ids = treeObj.getCheckedNodes(true);
	for(var i=0;i<=station_ids.length-1;i++){
		if(station_ids[i].id){
			node.push(station_ids[i].id);
		}
	}
	send = $("input[name='cmd']:checked").val();
	if(!send){
		$("#dialog").html("命令未选择");
		$("#dialog").dialog( "open" );
		return false;
	}
	if(node ==""){
		$("#dialog").html("节点未选择");
		$("#dialog").dialog( "open" );
		return false;
	}else{
		return true;
	}
}

function eod_show() {
	var button;
	$('table.cmd-table input[name^="cmd"]:checked').attr("checked",false);
	if(station_id ==0){
		button =  [ {
			text : "正常下发",
			click : function() {
					if(eod_is()){
						username_show("normalSend");
					}		
			}
				
		}, {
			text : "测试下发",
			click : function() {
				if(eod_is()){
					username_show("testSend");
				}
			}	
		},  {
			text : "取消",
			click : function() {
				$(this).dialog("close");
			}
		} ];
	}else{
		button =  [ {
			text : "正常下发",
			click :function() {
				if(eod_is()){
					username_show("normalSend");
				}		
		}
		}, {
			text : "取消",
			click : function() {
				$(this).dialog("close");
			}
		} ];
	}
	$("#eod-dialog").dialog({
		'width' : 500,
		modal: true,
		buttons : button,
	});
}

function myShow() {
	$("#myShow").dialog({
		'width' : 30,
		modal: true,
		open: function(event, ui) {
            $("[aria-describedby='myShow'] .ui-dialog-titlebar-close").hide();
            $("[aria-describedby='myShow'] .ui-widget-header").hide();
        },
	});
}

function checkSendFutureVersionParam() {
	var effectiveVersion = $("#furtur_eod_version input[name='eff_version']").val();
	if (effectiveVersion) {
		if (!isPositiveInteger(effectiveVersion)) {
			$("#dialog").html("生效版本号必须为正整数");
			$("#dialog").dialog( "open" );
			return false;
		}
		return true;
	} else {
		$("#dialog").html("生效版本号不能为空");
		$("#dialog").dialog( "open" );
		return false;
	}
}

function checkSendFutureEndDateParam() {
	var effStartTime = $("#effective_start_date").val();
	var effEndTime = $("#effective_end_date").val();
	if (effEndTime) {
		var effectiveStart = new Date(effStartTime.replace("-", "/").replace("-", "/"));
		var effectiveEnd = new Date(effEndTime.replace("-", "/").replace("-", "/"));
		if(effectiveEnd < effectiveStart){
			$("#dialog").html("生效结束时间必须大于等于生效开始时间");
			$("#dialog").dialog( "open" );
	        return false;
	    }
	    return true;
	} else {
		$("#dialog").html("生效结束时间不能为空");
		$("#dialog").dialog( "open" );
		return false;
	}
}

function isPositiveInteger(str) {
	var regu = /^[1-9]*[1-9][0-9]*$/;
	return regu.test(str);
}

function resetFutureVersionParam() {
	$("#furtur_eod_version input[name='acc_zlc']")[0].checked = true;
	$("#furtur_eod_version input[name='eff_version']").val("");
	$("#effective_start_date").datepicker("setDate", new Date());
	$("#effective_end_date").val("");
}