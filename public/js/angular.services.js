'use strict';

/* Services */
var lhpServices = angular.module('lhpServices', []);
lhpServices.service('SaveService', [ '$http', SaveService ]);

util.log("Services configured");