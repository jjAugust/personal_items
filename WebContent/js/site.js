jQuery(function ($) {
    var currentMenu = null;
    $('#sidebar>ul>li').each(function () {
        if ($(this).find('li').length == 0) {
            $(this).addClass('nosubmenu');
        }
    });
    $('#sidebar>ul>li[class!="nosubmenu"]>a').each(function () {
        if (!$(this).parent().hasClass('current')) {
            $(this).parent().find('ul:first').hide();
        } else {
            currentMenu = $(this);
        }
        $(this).click(function () {
            $('#sidebar>ul>li.current').removeClass('current');
            if (currentMenu != null && currentMenu.text() != $(this).text()) {
                currentMenu.parent().find('ul:first').slideUp();
            }
            if (currentMenu != null && currentMenu.text() == $(this).text()) {
                currentMenu.parent().find('ul:first').slideUp();
                currentMenu = null;
            } else {
                currentMenu = $(this);
                currentMenu.parent().addClass('current');
                currentMenu.parent().find('ul:first').slideDown();
            }
            return false;
        });
    });

    $(document).click(function(e) {
    	hidemenu(e);
        
    });
    
    $('#navbar').click(function(event){
        event.stopPropagation();
    });

});

function station_model(id){
	 $.ajax({
	        url: "getStationModelByStationId", 
	        data: {stationSubId: id},
	        type: "GET",
	        cache: false,
	        dataType: "json",
	        success: function (data) {	
	        	$("#menu_station").show();
	        	$("#menu_station_msg").html(data.stationModel);
	        }
	    });
}

function hidemenu(e){
	var target = e.target;

    if (!$(target).is('#menu') && !$(target).parents().is('#menu')) {
        $('#menu').hide();
    }
    if (!$(target).is('#stations') && !$(target).parents().is('#stations')) {
        $('#stations').hide();
    }
}

function SetCurrentMenu(obj) {
	var s = ["","事件管理","状态管理","线路监控","车站监控","客流监控","EOD管理","服务管理","库存查询"];
	$("#menu_msg").html(s[obj]);
}

var dateConfig = {
    dateFormat: "yy-mm-dd",
    changeMonth: true,
    changeYear: true,
    dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
    firstDay: 1,
    monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    showOtherMonths: true,
    showOn: "button",
    buttonImage: "/images/calendar.gif",
    buttonImageOnly: true,
    yearRange: "c-50:c+5"
};
function SomeJavaScriptCode(){
	alert("aaa");
}
function make_svg_embed(path,station) {
	var height ="height:400px;";
	var html = '<embed  wmode="transparent" onerror="SomeJavaScriptCode" style="width:100%;'+height+'" type="image/svg+xml" src="'
	+ path + '"></embed >';
	if(station == "station"){
		height ="height:675px;";
		html = '<embed align="center" onerror="SomeJavaScriptCode" wmode="transparent"  style="width:100%;" align="center" type="image/svg+xml" src="'
		+ path + '"></embed>';
	}
	
	
	return html;
}

function make_svg_div(id, path, cls) {
	var clsin = "";
	if (typeof cls !== "undefined" && cls)
		clsin = ' class="' + cls + '" ';
	return '<div id="' + id + '"' + clsin + '>' + make_svg_embed(path) + '</div>';
}

function pad_left(str,lenght)
{
    if(str.length >= lenght) 
        return str;
    else
        return pad_left("0" +str,lenght);
}
function format_device_name(type, index)
{
    return type.toUpperCase() + "_" + pad_left(index,3);
}