"use strict";

var modesApp = angular.module('modesApp', []);
modesApp.controller('modeDialogCtrl', function($scope, $http) {
	$http.get('GetStations').success(function(data) {
		$scope.stations = data;
	});
	$http.get('GetModes').success(function(data) {
		$scope.modes = data;
		$scope.mode_id = data[0].id;
	});
	$scope.station_id = $.url().param("s");
});

function load_station_tree(){
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
				url:"../AFCSUP/GetAllLines",
				autoParam:["id", "name=n", "level=lv"],
				otherParam:{"stationSubId":station_id}
			}
		};
			setTimeout(function() {
				$.fn.zTree.init($("#switch-lis"), setting);
							}, 500);
}

function submit_station() {
	var node =[];
	var treeObj = $.fn.zTree.getZTreeObj("switch-lis");
	var station_ids = treeObj.getCheckedNodes(true);
	for(var i=0;i<=station_ids.length-1;i++){
		if(station_ids[i].id){
			node.push(station_ids[i].id);
		}
		
	}
	var cmd = $('table.station-table input:checked').val();
	var params = [];
	if(cmd =="STATEM_0"){
		cmd = "STATEM";
		params.push("0");
	}else if(cmd =="STATEM_1"){
		cmd = "STATEM";
		params.push("1");
	}
	if(node ==""){
		$("#station_msg").html("节点未选择");
		setTimeout('$("#station_msg").html("&nbsp;")',5000);
		return;
	}
	var param = $.param({
		stationId: JSON.stringify(node),
		sendCommand: cmd,
		sendParam: JSON.stringify(params)
	});
	$.post("SendCommand", param, function(data) {
		if(data ==""){
			$("#station_msg").html("发送失败");
			setTimeout('$("#station_msg").html("&nbsp;")',5000);
			return;
		}
		var so=JSON.parse(data);
		var status =[];
		for(var z=0;z<=so.length-1;z++) {
			status[so[z].stationId] = so[z].status[0];
			
    	}
		var treeObj = $.fn.zTree.getZTreeObj("switch-lis");
		var nodes = treeObj.getNodes();
		if (nodes.length>0) {
			for(var i=0;i<=nodes.length-1;i++) {
				for(var j=0;j<=nodes[i].children.length-1;j++){
					if(status[nodes[i].children[j].id]==-1){
						nodes[i].children[j].iconSkin ='diy03';
        				treeObj.updateNode(nodes[i].children[j]);
					}else if(status[nodes[i].children[j].id]==0){
						nodes[i].children[j].iconSkin = "diy01";
        				treeObj.updateNode(nodes[i].children[j]);
					}
					
				}
				
        	}
		}
		$("#station_msg").html("已发送");
		setTimeout('$("#station_msg").html("&nbsp;")',5000);
	}).fail(function(){
		alert("发送失败");
	});
}

// 运营模式
function switch_station_show() {
	dialog = "switch_station";
	$("#station-dialog").dialog({
		'width' : 800,
		modal: true,
		buttons : [ {
			text : "发送",
			click : function() {
				var node = [];
				var treeObj = $.fn.zTree.getZTreeObj("switch-lis");
				var station_ids = treeObj.getCheckedNodes(true);
				for(var i=0;i<=station_ids.length-1;i++){
					if(station_ids[i].id){
						node.push(station_ids[i].id);
					}
					
				}
				
				if(node ==""){
					$("#station_msg").html("节点未选择");
					setTimeout('$("#station_msg").html("&nbsp;")',5000);
					return;
				}
				username_show();
			}
		}, {
			text : "取消",
			click : function() {
				$(this).dialog("close");
			}
		} ],
	});
}