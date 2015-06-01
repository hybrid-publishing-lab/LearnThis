lhpControllers.controller('DocumentController', [ '$scope', '$http', '$location', 'SaveService', 'LocalStorageService', DocumentController ]);
function DocumentController($scope, $http, $location, saveService, localStorageService) {

	$scope.init = function(docId) {
		if (!$scope.isInit && docId) {
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
	  saveService.deleteDoc(id, $scope.document.title);
	}
		
	$scope.fbshare = function(){ 
	  var url =  $location.absUrl();
		var sharer = "https://www.facebook.com/sharer/sharer.php?u="+url; 
		window.open(sharer,'sharer', 'width=626,height=436'); 
	}

	$scope.twittershare = function (){ 
	  var text = $scope.document.title + " " + $location.absUrl();
		var sharer = "https://twitter.com/intent/tweet?text="+text; 
		window.open(sharer,'sharer', 'width=626,height=486'); 
	}
	

	$scope.whatsappshare = function (){ 
		 var text = "Kartenset: " + $scope.document.title + " : " + $location.absUrl();
		var sharer = "whatsapp://send?text="+text; 
		document.location.href=sharer;
	}
	
	
	$scope.mailtoshare = function (){ 
		 var text = "Kartenset: " + $scope.document.title + "&body=" + $location.absUrl();
		var sharer = "mailto:?subject="+text; 
		document.location.href=sharer; 


	}
	


}
