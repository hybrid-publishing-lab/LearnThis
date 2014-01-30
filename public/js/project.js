// Leave Edit Mode
//$(document).mouseup(function (e){
//  var container = $(".edit-mode-active");
//  // if the target of the click isn't the container
//  // nor a descendant of the container
//  if (!container.is(e.target) && container.has(e.target).length === 0){
//      leaveEditMode();
//  }
//});

//$(document).ready( function(){

  // autogrow textareas
//  $('textarea').autogrow({onInitialize: true});
  
//  // Focus a Textarea
//  $('.text-element textarea').focus(function(event){
//    var target = $(event.target);
//    if(!isEditModeActive(target)){
//      leaveEditMode();
//      enterEditMode(target);
//    } 
//  });
//
//  // Focus a Headline
//  $('.text-element .headline').focus(function(event){
//    var target = $(event.target);
//    if(!isEditModeActive(target)){
//      leaveEditMode();
//      enterEditMode(target);
//    } 
//  });
//
//
//  // Add Text Button
//  $('.add-text-button').click(function(event){
//    var comment = $('textarea#comment');
//    if(comment.is(':hidden')){
//      // ŕeveal comment textarea and focus
//      comment.show().focus();
//      $('.comment-button').text('Delete Comment');
//    }else{
//      comment.hide();
//      comment.val('');
//      $('.comment-button').text('Add Comment');
//    }
//  });
//});

//function dimmOtherTextElements(element){
//  var textElement = $(element).closest('.text-element');
//  $(".text-element").not(textElement).addClass('dimmed');
//}
//
//function unDimmTextElements(){
//  $(".text-element").removeClass('dimmed');
//}
//
//function removeMenues(){
//  $('.text-element').find('.show-on-focus').slideUp();
//}
//
//function displayMenues(element){
//  var showElements = $(element).closest('.text-element').find('.show-on-focus');
//  showElements.slideDown();
//}
//
//function enterEditMode(element){
//  if(!isEditModeActive(element)){
//    // deactivate Editmode for all other text-elements
//    $('.text-element').removeClass('edit-mode-active');
//    $(element).closest('.text-element').addClass('edit-mode-active');
//
//    unDimmTextElements();
//    dimmOtherTextElements(element);
//
//    displayMenues(element);
//  }
//}
//
//function leaveEditMode(){
//  unDimmTextElements();
//  removeMenues();
//}
//
//function isEditModeActive(element){
//  var tmp = $(element).closest('.text-element');
//  return tmp.hasClass('edit-mode-active');
//}
