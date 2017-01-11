(function() {
    'use strict';
    angular
        .module('jmeterappApp')
        .factory('Estado', Estado);

    Estado.$inject = ['$resource'];

    function Estado ($resource) {
        var resourceUrl =  'api/estados/:id';

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
