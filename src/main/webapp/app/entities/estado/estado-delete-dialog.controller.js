(function() {
    'use strict';

    angular
        .module('jmeterappApp')
        .controller('EstadoDeleteController',EstadoDeleteController);

    EstadoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Estado'];

    function EstadoDeleteController($uibModalInstance, entity, Estado) {
        var vm = this;

        vm.estado = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Estado.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
