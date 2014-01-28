/*
 * from:  http://blog.justonepixel.com/geek/2013/08/14/angularjs-auto-expand/
 * leicht angepasst
 */


lhpApp.directive('ngAutoExpand', function() {
    return {
        restrict: 'A',
        link: function( $scope, elem, attrs) {
            elem.bind('keyup', function($event) {
                var element = $event.target;

                $(element).height(0);
                var height = $(element)[0].scrollHeight;

                // 8 is for the padding
                if (height < 20) {
                    height = 28;
                }
//                $(element).height(height-8);
                $(element).height(height);
            });

            // Expand the textarea as soon as it is added to the DOM
            setTimeout( function() {
                var element = elem;

                $(element).height(0);
                var height = $(element)[0].scrollHeight;

                // 8 is for the padding
                if (height < 20) {
                    height = 28;
                }
//                $(element).height(height-8);
                $(element).height(height);
            }, 0)
        }
    };
});

lhpApp.directive('saveCursorPosition', function() {
    return {
        restrict: 'A',
        link: function( $scope, elem, attrs) {
        	var saveCursorPosition = function($event) {
        		var element = $event.target;
        		$scope.lastCursor.position = element.selectionStart;
        		util.log("Last Cursor Position: "+$scope.lastCursor.position);
        	}
            elem.bind('keyup', saveCursorPosition);
            elem.bind('mouseup', saveCursorPosition);
        }
    };
});

