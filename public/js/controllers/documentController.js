lhpControllers.controller('DocumentController', [ '$scope', '$http', 'SaveService', DocumentController ]);
function DocumentController($scope, $http, saveService) {
	
	const filterAlle = "alle";
	const filterNurRueckseite = "nurRueckseite";
	const filterNachMetatag = "nachMetatag";

	$scope.saved = true;
	$scope.isInit = false;
	$scope.currentlySaving = false;
	$scope.lastCursor = {position: undefined};
	$scope.filter = filterAlle;
	$scope.filterExpression = "";
	$scope.search = {metatags: ""};
	$scope.show = {comments: true, metatags: false};
	

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

	$scope.logDoc = function() {
		util.log($scope.document);
	}

	$scope.saveDoc = function() {
		if (!$scope.saved) {
			saveService.saveDocument($scope, $scope.document);
		}
	}

	$scope.change = function() {
		$scope.saved = false;
	}

	$scope.deleteElement = function(ele, index) {
		$scope.saved = false;
		var docId = $scope.document.id;
		// ele wird nur mitgegeben, damit es einem post request entspricht, wird nicht benutzt
		$http.post('/json/document/'+docId+'/textelement/'+ele.id+'/delete', ele).success(function(data) {
			$scope.saved = true;
			if ($scope.document.textelements[index] == ele) {
				$scope.document.textelements.splice(index, 1);
			} else {
				alert('das element am index entspricht nicht dem zu löschenden element');
			}
		});
	}

	$scope.createParagraph = function(index, docId, text) {
		$http.post('/json/document/' + docId + '/paragraph/new/'+index, JSON.stringify(text)).success(function(data) {
                $scope.document.textelements.splice(index, 0, data);
		});
	}

	$scope.createHeadline = function(index, docId) {
		$http.get('/json/document/' + docId + '/headline/new/'+index).success(function(data) {
			$scope.document.textelements.splice(index, 0, data);
		});
	}

//	$scope.activateComment = function(textelement) {
//		textelement.comment = "Rückseite bearbeiten";
//		
////		TODO fokus auf das kommentarfeld setzen. Das Kommentarfeld scheint hier noch gar nicht im
////		dom zu existieren, da es mit data-ng-if ausgeblendet wird
//		
////		var selector = 'textarea#'+textelement.id;
////		var textarea = $(selector);
////		textarea.focus();
//	}

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
		var textelements = $scope.document.textelements;
	    if(index >= 1){
	    		textelements[index-1].text += "\n";
	    		textelements[index-1].text += textelements[index].text;
			    textelements.splice(index,1);
				$scope.saved = false;
				$scope.saveDoc();
	    }
	}
	
	$scope.mergeWithBottom = function(index){
		var textelements = $scope.document.textelements;
	    if(index >= 0 && textelements.length > index+1){
	    		textelements[index].text += "\n";
	            textelements[index].text += textelements[index+1].text;
			    textelements.splice(index+1,1);
				$scope.saved = false;
				$scope.saveDoc();
	    }
	}
	
	$scope.split = function(index){
		var textelements = $scope.document.textelements;
		var doc = $scope.document;
	    if(index >= 0 && textelements.length > index){
	    		var cursorPosition = $scope.lastCursor.position;
	    		var firstPart = textelements[index].text.substring(0, cursorPosition);
	    		var secondPart = textelements[index].text.substring(cursorPosition);
	    		textelements[index].text = firstPart;
	    		$scope.createParagraph(index+1, doc.id, secondPart);
				$scope.change();
	    }
	}
	
	$scope.sortableOptions = {
		handle : '.sort-handle',
		placeholder : "ui-state-highlight",
		cursor: "move",
		update : function(e, ui){
			$scope.saved = false;
			$scope.saveDoc();
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
	
	$scope.showComments = function(){
		$scope.show.comments = !$scope.show.comments;
	}
	
	$scope.showMetatags = function(){
		$scope.show.metatags = !$scope.show.metatags;
	}
}
