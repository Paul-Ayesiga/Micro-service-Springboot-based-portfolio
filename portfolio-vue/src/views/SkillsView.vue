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
  Code2,
  Database,
  Edit,
  Layers,
  Lightbulb,
  Plus,
  Search,
  Server,
  Smartphone
} from 'lucide-vue-next';
import { ref } from 'vue';

// Sample data for skills categories
const categories = ref([
  {
    id: 1,
    name: 'Frontend Development',
    icon: Layers,
    color: 'bg-blue-100 text-blue-600'
  },
  {
    id: 2,
    name: 'Backend Development',
    icon: Server,
    color: 'bg-purple-100 text-purple-600'
  },
  {
    id: 3,
    name: 'Database',
    icon: Database,
    color: 'bg-green-100 text-green-600'
  },
  {
    id: 4,
    name: 'Mobile Development',
    icon: Smartphone,
    color: 'bg-amber-100 text-amber-600'
  }
]);

// Sample data for skills
const skills = ref([
  {
    id: 1,
    name: 'JavaScript',
    category: 'Frontend Development',
    proficiency: 90,
    yearsExperience: 5,
    icon: 'https://placehold.co/100/f7df1e/000000/png?text=JS'
  },
  {
    id: 2,
    name: 'Vue.js',
    category: 'Frontend Development',
    proficiency: 85,
    yearsExperience: 3,
    icon: 'https://placehold.co/100/42b883/ffffff/png?text=Vue'
  },
  {
    id: 3,
    name: 'React',
    category: 'Frontend Development',
    proficiency: 80,
    yearsExperience: 4,
    icon: 'https://placehold.co/100/61dafb/ffffff/png?text=React'
  },
  {
    id: 4,
    name: 'Java',
    category: 'Backend Development',
    proficiency: 85,
    yearsExperience: 6,
    icon: 'https://placehold.co/100/007396/ffffff/png?text=Java'
  },
  {
    id: 5,
    name: 'Spring Boot',
    category: 'Backend Development',
    proficiency: 80,
    yearsExperience: 4,
    icon: 'https://placehold.co/100/6db33f/ffffff/png?text=Spring'
  },
  {
    id: 6,
    name: 'PostgreSQL',
    category: 'Database',
    proficiency: 75,
    yearsExperience: 5,
    icon: 'https://placehold.co/100/336791/ffffff/png?text=SQL'
  },
  {
    id: 7,
    name: 'MongoDB',
    category: 'Database',
    proficiency: 70,
    yearsExperience: 3,
    icon: 'https://placehold.co/100/4db33d/ffffff/png?text=Mongo'
  },
  {
    id: 8,
    name: 'Flutter',
    category: 'Mobile Development',
    proficiency: 65,
    yearsExperience: 2,
    icon: 'https://placehold.co/100/02569b/ffffff/png?text=Flutter'
  }
]);

// Function to get skills by category
const getSkillsByCategory = (categoryName) => {
  return skills.value.filter(skill => skill.category === categoryName);
};
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
                <BreadcrumbPage>Skills</BreadcrumbPage>
              </BreadcrumbItem>
            </BreadcrumbList>
          </Breadcrumb>
        </div>
      </header>
      <div class="flex flex-1 flex-col p-4 pt-0 overflow-x-hidden">
        <div class="container mx-auto px-4 max-w-full">
          <div class="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 sm:gap-0 mb-6">
            <h1 class="text-2xl font-bold">Skills</h1>
            <div class="flex flex-col sm:flex-row gap-3 w-full sm:w-auto">
              <div class="relative w-full sm:w-auto">
                <Search class="absolute left-2.5 top-2.5 h-4 w-4 text-gray-500" />
                <input
                  type="text"
                  placeholder="Search skills..."
                  class="w-full sm:w-auto pl-9 pr-4 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
              <Button class="gap-2 w-full sm:w-auto justify-center">
                <Plus class="h-4 w-4" />
                <span>Add Skill</span>
              </Button>
            </div>
          </div>

          <!-- Skill Categories -->
          <div class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
            <Card
              v-for="category in categories"
              :key="category.id"
              class="cursor-pointer hover:shadow-md transition-all"
            >
              <CardContent class="flex items-center p-6">
                <div :class="`p-3 rounded-full mr-4 ${category.color}`">
                  <component :is="category.icon" class="h-6 w-6" />
                </div>
                <div>
                  <p class="font-medium">{{ category.name }}</p>
                  <p class="text-sm text-gray-500">{{ getSkillsByCategory(category.name).length }} skills</p>
                </div>
              </CardContent>
            </Card>
          </div>

          <!-- Skills by Category -->
          <div v-for="category in categories" :key="`section-${category.id}`" class="mb-8">
            <h2 class="text-xl font-semibold mb-4 flex items-center">
              <component :is="category.icon" :class="`h-5 w-5 mr-2 ${category.color.split(' ')[1]}`" />
              {{ category.name }}
            </h2>

            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              <Card v-for="skill in getSkillsByCategory(category.name)" :key="skill.id" class="overflow-hidden">
                <CardContent class="p-0">
                  <div class="flex items-center p-4">
                    <img :src="skill.icon" :alt="skill.name" class="w-10 h-10 rounded mr-3" />
                    <div class="flex-1">
                      <h3 class="font-medium">{{ skill.name }}</h3>
                      <div class="flex items-center text-sm text-gray-500">
                        <span>{{ skill.yearsExperience }} years</span>
                      </div>
                    </div>
                    <Button variant="ghost" size="sm" class="h-8 w-8 p-0">
                      <Edit class="h-4 w-4" />
                    </Button>
                  </div>

                  <div class="px-4 pb-4">
                    <div class="flex justify-between text-xs mb-1">
                      <span>Proficiency</span>
                      <span>{{ skill.proficiency }}%</span>
                    </div>
                    <div class="w-full bg-gray-200 rounded-full h-2">
                      <div
                        class="h-2 rounded-full"
                        :class="category.color.split(' ')[0]"
                        :style="`width: ${skill.proficiency}%`"
                      ></div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>
          </div>
        </div>
      </div>
    </SidebarInset>
  </SidebarProvider>
</template>
