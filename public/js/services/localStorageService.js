function LocalStorageService ($http) {

  var me = this;

  this.saveDoc = function (docId, doc) {
    localStorage.setItem(docId, JSON.stringify(doc));
  }
  
  this.loadDoc = function(docId) {
    var savedResults = JSON.parse(localStorage.getItem(docId));
    if (!savedResults) {
      savedResults = {password:{pw:""}};
    }
    return savedResults;
  }
  
}