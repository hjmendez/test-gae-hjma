'use strict';

angular.module('book')
    .controller('DetailCtrl', function ($scope, $routeParams, book) {    	
        $scope.load = function() {
            book.get($routeParams.id, function (detail) {
                $scope.form = detail.data;
            });
        }
        $scope.load();
    });
