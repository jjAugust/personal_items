var type_list="event";
var deviceid="3";
var stationId="3";
$(function(){
	
	SetCurrentMenu(2);
	$("#tab-tables" ).tabs();
	
	$("#status-list").jqGrid({
		datatype: "json",
		height:630,
		width:1250,
	   	colNames:['发生时间', '车站名称', '设备名称', '设备编号',
	   	          '事件标记',  '事件标记名','事件标志值'],
	   	colModel:[
	   		{name: 'time', width: 160, formatter: 'date', formatoptions: {
	   			srcformat: 'U', newformat: 'Y-m-d H:i:s'}},
	   		{name: 'stationName', sortable: false},
	   		{name: 'deviceName', sortable: false},
	   		{name: 'deviceSubId', sortable: false},
	   		{name: 'TAG', sortable: false},
	   		{name: 'tagname', sortable: false},
	   		{name: 'tagval', sortable: false}
	   	],
	   	rowNum:300,
	   	forceFit: false,
	   	autowidth: false,
	   	autoencode: false,
	    sortorder: "desc",
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
			setInterval("zTreeOnclick()",30000);
			
			
	
});


function zTreeOnclick(event, treeId, treeNode){
	//$("#"+type_list+"-list").jqGrid("clearGridData");
	if(treeNode != null){
		$("#menu_tree").show();
		deviceid=treeNode.id;
		stationId=treeNode.pId;
		$("#menu_tree").show();
		if(stationId == null){
			$("#menu_tree_msg").html(treeNode.name);
		}else{
			var treeObj = $.fn.zTree.getZTreeObj("station-lis");
			var node = treeObj.getNodeByParam("id",treeNode.pId,null);
			$("#menu_tree_msg").html(node.name+" "+treeNode.name);
		}
		
	}
	var deviceid_array = deviceid.split("_");
	if(deviceid_array[0] =="L"){
		return;
	}
	var stationId_array = stationId.split("_");
	if(stationId_array[0] =="L"){
		return;
	}
		var param = {
			url:"getStatusByDeviceId?type=status&stationSubId="+stationId+"&deviceId="+deviceid,
			};
		$("#status-list").setGridParam(param).trigger("reloadGrid").trigger('refreshIndex');
}