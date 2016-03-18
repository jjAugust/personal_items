/*
function transform(root) {
    var content = document.getElementById("svgholder");
    var clientWidth = content.clientWidth;
    var clientHeight = content.clientHeight;
    var container = root.getElementById("viewport");
    var svgWidth = root.getAttribute("width");
    var svgHeight = root.getAttribute("height");
    var scale = ((clientWidth / svgWidth) > (clientHeight / svgHeight)) ? (clientHeight / svgHeight) : (clientWidth / svgWidth);
    if ((clientWidth / svgWidth) > (clientHeight / svgHeight)) {
        var loc = Math.ceil((clientWidth - svgWidth * scale) / 2);
        container.setAttribute("transform", "scale(" + scale + ")translate(0, 0)");
    }
    else {
        var loc = Math.ceil((clientHeight - svgHeight * scale) / 2);
        container.setAttribute("transform", "scale(" + scale + ")translate(0, 0)");
    }
}

function transformInDialog(root) {
    var clientWidth = 640;
    var clientHeight = 480;
    var container = root.getElementById("viewport");
    var svgWidth = root.getAttribute("width");
    var svgHeight = root.getAttribute("height");
    var scale = ((clientWidth / svgWidth) > (clientHeight / svgHeight)) ? (clientHeight / svgHeight) : (clientWidth / svgWidth);
    if ((clientWidth / svgWidth) > (clientHeight / svgHeight)) {
        var loc = Math.ceil((clientWidth - svgWidth * scale) / 2);
        container.setAttribute("transform", "scale(" + scale + ")translate(" + loc + ",0)");
    }
    else {
        var loc = Math.ceil((clientHeight - svgHeight * scale) / 2);
        container.setAttribute("transform", "scale(" + scale + ")translate(0," + loc + ")");
    }
}*/