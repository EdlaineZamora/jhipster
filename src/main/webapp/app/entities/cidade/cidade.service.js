(function() {
    'use strict';
    angular
        .module('jmeterappApp')
        .factory('Cidade', Cidade);

    Cidade.$inject = ['$resource'];

    function Cidade ($resource) {
        var resourceUrl =  'api/cidades/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
