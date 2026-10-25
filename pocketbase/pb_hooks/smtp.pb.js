/// <reference path="../pb_data/types.d.ts" />
// Sends mail (verification, password reset) through Resend when
// RESEND_API_KEY is set. The key stays in the environment, never in the repo.
// The sender must be an address on a domain verified in Resend (set it in the
// admin UI, or with MAIL_FROM); onboarding@resend.dev only delivers to the
// Resend account owner. APP_URL (the server's public address) is what the
// links in verification and reset mails point to; the default is localhost.
onBootstrap((e) => {
  e.next();

  const apiKey = $os.getenv("RESEND_API_KEY");
  if (!apiKey) return;

  const settings = $app.settings();
  settings.smtp.enabled = true;
  settings.smtp.host = "smtp.resend.com";
  settings.smtp.port = 465;
  settings.smtp.tls = true;
  settings.smtp.username = "resend";
  settings.smtp.password = apiKey;
  // Sender is only overridden when MAIL_FROM is set, so a value saved in the
  // admin UI survives restarts.
  const from = $os.getenv("MAIL_FROM");
  if (from) settings.meta.senderAddress = from;
  const appURL = $os.getenv("APP_URL");
  if (appURL) settings.meta.appURL = appURL;
  $app.save(settings);
});
