<script setup>
import { RouterView, useRouter } from 'vue-router'
import { onMounted, ref, watch } from 'vue'
import { useAuthStore } from './stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const isInitializing = ref(true)

// Watch for changes in authentication state
watch(
  () => [authStore.isAuthenticated, authStore.isAdmin],
  ([isAuthenticated, isAdmin]) => {
    // Only redirect admin users from login/register pages to dashboard
    // Allow admin users to access the home page
    if (isAuthenticated && isAdmin &&
        (router.currentRoute.value.name === 'login' || router.currentRoute.value.name === 'register')) {
      console.log('App detected admin user on login/register page, redirecting to dashboard')
      router.push('/dashboard')
    }
  }
)

onMounted(async () => {
  // Initialize authentication on app mount
  try {
    await authStore.initialize()

    // Only redirect admin users from login/register pages to dashboard
    // Allow admin users to access the home page
    if (authStore.isAuthenticated && authStore.isAdmin &&
        (router.currentRoute.value.name === 'login' || router.currentRoute.value.name === 'register')) {
      console.log('App initialization detected admin user on login/register page, redirecting to dashboard')
      router.push('/dashboard')
    }
  } catch (error) {
    console.error('Failed to initialize authentication:', error)
  } finally {
    isInitializing.value = false
  }
})
</script>

<template>
  <div class="min-h-screen bg-gray-50">
    <div v-if="isInitializing" class="flex items-center justify-center min-h-screen">
      <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
    </div>

    <template v-else>
      <main :class="{'p-0 m-0': $route.path.startsWith('/dashboard')}">
        <RouterView />
      </main>
    </template>
  </div>
</template>
