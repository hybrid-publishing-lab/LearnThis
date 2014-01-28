function SaveService($http){
	
	var me = this;
	
	this.saveDocument = function($scope, document){
		$scope.currentlySaving = true;
		util.log("Saving document", document);
		
		$scope.saved = false;
		
		$http.post('/json/document/save', document)
		.success(function(data, status, header, config){
			$scope.document = data;
			$scope.saved = true;
			$scope.currentlySaving = false;
		})
		.error(function(data, status, header, config){
			$scope.currentlySaving = false;
		});
	}
	
}