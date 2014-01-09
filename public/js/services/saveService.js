function SaveService($http){
	
	var me = this;
	
	this.saveDocument = function($scope, document){
		
		$scope.saved = false;
		
		$http.post('/document/save', document).success(function(){
			$scope.saved = true;
		});
	}
	
}