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

function submit_mode() {
	var node =[];
	var deviceid = [];
	var treeObj = $.fn.zTree.getZTreeObj("seq-lis");
	var station_ids = treeObj.getCheckedNodes(true);
	for(var i=0;i<=station_ids.length-1;i++){
		if(station_ids[i].id){
			node.push(station_ids[i].id);
		}
		
	}
	var mode = $('#mode-list input:checked');
	var sub_mode_id = parseInt(mode.parents('p').find('select').val());
	if(node ==""){
		$("#eq_msg").html("节点未选择");
		setTimeout('$("#eq_msg").html("&nbsp;")',5000);
		return;
	}
	var param = $.param({
		stationId: JSON.stringify(node),
		sendCommand:  "INSERV",
		sendParam: '['+sub_mode_id+']'
	});
	console.log(33333);
	$.post("SendCommand", param, function(data) {
		if(data ==""){
			$("#eq_msg").html("发送失败");
			setTimeout('$("#eq_msg").html("&nbsp;")',5000);
			return;
		}
		var so=JSON.parse(data);
		var status =[];
		for(var z=0;z<=so.length-1;z++) {
			status[so[z].stationId] = so[z].status[0];
			
    	}
		var treeObj = $.fn.zTree.getZTreeObj("seq-lis");
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
		$("#eq_msg").html("已发送");
		setTimeout('$("#eq_msg").html("&nbsp;")',5000);
	}).fail(function(){
		alert("发送失败");
	});
}

function load_eq_tree(){
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
				$.fn.zTree.init($("#seq-lis"), setting);
							}, 500);
}
// 运营模式
function switch_mode_show() {
	dialog = "switch_mode";
	$("#mode-dialog").dialog({
		'width' : 800,
		modal: true,
		buttons : [ {
			text : "发送",
			click : function() {
				var node=[];
				var treeObj = $.fn.zTree.getZTreeObj("seq-lis");
				var station_ids = treeObj.getCheckedNodes(true);
				for(var i=0;i<=station_ids.length-1;i++){
					if(station_ids[i].id){
						node.push(station_ids[i].id);
					}
				}
				
				if(node ==""){
					$("#eq_msg").html("节点未选择");
					setTimeout('$("#eq_msg").html("&nbsp;")',5000);
				}else{
					username_show();
				}
				
			}
		}, {
			text : "取消",
			click : function() {
				$(this).dialog("close");
			}
		} ],
	});
}

