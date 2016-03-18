var type_list="event";
var deviceid="3";
var stationId="3";
$(function(){
	
	SetCurrentMenu(1);
	$("#tab-tables" ).tabs();
	$("#event-list").jqGrid({
		datatype: "json",
		height:630,
		width:1250,
	   	colNames:['发生时间', '车站名称/编号', '设备名称', '设备编号',
	   	          '事件标记名', '事件标志值',  '事件说明','事件级别'],
	   	colModel:[
	   		{name: 'time', width: 160, formatter: 'date', formatoptions: {
	   			srcformat: 'U', newformat: 'Y-m-d H:i:s'}},
	   		{name: 'stationNameAndId', sortable: false},
	   		{name: 'deviceName', sortable: false},
	   		{name: 'deviceId', sortable: false},
	   		{name: 'tagName',width: 240, sortable: false},
	   		{name: 'value', sortable: false},
	   		{name: 'valDesc',width: 300, sortable: false},
	   		{name: 'level', sortable: false},
	   	],
	   	rowNum:500,
	   	forceFit: false,
	   	autowidth: false,
	   	autoencode: false,
	    sortorder: "desc",
	});
	
	$("#alarm-list").jqGrid({
		datatype: "json",
		height:630,
		width:1250,
		colNames:['发生时间', '车站名称/编号', '设备名称', '设备编号',
	   	          '事件标记名', '事件标志值',  '事件说明','事件级别'],
	   	colModel:[
	   		{name: 'time', width: 160, formatter: 'date', formatoptions: {
	   			srcformat: 'U', newformat: 'Y-m-d H:i:s'}},
	   		{name: 'stationNameAndId', sortable: false},
	   		{name: 'deviceName', sortable: false},
	   		{name: 'deviceId', sortable: false},
	   		{name: 'tagName',width: 240, sortable: false},
	   		{name: 'value', sortable: false},
	   		{name: 'valDesc',width: 300, sortable: false},
	   		{name: 'level', sortable: false},
	   	],
	   	rowNum:500,
	   	forceFit: false,
	   	autowidth: false,
	   	autoencode: false,
	    sortorder: "desc",
	});
	
	$("#warning-list").jqGrid({
		datatype: "json",
		height:630,
		width:1250,
		colNames:['发生时间', '车站名称/编号', '设备名称', '设备编号',
	   	          '事件标记名', '事件标志值',  '事件说明','事件级别'],
	   	colModel:[
	   		{name: 'time', width: 160, formatter: 'date', formatoptions: {
	   			srcformat: 'U', newformat: 'Y-m-d H:i:s'}},
	   		{name: 'stationNameAndId', sortable: false},
	   		{name: 'deviceName', sortable: false},
	   		{name: 'deviceId', sortable: false},
	   		{name: 'tagName',width: 240, sortable: false},
	   		{name: 'value', sortable: false},
	   		{name: 'valDesc',width: 300, sortable: false},
	   		{name: 'level', sortable: false},
	   	],
	   	rowNum:500,
	   	forceFit: false,
	   	autowidth: false,
	   	autoencode: false,
	});
	
	
	var setting = {
			check: {
				enable: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			view: {
				fontCss : {color:"#333333"}
			},
			callback: {
				onClick: zTreeOnclick
			},
			async: {
				enable: true,
				type: "get",
				url:"../AFCSUP/getAllLinesDevices",
				autoParam:["id", "name=n", "level=lv"],
				otherParam:{"otherParam":"zTreeAsyncTest"}
			}
		};

			$.fn.zTree.init($("#station-lis"), setting);
			setInterval("updatetree()",5000);
			setInterval("zTreeOnclick()",30000);
			
			
	
});


function zTreeOnclick(event, treeId, treeNode){
	//在主页面显示选中的节点值
	if(treeNode != null){
		$("#menu_tree").show();
		deviceid=treeNode.id;
		stationId=treeNode.pId;
		if(!deviceid){
			$("#menu_tree").hide();
			return;
		}
		if(stationId == null){
			$("#menu_tree_msg").html(treeNode.name);	
		}else{
			var treeObj = $.fn.zTree.getZTreeObj("station-lis");
			var node = treeObj.getNodeByParam("id",treeNode.pId,null);
			$("#menu_tree_msg").html(node.name+" "+treeNode.name);
		}
	}
	//若当前节点或父节点为线路，直接返回页面
	var deviceid_array = deviceid.split("_");
	if(deviceid_array[0] =="L"){
		return;
	}
	var stationId_array = stationId.split("_");
	if(stationId_array[0] =="L"){
		return;
	}
	//若选中的节点是设备或站，则启用此处URL，否则调取if中的URL
	var url ="getEventsByStationId?type="+type_list+"&stationSubId="+stationId+"&deviceId="+deviceid;
	if(!stationId){
		url ="getEventsByStationId?type="+type_list+"&stationSubId="+deviceid;
	}
	//$("#"+type_list+"-list").jqGrid("clearGridData");
		var param = {
			url:url,
			};
		$("#"+type_list+"-list").setGridParam(param).trigger("reloadGrid").trigger('refreshIndex');
}