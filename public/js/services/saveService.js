function SaveService($http, localStorageService){
	
	var me = this;
	
	this.saveDocument = function($scope, document, pw){
		$scope.currentlySaving = true;
		util.log("Saving document", document);
		
		$scope.saved = false;
		
		document.pw = pw;
		var email = document.email;
		$http.post('/json/document/save', document)
		.success(function(data, status, header, config){
			$scope.document = data;
			$scope.document.email = email;
			$scope.saved = true;
			$scope.currentlySaving = false;
			if (!$scope.isPersisted()) {
			  // Adressbar needs to change from /document/new to /document/id
			  var title = "Kartenset: " + $scope.document.title + " - LearnThis";
			  window.history.replaceState(title, title, "/document/edit/"+$scope.document.id);
      }
		})
		.error(function(data, status, header, config){
			$scope.currentlySaving = false;
		});
	}

  this.deleteDocument = function(id, pw) {
    $http.post('/json/document/delete/'+id, {pw:pw}).
    success(function(data, status, headers, config) {
      window.location.replace("/");
    }).
    error(function(data, status, headers, config) {
      alert("Passwort inkorrekt.");
    });
  }
  
  this.deleteDoc = function(id, title) {
    // get doc from localStorage to check password
    var savedResult = localStorageService.loadDoc(id);
    util.log("savedResult", savedResult);
    var password = '';
    if (savedResult.password) {
      password = savedResult.password.pw;
    }

    // check if password matches
    $http.post('/json/document/checkpw/' + id, {pw:password})
      .success(function (data, status, headers, config) {
        if (confirm("Wollen Sie das Kartenset '"+title+"' wirklich löschen?")) {
          me.deleteDocument(id, password);
        }
      }).error(function (data, status, headers, config) {
        password = prompt("Zum Löschen des Kartensets '"+title+"' wird das Passwort benötigt.", "");
        me.deleteDocument(id, password);
      });
  }
    
}