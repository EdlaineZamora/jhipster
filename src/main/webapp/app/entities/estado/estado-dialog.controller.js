(function() {
    'use strict';

    angular
        .module('jmeterappApp')
        .controller('EstadoDialogController', EstadoDialogController);

    EstadoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Estado'];

    function EstadoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Estado) {
        var vm = this;

        vm.estado = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.estado.id !== null) {
                Estado.update(vm.estado, onSaveSuccess, onSaveError);
            } else {
                Estado.save(vm.estado, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jmeterappApp:estadoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
