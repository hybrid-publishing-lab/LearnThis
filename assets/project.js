// Leave Edit Mode
$(document).mouseup(function (e){
  var container = $(".active");
  // if the target of the click isn't the container
  // nor a descendant of the container
  if (!container.is(e.target) && container.has(e.target).length === 0){
      leaveEditMode();
  }
});

$(document).ready( function(){
  // autogrow textareas
  $('textarea').autogrow({onInitialize: true});

  // Focus a Textarea
  $('textarea').focus(function(event){
    leaveEditMode();
    enterEditMode(this);
  });

  // Focus a Headline
  $('.headline').focus(function(event){
    leaveEditMode();
    enterEditMode(this);
  });

  // Comment Button
  $('.comment-button').click(function(event){
    var comment = $('textarea#comment');
    if(comment.is(':hidden')){
      // ŕeveal comment textarea and focus
      comment.show().focus();
      $('.comment-button').text('Delete Comment');
    }else{
      comment.hide();
      comment.val('');
      $('.comment-button').text('Comment');
    }
  });

  // Add Text Button
  $('.add-text-button').click(function(event){
    var comment = $('textarea#comment');
    if(comment.is(':hidden')){
      // ŕeveal comment textarea and focus
      comment.show().focus();
      $('.comment-button').text('Delete Comment');
    }else{
      comment.hide();
      comment.val('');
      $('.comment-button').text('Comment');
    }
  });
});

function dimmOtherTextElements(element){
  var textElement = $(element).closest('.text-element');
  $(".text-element").not(textElement).addClass('dimmed');
}

function unDimmTextElements(){
  $(".text-element").removeClass('dimmed');
}

function removeMenues(){
  $('.text-element').find('.show-on-focus').fadeOut();
}

function displayMenues(element){
  var showElements = $(element).closest('.text-element').find('.show-on-focus');
  showElements.fadeIn();
}

function enterEditMode(element){
  // deactivate Editmode for all other text-elements
  $('.text-element').removeClass('active');
  $(element).closest('.text-element').addClass('active');

  unDimmTextElements();
  dimmOtherTextElements(element);

  displayMenues(element);
}

function leaveEditMode(){
  unDimmTextElements();
  removeMenues();
}
