/**
 * Created by michael.r-p on 8/21/2016.
 */
/*

 # kudosCategory
 id 			<- Unique
 name 		<- Unique
 desc
 dateCreated
 dateModified
 color
 icon

 [GET]	.../v1/cat/all
 Returns all Categories

 [GET]	.../v1/cat/{name}
 Expects the name to be in the URL path
 Returns a KudosCategory Object

 [POST]	.../create
 Expects KudosCategory Object in the RequestBody - must contain name
 Returns created KudosCategory object

 [POST]	.../update
 Expects a KudosCategory Object in the RequestBody - must contain id
 Returns created KudosCategory object

 */

(function(){
  'use strict';
  angular.module('kudosApp')
    .factory('kudosCategories', function(_, $resource, restUrl) {

      var url = restUrl + 'cat';

      var categoryResources = $resource(url, {}, {
        getAll: { method: 'get', isArray: true, url: url + '/all' },
        getCategoryById: { method: 'get', url: url + '/byId/:id' },
        create: { method: 'post', url: url + '/create' },
        update: { method: 'post', url: url + '/update' }
      });

      function getKudosCategries() {
        return categoryResources.getAll();
      }

      function addKudosCategory(kudosCategory){
        return categoryResources.create(kudosCategory).$promise;
      }

      // function getKudosCategory(id) {
      //   //return _(kudosCategories).find(function(kc) { return kc.id === id; });
      //   var categories = categoryResources.getAll();
      //   var category = {}; //_.find(categories, { id: id });
      //
      //   categories.$promise.then(function(data) {
      //     category = _.find(categories, { id: id });
      //     // console.log(category);
      //   });
      //
      //   return category;
      // }

      function getKudosCategoryById(id) {
        return categoryResources.getCategoryById({ id: id });
      }

      function updateKudosCategory(kudosCategory){
        return categoryResources.update(kudosCategory).$promise;
      }

      return {
        addKudosCategory: addKudosCategory,
        updateKudosCategory: updateKudosCategory,
        getKudosCategories: getKudosCategries,
        //getKudosCategory: getKudosCategory,
        getKudosCategoryById: getKudosCategoryById
      };
    });
})();
