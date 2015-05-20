util = new F3Util();

function F3Util(){
	// get the watch count
	// scopeHash is an optional parameter, but if you provide one, this function will modify it for later use (possibly debugging)
	this.getWatchCount = function(scope, scopeHash) {
	    // default for scopeHash
	    if (scopeHash === undefined) {
	        scopeHash = {};
	    }

	    // make sure scope is defined and we haven't already processed this scope
	    if (!scope || scopeHash[scope.$id] !== undefined) {
	        return 0;
	    }

	    var watchCount = 0;

	    if (scope.$$watchers) {
	        watchCount = scope.$$watchers.length;
	    }
	    scopeHash[scope.$id] = watchCount;

	    // get the counts of children and sibling scopes
	    // we only need childHead and nextSibling (not childTail or prevSibling)
	    watchCount+= this.getWatchCount(scope.$$childHead, scopeHash);
	    watchCount+= this.getWatchCount(scope.$$nextSibling, scopeHash);

	    return watchCount;
	}
	
	this.isArray = function(obj){
		return Object.prototype.toString.call( obj) === '[object Array]';
	}
	

	this.isInArray = function(obj, array) {
		if (!array) {
			return false;
		}
	    var i = array.length;
	    while (i--) {
	       if (array[i] == obj) {
	           return true;
	       }
	    }
	    return false;
	}
	
	this.log = function(msg){
		if(typeof console != 'undefined' && console.log){
			for (var i = 0; i < arguments.length; i++){
				console.log(arguments[i]);
			}
		}
	}

	this.debug= function(msg){
		if(typeof console != 'undefined'  && console.debug){
			for (var i = 0; i < arguments.length; i++){
				console.debug(arguments[i]);
			}
		}
	}
	
	this.error = function(){
		if(typeof console != 'undefined'  && console.error){
			for (var i = 0; i < arguments.length; i++){
				console.error(arguments[i]);
			}
		}
	}
	
	this.stackTrace = function(e){
		if(typeof console != 'undefined' && console.error){
		 var stack = e.stack.replace(/^[^\(]+?[\n$]/gm, '')
		 .replace(/^\s+at\s+/gm, '')
		 .replace(/^Object.<anonymous>\s*\(/gm, '{anonymous}()@')
		 .split('\n');
		 console.error(e.stack);
		}
	}



	/*** temporary solution of scrolling functions ****/
	this.scrollToPrefs = function() {
		hScrollToElement("#prefs");
	}

	function showHomeNavi() {
		$("#homeNav").slideToggle();
	}

	function hScrollToElement(selector){
		util.log('scrollTo' + selector);
		t = $(selector).offset().top;
		if(Math.abs($(window).scrollTop() - t) > 10 ){
			$('html:not(:animated),body:not(:animated)').animate(
				{scrollTop: t},700);
		}
	}
   
}

/**
 * Export as node module
 */
if(typeof(module) != "undefined"){
	module.exports = util;
}