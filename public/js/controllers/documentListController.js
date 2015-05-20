
lhpControllers.controller('DocumentListController', [ '$scope', '$http', 'SaveService', DocumentListController]);
function DocumentListController($scope, $http, saveService){
	
	$scope.saved = true;
	$scope.isInit = false;
	
	$scope.init = function(fetchAll){
		if (!$scope.isInit) {
			$scope.isInit = true;
			var url = '/json/document/random/5';
			if (fetchAll) {
			  url = '/json/document/all';
      }
			$http.get(url).success(function(data){
				$scope.documents = data;
				for(j = 0; j < $scope.documents.length; ++j) {
					var combinedTexts = "" + $scope.documents[j].title;
					for(index = 0; index < $scope.documents[j].cards.length; ++index) {
						var card = $scope.documents[j].cards[index];
						if (card.front && card.front.text) {
							combinedTexts += ' ' + card.front.text;
						}
						if (card.back && card.back.text) {
							combinedTexts += ' ' + card.back.text;
						}
					}
					$scope.documents[j].combinedTexts = combinedTexts;
				}
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

