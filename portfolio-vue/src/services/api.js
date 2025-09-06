import axios from 'axios';
import { useAuthStore } from '@/stores/auth';
import { storeToRefs } from 'pinia';

// Create axios instance
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:9090/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// Request interceptor for API calls
api.interceptors.request.use(
  async (config) => {
    const authStore = useAuthStore();
    const { isAuthenticated } = storeToRefs(authStore);

    if (isAuthenticated.value) {
      // Get fresh token if needed
      const token = await authStore.refreshToken();
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor for API calls
api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const authStore = useAuthStore();

    // Handle 401 Unauthorized errors
    if (error.response && error.response.status === 401) {
      // Redirect to login
      await authStore.login();
    }

    return Promise.reject(error);
  }
);

export default api;
