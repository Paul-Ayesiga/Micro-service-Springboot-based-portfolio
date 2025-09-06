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
  Building2,
  Calendar,
  Edit,
  MapPin,
  Plus,
  Search,
  Trash2
} from 'lucide-vue-next';
import { ref } from 'vue';

// Sample data for experience
const experiences = ref([
  {
    id: 1,
    company: 'Tech Innovations Inc.',
    position: 'Senior Software Engineer',
    location: 'San Francisco, CA',
    startDate: 'Jan 2020',
    endDate: 'Present',
    current: true,
    description: 'Led the development of a microservices-based e-commerce platform using Spring Boot and Vue.js. Implemented CI/CD pipelines and mentored junior developers.',
    responsibilities: [
      'Architected and developed microservices using Spring Boot',
      'Built responsive frontend interfaces with Vue.js',
      'Implemented automated testing and CI/CD pipelines',
      'Mentored junior developers and conducted code reviews'
    ],
    technologies: ['Java', 'Spring Boot', 'Vue.js', 'PostgreSQL', 'Docker', 'Jenkins'],
    logo: 'https://placehold.co/100/3498db/ffffff/png?text=TI'
  },
  {
    id: 2,
    company: 'DataSphere Solutions',
    position: 'Full Stack Developer',
    location: 'Boston, MA',
    startDate: 'Mar 2017',
    endDate: 'Dec 2019',
    current: false,
    description: 'Developed and maintained web applications for data analytics and visualization. Worked with React, Node.js, and MongoDB.',
    responsibilities: [
      'Developed full-stack web applications using React and Node.js',
      'Designed and implemented RESTful APIs',
      'Created data visualization dashboards using D3.js',
      'Collaborated with data scientists to implement machine learning models'
    ],
    technologies: ['JavaScript', 'React', 'Node.js', 'MongoDB', 'D3.js', 'AWS'],
    logo: 'https://placehold.co/100/9b59b6/ffffff/png?text=DS'
  },
  {
    id: 3,
    company: 'WebFront Systems',
    position: 'Frontend Developer',
    location: 'Seattle, WA',
    startDate: 'Jun 2015',
    endDate: 'Feb 2017',
    current: false,
    description: 'Designed and implemented user interfaces for web applications. Focused on responsive design and accessibility.',
    responsibilities: [
      'Developed responsive web interfaces using HTML, CSS, and JavaScript',
      'Implemented designs using Angular and Bootstrap',
      'Ensured web accessibility compliance',
      'Optimized frontend performance'
    ],
    technologies: ['HTML', 'CSS', 'JavaScript', 'Angular', 'Bootstrap', 'SASS'],
    logo: 'https://placehold.co/100/e74c3c/ffffff/png?text=WF'
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
                <BreadcrumbPage>Experience</BreadcrumbPage>
              </BreadcrumbItem>
            </BreadcrumbList>
          </Breadcrumb>
        </div>
      </header>
      <div class="flex flex-1 flex-col p-4 pt-0 overflow-x-hidden">
        <div class="container mx-auto px-4 max-w-full">
          <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 sm:gap-0 mb-6">
            <h1 class="text-2xl font-bold">Professional Experience</h1>
            <Button class="gap-2 w-full sm:w-auto justify-center">
              <Plus class="h-4 w-4" />
              <span>Add Experience</span>
            </Button>
          </div>

          <!-- Experience Timeline -->
          <div class="relative border-l-2 border-gray-200 ml-6 pl-8 mb-8">
            <div v-for="(experience, index) in experiences" :key="experience.id" class="mb-12 relative">
              <!-- Timeline dot -->
              <div class="absolute -left-[3.25rem] top-0 w-10 h-10 rounded-full flex items-center justify-center" :class="experience.current ? 'bg-blue-100' : 'bg-gray-100'">
                <img :src="experience.logo" :alt="experience.company" class="w-8 h-8 rounded-full" />
              </div>

              <!-- Experience card -->
              <Card class="w-full">
                <CardHeader>
                  <div class="flex justify-between">
                    <div>
                      <CardTitle class="text-xl">{{ experience.position }}</CardTitle>
                      <CardDescription class="flex items-center mt-1">
                        <Building2 class="h-4 w-4 mr-1" />
                        {{ experience.company }}
                      </CardDescription>
                    </div>
                    <div class="flex gap-2">
                      <Button variant="ghost" size="sm" class="h-8 w-8 p-0">
                        <Edit class="h-4 w-4" />
                      </Button>
                      <Button variant="ghost" size="sm" class="h-8 w-8 p-0 text-red-500 hover:text-red-600 hover:bg-red-50">
                        <Trash2 class="h-4 w-4" />
                      </Button>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <div class="flex flex-col md:flex-row md:justify-between mb-4">
                    <div class="flex items-center text-gray-600 mb-2 md:mb-0">
                      <MapPin class="h-4 w-4 mr-1" />
                      <span>{{ experience.location }}</span>
                    </div>
                    <div class="flex items-center text-gray-600">
                      <Calendar class="h-4 w-4 mr-1" />
                      <span>{{ experience.startDate }} - {{ experience.endDate }}</span>
                      <span v-if="experience.current" class="ml-2 px-2 py-0.5 bg-blue-100 text-blue-700 rounded-full text-xs font-medium">Current</span>
                    </div>
                  </div>

                  <p class="text-gray-700 mb-4">{{ experience.description }}</p>

                  <div class="mb-4">
                    <h4 class="font-medium mb-2">Key Responsibilities:</h4>
                    <ul class="list-disc pl-5 space-y-1">
                      <li v-for="(responsibility, i) in experience.responsibilities" :key="i" class="text-gray-700">
                        {{ responsibility }}
                      </li>
                    </ul>
                  </div>

                  <div class="flex flex-wrap gap-2">
                    <span
                      v-for="(tech, i) in experience.technologies"
                      :key="i"
                      class="px-2 py-1 bg-gray-100 text-gray-700 rounded-md text-xs font-medium"
                    >
                      {{ tech }}
                    </span>
                  </div>
                </CardContent>
              </Card>

              <!-- Connector line to next item -->
              <div v-if="index < experiences.length - 1" class="absolute h-12 w-0.5 bg-gray-200 -left-[2.75rem] top-full"></div>
            </div>
          </div>
        </div>
      </div>
    </SidebarInset>
  </SidebarProvider>
</template>
