var lhp = {};

// ///////////////////////////
// Init angular App
//
var lhpApp = angular.module('lhpApp', [ 'lhpControllers', 'lhpServices' ]);

var lhpControllers = angular.module('lhpControllers', []);

/**
 * logs the event and uses $scope.broadcast to broadcast it
 * 
 * Pass as many arguments as you like
 */
lhp.sendEvent = function($scope, event, arg0){
//	util.debug("sending event "+ event, arguments);
    util.debug("sending event "+ event);
	var args = Array.prototype.slice.call(arguments);
	$scope.$broadcast.apply($scope, args.slice(1));
}