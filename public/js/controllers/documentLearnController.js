lhpControllers.controller('DocumentLearnController', [ '$scope', '$http', 'ScoringService',DocumentLearnController ]);
function DocumentLearnController($scope, $http, scoringService) {
	var CARDSIDE_FRONT = 'front';
	var CARDSIDE_BACK = 'back';
	
	$scope.isInit = false;
	$scope.showScore = false;

	$scope.pager = [];
	/**
	 * index of the current page within $scope.pager
	 */
	$scope.currentPage = 0;
	/**
	 * current card Object
	 */
	$scope.currentCard;
	/**
	 * currently active side of currentCard, either 'front' or 'back'
	 */
	$scope.currentSide;
	/**
	 * Index of the currentCard within $scope.document.cards
	 */
	$scope.currentIndex;


	$scope.init = function(docId) {
		if (!$scope.isInit && docId) {
			localStorage.clear();
			$scope.isInit = true;
			$http.get('/json/document/' + docId).success(function(data) {
				$scope.document = data;
				$scope.populatePager();
				$scope.updateCard();
			});
			$http.get('/json/textelement/types').success(function(data) {
				$scope.textelementTypes = data;
			});
		}
	}
	
	$scope.populatePager = function() {
		for (index = 0; index < $scope.document.cards.length; ++index) {
			if ($scope.document.cards[index].front != null) {
				$scope.pager.push({cardIndex:index, cardSide: CARDSIDE_FRONT});
			}
			if ($scope.document.cards[index].back != null){
				$scope.pager.push({cardIndex:index, cardSide: CARDSIDE_BACK});
			}
		}
	} 
	
	$scope.showScorePage = function() {
		$scope.showScore = true;
		$scope.score = scoringService.calcScore($scope.document.id);
		$scope.maxScore = scoringService.calcMaxScore($scope.pager);
	}
	
	$scope.nextCard = function() {
		util.log(localStorage.getItem($scope.document.id));
		if ($scope.currentPage < $scope.pager.length-1) {
			$scope.currentPage++;
			$scope.updateCard();
		} else {
			$scope.showScorePage();
		}
	} 
	
	$scope.prevCard = function() {
		if ($scope.showScore) {
			$scope.currentPage = $scope.pager.length-1;
			$scope.showScore = false;
			$scope.updateCard();
		} else {
			if ($scope.currentPage > 0) {
				$scope.currentPage--;
				$scope.updateCard();
			}
		}
	} 
	
	$scope.updateCard = function() {
		$scope.currentSide = $scope.pager[$scope.currentPage].cardSide;
		$scope.currentIndex = $scope.pager[$scope.currentPage].cardIndex;
		$scope.currentCard = $scope.document.cards[$scope.currentIndex];
	}
	
	$scope.userUnderstood = function(understood) {
		var card = $scope.currentCard[$scope.currentSide];
		card.understood = understood;
		scoringService.scoreText($scope.document.id, card);
		$scope.nextCard();
	}
	
	$scope.userSolvesMc = function() {
		var card = $scope.currentCard[$scope.currentSide];
		card.solved = true;
		scoringService.scoreMc($scope.document.id, card);
	}
	
	$scope.restart = function() {
		location.reload();
	}
}
