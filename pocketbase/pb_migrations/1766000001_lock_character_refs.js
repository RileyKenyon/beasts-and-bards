/// <reference path="../pb_data/types.d.ts" />
// An owner may edit a character but not move it to another game or owner.
migrate((app) => {
  const characters = app.findCollectionByNameOrId("characters");
  characters.updateRule =
    "owner = @request.auth.id && @request.body.game:isset = false && @request.body.owner:isset = false";
  app.save(characters);
}, (app) => {
  const characters = app.findCollectionByNameOrId("characters");
  characters.updateRule = "owner = @request.auth.id";
  app.save(characters);
});
