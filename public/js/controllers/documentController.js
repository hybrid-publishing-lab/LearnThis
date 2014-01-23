lhpControllers.controller('DocumentController', [ '$scope', '$http', 'SaveService', DocumentController ]);
function DocumentController($scope, $http, saveService) {

	$scope.saved = true;
	$scope.isInit = false;

	$scope.init = function(docId) {
		if (!$scope.isInit && docId) {
			$scope.isInit = true;
			$http.get('/json/document/' + docId).success(function(data) {
				$scope.document = data;
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
		$http.post('/json/textelement/delete/' + ele.id, ele).success(function(data) {
			$scope.saved = true;
			if ($scope.document.textelements[index] == ele) {
				$scope.document.textelements.splice(index, 1);
			} else {
				alert('das element am index entspricht nicht dem zu löschenden element');
			}
		});
	}

	$scope.createParagraph = function(index, docId) {
		$http.get('/json/document/' + docId + '/paragraph/new').success(function(data) {
			$scope.document.textelements.push(data);
		});
	}

	$scope.createHeadline = function(index, docId) {
		$http.get('/json/document/' + docId + '/headline/new').success(function(data) {
			$scope.document.textelements.push(data);
		});
	}

	$scope.activateComment = function(textelement) {
		textelement.comment = "Kommentar bearbeiten";
		
//		TODO fokus auf das kommentarfeld setzen. Das Kommentarfeld scheint hier noch gar nicht im
//		dom zu existieren, da es mit data-ng-if ausgeblendet wird
		
//		var selector = 'textarea#'+textelement.id;
//		var textarea = $(selector);
//		textarea.focus();
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

	$scope.activateElement = function(id) {
		return $scope.activeEle = id;
	}

	$scope.isActive = function(id) {
		return $scope.activeEle == id;
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
}
