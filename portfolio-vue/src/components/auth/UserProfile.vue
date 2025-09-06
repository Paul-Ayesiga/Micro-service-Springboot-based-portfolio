<template>
  <div v-if="authStore.isAuthenticated && authStore.user" class="user-profile">
    <div class="flex items-center space-x-3">
      <div class="avatar">
        <div class="w-10 h-10 rounded-full bg-blue-500 flex items-center justify-center text-white font-bold">
          {{ userInitials }}
        </div>
      </div>
      <div class="user-info">
        <p class="font-medium">{{ authStore.user.fullName }}</p>
        <p class="text-sm text-gray-500">{{ authStore.user.email }}</p>
      </div>
    </div>
    <div v-if="authStore.isAdmin" class="mt-2 text-xs px-2 py-1 bg-blue-100 text-blue-800 rounded-full inline-block">
      Admin
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();

const userInitials = computed(() => {
  if (!authStore.user) return '';
  
  const firstName = authStore.user.firstName || '';
  const lastName = authStore.user.lastName || '';
  
  return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
});
</script>
