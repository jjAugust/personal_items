function updatetree(){
	var status = [];
	var diy =['diy01','diy06','diy02','diy03','diy04'];
	$.ajax({
        url: "GetAllStationEvents",
        data:{"stationlist": 2},
        type: "GET",
        cache: false,
        dataType: "json",
        success: function (stations) {
        		for(var i=0;i<=stations.length-1;i++) {
            		if(stations[i].status !=""){
            			status[stations[i].stationid]=stations[i].status[0].METSER.value;
            		}   			
            	}

        		var treeObj = $.fn.zTree.getZTreeObj("station-lis");
        		var nodes = treeObj.getNodes();
        		if (nodes.length>0) {
        			for(var i=0;i<=nodes.length-1;i++) {
        				for(var j=0;j<=nodes[i].children.length-1;j++){
        					if(status[nodes[i].children[j].id]!=null){
        						var s =status[nodes[i].children[j].id];
        						nodes[i].children[j].iconSkin = diy[s];
                				treeObj.updateNode(nodes[i].children[j]);
        					}else{
        						nodes[i].children[j].iconSkin = "diy04";
                				treeObj.updateNode(nodes[i].children[j]);
        					}        					
        				}        				
                	}
        		}
        }
    });
	
	
}


