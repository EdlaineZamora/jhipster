(function() {
    'use strict';

    angular
        .module('jmeterappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cidade', {
            parent: 'entity',
            url: '/cidade?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jmeterappApp.cidade.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cidade/cidades.html',
                    controller: 'CidadeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cidade');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cidade-detail', {
            parent: 'entity',
            url: '/cidade/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jmeterappApp.cidade.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cidade/cidade-detail.html',
                    controller: 'CidadeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cidade');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cidade', function($stateParams, Cidade) {
                    return Cidade.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cidade',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cidade-detail.edit', {
            parent: 'cidade-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cidade/cidade-dialog.html',
                    controller: 'CidadeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cidade', function(Cidade) {
                            return Cidade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cidade.new', {
            parent: 'cidade',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cidade/cidade-dialog.html',
                    controller: 'CidadeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cidade', null, { reload: 'cidade' });
                }, function() {
                    $state.go('cidade');
                });
            }]
        })
        .state('cidade.edit', {
            parent: 'cidade',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cidade/cidade-dialog.html',
                    controller: 'CidadeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cidade', function(Cidade) {
                            return Cidade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cidade', null, { reload: 'cidade' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cidade.delete', {
            parent: 'cidade',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cidade/cidade-delete-dialog.html',
                    controller: 'CidadeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cidade', function(Cidade) {
                            return Cidade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cidade', null, { reload: 'cidade' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
