(function() {
    'use strict';

    angular
        .module('jmeterappApp')
        .controller('CidadeDialogController', CidadeDialogController);

    CidadeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cidade', 'Estado'];

    function CidadeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cidade, Estado) {
        var vm = this;

        vm.cidade = entity;
        vm.clear = clear;
        vm.save = save;
        vm.estados = Estado.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cidade.id !== null) {
                Cidade.update(vm.cidade, onSaveSuccess, onSaveError);
            } else {
                Cidade.save(vm.cidade, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jmeterappApp:cidadeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
