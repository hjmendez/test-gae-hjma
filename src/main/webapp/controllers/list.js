'use strict';

angular.module('book')
    .controller('ListCtrl', function ($scope, book) {
    	$scope.loading = false;

        $scope.load = function() {
        	$scope.loading = true;
            book.list(function (list) {
                $scope.list = list.data;
                $scope.loading = false;
            });
        }

        $scope.save = function() {
        	if(!$scope.form.name || !$scope.form.author || !$scope.form.year || !$scope.form.genre ){
        		alert("Por favor todos los campos son obligatorios");
        		return;
        	}
        	
            book.save($scope.form, function() {            	
            	$scope.form = {}; //reset form            	            	
                $scope.load();
            });
        }
        
        $scope.resetForm = function(){
        	$scope.form = {};
        }
        
        $scope.delete = function(id) {
        	if(!confirm("¿Está seguro de que desea eliminar el elemento seleccionado?")){
        		return;
        	}
            book.delete(id, function() {
                $scope.load();
            });
        }
        
        $scope.search = function(){
        	console.log("search");
        	 if (!$scope.txt) {
                 alert("Es necesario incluir un texto para realizar la búsqueda");
                 return;
             }        	 
        	 book.search($scope.txt, function(list) {            	
        		 $scope.list = list.data;
             });        	 
        }
        
        $scope.reset = function(){
        	console.log("reset search");        	
        	$scope.txt = "";
        	$scope.load();
        }

        $scope.form = {};

        $scope.load();
    });
