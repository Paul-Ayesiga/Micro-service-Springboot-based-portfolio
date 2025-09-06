<template>
  <Navbar />
  <div class="flex min-h-screen items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="w-full max-w-md space-y-8">
      <div class="text-center">
        <h2 class="mt-6 text-3xl font-bold tracking-tight text-gray-900">Create your account</h2>
        <p class="mt-2 text-sm text-gray-600">
          Or
          <router-link to="/login" class="font-medium text-blue-600 hover:text-blue-500">
            sign in to your existing account
          </router-link>
        </p>
      </div>

      <Card class="p-6">
        <form @submit.prevent="handleSubmit" class="space-y-6">
          <div v-if="error" class="rounded-md bg-red-50 p-4 mb-4">
            <div class="flex">
              <div class="flex-shrink-0">
                <!-- Error icon -->
                <svg class="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.28 7.22a.75.75 0 00-1.06 1.06L8.94 10l-1.72 1.72a.75.75 0 101.06 1.06L10 11.06l1.72 1.72a.75.75 0 101.06-1.06L11.06 10l1.72-1.72a.75.75 0 00-1.06-1.06L10 8.94 8.28 7.22z" clip-rule="evenodd" />
                </svg>
              </div>
              <div class="ml-3">
                <h3 class="text-sm font-medium text-red-800">{{ error }}</h3>
              </div>
            </div>
          </div>

          <!-- Validation errors -->
          <div v-if="Object.keys(validationErrors).length > 0" class="rounded-md bg-yellow-50 p-4 mb-4">
            <div class="flex">
              <div class="flex-shrink-0">
                <!-- Warning icon -->
                <svg class="h-5 w-5 text-yellow-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path fill-rule="evenodd" d="M8.485 2.495c.673-1.167 2.357-1.167 3.03 0l6.28 10.875c.673 1.167-.17 2.625-1.516 2.625H3.72c-1.347 0-2.189-1.458-1.515-2.625L8.485 2.495zM10 5a.75.75 0 01.75.75v4.5a.75.75 0 01-1.5 0v-4.5A.75.75 0 0110 5zm0 10a1 1 0 100-2 1 1 0 000 2z" clip-rule="evenodd" />
                </svg>
              </div>
              <div class="ml-3">
                <h3 class="text-sm font-medium text-yellow-800">Please fix the following errors:</h3>
                <div class="mt-2 text-sm text-yellow-700">
                  <ul class="list-disc pl-5 space-y-1">
                    <li v-for="(message, field) in validationErrors" :key="field">
                      {{ field }}: {{ message }}
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div class="space-y-2">
              <Label for="firstName">First name</Label>
              <Input
                id="firstName"
                v-model="firstName"
                type="text"
                required
                autocomplete="given-name"
                :disabled="loading"
              />
            </div>

            <div class="space-y-2">
              <Label for="lastName">Last name</Label>
              <Input
                id="lastName"
                v-model="lastName"
                type="text"
                required
                autocomplete="family-name"
                :disabled="loading"
              />
            </div>
          </div>

          <div class="space-y-2">
            <Label for="email">Email address</Label>
            <Input
              id="email"
              v-model="email"
              type="email"
              required
              autocomplete="email"
              :disabled="loading"
            />
          </div>

          <div class="space-y-2">
            <Label for="username">Username</Label>
            <Input
              id="username"
              v-model="username"
              type="text"
              required
              autocomplete="username"
              :disabled="loading"
            />
          </div>

          <div class="space-y-2">
            <Label for="password">Password</Label>
            <Input
              id="password"
              v-model="password"
              type="password"
              required
              autocomplete="new-password"
              :disabled="loading"
            />
          </div>

          <div class="space-y-2">
            <Label for="confirmPassword">Confirm password</Label>
            <Input
              id="confirmPassword"
              v-model="confirmPassword"
              type="password"
              required
              autocomplete="new-password"
              :disabled="loading"
            />
          </div>

          <div class="flex items-center">
            <input
              id="terms"
              name="terms"
              type="checkbox"
              v-model="agreeToTerms"
              required
              class="h-4 w-4 rounded border-gray-300 text-blue-600 focus:ring-blue-600"
            />
            <label for="terms" class="ml-2 block text-sm text-gray-900">
              I agree to the
              <a href="#" class="font-medium text-blue-600 hover:text-blue-500">Terms of Service</a>
              and
              <a href="#" class="font-medium text-blue-600 hover:text-blue-500">Privacy Policy</a>
            </label>
          </div>

          <div>
            <Button
              type="submit"
              class="w-full"
              :disabled="loading || !isFormValid"
            >
              <span v-if="loading" class="mr-2">
                <!-- Loading spinner -->
                <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
              </span>
              {{ loading ? 'Creating account...' : 'Create account' }}
            </Button>
          </div>
        </form>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { Button } from '@/components/ui/button';
import Navbar from '@/components/layout/Navbar.vue';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card } from '@/components/ui/card';
import { useAuthStore } from '@/stores/auth';
import api from '@/services/api';
import { useToast } from 'vue-toastification';

const router = useRouter();
const authStore = useAuthStore();
const toast = useToast();

const firstName = ref('');
const lastName = ref('');
const email = ref('');
const username = ref('');
const password = ref('');
const confirmPassword = ref('');
const agreeToTerms = ref(false);
const loading = ref(false);
const error = ref('');
const validationErrors = ref({});

const isFormValid = computed(() => {
  return (
    firstName.value.trim() !== '' &&
    lastName.value.trim() !== '' &&
    email.value.trim() !== '' &&
    username.value.trim() !== '' &&
    password.value.trim() !== '' &&
    password.value === confirmPassword.value &&
    agreeToTerms.value
  );
});

const handleSubmit = async () => {
  if (!isFormValid.value) {
    return;
  }

  loading.value = true;
  error.value = '';

  try {
    // Clear previous errors
    error.value = '';
    validationErrors.value = {};

    // Register the user through our backend API
    const response = await api.post('/public/auth/register', {
      username: username.value,
      email: email.value,
      firstName: firstName.value,
      lastName: lastName.value,
      password: password.value
    });

    // Show success toast
    toast.success('Your account has been created successfully.', {
      timeout: 5000,
      closeButton: true,
    });

    // Automatically log in the user after successful registration
    const loginSuccess = await authStore.loginWithCredentials(
      username.value,
      password.value
    );

    if (loginSuccess) {
      // Redirect to home page after successful registration and login
      router.push('/');
    } else {
      // If auto-login fails, redirect to login page with success message
      router.push({
        path: '/login',
        query: { registered: 'true' }
      });
    }
  } catch (err) {
    console.error('Registration error:', err);

    // Handle different types of errors
    if (err.response) {
      // The request was made and the server responded with a status code
      // that falls out of the range of 2xx
      const responseData = err.response.data;

      if (responseData.validationErrors) {
        // Handle validation errors
        validationErrors.value = responseData.validationErrors;
        error.value = 'Please fix the validation errors.';
      } else if (responseData.error) {
        // Handle server error with message
        error.value = `${responseData.error}: ${responseData.message || ''}`;
      } else {
        // Generic error with status
        error.value = `Error (${err.response.status}): ${err.response.statusText}`;
      }
    } else if (err.request) {
      // The request was made but no response was received
      error.value = 'No response from server. Please check your connection.';
    } else {
      // Something happened in setting up the request that triggered an Error
      error.value = err.message || 'Registration failed. Please try again.';
    }

    // Show error toast
    toast.error(error.value, {
      timeout: 8000,
      closeButton: true,
    });
  } finally {
    loading.value = false;
  }
};
</script>
