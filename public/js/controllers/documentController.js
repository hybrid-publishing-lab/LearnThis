lhpControllers.controller('DocumentController', [ '$scope', '$http', 'SaveService', DocumentController ]);
function DocumentController($scope, $http, saveService) {

	$scope.init = function(docId) {
		if (!$scope.isInit && docId) {
			localStorage.clear();
			$scope.isInit = true;
			$http.get('/json/document/' + docId).success(function(data) {
				$scope.document = data;
				var combinedTexts = "" + $scope.document.title;
				for(index = 0; index < $scope.document.cards.length; ++index) {
					var card = $scope.document.cards[index];
					if (card.front && card.front.text) {
						combinedTexts += ' ' + card.front.text;
					}
					if (card.back && card.back.text) {
						combinedTexts += ' ' + card.back.text;
					}
				}
				$scope.document.combinedTexts = combinedTexts;
			});
			$http.get('/json/textelement/types').success(function(data) {
				$scope.textelementTypes = data;
			});
		}
	}
	
	$scope.deleteDoc = function(id) {
	    var pw = prompt("Zum löschen wird das Passwort benötigt.", "");
	    $http.post('/json/document/delete/'+id, {pw:pw}).
	    success(function(data, status, headers, config) {
	    	window.location.replace("/");
	    }).
	    error(function(data, status, headers, config) {
	    	alert("Passwort inkorrekt.");
	    });
	}
}
