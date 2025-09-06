<template>
  <Navbar />
  <div class="flex min-h-screen items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="w-full max-w-md space-y-8">
      <div class="text-center">
        <h2 class="mt-6 text-3xl font-bold tracking-tight text-gray-900">Sign in to your account</h2>
        <p class="mt-2 text-sm text-gray-600">
          Or
          <router-link to="/register" class="font-medium text-blue-600 hover:text-blue-500">
            create a new account
          </router-link>
        </p>
      </div>

      <Card class="p-6">
        <form @submit.prevent="handleSubmit" class="space-y-6">
          <div v-if="successMessage" class="rounded-md bg-green-50 p-4 mb-4">
            <div class="flex">
              <div class="flex-shrink-0">
                <!-- Success icon -->
                <svg class="h-5 w-5 text-green-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.857-9.809a.75.75 0 00-1.214-.882l-3.483 4.79-1.88-1.88a.75.75 0 10-1.06 1.061l2.5 2.5a.75.75 0 001.137-.089l4-5.5z" clip-rule="evenodd" />
                </svg>
              </div>
              <div class="ml-3">
                <h3 class="text-sm font-medium text-green-800">{{ successMessage }}</h3>
              </div>
            </div>
          </div>

          <div v-if="authStore.error" class="rounded-md bg-red-50 p-4">
            <div class="flex">
              <div class="flex-shrink-0">
                <!-- Error icon -->
                <svg class="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                  <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.28 7.22a.75.75 0 00-1.06 1.06L8.94 10l-1.72 1.72a.75.75 0 101.06 1.06L10 11.06l1.72 1.72a.75.75 0 101.06-1.06L11.06 10l1.72-1.72a.75.75 0 00-1.06-1.06L10 8.94 8.28 7.22z" clip-rule="evenodd" />
                </svg>
              </div>
              <div class="ml-3">
                <h3 class="text-sm font-medium text-red-800">{{ authStore.error }}</h3>
              </div>
            </div>
          </div>

          <div class="space-y-2">
            <Label for="username">Username</Label>
            <Input
              id="username"
              v-model="username"
              type="text"
              required
              autocomplete="username"
              :disabled="authStore.loading"
            />
          </div>

          <div class="space-y-2">
            <div class="flex items-center justify-between">
              <Label for="password">Password</Label>
              <div class="text-sm">
                <a href="#" class="font-medium text-blue-600 hover:text-blue-500">Forgot your password?</a>
              </div>
            </div>
            <Input
              id="password"
              v-model="password"
              type="password"
              required
              autocomplete="current-password"
              :disabled="authStore.loading"
            />
          </div>

          <div class="flex items-center">
            <input
              id="remember-me"
              name="remember-me"
              type="checkbox"
              v-model="rememberMe"
              class="h-4 w-4 rounded border-gray-300 text-blue-600 focus:ring-blue-600"
            />
            <label for="remember-me" class="ml-2 block text-sm text-gray-900">Remember me</label>
          </div>

          <div>
            <Button
              type="submit"
              class="w-full"
              :disabled="authStore.loading"
            >
              <span v-if="authStore.loading" class="mr-2">
                <!-- Loading spinner -->
                <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
              </span>
              {{ authStore.loading ? 'Signing in...' : 'Sign in' }}
            </Button>
          </div>
        </form>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import Navbar from '@/components/layout/Navbar.vue';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card } from '@/components/ui/card';
import { useToast } from 'vue-toastification';

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const toast = useToast();

// Check for registration success message
const successMessage = ref('');

onMounted(() => {
  if (route.query.registered === 'true') {
    successMessage.value = 'Registration successful! Please sign in with your new account.';
    toast.success(successMessage.value);
  }
});

const username = ref('');
const password = ref('');
const rememberMe = ref(false);

const handleSubmit = async () => {
  try {
    const success = await authStore.loginWithCredentials(username.value, password.value);

    if (success) {
      toast.success('Login successful!');
      // Check if user is admin and redirect to dashboard, otherwise go to home
      if (authStore.isAdmin) {
        console.log('User is admin, redirecting to dashboard');
        router.push('/dashboard');
      } else {
        console.log('User is not admin, redirecting to home');
        router.push('/');
      }
    }
  } catch (error) {
    toast.error(error.message || 'Login failed. Please check your credentials.');
  }
};
</script>
