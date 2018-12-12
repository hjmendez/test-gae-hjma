'use strict';

angular.module('book')
    .service('book', function ($http) {
        return {            
            list: function (success) {
                return $http.get("/rest/book").then(success);
            },
            save: function (book, success) {
                return $http.post("/rest/book", book).then(success);
            },
            delete: function (id, success) {
                return $http.delete("/rest/book/" + id).then(success);
            },
            get: function (id, success) {
                return $http.get("/rest/book/" + id).then(success);
            },
            search: function (text, success) {
                return $http.get("/rest/book/search/" + text).then(success);
            }
        };
    });
