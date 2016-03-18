(function($) {
	$.fn.getSVG = function(idx) {
		if (typeof idx == 'undefined')
			idx = 0;
		var doc = this[idx].getSVGDocument();
		return $('svg', doc);
	};
	
	function transform_to_dict(transform) {
		var p = /([^()]+)\s*\(([^()]*)\)/gi;
		var m, dict = {};
		while (m = p.exec(transform)) {
			dict[m[1]] = m[2];
		}
		
		return dict;
	}
	
	function dict_to_transform(dict) {
		var trans = '';
		$.each(dict, function(k, v){
			trans += k + '(' + v + ') ';
		});
		return trans;
	}
	
	$.fn.scale = function(x, y) {
		var e = $('#viewport', this);
		var transform = e.attr('transform');
		var dict = transform_to_dict(transform);
		dict.scale = x + ',' + y;
		transform = dict_to_transform(dict);
		e.attr('transform', transform);
	};
	
	$.fn.setBox = function(w, h) {
		var e = $('#viewport', this)[0];
		var box = e.getBBox();
		var x = w / box.width;
		var y = h / box.height;
		this.scale(x, y);
		this.attr('width', w + 'px');
		this.attr('height', h + 'px');
	};
	
	$.fn.fitToBox = function(w, h) {
		var e = $('#viewport', this)[0];
		var box = e.getBBox();
		var hw = box.height / box.width;
		if (hw * w > h) {
			w = h / hw; 
		} else {
			h = w * hw;
		}
		this.setBox(w, h);
	};
}(jQuery));