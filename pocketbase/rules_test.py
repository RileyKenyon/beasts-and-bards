# Access-rule tests. Needs a fresh, empty PocketBase with the migrations applied:
#   ./pocketbase serve --dir /tmp/pbtest   (then)   PB_URL=http://127.0.0.1:8090 python3 rules_test.py
import json, urllib.request, urllib.error
import os, sys
B=os.environ.get('PB_URL', 'http://127.0.0.1:8090').rstrip('/') + '/api/'
def call(method, path, body=None, tok=None):
    h={'content-type':'application/json'}
    if tok: h['Authorization']=tok
    r=urllib.request.Request(B+path, data=json.dumps(body).encode() if body is not None else None, headers=h, method=method)
    try:
        with urllib.request.urlopen(r) as f: return f.status, json.load(f)
    except urllib.error.HTTPError as e:
        return e.code, json.loads(e.read() or b'{}')
def user(n):
    call('POST','collections/users/records',{'email':f'{n}@t.dev','password':'password123','passwordConfirm':'password123','name':n})
    s,d=call('POST','collections/users/auth-with-password',{'identity':f'{n}@t.dev','password':'password123'})
    assert s==200,(s,d); return d['token'], d['record']['id']
ta,a=user('alice'); tb,b=user('bob'); tc,c=user('carol')
fails=[]
def check(label, got, want):
    ok = got==want; print(('PASS' if ok else 'FAIL'), label, got)
    if not ok: fails.append(label)
check('anon sees no games', len(call('GET','collections/games/records')[1].get('items', [])), 0)
check('create game dm=other denied', call('POST','collections/games/records',{'name':'x','dm':b},ta)[0], 400)
s,g=call('POST','collections/games/records',{'name':'Quest','dm':a,'players':[]},ta); check('alice creates game', s, 200); gid=g['id']
check('alice sees game', len(call('GET','collections/games/records',tok=ta)[1]['items']), 1)
check('bob sees no games', len(call('GET','collections/games/records',tok=tb)[1]['items']), 0)
check('bob view game 404', call('GET',f'collections/games/records/{gid}',tok=tb)[0], 404)
check('bob update game denied', call('PATCH',f'collections/games/records/{gid}',{'name':'hax'},tb)[0], 404)
ch={'name':'Bob','race':'Elf','abilities':{'charisma':1},'game':gid,'active':False}
check('bob create char before invite denied', call('POST','collections/characters/records',{**ch,'owner':b},tb)[0], 400)
s,r=call('POST','collections/characters/records',{**ch,'owner':a},ta); check('alice creates char', s, 200); cid=r['id']
check('alice cannot create char as bob', call('POST','collections/characters/records',{**ch,'owner':b},ta)[0], 400)
check('owner cannot move char to another game', call('PATCH',f'collections/characters/records/{cid}',{'game':gid},ta)[0], 404)
check('owner cannot change char owner', call('PATCH',f'collections/characters/records/{cid}',{'owner':b},ta)[0], 404)
check('owner can edit char name', call('PATCH',f'collections/characters/records/{cid}',{'name':'Alicia'},ta)[0], 200)
check('alice invites bob', call('PATCH',f'collections/games/records/{gid}',{'players':[b]},ta)[0], 200)
check('bob sees game', len(call('GET','collections/games/records?expand=dm',tok=tb)[1]['items']), 1)
s,d=call('GET','collections/games/records?expand=dm',tok=tb); check('bob sees dm name via expand', d['items'][0]['expand']['dm']['name'], 'alice')
check('bob create char after invite', call('POST','collections/characters/records',{**ch,'owner':b},tb)[0], 200)
check('bob sees both party chars', len(call('GET',f'collections/characters/records?filter=game="{gid}"',tok=tb)[1]['items']), 2)
check('carol create char denied', call('POST','collections/characters/records',{**ch,'owner':c},tc)[0], 400)
check('carol sees no chars', len(call('GET','collections/characters/records',tok=tc)[1]['items']), 0)
check('bob edit alice char denied', call('PATCH',f'collections/characters/records/{cid}',{'name':'x'},tb)[0], 404)
check('friends owner-only create', call('POST','collections/friends/records',{'owner':a,'friend':b},tb)[0], 400)
check('alice adds friend', call('POST','collections/friends/records',{'owner':a,'friend':b},ta)[0], 200)
check('duplicate friend rejected', call('POST','collections/friends/records',{'owner':a,'friend':b},ta)[0], 400)
check('bob sees no friends rows', len(call('GET','collections/friends/records',tok=tb)[1]['items']), 0)
print('FAILS:', fails)
sys.exit(1 if fails else 0)
