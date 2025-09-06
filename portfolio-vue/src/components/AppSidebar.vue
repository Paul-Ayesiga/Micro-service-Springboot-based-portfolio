<script setup>
import NavMain from '@/components/NavMain.vue';
import NavUser from '@/components/NavUser.vue';
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarRail,
} from '@/components/ui/sidebar';
import { useAuthStore } from '@/stores/auth';
import { useRoute } from 'vue-router';
import { computed } from 'vue';

import {
  AudioWaveform,
  BookOpen,
  Bot,
  Command,
  Frame,
  GalleryVerticalEnd,
  Map,
  PieChart,
  Settings2,
  SquareTerminal,
  FileText,
  Briefcase,
  Lightbulb,
  User,
  Home
} from 'lucide-vue-next';

const props = defineProps({
  side: { type: String, required: false },
  variant: { type: String, required: false },
  collapsible: { type: String, required: false, default: 'icon' },
  class: { type: null, required: false },
});

// Get auth store for user information
const authStore = useAuthStore();

// Get current route
const route = useRoute();

// User data
const userData = {
  name: authStore.user?.fullName || authStore.user?.username || 'Admin User',
  email: authStore.user?.email || 'admin@example.com',
  avatar: '',
};

// Navigation items
const navItems = [
  {
    title: 'Dashboard',
    url: '/dashboard',
    icon: PieChart,
    items: [],
  },
  {
    title: 'Projects',
    url: '/projects',
    icon: Briefcase,
    items: [],
  },
  {
    title: 'Skills',
    url: '/skills',
    icon: Lightbulb,
    items: [],
  },
  {
    title: 'Experience',
    url: '/experience',
    icon: FileText,
    items: [],
  },
  {
    title: 'Settings',
    url: '/settings',
    icon: Settings2,
    items: [],
  },
];

// Add isActive property based on current route
const navMain = computed(() => {
  return navItems.map(item => ({
    ...item,
    isActive: route.path === item.url
  }));
});

// User data object
const data = {
  user: userData,
  projects: [],
};
</script>

<template>

  <Sidebar v-bind="props" class="sidebar-custom">

    <SidebarContent>
      <NavMain :items="navMain" />
    </SidebarContent>
    <SidebarFooter>
      <NavUser :user="data.user" />
    </SidebarFooter>
    <SidebarRail />
  </Sidebar>
</template>
