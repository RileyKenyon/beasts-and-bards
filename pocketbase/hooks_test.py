# Tests pb_hooks/smtp.pb.js by starting the real binary with different env.
# Usage (from this directory, with ./pocketbase present): python3 hooks_test.py
# Set PB_BIN to use another binary.
import json, os, subprocess, sys, tempfile, time, urllib.request

HERE = os.path.dirname(os.path.abspath(__file__))
BIN = os.environ.get('PB_BIN', os.path.join(HERE, 'pocketbase'))
PORT = 8094
fails = []

def serve(data_dir, env=None, args=()):
    e = {**os.environ, **(env or {})}
    for k in ('RESEND_API_KEY', 'MAIL_FROM', 'APP_URL'):
        if k not in (env or {}): e.pop(k, None)
    base = [BIN, '--dir', data_dir, '--migrationsDir', os.path.join(HERE, 'pb_migrations'),
            '--hooksDir', os.path.join(HERE, 'pb_hooks')]
    subprocess.run([BIN, 'superuser', 'upsert', 'a@t.dev', 'adminpass123', *base[1:], *args],
                   env=e, check=True, capture_output=True)
    p = subprocess.Popen([*base, 'serve', f'--http=127.0.0.1:{PORT}', *args], env=e,
                         stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    for _ in range(60):
        try:
            urllib.request.urlopen(f'http://127.0.0.1:{PORT}/api/health'); break
        except Exception: time.sleep(0.5)
    return p

def settings():
    base = f'http://127.0.0.1:{PORT}/api/'
    r = urllib.request.Request(base + 'collections/_superusers/auth-with-password',
        data=json.dumps({'identity': 'a@t.dev', 'password': 'adminpass123'}).encode(),
        headers={'content-type': 'application/json'})
    tok = json.load(urllib.request.urlopen(r))['token']
    r = urllib.request.Request(base + 'settings', headers={'Authorization': tok})
    return json.load(urllib.request.urlopen(r))

def run(data_dir, env=None, args=()):
    p = serve(data_dir, env, args)
    try: return settings()
    finally: p.terminate(); p.wait()

def check(label, got, want):
    ok = got == want
    print('PASS' if ok else 'FAIL', label, got)
    if not ok: fails.append(label)

with tempfile.TemporaryDirectory() as d:
    s = run(d)
    check('no key: smtp stays off', s['smtp']['enabled'], False)
    default_from = s['meta']['senderAddress']

    s = run(d, {'RESEND_API_KEY': 'k1'})
    check('key: smtp on', (s['smtp']['enabled'], s['smtp']['host'], s['smtp']['port'], s['smtp']['username']),
          (True, 'smtp.resend.com', 465, 'resend'))
    check('key only: sender unchanged', s['meta']['senderAddress'], default_from)

    s = run(d, {'RESEND_API_KEY': 'k1', 'MAIL_FROM': 'noreply@example.com', 'APP_URL': 'https://app.example.com'})
    check('MAIL_FROM sets sender', s['meta']['senderAddress'], 'noreply@example.com')
    check('APP_URL sets appURL', s['meta']['appURL'], 'https://app.example.com')

    s = run(d, {'RESEND_API_KEY': 'k2'})
    check('restart without MAIL_FROM keeps saved sender', s['meta']['senderAddress'], 'noreply@example.com')
    check('restart without APP_URL keeps saved appURL', s['meta']['appURL'], 'https://app.example.com')

with tempfile.TemporaryDirectory() as d:
    enc = {'PB_ENCRYPTION_KEY': 'k' * 32, 'RESEND_API_KEY': 'k1', 'MAIL_FROM': 'noreply@example.com'}
    s = run(d, enc, ['--encryptionEnv=PB_ENCRYPTION_KEY'])
    check('encrypted settings: hook applies', (s['smtp']['enabled'], s['meta']['senderAddress']), (True, 'noreply@example.com'))
    s = run(d, {'PB_ENCRYPTION_KEY': 'k' * 32, 'RESEND_API_KEY': 'k1'}, ['--encryptionEnv=PB_ENCRYPTION_KEY'])
    check('encrypted settings: survive restart', s['meta']['senderAddress'], 'noreply@example.com')

print('FAILS:', fails)
sys.exit(1 if fails else 0)
