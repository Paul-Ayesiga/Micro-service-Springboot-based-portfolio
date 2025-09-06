<template>
  <nav class="rounded-lg sticky top-0 z-40 w-full backdrop-blur-lg bg-biege/80 border-b border-gray-200 shadow-sm transition-all">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16">
        <!-- Logo and desktop navigation -->
        <div class="flex">
          <div class="flex-shrink-0 flex items-center">
            <router-link to="/" class="text-xl font-bold text-blue-600">Portfolio</router-link>
          </div>
          <!-- Desktop navigation -->
          <div class="hidden md:ml-6 md:flex md:space-x-8">
            <router-link
              to="/"
              class="inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium transition-colors"
              :class="[
                $route.path === '/'
                  ? 'border-blue-500 text-gray-900'
                  : 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700'
              ]"
            >
              Home
            </router-link>
          </div>
        </div>

        <!-- Desktop auth buttons -->
        <div class="hidden md:flex md:items-center md:space-x-4">
          <template v-if="authStore.isAuthenticated">
            <UserProfile class="mr-2" />
            <Button variant="outline" size="sm" @click="logout">Sign out</Button>
          </template>
          <template v-else>
            <router-link to="/login">
              <Button variant="outline" size="sm">Sign in</Button>
            </router-link>
            <router-link to="/register">
              <Button size="sm">Sign up</Button>
            </router-link>
          </template>
        </div>

        <!-- Mobile menu button -->
        <div class="flex items-center md:hidden">
          <button
            @click="isMenuOpen = !isMenuOpen"
            class="inline-flex items-center justify-center p-2 rounded-md text-gray-500 hover:text-gray-700 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-blue-500"
            aria-expanded="false"
          >
            <span class="sr-only">{{ isMenuOpen ? 'Close menu' : 'Open menu' }}</span>
            <!-- Hamburger icon when closed -->
            <svg
              v-if="!isMenuOpen"
              class="block h-6 w-6"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              aria-hidden="true"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
            <!-- X icon when open -->
            <svg
              v-else
              class="block h-6 w-6"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              aria-hidden="true"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>
    </div>

    <!-- Mobile menu -->
    <div
      v-if="isMenuOpen"
      class="md:hidden bg-white/95 backdrop-blur-sm border-b border-gray-200 shadow-md transition-all"
    >
      <div class="px-2 pt-2 pb-3 space-y-1">
        <router-link
          to="/"
          class="block px-3 py-2 rounded-md text-base font-medium transition-colors"
          :class="[
            $route.path === '/'
              ? 'bg-blue-50 text-blue-700'
              : 'text-gray-700 hover:bg-gray-50 hover:text-gray-900'
          ]"
          @click="isMenuOpen = false"
        >
          Home
        </router-link>
      </div>

      <!-- Mobile auth buttons -->
      <div class="pt-4 pb-3 border-t border-gray-200">
        <div class="flex items-center px-4">
          <div v-if="authStore.isAuthenticated" class="flex-shrink-0">
            <UserProfile />
          </div>
        </div>
        <div class="mt-3 px-2 space-y-1">
          <template v-if="authStore.isAuthenticated">
            <button
              @click="mobileLogout"
              class="block w-full text-left px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:bg-gray-50 hover:text-gray-900 transition-colors"
            >
              Sign out
            </button>
          </template>
          <template v-else>
            <router-link
              to="/login"
              class="block px-3 py-2 rounded-md text-base font-medium text-gray-700 hover:bg-gray-50 hover:text-gray-900 transition-colors"
              @click="isMenuOpen = false"
            >
              Sign in
            </router-link>
            <router-link
              to="/register"
              class="block px-3 py-2 rounded-md text-base font-medium text-blue-600 hover:bg-blue-50 transition-colors"
              @click="isMenuOpen = false"
            >
              Sign up
            </router-link>
          </template>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { Button } from '@/components/ui/button';
import UserProfile from '@/components/auth/UserProfile.vue';

const authStore = useAuthStore();
const isMenuOpen = ref(false);

const logout = async () => {
  await authStore.logout();
};

const mobileLogout = async () => {
  isMenuOpen.value = false;
  await authStore.logout();
};
</script>
