class Friend {
  Friend({required this.name, required this.message});
  final String name;
  final String message;

  Map<String, dynamic> toJson() {
    return {
      'name': name,
      'message': message,
    };
  }
}

List<Friend> getFriendsList(List<dynamic> iterable) {
  var friendslist = List<Friend>.empty(growable: true);
  for (var m in iterable) {
    friendslist.add(getFriendFromMap(m));
  }
  return friendslist;
}

Friend getFriendFromMap(Map<String, dynamic> m) {
  return Friend(name: m['name'] ?? "", message: m['message'] ?? "");
}
// Should look like
/**
 * Game
 *  - players (array)
 *    - ref doc 1 --> 'player/player.uuid'
 *    - ref doc 2
 *    ...
 *    - ref doc n
 * 
 * 
 * Characters
 *  
 *    
 */