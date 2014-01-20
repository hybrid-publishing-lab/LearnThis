
lhpControllers.controller('DocumentListController', [ '$scope', '$http', 'SaveService', DocumentListController]);
function DocumentListController($scope, $http, saveService){
	
	$scope.saved = true;
	$scope.isInit = false;
	
	$scope.init = function(){
		if (!$scope.isInit) {
			$scope.isInit = true;
			$http.get('/json/document/all').success(function(data){
				$scope.documents = data;
			});
		}
	}
	
	$scope.deleteElement = function (ele, index) {
		$scope.saved = false;
		$http.post('/json/document/delete/'+ele.id, ele).success(function(data){
			$scope.saved = true;
			if($scope.documents[index] == ele) {
				$scope.documents.splice(index, 1);
			} else {
				alert('das element am index entspricht nicht dem zu löschenden element');
			}
		});
	}
}

