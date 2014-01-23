function SaveService($http){
	
	var me = this;
	
	this.saveDocument = function($scope, document){
		
		util.log("Saving document", document);
		
		$scope.saved = false;
		
		$http.post('/json/document/save', document)
		.success(function(data, status, header, config){
			$scope.saved = true;
		})
		.error(function(data, status, header, config){
			
		});
	}
	
}