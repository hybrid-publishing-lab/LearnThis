function getIntersect(arr1, arr2) {
    var r = [], o = {}, l = arr2.length, i, v;
    for (i = 0; i < l; i++) {
        o[arr2[i]] = true;
    }
    l = arr1.length;
    for (i = 0; i < l; i++) {
        v = arr1[i];
        if (v in o) {
            r.push(v);
        }
    }
    return r;
}

var intersect = function(arr1, arr2){
	return !(getIntersect(arr1, arr2).length == 0);
}

lhpFilters.filter('nonEmptyIntersection', function() {
	return function(textelements, keywords) {
		// no keywords set
		if(keywords.length == 0){
			return textelements;
		}
		
		var out = [];
		for (i = 0; i < textelements.length; i++) {
		    var ele = textelements[i];
	    	if(intersect(ele.keywords, keywords)){
	    		out.push(ele);
	    	}
		}
		return out;
	}
});

