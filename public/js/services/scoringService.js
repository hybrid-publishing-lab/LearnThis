function ScoringService(){
	
	var me = this;
	
	this.scoreMc = function(docId, card){
		var score = 1;
		for (index = 0; index < card.choices.length; ++index) {
			var choice = card.choices[index];
			if ((choice.userChecked && !choice.correct) || (!choice.userChecked && choice.correct)) {
				// answer is wrong
				score = 0;
				break;
			}
		}
		util.log(card.id + ' score is ' + score);
		
		var savedResults = JSON.parse(localStorage.getItem(docId));
		if (!savedResults) {
			savedResults = {};
		} 
		savedResults[card.id] = ""+score;
		localStorage.setItem(docId, JSON.stringify(savedResults));
	}

	this.scoreText = function(docId, card){
		util.log("card", card);
		var savedResults = JSON.parse(localStorage.getItem(docId));
		if (!savedResults) {
			savedResults = {};
		} 
		if (card.understood) {
			savedResults[card.id] = "1";
		} else {
			savedResults[card.id] = "0";
		}
		localStorage.setItem(docId, JSON.stringify(savedResults));
	}
	
	this.calcScore = function(docId) {
		var savedResults = localStorage.getItem(docId);
		if (savedResults) {
			console.log("SavedResults: " + savedResults);
			var results = JSON.parse(savedResults);
			var totalScore = 0;
			for(score in results) {
				totalScore += parseInt(results[score]);
			}
			return totalScore;
		}
		return 0;
	}
	
	this.calcMaxScore = function(pager) {
		var maxScore = 0;
		var lastIndex = -1;
		for(page in pager) {
			if(pager[page].cardIndex != lastIndex) {
				maxScore++;
			}
			lastIndex = pager[page].cardIndex; 
		}
		return maxScore;
	}
}