'use strict';

/* Services */
var lhpServices = angular.module('lhpServices', []);
lhpServices.service('SaveService', [ '$http', 'LocalStorageService', SaveService ]);
lhpServices.service('ScoringService', [ ScoringService ]);
lhpServices.service('LocalStorageService', [ LocalStorageService ]);

util.log("Services configured");