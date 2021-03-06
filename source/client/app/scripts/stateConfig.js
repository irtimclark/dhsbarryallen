'use strict';
(function(){
    angular.module('kudosApp').config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/home');

    function addRequiredResolve() {
      //we're doing this because we want to resolve the currentUser during each state, but in order for that to work, the state needs to have other resolves, otherwise we can't add new
      var oldStateFunc = $stateProvider.state;
      $stateProvider.state = function (name, def) {
        def.resolve = def.resolve || {};
        oldStateFunc(name, def);
        return $stateProvider;
      };
    }

    addRequiredResolve();

    $stateProvider
      .state('app',{
        abstract: true,
        template:'<ui-view/>'
      })
      .state('app.home', {
        allowAnon: true,
        pageTitle: 'Home',
        templateUrl: 'views/main.html',
        controller: 'MainCtrl as vm',
        url:'/home'
      })
      .state('app.kudosCategories', {
        pageTitle: 'Kudos Category List',
        templateUrl: 'views/kudosCategory/list.html',
        controller: 'KudosCategoryCtrl as vm',
        url:'/kudosCategory/list'
      })
      .state('app.profile', {
        pageTitle: 'Your Profile',
        templateUrl: 'views/account/profile.html',
        controller: 'ProfileCtrl as vm',
        url:'/account/profile'
      })
      .state('app.editKudosCategory', {
        pageTitle: 'Edit Kudos Category',
        templateUrl: 'views/kudosCategory/edit.html',
        controller: 'KudosCategoryEditorCtrl as vm',
        url:'/kudosCategory/edit/{id:string}',
        resolve: {
          kudosCategory: function($stateParams, kudosCategories){
            return kudosCategories.getKudosCategoryById($stateParams.id);
          },
          kudosInCategory: function($stateParams, kudos) {
            return kudos.getKudosByCategory($stateParams.id);
          }
        }
      })
      .state('app.createKudosCategory', {
        pageTitle: 'Create Kudos Category',
        templateUrl: 'views/kudosCategory/edit.html',
        controller: 'KudosCategoryEditorCtrl as vm',
        url:'/kudosCategory/create/',
        resolve: {
          kudosCategory : function(){
            return {};
          },
          kudosInCategory: function() {
            return [];
          }
        }
      })
      .state('app.createKudos', {
        pageTitle: 'Create Kudos',
        templateUrl: 'views/kudos/create.html',
        controller: 'KudosCtrl as vm',
        url: '/kudos/create/{userId}'
      })
      .state('app.users', {
        pageTitle: 'All Users',
        templateUrl: 'views/users/list.html',
        controller: 'UsersCtrl as vm',
        url:'/users'
      })
      .state('app.user', {
        templateUrl: 'views/users/details.html',
        controller: 'UserDetailsCtrl as vm',
        url:'/user/{id:string}',
        params: {
          message: null
        },
        resolve: {
          user: function($stateParams, users){
            return users.getUser($stateParams.id).$promise;
          },
          kudosToUser: function($stateParams, kudos) {
            return kudos.getKudosToUser($stateParams.id);
          },
          kudosFromUser: function($stateParams, kudos) {
            return kudos.getKudosFromUser($stateParams.id);
          },
          pageTitle: function(user){
            return user.firstName + ' ' + user.lastName;
          }
        }
      })
      .state('app.login', {
        allowAnon: true,
        templateUrl: 'views/account/login.html',
        controller: 'Login as vm',
        url:'/account/login'
      })
      .state('app.register', {
        allowAnon: true,
        templateUrl: 'views/account/register.html',
        controller: 'RegisterCtrl as vm',
        url:'/account/register'
      });
  });
})();
