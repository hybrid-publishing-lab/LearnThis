function SaveService($http){
	
	var me = this;
	
	this.saveDocument = function($scope, document, pw){
		$scope.currentlySaving = true;
		util.log("Saving document", document);
		
		$scope.saved = false;
		
		document.pw = pw;
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