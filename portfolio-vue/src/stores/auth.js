import { defineStore } from 'pinia';
import keycloakService from '@/services/keycloak';

export const useAuthStore = defineStore('auth', {
  state: () => {
    // Try to restore state from localStorage
    const storedAuth = localStorage.getItem('kc_authenticated') === 'true';
    const storedToken = localStorage.getItem('kc_token');

    return {
      user: null,
      isAuthenticated: storedAuth || false,
      isAdmin: false,
      token: storedToken || null,
      loading: false,
      error: null,
      initialized: false
    };
  },

  getters: {
    getUser: (state) => state.user,
    getIsAuthenticated: (state) => state.isAuthenticated,
    getIsAdmin: (state) => state.isAdmin,
    getToken: (state) => state.token,
    getLoading: (state) => state.loading,
    getError: (state) => state.error,
    getInitialized: (state) => state.initialized
  },

  actions: {
    async initialize() {
      // If already initialized and not loading, don't do it again
      if (this.initialized && !this.loading) {
        return;
      }

      this.loading = true;
      try {
        // Check if we have stored tokens in localStorage
        const hasStoredTokens = localStorage.getItem('kc_authenticated') === 'true' &&
                               localStorage.getItem('kc_token') &&
                               localStorage.getItem('kc_refreshToken');

        // Check if keycloak is already initialized
        if (keycloakService.initialized) {
          if (keycloakService.isAuthenticated()) {
            this.isAuthenticated = true;
            this.token = keycloakService.getToken();
            this.isAdmin = keycloakService.hasRole('ADMIN');
            await this.loadUserProfile();
          } else if (hasStoredTokens) {
            // If we have stored tokens but keycloak doesn't think we're authenticated,
            // we need to manually restore the session
            console.log('Restoring session from stored tokens');
            this.isAuthenticated = true;
            this.token = localStorage.getItem('kc_token');
            // Check for admin role in the token
            try {
              const tokenData = JSON.parse(atob(this.token.split('.')[1]));
              this.isAdmin = tokenData.realm_access?.roles?.includes('ADMIN') || false;
              console.log('Admin role from token:', this.isAdmin);
            } catch (e) {
              console.error('Error parsing token for roles:', e);
            }
            // We'll try to load the user profile later
          }
        } else {
          // Initialize keycloak
          console.log('Initializing Keycloak...');
          const authenticated = await keycloakService.init();
          console.log('Keycloak initialization result:', authenticated);

          if (authenticated) {
            this.isAuthenticated = true;
            this.token = keycloakService.getToken();
            this.isAdmin = keycloakService.hasRole('ADMIN');
            await this.loadUserProfile();
          } else if (hasStoredTokens) {
            // If we have stored tokens but keycloak init didn't authenticate us,
            // we'll try to manually restore the session
            console.log('Keycloak init did not authenticate, but we have stored tokens');
            this.isAuthenticated = true;
            this.token = localStorage.getItem('kc_token');
            // Check for admin role in the token
            try {
              const tokenData = JSON.parse(atob(this.token.split('.')[1]));
              this.isAdmin = tokenData.realm_access?.roles?.includes('ADMIN') || false;
              console.log('Admin role from token:', this.isAdmin);
            } catch (e) {
              console.error('Error parsing token for roles:', e);
            }
            // Try to refresh the token
            try {
              await this.refreshToken();
              await this.loadUserProfile();
            } catch (refreshError) {
              console.error('Failed to refresh token from stored tokens:', refreshError);
              // Clear invalid tokens
              this.logout();
            }
          }
        }
      } catch (error) {
        this.error = error.message || 'Authentication initialization failed';
        console.error('Auth initialization error:', error);
      } finally {
        this.loading = false;
        this.initialized = true;
      }
    },

    // Helper method to load user profile
    async loadUserProfile() {
      try {
        const profile = await keycloakService.getUserProfile();
        this.user = {
          id: profile.id,
          username: profile.username,
          email: profile.email,
          firstName: profile.firstName,
          lastName: profile.lastName,
          fullName: `${profile.firstName} ${profile.lastName}`
        };
      } catch (profileError) {
        console.error('Error loading user profile:', profileError);
        // Continue even if profile loading fails
      }
    },

    async login() {
      await keycloakService.login();
    },

    async loginWithCredentials(username, password) {
      this.loading = true;
      this.error = null;

      try {
        await keycloakService.loginWithCredentials(username, password);
        this.isAuthenticated = true;
        this.token = keycloakService.getToken();

        // Check for admin role in the token
        try {
          const tokenData = JSON.parse(atob(this.token.split('.')[1]));
          this.isAdmin = tokenData.realm_access?.roles?.includes('ADMIN') || false;
          console.log('Admin role from token:', this.isAdmin);
        } catch (e) {
          console.error('Error parsing token for roles:', e);
          // Fallback to keycloak service method
          this.isAdmin = keycloakService.hasRole('ADMIN');
        }

        console.log('User authenticated, isAdmin:', this.isAdmin);
        this.initialized = true;

        try {
          // Load user profile
          await this.loadUserProfile();
        } catch (profileError) {
          console.warn('Error loading user profile:', profileError);
          // Create minimal user object if profile loading fails
          this.user = {
            username: username,
            fullName: username
          };
        }

        return true;
      } catch (error) {
        this.error = error.message || 'Login failed';
        console.error('Login error:', error);
        // Throw the error so it can be caught by the component
        throw new Error(this.error);
      } finally {
        this.loading = false;
      }
    },

    async logout() {
      this.user = null;
      this.isAuthenticated = false;
      this.isAdmin = false;
      this.token = null;
      await keycloakService.logout();
    },

    async refreshToken() {
      try {
        this.token = await keycloakService.updateToken();
        return this.token;
      } catch (error) {
        this.error = 'Token refresh failed';
        console.error('Token refresh error:', error);
        // If token refresh fails, redirect to login
        await this.login();
      }
    }
  }
});
