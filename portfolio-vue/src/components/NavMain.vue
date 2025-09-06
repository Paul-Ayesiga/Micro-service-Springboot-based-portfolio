<script setup>
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from '@/components/ui/collapsible';
import { Separator } from '@/components/ui/separator'
import {
  SidebarGroup,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSub,
  SidebarMenuSubButton,
  SidebarMenuSubItem,
} from '@/components/ui/sidebar';
import { ChevronRight } from 'lucide-vue-next';
import { useSidebar } from '@/components/ui/sidebar/utils';
import { RouterLink, useRoute } from 'vue-router';

defineProps({
  items: { type: Array, required: true },
});

const route = useRoute();
const { state } = useSidebar();

// Function to check if a route is active
const isRouteActive = (url) => {
  return route.path === url;
};
</script>

<template>
  <SidebarGroup>
    <!-- Logo section that adapts to sidebar state -->
    <div class="flex items-center py-4 transition-all duration-200 sidebar-logo-container" :class="{ 'justify-center px-1': state === 'collapsed', 'px-3': state !== 'collapsed' }">
      <img
        src="@/assets/logo.svg"
        alt="Vue Logo"
        :class="{
          'h-8 w-auto': state === 'collapsed',
          'h-10 w-auto mr-3': state !== 'collapsed'
        }"
      />
      <span class="font-semibold text-lg transition-opacity duration-200" :class="{ 'opacity-0 w-0 overflow-hidden': state === 'collapsed', 'opacity-100': state !== 'collapsed' }">Portfolio</span>
    </div>
    <Separator class="sidebar-separator" />
    <!-- Sidebar group label -->
    <SidebarGroupLabel>Platform</SidebarGroupLabel>
    <SidebarMenu>
      <!-- Regular menu items (no dropdown) -->
      <template v-for="item in items" :key="item.title">
        <!-- If item has no subitems, render as a regular menu item -->
        <SidebarMenuItem v-if="item.items.length === 0">
          <SidebarMenuButton :tooltip="item.title" as-child :isActive="isRouteActive(item.url)">
            <router-link :to="item.url">
              <component :is="item.icon" v-if="item.icon" />
              <span>{{ item.title }}</span>
            </router-link>
          </SidebarMenuButton>
        </SidebarMenuItem>

        <!-- If item has subitems, render as a collapsible dropdown -->
        <Collapsible
          v-else
          as-child
          :default-open="item.isActive"
          class="group/collapsible"
        >
          <SidebarMenuItem>
            <CollapsibleTrigger as-child>
              <SidebarMenuButton :tooltip="item.title" :isActive="item.items.some(subItem => isRouteActive(subItem.url))">
                <component :is="item.icon" v-if="item.icon" />
                <span>{{ item.title }}</span>
                <ChevronRight
                  class="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90"
                />
              </SidebarMenuButton>
            </CollapsibleTrigger>
            <CollapsibleContent>
              <SidebarMenuSub>
                <SidebarMenuSubItem
                  v-for="subItem in item.items"
                  :key="subItem.title"
                >
                  <SidebarMenuSubButton as-child :isActive="isRouteActive(subItem.url)">
                    <router-link :to="subItem.url">
                      <span>{{ subItem.title }}</span>
                    </router-link>
                  </SidebarMenuSubButton>
                </SidebarMenuSubItem>
              </SidebarMenuSub>
            </CollapsibleContent>
          </SidebarMenuItem>
        </Collapsible>
      </template>
    </SidebarMenu>
  </SidebarGroup>
</template>
