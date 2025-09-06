<script setup>
import { SidebarProvider } from '@/components/ui/sidebar';
import AppSidebar from '@/components/AppSidebar.vue';
import { SidebarInset } from '@/components/ui/sidebar';
import { Separator } from '@/components/ui/separator';
import { Button } from '@/components/ui/button';
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from '@/components/ui/breadcrumb';
import { SidebarTrigger } from '@/components/ui/sidebar';
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import {
  BarChart,
  Briefcase,
  ChevronRight,
  Code2,
  Eye,
  FileEdit,
  Lightbulb,
  Plus,
  Settings,
  Share2,
  Star,
  Trophy,
  Users
} from 'lucide-vue-next';
import { ref } from 'vue';

// Sample data for dashboard stats
const portfolioStats = ref({
  projects: 12,
  skills: 24,
  experience: 5,
  views: 1250
});

// Sample data for quick actions
const quickActions = ref([
  {
    title: 'Add Project',
    description: 'Showcase your latest work',
    icon: Plus,
    color: 'bg-blue-100 text-blue-600'
  },
  {
    title: 'Edit Profile',
    description: 'Update your information',
    icon: FileEdit,
    color: 'bg-purple-100 text-purple-600'
  },
  {
    title: 'Add Skill',
    description: 'Highlight your expertise',
    icon: Lightbulb,
    color: 'bg-amber-100 text-amber-600'
  },
  {
    title: 'Settings',
    description: 'Configure your account',
    icon: Settings,
    color: 'bg-slate-100 text-slate-600'
  },
]);

// Sample data for recent activities
const recentActivities = ref([
  { title: 'Portfolio viewed by 5 recruiters', icon: Eye, time: '2 hours ago' },
  { title: 'New skill endorsement received', icon: Star, time: '1 day ago' },
  { title: 'Project "Vue Dashboard" shared', icon: Share2, time: '3 days ago' },
  { title: 'Profile completion reached 95%', icon: Trophy, time: '1 week ago' },
]);
</script>

<template>
  <SidebarProvider class="min-h-screen">
    <AppSidebar />
    <SidebarInset>
      <header
        class="flex h-16 shrink-0 items-center gap-2 transition-[width,height] ease-linear group-has-data-[collapsible=icon]/sidebar-wrapper:h-12"
      >
        <div class="flex items-center gap-2 px-4">
          <SidebarTrigger class="-ml-1" />
          <Separator
            orientation="vertical"
            class="mr-2 data-[orientation=vertical]:h-4"
          />
          <Breadcrumb>
            <BreadcrumbList>
              <BreadcrumbItem class="hidden md:block">
                <BreadcrumbLink href="#">
                  Admin Dashboard
                </BreadcrumbLink>
              </BreadcrumbItem>
              <BreadcrumbSeparator class="hidden md:block" />
              <BreadcrumbItem>
                <BreadcrumbPage>Overview</BreadcrumbPage>
              </BreadcrumbItem>
            </BreadcrumbList>
          </Breadcrumb>
        </div>
      </header>
      <div class="flex flex-1 flex-col p-4 pt-0 overflow-x-hidden">
        <div class="container mx-auto px-4 max-w-full">
          <h1 class="text-2xl font-bold mb-6">Portfolio Dashboard</h1>

          <!-- Welcome Card -->
          <Card class="mb-8">
            <CardHeader>
              <CardTitle>Welcome to Your Portfolio Dashboard</CardTitle>
              <CardDescription>Manage your professional portfolio and track your progress</CardDescription>
            </CardHeader>
            <CardContent>
              <p class="text-gray-700">From here you can add projects, update your skills, and monitor engagement with your portfolio.</p>
            </CardContent>
          </Card>

          <!-- Stats Overview -->
          <h2 class="text-xl font-semibold mb-4">Portfolio Insights</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            <!-- Projects Stat -->
            <Card class="overflow-hidden border-l-4 border-l-blue-500 hover:shadow-lg transition-all duration-300">
              <CardContent class="p-0">
                <div class="flex items-stretch h-full">
                  <div class="w-2/5 bg-gradient-to-br from-blue-500 to-blue-600 p-4 flex items-center justify-center">
                    <Briefcase class="h-10 w-10 text-white" />
                  </div>
                  <div class="w-3/5 p-4 flex flex-col justify-center">
                    <p class="text-sm font-medium text-gray-500 mb-1">Projects</p>
                    <div class="flex items-baseline">
                      <p class="text-3xl font-bold text-blue-600">{{ portfolioStats.projects }}</p>
                      <span class="ml-2 text-xs text-green-500">+2 new</span>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            <!-- Skills Stat -->
            <Card class="overflow-hidden border-l-4 border-l-purple-500 hover:shadow-lg transition-all duration-300">
              <CardContent class="p-0">
                <div class="flex items-stretch h-full">
                  <div class="w-2/5 bg-gradient-to-br from-purple-500 to-purple-600 p-4 flex items-center justify-center">
                    <Code2 class="h-10 w-10 text-white" />
                  </div>
                  <div class="w-3/5 p-4 flex flex-col justify-center">
                    <p class="text-sm font-medium text-gray-500 mb-1">Skills</p>
                    <div class="flex items-baseline">
                      <p class="text-3xl font-bold text-purple-600">{{ portfolioStats.skills }}</p>
                      <span class="ml-2 text-xs text-green-500">+3 new</span>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            <!-- Experience Stat -->
            <Card class="overflow-hidden border-l-4 border-l-amber-500 hover:shadow-lg transition-all duration-300">
              <CardContent class="p-0">
                <div class="flex items-stretch h-full">
                  <div class="w-2/5 bg-gradient-to-br from-amber-500 to-amber-600 p-4 flex items-center justify-center">
                    <Briefcase class="h-10 w-10 text-white" />
                  </div>
                  <div class="w-3/5 p-4 flex flex-col justify-center">
                    <p class="text-sm font-medium text-gray-500 mb-1">Years Experience</p>
                    <div class="flex items-baseline">
                      <p class="text-3xl font-bold text-amber-600">{{ portfolioStats.experience }}</p>
                      <span class="ml-2 text-xs text-green-500">+1 yr</span>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            <!-- Views Stat -->
            <Card class="overflow-hidden border-l-4 border-l-green-500 hover:shadow-lg transition-all duration-300">
              <CardContent class="p-0">
                <div class="flex items-stretch h-full">
                  <div class="w-2/5 bg-gradient-to-br from-green-500 to-green-600 p-4 flex items-center justify-center">
                    <Eye class="h-10 w-10 text-white" />
                  </div>
                  <div class="w-3/5 p-4 flex flex-col justify-center">
                    <p class="text-sm font-medium text-gray-500 mb-1">Profile Views</p>
                    <div class="flex items-baseline">
                      <p class="text-3xl font-bold text-green-600">{{ portfolioStats.views }}</p>
                      <span class="ml-2 text-xs text-green-500">+125 this week</span>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          <!-- Quick Actions -->
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-semibold">Quick Actions</h2>
          </div>
          <div class="grid grid-cols-2 md:grid-cols-4 gap-6 mb-8">
            <Card
              v-for="action in quickActions"
              :key="action.title"
              class="cursor-pointer group overflow-hidden relative hover:shadow-lg transition-all duration-300 border-0"
            >
              <div :class="`absolute inset-0 opacity-10 group-hover:opacity-20 transition-opacity ${action.color.split(' ')[0]}`"></div>
              <div class="absolute top-0 right-0 w-16 h-16 -mr-8 -mt-8 rounded-full opacity-20" :class="action.color.split(' ')[0]"></div>

              <CardContent class="flex flex-col items-start p-6 relative z-10">
                <div :class="`p-3 rounded-lg mb-4 shadow-sm ${action.color}`">
                  <component :is="action.icon" class="h-6 w-6" />
                </div>
                <h3 class="font-semibold text-lg mb-1">{{ action.title }}</h3>
                <p class="text-sm text-gray-500">{{ action.description }}</p>
                <div class="mt-4 flex items-center text-sm font-medium" :class="action.color.split(' ')[1]">
                  <span>Get started</span>
                  <ChevronRight class="ml-1 h-4 w-4" />
                </div>
              </CardContent>
            </Card>
          </div>

          <!-- Recent Activity -->
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-semibold">Recent Activity</h2>
            <Button variant="outline" size="sm" class="gap-1">
              <span>View All</span>
              <ChevronRight class="h-4 w-4" />
            </Button>
          </div>
          <Card class="overflow-hidden border-t-0 border-r-0 border-b-0 border-l-4 border-l-indigo-500">
            <CardHeader class="pb-2">
              <CardTitle class="text-lg">Activity Timeline</CardTitle>
              <CardDescription>Your recent portfolio interactions</CardDescription>
            </CardHeader>
            <CardContent class="p-0">
              <ul class="divide-y">
                <li v-for="activity in recentActivities" :key="activity.title" class="relative pl-8 pr-4 py-4 hover:bg-gray-50 transition-colors">
                  <!-- Timeline connector -->
                  <div class="absolute left-4 top-0 bottom-0 w-px bg-gray-200"></div>

                  <!-- Icon bubble -->
                  <div class="absolute left-2.5 top-4 p-1.5 rounded-full z-10 shadow-sm"
                       :class="{
                         'bg-blue-100': activity.icon === Eye,
                         'bg-amber-100': activity.icon === Star,
                         'bg-green-100': activity.icon === Share2,
                         'bg-purple-100': activity.icon === Trophy
                       }">
                    <component :is="activity.icon" class="h-4 w-4"
                             :class="{
                               'text-blue-600': activity.icon === Eye,
                               'text-amber-600': activity.icon === Star,
                               'text-green-600': activity.icon === Share2,
                               'text-purple-600': activity.icon === Trophy
                             }" />
                  </div>

                  <div class="flex-1">
                    <p class="font-medium">{{ activity.title }}</p>
                    <p class="text-sm text-gray-500 mt-1">{{ activity.time }}</p>
                  </div>
                </li>
              </ul>
            </CardContent>
            <CardFooter class="border-t bg-gray-50 py-3 px-4">
              <Button variant="ghost" size="sm" class="ml-auto text-indigo-600 hover:text-indigo-700 hover:bg-indigo-50">
                <span>Show more activities</span>
              </Button>
            </CardFooter>
          </Card>
        </div>
      </div>
    </SidebarInset>
  </SidebarProvider>
</template>
