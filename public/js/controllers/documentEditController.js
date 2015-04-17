lhpControllers.controller('DocumentEditController', [ '$scope', '$http', 'SaveService', DocumentEditController ]);
function DocumentEditController($scope, $http, saveService) {
	
	const filterAlle = "alle";
	const filterNurRueckseite = "nurRueckseite";
	const filterNachMetatag = "nachMetatag";

	$scope.saved = true;
	$scope.isInit = false;
	$scope.currentlySaving = false;
	$scope.lastCursor = {position: undefined};
	$scope.filter = filterAlle;
	$scope.filterExpression = "";
	$scope.search = {metatags: "", hashtags: []};
	$scope.show = {comments: true, metatags: false};
	$scope.pwForm = {};
	$scope.passwordCorrect = false;

	$scope.init = function(docId) {
		if (!$scope.isInit && docId) {
			$scope.isInit = true;
			$http.get('/json/document/' + docId).success(function(data) {
				$scope.document = data;
			});
			$http.get('/json/textelement/types').success(function(data) {
				$scope.textelementTypes = data;
			});
			// in fail
			// $scope.isInit = false;
		}
	}

	$scope.submitPw = function() {
		if ($scope.isInit) {
			$http.post('/json/document/checkpw/'+$scope.document.id, $scope.pwForm).
		    success(function(data, status, headers, config) {
		    	$scope.passwordCorrect = true;
		    }).
		    error(function(data, status, headers, config) {
		    	alert("Passwort inkorrekt.");
		    });
		}
	}

	$scope.changePw = function() {
		if ($scope.isInit) {
		    var pwNew = prompt("Neues Passwort", "");
			$http.post('/json/document/changepw/'+$scope.document.id, {pw: $scope.pwForm.pw, pwNew: pwNew}).
		    success(function(data, status, headers, config) {
		    	$scope.pwForm.pw = pwNew;
		    }).
		    error(function(data, status, headers, config) {
		    	alert("Passwort inkorrekt.");
		    });
		}
	}
	
	$scope.removeBack = function(card) {
		card.back = null;
		$scope.change();
	}
	
	$scope.logDoc = function() {
		util.log($scope.document);
	}

	$scope.saveDoc = function() {
		if (!$scope.saved) {
			saveService.saveDocument($scope, $scope.document, $scope.pwForm.pw);
		}
	}
	
	$scope.reloadPage = function(){
		window.location.reload();
	}

	$scope.change = function() {
		$scope.saved = false;
	}

	$scope.addOption = function(choices) {
		choices.push({text:'Neue Option',correct:'false'});
		$scope.change();
	}
	
	$scope.deleteOption = function(choices, index) {
		choices.splice(index, 1);
		$scope.change();
	}
	
	$scope.deleteCard = function(ele, index) {
		$scope.saved = false;
		var docId = $scope.document.id;
		// ele wird nur mitgegeben, damit es einem post request entspricht, wird nicht benutzt
		$http.post('/json/document/'+docId+'/card/'+ele.id+'/delete', ele).success(function(data) {
			$scope.saved = true;
			if ($scope.document.cards[index] == ele) {
				$scope.document.cards.splice(index, 1);
			} else {
				alert('das element am index entspricht nicht dem zu löschenden element');
			}
		});
	}

	$scope.createMultipleChoice = function(index, docId, text) {
		$http.post('/json/document/' + docId + '/multiplechoice/new/'+index, JSON.stringify(text)).success(function(data) {
            $scope.document.cards.splice(index, 0, data);
		});
	}
	
	$scope.createParagraph = function(index, docId, text) {
		$http.post('/json/document/' + docId + '/paragraph/new/'+index, JSON.stringify(text)).success(function(data) {
                $scope.document.cards.splice(index, 0, data);
		});
	}

	$scope.createHeadline = function(index, docId) {
		$http.get('/json/document/' + docId + '/headline/new/'+index).success(function(data) {
			$scope.document.cards.splice(index, 0, data);
		});
	}

	$scope.deleteComment = function(textelement) {
		textelement.comment = null;
	}

	$scope.changeSize = function($event, ele, change) {
		if((change < 0 && ele.size > 1) || (change > 0 && ele.size < 6)){
			ele.size = ele.size + change;
			this.change();
		}
		// focus nicht verlieren
		$event.preventDefault();
	}

	$scope.activateElement = function(textelement) {
		return $scope.activeEle = textelement;
	}

	$scope.isActive = function(textelement) {
		return $scope.activeEle === textelement;
	}

	$scope.mergeWithTop = function(index){
		var cards = $scope.document.cards;
	    if(index >= 1){
	    	cards[index-1].front.text += "\n";
	    	cards[index-1].front.text += cards[index].front.text;
	    	cards[index-1].back.text += "\n";
	    	cards[index-1].back.text += cards[index].back.text;
	    	cards.splice(index,1);
			$scope.change();
	    }
	}
	
	$scope.mergeWithBottom = function(index){
		var cards = $scope.document.cards;
	    if(index >= 0 && cards.length > index+1){
	    	cards[index+1].front.text += "\n";
	    	cards[index+1].front.text += cards[index].front.text;
	    	cards[index+1].back.text += "\n";
	    	cards[index+1].back.text += cards[index].back.text;
	    	cards.splice(index,1);
			$scope.change();
	    }
	}
	
	$scope.split = function(index){
		var cards = $scope.document.cards;
		var doc = $scope.document;
	    if(index >= 0 && cards.length > index){
	    		var cursorPosition = $scope.lastCursor.position;
	    		var firstPart = cards[index].front.text.substring(0, cursorPosition);
	    		var secondPart = cards[index].front.text.substring(cursorPosition);
	    		cards[index].front.text = firstPart;
	    		$scope.createParagraph(index+1, doc.id, secondPart);
				$scope.change();
	    }
	}
	
	$scope.sortableOptions = {
		handle : '.sort-handle',
		placeholder : "ui-state-highlight",
		cursor: "move",
		update : function(e, ui){
			$scope.change();
		}
	};
	
	$scope.filterNurRueckseite = function(){
		$scope.filter = filterNurRueckseite;
		$scope.filterExpression = {comment: ''};
	}
	
	$scope.filterAlle = function(){
		$scope.filter = filterAlle;
		$scope.filterExpression = "";
	}

	$scope.filterNachMetatag = function(){
		$scope.filter = filterNachMetatag;
		$scope.filterExpression = {metatags: $scope.search.metatags};
	}

	$scope.filterNachHashTags = function(){
		$scope.filter = filterNachHashtags;
		$scope.filterExpression = {hashtags: $scope.search.hashtags};
	}
	
	$scope.clearSelectedHashTags = function(){
		$scope.search.hashtags = [];
	}
	
	$scope.selectAllHashTags = function(){
		$scope.search.hashtags = $scope.document.keywords;
	}
	
	$scope.showComments = function(){
		$scope.show.comments = !$scope.show.comments;
	}
	
	$scope.showMetatags = function(){
		$scope.show.metatags = !$scope.show.metatags;
	}
}
