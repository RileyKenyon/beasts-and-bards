/// <reference path="../pb_data/types.d.ts" />
// Schema and access rules for Beasts and Bards. Everything requires sign-in.
migrate((app) => {
  const users = app.findCollectionByNameOrId("users");
  users.fields.add(new TextField({ name: "name" }));
  // Needed so related users (dm, friend) can be expanded by other players.
  users.listRule = "@request.auth.id != ''";
  users.viewRule = "@request.auth.id != ''";
  app.save(users);

  const games = new Collection({
    type: "base",
    name: "games",
    fields: [
      { type: "text", name: "name", required: true },
      { type: "bool", name: "active" },
      { type: "relation", name: "dm", required: true, maxSelect: 1, collectionId: users.id },
      { type: "relation", name: "players", maxSelect: 100, collectionId: users.id },
      { type: "autodate", name: "created", onCreate: true },
    ],
    listRule: "dm = @request.auth.id || players.id ?= @request.auth.id",
    viewRule: "dm = @request.auth.id || players.id ?= @request.auth.id",
    createRule: "@request.auth.id != '' && @request.body.dm = @request.auth.id",
    updateRule: "dm = @request.auth.id",
    deleteRule: "dm = @request.auth.id",
  });
  app.save(games);

  const characters = new Collection({
    type: "base",
    name: "characters",
    fields: [
      { type: "text", name: "name", required: true },
      { type: "text", name: "race" },
      { type: "bool", name: "active" },
      { type: "json", name: "abilities" },
      { type: "relation", name: "game", required: true, maxSelect: 1, collectionId: games.id, cascadeDelete: true },
      { type: "relation", name: "owner", required: true, maxSelect: 1, collectionId: users.id, cascadeDelete: true },
    ],
    listRule: "owner = @request.auth.id || game.dm = @request.auth.id || game.players.id ?= @request.auth.id",
    viewRule: "owner = @request.auth.id || game.dm = @request.auth.id || game.players.id ?= @request.auth.id",
    createRule: "@request.auth.id != '' && @request.body.owner = @request.auth.id && (@request.body.game.dm = @request.auth.id || @request.body.game.players.id ?= @request.auth.id)",
    updateRule: "owner = @request.auth.id",
    deleteRule: "owner = @request.auth.id",
  });
  app.save(characters);

  const friends = new Collection({
    type: "base",
    name: "friends",
    fields: [
      { type: "relation", name: "owner", required: true, maxSelect: 1, collectionId: users.id, cascadeDelete: true },
      { type: "relation", name: "friend", required: true, maxSelect: 1, collectionId: users.id, cascadeDelete: true },
    ],
    indexes: ["CREATE UNIQUE INDEX idx_friends_pair ON friends (owner, friend)"],
    listRule: "owner = @request.auth.id",
    viewRule: "owner = @request.auth.id",
    createRule: "@request.auth.id != '' && @request.body.owner = @request.auth.id",
    updateRule: "owner = @request.auth.id",
    deleteRule: "owner = @request.auth.id",
  });
  app.save(friends);
}, (app) => {
  for (const name of ["friends", "characters", "games"]) {
    app.delete(app.findCollectionByNameOrId(name));
  }
  const users = app.findCollectionByNameOrId("users");
  users.fields.removeByName("name");
  users.listRule = "id = @request.auth.id";
  users.viewRule = "id = @request.auth.id";
  app.save(users);
});
