
lhpControllers.controller('DocumentListController', [ '$scope', '$http', 'SaveService', DocumentListController]);
function DocumentListController($scope, $http, saveService){
	
	$scope.saved = true;
	$scope.isInit = false;
	$scope.page = 0;
	$scope.hasAll = false;
	
	$scope.init = function(fetchAll){
		if (!$scope.isInit) {
			$scope.isInit = true;
			$scope.fetchData(true);
		}
	}
	
	$scope.fetchData = function(fetchAll) {
	  var url = '/json/document/random/5';
	  if (fetchAll) {
	    url = '/json/document/all/'+$scope.page;
	  }
	  $http.get(url).success(function(data){
	    if (data && data.length > 0) {
	      for(j = 0; j < data.length; ++j) {
	        var combinedTexts = "" + data[j].title;
	        for(index = 0; index < data[j].cards.length; ++index) {
	          var card = data[j].cards[index];
	          if (card.front && card.front.text) {
	            combinedTexts += ' ' + card.front.text;
	          }
	          if (card.back && card.back.text) {
	            combinedTexts += ' ' + card.back.text;
	          }
	        }
	        data[j].combinedTexts = combinedTexts;
	      }
	      if ($scope.documents) {
	        $scope.documents.push.apply($scope.documents, data);
	      } else {
	        $scope.documents = data;
	      }
      } else {
        $scope.hasAll = true;
      }
	  });
	}

  $scope.fetchNext = function() {
    $scope.page++;
    $scope.fetchData(true);
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

