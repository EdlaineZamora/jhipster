(function() {
    'use strict';

    angular
        .module('jmeterappApp')
        .controller('PessoaDeleteController',PessoaDeleteController);

    PessoaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pessoa'];

    function PessoaDeleteController($uibModalInstance, entity, Pessoa) {
        var vm = this;

        vm.pessoa = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Pessoa.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
