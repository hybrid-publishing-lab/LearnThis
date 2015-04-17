/*
 * from:  http://blog.justonepixel.com/geek/2013/08/14/angularjs-auto-expand/
 * leicht angepasst
 */


lhpApp.directive('ngAutoExpand', function() {
    return {
        restrict: 'A',
        link: function( $scope, elem, attrs) {
            elem.bind('keyup', function($event) {
                var element = $event.target;

                $(element).height(0);
                var height = $(element)[0].scrollHeight;

                // 8 is for the padding
                if (height < 20) {
                    height = 28;
                }
//                $(element).height(height-8);
                $(element).height(height);
            });

            // Expand the textarea as soon as it is added to the DOM
            setTimeout( function() {
                var element = elem;

                $(element).height(0);
                var height = $(element)[0].scrollHeight;

                // 8 is for the padding
                if (height < 20) {
                    height = 28;
                }
//                $(element).height(height-8);
                $(element).height(height);
            }, 0)
        }
    };
});

lhpApp.directive('saveCursorPosition', function() {
    return {
        restrict: 'A',
        link: function( $scope, elem, attrs) {
        	var saveCursorPosition = function($event) {
        		var element = $event.target;
        		$scope.lastCursor.position = element.selectionStart;
        		util.log("Last Cursor Position: "+$scope.lastCursor.position);
        	}
            elem.bind('keyup', saveCursorPosition);
            elem.bind('mouseup', saveCursorPosition);
        }
    };
});

lhpApp.directive('wordcloud', function () {
    return {
    	restrict: 'E',
	        scope:{
	            // attributes
	            width : "@",
	            height: "@",
	            fontFamily : "@",
	            fontSize: "@",
	            text: "@",
	
	            // EventCallbacks
	            onClick: "&",
	            onHover: "&"
	        },
	    link: function postLink(scope, element, attrs) {
			// Default Values
			var w             =   300;
			var h            =   200;
			var fontFamily        =   "Impact";
			var fontSize          =   50;
			var text;
			var MAX_WORD_COUNT = 250;
			
			// Check and set attributes, else keep then default values
			if(angular.isDefined(attrs.width))        w           = attrs.width;
			if(angular.isDefined(attrs.height))       h          = attrs.height;
			if(angular.isDefined(attrs.fontFamily))   fontFamily      = attrs.fontFamily;
			if(angular.isDefined(attrs.fontSize))     fontSize        = attrs.fontSize * 1 || 0; // !parseInt, detect wrong input
			if(angular.isDefined(attrs.text))         text            = attrs.text;
			
			var cloudFactory = function(text){
				var WORD_MAXLENGTH = 30;
				var stopWords = /^(aber|als|am|an|auch|auf|aus|bei|bin|bis|bist|da|dadurch|daher|darum|das|daß|dass|dein|deine|dem|den|der|des|dessen|deshalb|die|dies|dieser|dieses|doch|dort|du|durch|ein|eine|einem|einen|einer|eines|er|es|euer|eure|für|hatte|hatten|hattest|hattet|hier	hinter|ich|ihr|ihre|im|in|ist|ja|jede|jedem|jeden|jeder|jedes|jener|jenes|jetzt|kann|kannst|können|könnt|machen|mein|meine|mit|muß|mußt|musst|müssen|müßt|nach|nachdem|nein|nicht|nun|oder|seid|sein|seine|sich|sie|sind|soll|sollen|sollst|sollt|sonst|soweit|sowie|und|unser	unsere|unter|vom|von|vor|wann|warum|was|weiter|weitere|wenn|wer|werde|werden|werdet|weshalb|wie|wieder|wieso|wir|wird|wirst|wo|woher|wohin|zu|zum|zur|über|i|me|my|myself|we|us|our|ours|ourselves|you|your|yours|yourself|yourselves|he|him|his|himself|she|her|hers|herself|it|its|itself|they|them|their|theirs|themselves|what|which|who|whom|whose|this|that|these|those|am|is|are|was|were|be|been|being|have|has|had|having|do|does|did|doing|will|would|should|can|could|ought|i'm|you're|he's|she's|it's|we're|they're|i've|you've|we've|they've|i'd|you'd|he'd|she'd|we'd|they'd|i'll|you'll|he'll|she'll|we'll|they'll|isn't|aren't|wasn't|weren't|hasn't|haven't|hadn't|doesn't|don't|didn't|won't|wouldn't|shan't|shouldn't|can't|cannot|couldn't|mustn't|let's|that's|who's|what's|here's|there's|when's|where's|why's|how's|a|an|the|and|but|if|or|because|as|until|while|of|at|by|for|with|about|against|between|into|through|during|before|after|above|below|to|from|up|upon|down|in|out|on|off|over|under|again|further|then|once|here|there|when|where|why|how|all|any|both|each|few|more|most|other|some|such|no|nor|not|only|own|same|so|than|too|very|say|says|said|shall)$/;
				var punctuation = new RegExp("[" + unicodePunctuationRe + "]", "g");
				var wordSeparators = /[ \f\n\r\t\v\u1680\u180e\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u2028\u2029\u202f\u205f\u3000\u3031-\u3035\u309b\u309c\u30a0\u30fc\uff70]+/g;
				var discard = /^(@|https?:|\/\/)/;
				var htmlTags = /(<[^>]*?>|<script.*?<\/script>|<style.*?<\/style>|<head.*?><\/head>)/g;
				var matchTwitter = /^https?:\/\/([^\.]*\.)?twitter\.com/;
				var scale = 1;
				var fill = d3.scale.category20b();

				var tags;
				var layout = d3.layout.cloud().timeInterval(10).size([ w, h ]).fontSize(function(d) {
					return fontSize(+d.value);
				}).text(function(d) {
					return d.key;
				}).on("word", progress).on("end", draw);

				var words = parseText(text);
				var rootElement = element[0];
				var svg = d3.select(rootElement).append("svg").attr("width", w).attr("height", h);

				var background = svg.append("g"), vis = svg.append("g").attr("transform", "translate(" + [ w >> 1, h >> 1 ] + ")");
				
				function parseText(text) {
					tags = {};
					var cases = {};
					text.split(wordSeparators).forEach(function(word) {
						if (discard.test(word))
							return;
						word = word.replace(punctuation, "");
						if (stopWords.test(word.toLowerCase()))
							return;
						word = word.substr(0, WORD_MAXLENGTH);
						cases[word.toLowerCase()] = word;
						tags[word = word.toLowerCase()] = (tags[word] || 0) + 1;
					});
					tags = d3.entries(tags).sort(function(a, b) {
						return b.value - a.value;
					});
					tags.forEach(function(d) {
						d.key = cases[d.key];
					});
					generate();
					return tags.slice(0, MAX_WORD_COUNT);
				}


				function generate() {
					layout.font(fontFamily).spiral("rectangular");
					fontSize = d3.scale["sqrt"]().range([ 10, 50 ]);
//					fontSize = d3.scale["log"]().range([ 10, 100 ]);
					if (tags.length)
						fontSize.domain([ +tags[tags.length - 1].value || 1, +tags[0].value ]);
					complete = 0;
//					statusText.style("display", null);
					words = [];
					layout.stop().words(tags.slice(0, MAX_WORD_COUNT)).start();
				}

				function progress(d) {
//					statusText.text(++complete + "/" + max);
				}

				function draw(data, bounds) {
					scale = bounds ? Math.min(w / Math.abs(bounds[1].x - w / 2), w / Math.abs(bounds[0].x - w / 2), h / Math.abs(bounds[1].y - h / 2), h
							/ Math.abs(bounds[0].y - h / 2)) / 2 : 1;
					words = data;
					var text = vis.selectAll("text").data(words, function(d) {
						return d.text.toLowerCase();
					});
					text.transition().duration(1000).attr("transform", function(d) {
						return "translate(" + [ d.x, d.y ] + ")rotate(" + d.rotate + ")";
					}).style("font-size", function(d) {
						return d.size + "px";
					});
					text.enter().append("text").attr("text-anchor", "middle").attr("transform", function(d) {
						return "translate(" + [ d.x, d.y ] + ")rotate(" + d.rotate + ")";
					}).style("font-size", "1px").transition().duration(1000).style("font-size", function(d) {
						return d.size + "px";
					});
					text.style("font-family", function(d) {
						return d.font;
					}).style("fill", function(d) {
						return fill(d.text.toLowerCase());
					}).text(function(d) {
						return d.text;
					});
					var exitGroup = background.append("g").attr("transform", vis.attr("transform"));
					var exitGroupNode = exitGroup.node();
					text.exit().each(function() {
						exitGroupNode.appendChild(this);
					});
					exitGroup.transition().duration(1000).style("opacity", 1e-6).remove();
					vis.transition().delay(1000).duration(750).attr("transform", "translate(" + [ w >> 1, h >> 1 ] + ")scale(" + scale + ")");
				}

//				var fill = d3.scale.category20();
//				d3.layout.cloud().size([w, h])
//				    .words(words.map(function(d) {
//				        return {text: d, size: Math.random() * fontSize};
//				    }))
//				    .rotate(function() { return (~~(Math.random() * 6) - 3) * 30; })
//				    .font(fontFamily)
//				    .fontSize(function(d) { return d.size; })
//				    .on("end", draw)
//				    .start();
//				function draw(words) {
//				    // Center the drawing
//					var height_translate = h/2;
//					var width_translate = w/2;
//					var rootElement = element[0];
//					
//					
//					d3.select(rootElement)
//					    .append("svg")
//						.attr("width", w)
//						.attr("height", h)
//						.append("g")
//						.attr("transform", "translate("+width_translate+","+height_translate+")")// Translate to center
//						.selectAll("text")
//						.data(words)
//						.enter().append("text")
//						.style("font-size", function(d) { return d.size + "px"; })
//						.style("font-family", fontFamily)
//						.style("fill", function(d, i) { return fill(i); })
//						.attr("text-anchor", "middle")
//						.attr("transform", function(d) {
//							return "translate(" + [d.x, d.y] + ") rotate(" + d.rotate + ")";
//						})
//						.text(function(d) { return d.text.key; })
//						.on("click",function(d){
//							scope.onClick({element: d});
//						})
//						.on("mouseover",function(d){
//			                scope.onHover({element: d});
//			            });
//			    }
			};
			
			// Execute
			cloudFactory(text);
	    }
    };
});