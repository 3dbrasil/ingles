import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'app.lovable.cc2ce60caccb461daf8c53ee6e3e11ee',
  appName: 'Dialogoo',
  webDir: 'dist',
  server: {
    // O app é SSR (TanStack Start) e não gera bundle estático.
    // O APK funciona como wrapper do site publicado no Lovable.
    url: 'https://ais-dev-faaqlyfqxtp4jtltrgd27a-202636042140.us-east1.run.app',
    cleartext: true,
    allowNavigation: [
      '*.run.app',
      'ais-dev-faaqlyfqxtp4jtltrgd27a-202636042140.us-east1.run.app',
      'ais-pre-faaqlyfqxtp4jtltrgd27a-202636042140.us-east1.run.app',
      'language-bliss-61.lovable.app',
      '*.lovable.app',
      'oauth.lovable.app',
      'accounts.google.com',
      '*.google.com',
      '*.googleusercontent.com',
      '*.supabase.co',
    ],
  },
  android: {
    allowMixedContent: true,
    webContentsDebuggingEnabled: true,
  },
};

export default config;
