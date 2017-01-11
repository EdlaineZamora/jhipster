(function() {
    'use strict';

    angular
        .module('jmeterappApp')
        .controller('CidadeDeleteController',CidadeDeleteController);

    CidadeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cidade'];

    function CidadeDeleteController($uibModalInstance, entity, Cidade) {
        var vm = this;

        vm.cidade = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cidade.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
