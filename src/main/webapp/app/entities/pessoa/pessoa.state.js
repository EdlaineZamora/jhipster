(function() {
    'use strict';

    angular
        .module('jmeterappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pessoa', {
            parent: 'entity',
            url: '/pessoa?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jmeterappApp.pessoa.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pessoa/pessoas.html',
                    controller: 'PessoaController',
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
                    $translatePartialLoader.addPart('pessoa');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pessoa-detail', {
            parent: 'entity',
            url: '/pessoa/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jmeterappApp.pessoa.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pessoa/pessoa-detail.html',
                    controller: 'PessoaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pessoa');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pessoa', function($stateParams, Pessoa) {
                    return Pessoa.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pessoa',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pessoa-detail.edit', {
            parent: 'pessoa-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pessoa/pessoa-dialog.html',
                    controller: 'PessoaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pessoa', function(Pessoa) {
                            return Pessoa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pessoa.new', {
            parent: 'pessoa',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pessoa/pessoa-dialog.html',
                    controller: 'PessoaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                documento: null,
                                idade: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pessoa', null, { reload: 'pessoa' });
                }, function() {
                    $state.go('pessoa');
                });
            }]
        })
        .state('pessoa.edit', {
            parent: 'pessoa',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pessoa/pessoa-dialog.html',
                    controller: 'PessoaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pessoa', function(Pessoa) {
                            return Pessoa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pessoa', null, { reload: 'pessoa' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pessoa.delete', {
            parent: 'pessoa',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pessoa/pessoa-delete-dialog.html',
                    controller: 'PessoaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pessoa', function(Pessoa) {
                            return Pessoa.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pessoa', null, { reload: 'pessoa' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
