import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';

import 'app_state.dart';
import 'pb.dart';

void _snack(BuildContext context, String message) =>
    ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(message)));

class SignInPage extends StatefulWidget {
  const SignInPage({super.key});

  @override
  State<SignInPage> createState() => _SignInPageState();
}

class _SignInPageState extends State<SignInPage> {
  final _email = TextEditingController();
  final _password = TextEditingController();
  bool _registering = false;
  bool _busy = false;

  @override
  void dispose() {
    _email.dispose();
    _password.dispose();
    super.dispose();
  }

  Future<void> _submit() async {
    final appState = context.read<ApplicationState>();
    final router = GoRouter.of(context);
    final messenger = ScaffoldMessenger.of(context);
    setState(() => _busy = true);
    try {
      if (_registering) {
        await appState.register(_email.text.trim(), _password.text);
        messenger.showSnackBar(const SnackBar(
            content:
                Text('Please check your email to verify your email address')));
      } else {
        await appState.signIn(_email.text.trim(), _password.text);
        if (!appState.emailVerified) {
          messenger.showSnackBar(const SnackBar(
              content: Text('Your email address is not verified yet')));
        }
      }
      router.replace('/');
    } catch (e) {
      messenger.showSnackBar(SnackBar(content: Text(errorMessage(e))));
    } finally {
      if (mounted) setState(() => _busy = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(_registering ? 'Register' : 'Sign in')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: ListView(
          children: [
            TextField(
              controller: _email,
              keyboardType: TextInputType.emailAddress,
              decoration: const InputDecoration(labelText: 'Email'),
            ),
            TextField(
              controller: _password,
              obscureText: true,
              decoration: const InputDecoration(labelText: 'Password'),
              onSubmitted: (_) => _busy ? null : _submit(),
            ),
            const SizedBox(height: 16),
            OutlinedButton(
              onPressed: _busy ? null : _submit,
              child: Text(_registering ? 'Register' : 'Sign in'),
            ),
            TextButton(
              onPressed: () => setState(() => _registering = !_registering),
              child: Text(_registering
                  ? 'Have an account? Sign in'
                  : 'New here? Register'),
            ),
            if (!_registering)
              TextButton(
                onPressed: () => context.push(Uri(
                    path: '/sign-in/forgot-password',
                    queryParameters: {'email': _email.text}).toString()),
                child: const Text('Forgot password?'),
              ),
          ],
        ),
      ),
    );
  }
}

class ForgotPasswordPage extends StatefulWidget {
  const ForgotPasswordPage({super.key, this.email});
  final String? email;

  @override
  State<ForgotPasswordPage> createState() => _ForgotPasswordPageState();
}

class _ForgotPasswordPageState extends State<ForgotPasswordPage> {
  late final _email = TextEditingController(text: widget.email);

  @override
  void dispose() {
    _email.dispose();
    super.dispose();
  }

  Future<void> _submit() async {
    final appState = context.read<ApplicationState>();
    final router = GoRouter.of(context);
    final messenger = ScaffoldMessenger.of(context);
    try {
      await appState.requestPasswordReset(_email.text.trim());
      messenger.showSnackBar(
          const SnackBar(content: Text('Check your email for a reset link')));
      router.pop();
    } catch (e) {
      messenger.showSnackBar(SnackBar(content: Text(errorMessage(e))));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Forgot password')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: ListView(
          children: [
            TextField(
              controller: _email,
              keyboardType: TextInputType.emailAddress,
              decoration: const InputDecoration(labelText: 'Email'),
            ),
            const SizedBox(height: 16),
            OutlinedButton(
                onPressed: _submit, child: const Text('Send reset link')),
          ],
        ),
      ),
    );
  }
}

class ProfilePage extends StatelessWidget {
  const ProfilePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Consumer<ApplicationState>(
      builder: (context, appState, _) => Scaffold(
        appBar: AppBar(title: const Text('Profile')),
        body: Padding(
          padding: const EdgeInsets.all(16),
          child: ListView(
            children: [
              Text(appState.userName,
                  style: Theme.of(context).textTheme.headlineMedium),
              Text(appState.email),
              const SizedBox(height: 16),
              if (!appState.emailVerified)
                OutlinedButton(
                  child: const Text('Recheck Verification State'),
                  onPressed: () async {
                    try {
                      await appState.refreshLoggedInUser();
                    } catch (e) {
                      if (context.mounted) _snack(context, errorMessage(e));
                    }
                  },
                ),
              OutlinedButton(
                child: const Text('Sign out'),
                onPressed: () {
                  appState.signOut();
                  context.pushReplacement('/');
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
