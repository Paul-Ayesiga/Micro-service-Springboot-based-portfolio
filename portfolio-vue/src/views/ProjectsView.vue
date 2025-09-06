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
  Briefcase,
  Code2,
  ExternalLink,
  Github,
  Plus,
  Search,
  Tag
} from 'lucide-vue-next';
import { ref } from 'vue';

// Sample data for projects
const projects = ref([
  {
    id: 1,
    title: 'Portfolio Website',
    description: 'A personal portfolio website built with Vue.js and Spring Boot',
    image: 'https://placehold.co/600x400/2c3e50/FFFFFF/png?text=Portfolio+Website',
    technologies: ['Vue.js', 'Spring Boot', 'PostgreSQL'],
    githubUrl: 'https://github.com/username/portfolio',
    liveUrl: 'https://portfolio.example.com',
    featured: true
  },
  {
    id: 2,
    title: 'E-commerce Platform',
    description: 'A full-featured e-commerce platform with product management and payment processing',
    image: 'https://placehold.co/600x400/3498db/FFFFFF/png?text=E-commerce+Platform',
    technologies: ['React', 'Node.js', 'MongoDB'],
    githubUrl: 'https://github.com/username/ecommerce',
    liveUrl: 'https://ecommerce.example.com',
    featured: true
  },
  {
    id: 3,
    title: 'Task Management App',
    description: 'A task management application with drag-and-drop functionality',
    image: 'https://placehold.co/600x400/9b59b6/FFFFFF/png?text=Task+Management',
    technologies: ['Angular', 'Firebase', 'TypeScript'],
    githubUrl: 'https://github.com/username/taskmanager',
    liveUrl: 'https://taskmanager.example.com',
    featured: false
  },
  {
    id: 4,
    title: 'Weather Dashboard',
    description: 'A weather dashboard that displays current and forecasted weather data',
    image: 'https://placehold.co/600x400/e74c3c/FFFFFF/png?text=Weather+Dashboard',
    technologies: ['JavaScript', 'OpenWeather API', 'Chart.js'],
    githubUrl: 'https://github.com/username/weather',
    liveUrl: 'https://weather.example.com',
    featured: false
  }
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
                <BreadcrumbLink href="/dashboard">
                  Dashboard
                </BreadcrumbLink>
              </BreadcrumbItem>
              <BreadcrumbSeparator class="hidden md:block" />
              <BreadcrumbItem>
                <BreadcrumbPage>Projects</BreadcrumbPage>
              </BreadcrumbItem>
            </BreadcrumbList>
          </Breadcrumb>
        </div>
      </header>
      <div class="flex flex-1 flex-col p-4 pt-0 overflow-x-hidden">
        <div class="container mx-auto px-4 max-w-full">
          <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 sm:gap-0 mb-6">
            <h1 class="text-2xl font-bold">Projects</h1>
            <div class="flex flex-col sm:flex-row gap-3 w-full sm:w-auto">
              <div class="relative w-full sm:w-auto">
                <Search class="absolute left-2.5 top-2.5 h-4 w-4 text-gray-500" />
                <input
                  type="text"
                  placeholder="Search projects..."
                  class="w-full sm:w-auto pl-9 pr-4 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
              <Button class="gap-2 w-full sm:w-auto justify-center">
                <Plus class="h-4 w-4" />
                <span>Add Project</span>
              </Button>
            </div>
          </div>

          <!-- Featured Projects -->
          <div class="mb-8">
            <h2 class="text-xl font-semibold mb-4">Featured Projects</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <Card v-for="project in projects.filter(p => p.featured)" :key="project.id" class="overflow-hidden">
                <img :src="project.image" :alt="project.title" class="w-full h-48 object-cover" />
                <CardHeader>
                  <CardTitle>{{ project.title }}</CardTitle>
                  <CardDescription>{{ project.description }}</CardDescription>
                </CardHeader>
                <CardContent>
                  <div class="flex flex-wrap gap-2 mb-4">
                    <span
                      v-for="tech in project.technologies"
                      :key="tech"
                      class="px-2 py-1 bg-blue-100 text-blue-700 rounded-md text-xs font-medium flex items-center gap-1"
                    >
                      <Code2 class="h-3 w-3" />
                      {{ tech }}
                    </span>
                  </div>
                </CardContent>
                <CardFooter class="flex justify-between">
                  <Button variant="outline" size="sm" class="gap-1">
                    <Github class="h-4 w-4" />
                    <span>GitHub</span>
                  </Button>
                  <Button size="sm" class="gap-1">
                    <ExternalLink class="h-4 w-4" />
                    <span>View Live</span>
                  </Button>
                </CardFooter>
              </Card>
            </div>
          </div>

          <!-- All Projects -->
          <div>
            <h2 class="text-xl font-semibold mb-4">All Projects</h2>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
              <Card v-for="project in projects" :key="project.id" class="overflow-hidden">
                <CardHeader>
                  <CardTitle>{{ project.title }}</CardTitle>
                  <CardDescription>{{ project.description }}</CardDescription>
                </CardHeader>
                <CardContent>
                  <div class="flex flex-wrap gap-2">
                    <span
                      v-for="tech in project.technologies"
                      :key="tech"
                      class="px-2 py-1 bg-gray-100 text-gray-700 rounded-md text-xs font-medium"
                    >
                      {{ tech }}
                    </span>
                  </div>
                </CardContent>
                <CardFooter class="flex justify-end gap-2">
                  <Button variant="ghost" size="sm" class="h-8 w-8 p-0">
                    <Github class="h-4 w-4" />
                  </Button>
                  <Button variant="ghost" size="sm" class="h-8 w-8 p-0">
                    <ExternalLink class="h-4 w-4" />
                  </Button>
                </CardFooter>
              </Card>
            </div>
          </div>
        </div>
      </div>
    </SidebarInset>
  </SidebarProvider>
</template>
