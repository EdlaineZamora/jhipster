(function() {
    'use strict';

    angular
        .module('jmeterappApp')
        .controller('PessoaDetailController', PessoaDetailController);

    PessoaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pessoa'];

    function PessoaDetailController($scope, $rootScope, $stateParams, previousState, entity, Pessoa) {
        var vm = this;

        vm.pessoa = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jmeterappApp:pessoaUpdate', function(event, result) {
            vm.pessoa = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
