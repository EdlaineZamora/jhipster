(function() {
    'use strict';
    angular
        .module('jmeterappApp')
        .factory('Pessoa', Pessoa);

    Pessoa.$inject = ['$resource'];

    function Pessoa ($resource) {
        var resourceUrl =  'api/pessoas/:id';

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
