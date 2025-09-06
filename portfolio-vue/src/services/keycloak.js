import Keycloak from 'keycloak-js';

// Keycloak configuration
const keycloakConfig = {
  url: import.meta.env.VITE_KEYCLOAK_URL || 'http://localhost:8080',
  realm: import.meta.env.VITE_KEYCLOAK_REALM || 'PortfolioRealm',
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID || 'portfolio-frontend'
};

// Create a class to manage Keycloak instance
class KeycloakService {
  constructor() {
    this._keycloak = null;
    this._initialized = false;
    this._initPromise = null;

    // Check if we have tokens in localStorage from a previous session
    const hasStoredTokens = localStorage.getItem('kc_authenticated') === 'true' &&
                           localStorage.getItem('kc_token') &&
                           localStorage.getItem('kc_refreshToken');

    if (hasStoredTokens) {
      console.log('Found stored authentication tokens');
    }
  }

  // Get the Keycloak instance
  get keycloak() {
    if (!this._keycloak) {
      this._keycloak = new Keycloak(keycloakConfig);

      // Check if we have stored tokens and restore them
      const hasStoredTokens = localStorage.getItem('kc_authenticated') === 'true' &&
                             localStorage.getItem('kc_token') &&
                             localStorage.getItem('kc_refreshToken');

      if (hasStoredTokens) {
        // Restore tokens from localStorage
        this._keycloak.token = localStorage.getItem('kc_token');
        this._keycloak.refreshToken = localStorage.getItem('kc_refreshToken');
        this._keycloak.idToken = localStorage.getItem('kc_idToken');
        this._keycloak.authenticated = true;

        // Parse token to get expiration
        try {
          this._keycloak.tokenParsed = JSON.parse(atob(this._keycloak.token.split('.')[1]));
          this._keycloak.refreshTokenParsed = JSON.parse(atob(this._keycloak.refreshToken.split('.')[1]));
          console.log('Restored tokens from localStorage');
        } catch (error) {
          console.error('Failed to parse stored tokens:', error);
        }
      }
    }
    return this._keycloak;
  }

  // Check if initialized
  get initialized() {
    return this._initialized;
  }

  // Initialize Keycloak
  init() {
    // If already initializing, return the existing promise
    if (this._initPromise) {
      return this._initPromise;
    }

    // If already initialized, return resolved promise
    if (this._initialized) {
      return Promise.resolve(this.keycloak.authenticated);
    }

    // Create and store the initialization promise
    this._initPromise = this.keycloak.init({
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      pkceMethod: 'S256',
      enableLogging: true,
      checkLoginIframe: false,  // Disable iframe checking which can cause issues
      flow: 'standard',         // Use standard flow for better session handling
      responseMode: 'fragment',
      storage: localStorage      // Use localStorage for persistent session
    })
    .then(auth => {
      this._initialized = true;
      console.log('Keycloak initialized with auth state:', auth);
      return auth;
    })
    .catch(error => {
      console.error('Keycloak initialization error:', error);
      this._initPromise = null; // Reset promise on error
      throw error;
    });

    return this._initPromise;
  }

  // Login with redirect
  login() {
    return this.keycloak.login();
  }

  // Login with username and password
  loginWithCredentials(username, password) {
    return new Promise((resolve, reject) => {
      // Use direct grant flow (Resource Owner Password Credentials Grant)
      const url = `${keycloakConfig.url}/realms/${keycloakConfig.realm}/protocol/openid-connect/token`;
      const params = new URLSearchParams();
      params.append('client_id', keycloakConfig.clientId);
      params.append('username', username);
      params.append('password', password);
      params.append('grant_type', 'password');

      fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Login failed');
        }
        return response.json();
      })
      .then(data => {
        // Manually set the token in keycloak instance
        this.keycloak.token = data.access_token;
        this.keycloak.refreshToken = data.refresh_token;
        this.keycloak.idToken = data.id_token;
        this.keycloak.authenticated = true;

        // Set token expiration
        this.keycloak.tokenParsed = JSON.parse(atob(data.access_token.split('.')[1]));
        this.keycloak.refreshTokenParsed = JSON.parse(atob(data.refresh_token.split('.')[1]));

        // Set initialized flag
        this._initialized = true;

        // Store tokens in localStorage for persistence across page refreshes
        // This is safe because we're using HTTPS and these tokens are meant to be stored client-side
        localStorage.setItem('kc_token', data.access_token);
        localStorage.setItem('kc_refreshToken', data.refresh_token);
        localStorage.setItem('kc_idToken', data.id_token);
        localStorage.setItem('kc_authenticated', 'true');

        resolve(true);
      })
      .catch(error => {
        console.error('Login error:', error);
        reject(error);
      });
    });
  }

  // Logout
  logout() {
    // Clear stored tokens
    localStorage.removeItem('kc_token');
    localStorage.removeItem('kc_refreshToken');
    localStorage.removeItem('kc_idToken');
    localStorage.removeItem('kc_authenticated');

    return this.keycloak.logout({ redirectUri: window.location.origin });
  }

  // Get user profile
  getUserProfile() {
    return this.keycloak.loadUserProfile();
  }

  // Check if user is authenticated
  isAuthenticated() {
    return !!this.keycloak.authenticated;
  }

  // Check if user has a specific role
  hasRole(role) {
    // Convert role to uppercase for Keycloak
    const upperRole = role.toUpperCase();

    // First try using the keycloak method
    if (this.keycloak.hasRealmRole && typeof this.keycloak.hasRealmRole === 'function') {
      const hasRole = this.keycloak.hasRealmRole(upperRole);
      console.log(`Checking role ${upperRole} using keycloak.hasRealmRole: ${hasRole}`);
      if (hasRole) return true;
    }

    // If that fails or returns false, check the token directly
    if (this.keycloak.token) {
      try {
        const tokenData = JSON.parse(atob(this.keycloak.token.split('.')[1]));
        const hasRole = tokenData.realm_access?.roles?.includes(upperRole) || false;
        console.log(`Checking role ${upperRole} in token: ${hasRole}`);
        return hasRole;
      } catch (e) {
        console.error('Error parsing token for roles:', e);
      }
    }

    return false;
  }

  // Get access token
  getToken() {
    return this.keycloak.token;
  }

  // Update token
  updateToken(minValidity = 5) {
    return new Promise((resolve, reject) => {
      this.keycloak.updateToken(minValidity)
        .then(refreshed => {
          if (refreshed) {
            console.log('Token refreshed');
            // Update localStorage with new tokens
            localStorage.setItem('kc_token', this.keycloak.token);
            localStorage.setItem('kc_refreshToken', this.keycloak.refreshToken);
            if (this.keycloak.idToken) {
              localStorage.setItem('kc_idToken', this.keycloak.idToken);
            }
            localStorage.setItem('kc_authenticated', 'true');
          }
          resolve(this.keycloak.token);
        })
        .catch(error => {
          console.error('Failed to refresh token', error);
          reject(error);
        });
    });
  }
}

// Create a singleton instance
const keycloakService = new KeycloakService();

export default keycloakService;
