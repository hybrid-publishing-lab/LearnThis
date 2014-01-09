
lhpControllers.controller('DocumentController', [ '$scope', '$http', 'SaveService', DocumentController]);
function DocumentController($scope, $http, SaveService){
	
	$scope.saved = true;
	
//	$scope.newDoc = function(){
		$http.get('/json/document/new').success(function(data){
			$scope.document = data;
		});
//	}
	
//	$scope.getById = function(id){
//		$http.get('/json/document/'+id).success(function(data){
//			$scope.document = data;
//		});
//	}
	
}

