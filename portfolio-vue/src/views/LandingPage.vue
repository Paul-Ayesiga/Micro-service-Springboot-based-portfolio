<script setup>
/**
 * LandingPage.vue
 *
 * This component renders the main landing page of the portfolio website.
 * It includes sections for hero, about, skills, projects, experience, and contact.
 */

// Vue core imports
import { ref, onMounted, watch, onUnmounted, computed } from 'vue';

// Component imports
import { Button } from '@/components/ui/button';
import UserProfile from '@/components/auth/UserProfile.vue';
import DecorativeShapes from '@/components/ui/decorative-shapes.vue';
import {
  Avatar,
  AvatarFallback,
  AvatarImage,
} from '@/components/ui/avatar';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
  CommandSeparator,
  CommandShortcut,
} from '@/components/ui/command';
import {
  Card,
  CardContent,
} from '@/components/ui/card';
import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/carousel';
import Autoplay from 'embla-carousel-autoplay';

// Store imports
import { useAuthStore } from '@/stores/auth';

// Icon imports
import {
  ArrowUp,
  Briefcase,
  Building2,
  Calculator,
  Calendar,
  Code2,
  CreditCard,
  Download,
  ExternalLink,
  FileText,
  Github,
  Linkedin,
  LogOut,
  Mail,
  MapPin,
  Search,
  Settings,
  Smile,
  Twitter,
  User,
  UserCog
} from 'lucide-vue-next';

// ===== Authentication & Navigation =====
const authStore = useAuthStore();

/**
 * Computed property to get user initials for avatar
 */
const userInitials = computed(() => {
  if (!authStore.user) return '';

  const firstName = authStore.user.firstName || '';
  const lastName = authStore.user.lastName || '';

  if (firstName && lastName) {
    return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
  } else if (authStore.user.username) {
    return authStore.user.username.charAt(0).toUpperCase();
  } else {
    return 'U';
  }
});

/**
 * Handles user logout
 */
const logout = async () => {
  await authStore.logout();
};

/**
 * Handles mobile menu logout (closes menu and logs out)
 */
const mobileLogout = async () => {
  isMenuOpen.value = false;
  await authStore.logout();
};

// ===== Mobile Menu Handling =====
const isMenuOpen = ref(false);

// Watch for menu state changes to toggle body class for preventing scroll
watch(isMenuOpen, (newVal) => {
  if (newVal) {
    document.body.classList.add('menu-open');
  } else {
    document.body.classList.remove('menu-open');
  }
});

// ===== Section Navigation =====
// Track active section for navigation highlighting
const activeSection = ref('hero');
const SECTION_IDS = ['hero', 'about', 'skills', 'projects', 'experience', 'contact'];

// Back to top button visibility
const showBackToTop = ref(false);

/**
 * Determines which section is currently in view and updates activeSection
 * Also controls the visibility of the back-to-top button
 */
const checkActiveSection = () => {
  // Show back to top button when scrolled down 500px
  showBackToTop.value = window.scrollY > 500;

  for (const section of SECTION_IDS) {
    const element = document.getElementById(section);
    if (element) {
      const rect = element.getBoundingClientRect();
      // Consider a section in view if its top is within the top half of the viewport
      if (rect.top <= window.innerHeight / 2 && rect.bottom >= window.innerHeight / 2) {
        activeSection.value = section;
        break;
      }
    }
  }
};

/**
 * Scrolls to the top of the page smoothly
 */
const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });
};

// ===== Command Palette =====
// Command palette state
const isCommandOpen = ref(false);

/**
 * Opens the command palette
 */
const openCommandPalette = () => {
  isCommandOpen.value = true;
};

/**
 * Handles command selection
 * @param {string} command - The selected command
 */
const handleCommand = (command) => {
  switch (command) {
    case 'projects':
      document.getElementById('projects')?.scrollIntoView({ behavior: 'smooth' });
      break;
    case 'experience':
      document.getElementById('experience')?.scrollIntoView({ behavior: 'smooth' });
      break;
    case 'contact':
      document.getElementById('contact')?.scrollIntoView({ behavior: 'smooth' });
      break;
    case 'about':
      document.getElementById('about')?.scrollIntoView({ behavior: 'smooth' });
      break;
    case 'skills':
      document.getElementById('skills')?.scrollIntoView({ behavior: 'smooth' });
      break;
    case 'dashboard':
      if (authStore.isAdmin) {
        window.location.href = '/dashboard';
      }
      break;
    case 'settings':
      window.location.href = '/settings';
      break;
    case 'resume':
      // Download resume logic would go here
      break;
    default:
      break;
  }
  isCommandOpen.value = false;
};

// ===== Project Filtering =====
// Project categories for filtering
const projectCategories = ref([
  { id: 'all', name: 'All Projects', active: true },
  { id: 'frontend', name: 'Frontend', active: false },
  { id: 'backend', name: 'Backend', active: false },
  { id: 'fullstack', name: 'Full Stack', active: false },
  { id: 'mobile', name: 'Mobile', active: false }
]);

// Active filter state
const activeFilter = ref('all');
// Previous filter for animation
const previousFilter = ref('all');
// Animation state for projects
const isChangingFilter = ref(false);

/**
 * Sets the active project filter and updates category states
 * @param {string} filterId - The ID of the filter to activate
 */
const setFilter = (filterId) => {
  if (filterId === activeFilter.value) return;

  previousFilter.value = activeFilter.value;
  isChangingFilter.value = true;

  // Update filter after a short delay for animation
  setTimeout(() => {
    activeFilter.value = filterId;
    projectCategories.value.forEach(cat => {
      cat.active = cat.id === filterId;
    });

    // Reset animation state after filter change
    setTimeout(() => {
      isChangingFilter.value = false;
    }, 300);
  }, 300);
};

/**
 * Computed property that returns filtered projects based on active filter
 */
const filteredProjects = computed(() => {
  if (activeFilter.value === 'all') {
    return portfolio.value.featuredProjects;
  }
  return portfolio.value.featuredProjects.filter(project =>
    project.category === activeFilter.value
  );
});

// ===== Lifecycle Hooks =====

/**
 * Handles keyboard shortcuts
 * @param {KeyboardEvent} event - The keyboard event
 */
const handleKeyDown = (event) => {
  // Command/Ctrl + K to open command palette
  if ((event.metaKey || event.ctrlKey) && event.key === 'k') {
    event.preventDefault();
    openCommandPalette();
  }
};

/**
 * Component mounted lifecycle hook
 * Sets up event listeners and initializes component state
 */
onMounted(() => {
  // Add scroll event listener
  window.addEventListener('scroll', checkActiveSection, { passive: true });
  // Add keyboard shortcut listener
  window.addEventListener('keydown', handleKeyDown);
  // Initial check
  checkActiveSection();
  // Start typing effect
  startTypingEffect();
});

/**
 * Component unmounted lifecycle hook
 * Cleans up event listeners and intervals
 */
onUnmounted(() => {
  // Clean up scroll event listener
  window.removeEventListener('scroll', checkActiveSection);
  // Clean up keyboard shortcut listener
  window.removeEventListener('keydown', handleKeyDown);
  // Clean up menu class
  document.body.classList.remove('menu-open');
  // Clean up typing interval
  if (typingInterval) {
    clearInterval(typingInterval);
    typingInterval = null;
  }
});

// ===== Portfolio Data =====

/**
 * Portfolio data containing personal information, skills, projects, and experience
 */
const portfolio = ref({
  name: "AYESIGA PAUL",
  title: "Full Stack Developer",
  location: "Kampala, Uganda",
  about: "I'm a passionate full-stack developer with expertise in Vue.js, Spring Boot, and cloud technologies. I love building scalable applications and solving complex problems.",
  skills: [
    { name: "Vue.js", level: 90 },
    { name: "Spring Boot", level: 85 },
    { name: "JavaScript", level: 92 },
    { name: "Java", level: 88 },
    { name: "PostgreSQL", level: 80 },
    { name: "Docker", level: 75 }
  ],
  featuredProjects: [
    {
      title: "E-commerce Platform",
      description: "A full-featured e-commerce platform with product management and payment processing",
      image: "https://placehold.co/800x600/3498db/FFFFFF/png?text=E-commerce+Platform",
      technologies: ["Vue.js", "Spring Boot", "PostgreSQL"],
      github: "https://github.com/username/ecommerce",
      demo: "https://ecommerce.example.com",
      category: "fullstack"
    },
    {
      title: "Task Management App",
      description: "A task management application with drag-and-drop functionality",
      image: "https://placehold.co/800x600/9b59b6/FFFFFF/png?text=Task+Management",
      technologies: ["Vue.js", "Firebase", "Tailwind CSS"],
      github: "https://github.com/username/taskmanager",
      demo: "https://taskmanager.example.com",
      category: "frontend"
    },
    {
      title: "Portfolio Website",
      description: "A personal portfolio website built with Vue.js and Spring Boot",
      image: "https://placehold.co/800x600/2c3e50/FFFFFF/png?text=Portfolio+Website",
      technologies: ["Vue.js", "Spring Boot", "Tailwind CSS"],
      github: "https://github.com/username/portfolio",
      demo: "https://portfolio.example.com",
      category: "fullstack"
    },
    {
      title: "Weather Dashboard",
      description: "A weather dashboard that displays current and forecasted weather data",
      image: "https://placehold.co/800x600/e74c3c/FFFFFF/png?text=Weather+Dashboard",
      technologies: ["JavaScript", "OpenWeather API", "Chart.js"],
      github: "https://github.com/username/weather",
      demo: "https://weather.example.com",
      category: "frontend"
    },
    {
      title: "API Gateway Service",
      description: "A microservice API gateway for routing and authentication",
      image: "https://placehold.co/800x600/27ae60/FFFFFF/png?text=API+Gateway",
      technologies: ["Java", "Spring Cloud", "JWT"],
      github: "https://github.com/username/api-gateway",
      demo: "https://api.example.com",
      category: "backend"
    },
    {
      title: "Mobile Fitness App",
      description: "A cross-platform mobile app for tracking workouts and nutrition",
      image: "https://placehold.co/800x600/f39c12/FFFFFF/png?text=Fitness+App",
      technologies: ["React Native", "Firebase", "Redux"],
      github: "https://github.com/username/fitness-app",
      demo: "https://fitness.example.com",
      category: "mobile"
    }
  ],
  experience: [
    {
      id: 1,
      company: "Tech Innovations Inc.",
      position: "Senior Software Engineer",
      location: "Kampala, Uganda",
      startDate: "Jan 2020",
      endDate: "Present",
      period: "2020 - Present",
      current: true,
      description: "Led the development of microservices-based applications using Spring Boot and Vue.js.",
      responsibilities: [
        "Architected and developed microservices using Spring Boot",
        "Built responsive frontend interfaces with Vue.js",
        "Implemented automated testing and CI/CD pipelines",
        "Mentored junior developers and conducted code reviews"
      ],
      technologies: ["Java", "Spring Boot", "Vue.js", "Docker", "Kubernetes", "Jenkins"],
      logo: "https://placehold.co/100/3498db/ffffff/png?text=TI"
    },
    {
      id: 2,
      company: "DataSphere Solutions",
      position: "Full Stack Developer",
      location: "Boston, MA",
      startDate: "Mar 2017",
      endDate: "Dec 2019",
      period: "2017 - 2020",
      current: false,
      description: "Developed web applications for data analytics and visualization using React and Node.js.",
      responsibilities: [
        "Developed full-stack web applications using React and Node.js",
        "Designed and implemented RESTful APIs",
        "Created data visualization dashboards using D3.js",
        "Collaborated with data scientists to implement machine learning models"
      ],
      technologies: ["JavaScript", "React", "Node.js", "MongoDB", "D3.js", "AWS"],
      logo: "https://placehold.co/100/9b59b6/ffffff/png?text=DS"
    },
    {
      id: 3,
      company: "WebFront Systems",
      position: "Frontend Developer",
      location: "Seattle, WA",
      startDate: "Jun 2015",
      endDate: "Feb 2017",
      period: "2015 - 2017",
      current: false,
      description: "Designed and implemented user interfaces for web applications with a focus on responsive design.",
      responsibilities: [
        "Developed responsive web interfaces using HTML, CSS, and JavaScript",
        "Implemented designs using Angular and Bootstrap",
        "Ensured web accessibility compliance",
        "Optimized frontend performance"
      ],
      technologies: ["HTML", "CSS", "JavaScript", "Angular", "Bootstrap", "SASS"],
      logo: "https://placehold.co/100/e74c3c/ffffff/png?text=WF"
    }
  ],
  social: {
    github: "https://github.com/username",
    linkedin: "https://linkedin.com/in/username",
    twitter: "https://twitter.com/username",
    email: "contact@example.com"
  }
});

// ===== Typing Effect =====

// Constants for typing effect
const TYPING_SPEED = 100; // milliseconds between characters
const TITLE_TEXT = "Full Stack Developer";

// State for typing effect
const displayedTitle = ref('');
const currentIndex = ref(0);
let typingInterval = null;

/**
 * Starts or restarts the typing effect animation
 * Clears any existing interval, resets state, and begins typing
 */
const startTypingEffect = () => {
  // Clear any existing interval to prevent memory leaks
  if (typingInterval) {
    clearInterval(typingInterval);
  }

  // Reset values
  displayedTitle.value = '';
  currentIndex.value = 0;

  // Start new typing interval
  typingInterval = setInterval(() => {
    if (currentIndex.value < TITLE_TEXT.length) {
      displayedTitle.value += TITLE_TEXT.charAt(currentIndex.value);
      currentIndex.value++;
    } else {
      clearInterval(typingInterval);
      typingInterval = null;
    }
  }, TYPING_SPEED);
};
</script>

<template>
  <!-- Command Palette -->
  <Command v-if="isCommandOpen" class="fixed inset-0 z-[100] bg-black/50 flex items-center justify-center p-4" @keydown.esc="isCommandOpen = false">
    <div class="bg-white dark:bg-gray-800 rounded-lg shadow-xl max-w-[450px] w-full overflow-hidden" @click.stop>
      <CommandInput placeholder="Type a command or search..." />
      <CommandList>
        <CommandEmpty>No results found.</CommandEmpty>
        <CommandGroup heading="Navigation">
          <CommandItem value="about" @select="handleCommand('about')">
            <User class="h-4 w-4 mr-2" />
            <span>About</span>
            <CommandShortcut>A</CommandShortcut>
          </CommandItem>
          <CommandItem value="skills" @select="handleCommand('skills')">
            <Code2 class="h-4 w-4 mr-2" />
            <span>Skills</span>
            <CommandShortcut>S</CommandShortcut>
          </CommandItem>
          <CommandItem value="projects" @select="handleCommand('projects')">
            <Briefcase class="h-4 w-4 mr-2" />
            <span>Projects</span>
            <CommandShortcut>P</CommandShortcut>
          </CommandItem>
          <CommandItem value="experience" @select="handleCommand('experience')">
            <Calendar class="h-4 w-4 mr-2" />
            <span>Experience</span>
            <CommandShortcut>E</CommandShortcut>
          </CommandItem>
          <CommandItem value="contact" @select="handleCommand('contact')">
            <Mail class="h-4 w-4 mr-2" />
            <span>Contact</span>
            <CommandShortcut>C</CommandShortcut>
          </CommandItem>
        </CommandGroup>
        <CommandSeparator />
        <CommandGroup heading="Actions">
          <CommandItem value="resume" @select="handleCommand('resume')">
            <FileText class="h-4 w-4 mr-2" />
            <span>Download Resume</span>
            <CommandShortcut>R</CommandShortcut>
          </CommandItem>
          <CommandItem v-if="authStore.isAuthenticated" value="settings" @select="handleCommand('settings')">
            <Settings class="h-4 w-4 mr-2" />
            <span>Settings</span>
            <CommandShortcut>⌘S</CommandShortcut>
          </CommandItem>
          <CommandItem v-if="authStore.isAdmin" value="dashboard" @select="handleCommand('dashboard')">
            <UserCog class="h-4 w-4 mr-2" />
            <span>Dashboard</span>
            <CommandShortcut>⌘D</CommandShortcut>
          </CommandItem>
        </CommandGroup>
      </CommandList>
    </div>
  </Command>

  <!-- Main container with responsive design and dark mode support -->
  <div class="min-h-screen bg-gradient-to-b from-gray-50 to-gray-100 dark:from-gray-900 dark:to-gray-800 overflow-x-hidden">

    <!-- Back to top button -->
    <transition
      enter-active-class="transition duration-300 ease-out"
      enter-from-class="transform opacity-0 scale-95"
      enter-to-class="transform opacity-100 scale-100"
      leave-active-class="transition duration-200 ease-in"
      leave-from-class="transform opacity-100 scale-100"
      leave-to-class="transform opacity-0 scale-95"
    >
      <button
        v-show="showBackToTop"
        @click="scrollToTop"
        class="fixed bottom-6 right-6 z-50 p-3 rounded-full bg-orange-500 dark:bg-orange-600 text-white shadow-lg hover:bg-orange-600 dark:hover:bg-orange-700 transition-all duration-300 hover:shadow-xl focus:outline-none focus:ring-2 focus:ring-orange-400 focus:ring-offset-2 dark:focus:ring-offset-gray-900"
        aria-label="Back to top"
      >
        <ArrowUp class="h-6 w-6" />
      </button>
    </transition>
    <!-- Skip to content link for accessibility -->
    <a href="#main-content" class="sr-only focus:not-sr-only focus:absolute focus:top-4 focus:left-4 focus:z-50 focus:px-4 focus:py-2 focus:bg-blue-600 focus:text-white focus:rounded">
      Skip to content
    </a>
    <!-- Navigation -->
    <nav class="bg-white/90 dark:bg-gray-900/90 backdrop-blur-lg shadow-md fixed w-full z-50 border-b border-gray-200/50 dark:border-gray-800/50">
      <div class="container mx-auto px-6 md:px-8 lg:px-12 max-w-7xl py-3 flex justify-between items-center">
        <div class="flex items-center">
          <router-link to="/" class="text-2xl font-bold text-blue-600 dark:text-blue-400">Portfolio</router-link>
        </div>

        <!-- Command Palette Button -->
        <button
          @click="openCommandPalette"
          class="hidden md:flex items-center px-3 py-1.5 text-sm text-gray-500 dark:text-gray-400 border border-gray-300 dark:border-gray-600 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
        >
          <span class="mr-2">Search or jump to...</span>
          <kbd class="px-1.5 py-0.5 text-xs font-semibold text-gray-500 dark:text-gray-400 bg-gray-100 dark:bg-gray-700 rounded">
            <span class="text-xs">⌘</span>K
          </kbd>
        </button>
        <div class="hidden md:flex items-center space-x-8">
          <a
            href="#about"
            class="relative py-1 transition-colors"
            :class="{
              'text-blue-600 dark:text-blue-400 font-medium': activeSection === 'about',
              'text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400': activeSection !== 'about'
            }"
          >
            About
            <span
              class="absolute bottom-0 left-0 w-full h-0.5 bg-blue-600 dark:bg-blue-400 transform transition-transform duration-300"
              :class="activeSection === 'about' ? 'scale-x-100' : 'scale-x-0'"
            ></span>
          </a>
          <a
            href="#skills"
            class="relative py-1 transition-colors"
            :class="{
              'text-blue-600 dark:text-blue-400 font-medium': activeSection === 'skills',
              'text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400': activeSection !== 'skills'
            }"
          >
            Skills
            <span
              class="absolute bottom-0 left-0 w-full h-0.5 bg-blue-600 dark:bg-blue-400 transform transition-transform duration-300"
              :class="activeSection === 'skills' ? 'scale-x-100' : 'scale-x-0'"
            ></span>
          </a>
          <a
            href="#projects"
            class="relative py-1 transition-colors"
            :class="{
              'text-blue-600 dark:text-blue-400 font-medium': activeSection === 'projects',
              'text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400': activeSection !== 'projects'
            }"
          >
            Projects
            <span
              class="absolute bottom-0 left-0 w-full h-0.5 bg-blue-600 dark:bg-blue-400 transform transition-transform duration-300"
              :class="activeSection === 'projects' ? 'scale-x-100' : 'scale-x-0'"
            ></span>
          </a>
          <a
            href="#experience"
            class="relative py-1 transition-colors"
            :class="{
              'text-blue-600 dark:text-blue-400 font-medium': activeSection === 'experience',
              'text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400': activeSection !== 'experience'
            }"
          >
            Experience
            <span
              class="absolute bottom-0 left-0 w-full h-0.5 bg-blue-600 dark:bg-blue-400 transform transition-transform duration-300"
              :class="activeSection === 'experience' ? 'scale-x-100' : 'scale-x-0'"
            ></span>
          </a>
          <a
            href="#contact"
            class="relative py-1 transition-colors"
            :class="{
              'text-blue-600 dark:text-blue-400 font-medium': activeSection === 'contact',
              'text-gray-600 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400': activeSection !== 'contact'
            }"
          >
            Contact
            <span
              class="absolute bottom-0 left-0 w-full h-0.5 bg-blue-600 dark:bg-blue-400 transform transition-transform duration-300"
              :class="activeSection === 'contact' ? 'scale-x-100' : 'scale-x-0'"
            ></span>
          </a>
        </div>

        <!-- Desktop auth buttons -->
        <div class="hidden md:flex md:items-center md:space-x-4">
          <template v-if="authStore.isAuthenticated">
            <DropdownMenu>
              <DropdownMenuTrigger as-child>
                <button class="flex items-center space-x-2 focus:outline-none">
                  <Avatar class="h-9 w-9 border-2 border-blue-100 dark:border-blue-900">
                    <AvatarImage v-if="authStore.user?.profileImageUrl" :src="authStore.user.profileImageUrl" :alt="authStore.user?.fullName || authStore.user?.username || 'User'" />
                    <AvatarFallback class="bg-blue-600 text-white font-medium">
                      {{ userInitials }}
                    </AvatarFallback>
                  </Avatar>
                </button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" class="w-56">
                <DropdownMenuLabel>
                  <div class="flex flex-col space-y-1">
                    <p class="text-sm font-medium">{{ authStore.user?.fullName || authStore.user?.username || 'User' }}</p>
                    <p class="text-xs text-gray-500 truncate">{{ authStore.user?.email || '' }}</p>
                  </div>
                </DropdownMenuLabel>
                <DropdownMenuSeparator />
                <router-link to="/settings" v-slot="{ href, navigate }">
                  <DropdownMenuItem :to="href" @click="navigate" class="cursor-pointer">
                    <Settings class="mr-2 h-4 w-4" />
                    <span>Settings</span>
                  </DropdownMenuItem>
                </router-link>
                <router-link to="/dashboard" v-if="authStore.isAdmin" v-slot="{ href, navigate }">
                  <DropdownMenuItem :to="href" @click="navigate" class="cursor-pointer">
                    <UserCog class="mr-2 h-4 w-4" />
                    <span>Dashboard</span>
                  </DropdownMenuItem>
                </router-link>
                <DropdownMenuSeparator />
                <DropdownMenuItem @click="logout" class="cursor-pointer">
                  <LogOut class="mr-2 h-4 w-4" />
                  <span>Sign out</span>
                </DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </template>
          <template v-else>
            <Button variant="outline" size="sm" class="mr-2">
              <Download class="h-4 w-4 mr-2" />
              Resume
            </Button>
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
    </nav>

    <!-- Mobile menu (outside nav to avoid height constraints) -->
    <div
      class="md:hidden fixed inset-0 z-100 transition-all duration-300 ease-in-out"
      :class="isMenuOpen ? 'visible' : 'invisible'"
    >
      <!-- Backdrop -->
      <div
        class="absolute inset-0 bg-gray-800/40 dark:bg-black/60 backdrop-blur-sm transition-opacity duration-300 ease-in-out"
        :class="isMenuOpen ? 'opacity-100' : 'opacity-0'"
        @click="isMenuOpen = false"
      ></div>
      <!-- Menu content -->
      <div
        class="absolute right-0 top-0 h-full w-4/5 max-w-sm bg-white dark:bg-gray-800 shadow-xl transform transition-all duration-300 ease-in-out border-l border-gray-200 dark:border-gray-700 overflow-y-auto flex flex-col pb-safe"
        :class="isMenuOpen ? 'translate-x-0' : 'translate-x-full'"
      >
        <!-- Mobile menu header with close button -->
        <div class="flex items-center justify-between p-4 border-b border-gray-200 dark:border-gray-700">
          <h3 class="text-lg font-semibold text-gray-800 dark:text-white">Menu</h3>
          <button
            @click="isMenuOpen = false"
            class="p-2 rounded-md text-gray-500 hover:text-gray-700 hover:bg-gray-100 dark:hover:bg-gray-700 dark:hover:text-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <svg
              class="h-5 w-5"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
            <span class="sr-only">Close menu</span>
          </button>
        </div>

        <div class="px-4 pt-4 pb-3 space-y-1">
          <a
            href="#about"
            class="block px-3 py-2 rounded-md text-base font-medium transition-colors relative overflow-hidden"
            :class="{
              'bg-blue-50 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400': activeSection === 'about',
              'text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 hover:text-gray-900 dark:hover:text-white': activeSection !== 'about'
            }"
            @click="isMenuOpen = false"
          >
            <span class="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 dark:bg-blue-400 transition-opacity duration-300"
              :class="{ 'opacity-100': activeSection === 'about', 'opacity-0': activeSection !== 'about' }"></span>
            <span class="relative">About</span>
          </a>
          <a
            href="#skills"
            class="block px-3 py-2 rounded-md text-base font-medium transition-colors relative overflow-hidden"
            :class="{
              'bg-blue-50 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400': activeSection === 'skills',
              'text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 hover:text-gray-900 dark:hover:text-white': activeSection !== 'skills'
            }"
            @click="isMenuOpen = false"
          >
            <span class="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 dark:bg-blue-400 transition-opacity duration-300"
              :class="{ 'opacity-100': activeSection === 'skills', 'opacity-0': activeSection !== 'skills' }"></span>
            <span class="relative">Skills</span>
          </a>
          <a
            href="#projects"
            class="block px-3 py-2 rounded-md text-base font-medium transition-colors relative overflow-hidden"
            :class="{
              'bg-blue-50 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400': activeSection === 'projects',
              'text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 hover:text-gray-900 dark:hover:text-white': activeSection !== 'projects'
            }"
            @click="isMenuOpen = false"
          >
            <span class="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 dark:bg-blue-400 transition-opacity duration-300"
              :class="{ 'opacity-100': activeSection === 'projects', 'opacity-0': activeSection !== 'projects' }"></span>
            <span class="relative">Projects</span>
          </a>
          <a
            href="#experience"
            class="block px-3 py-2 rounded-md text-base font-medium transition-colors relative overflow-hidden"
            :class="{
              'bg-blue-50 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400': activeSection === 'experience',
              'text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 hover:text-gray-900 dark:hover:text-white': activeSection !== 'experience'
            }"
            @click="isMenuOpen = false"
          >
            <span class="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 dark:bg-blue-400 transition-opacity duration-300"
              :class="{ 'opacity-100': activeSection === 'experience', 'opacity-0': activeSection !== 'experience' }"></span>
            <span class="relative">Experience</span>
          </a>
          <a
            href="#contact"
            class="block px-3 py-2 rounded-md text-base font-medium transition-colors relative overflow-hidden"
            :class="{
              'bg-blue-50 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400': activeSection === 'contact',
              'text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 hover:text-gray-900 dark:hover:text-white': activeSection !== 'contact'
            }"
            @click="isMenuOpen = false"
          >
            <span class="absolute left-0 top-0 bottom-0 w-1 bg-blue-600 dark:bg-blue-400 transition-opacity duration-300"
              :class="{ 'opacity-100': activeSection === 'contact', 'opacity-0': activeSection !== 'contact' }"></span>
            <span class="relative">Contact</span>
          </a>
        </div>

        <!-- Mobile auth buttons -->
        <div class="pt-4 pb-6 border-t border-gray-200 dark:border-gray-700">
          <!-- Authenticated user profile -->
          <div v-if="authStore.isAuthenticated" class="px-4 mb-4 flex items-center">
            <div class="flex-shrink-0 mr-3">
              <Avatar class="h-10 w-10 border-2 border-blue-100 dark:border-blue-900">
                <AvatarImage v-if="authStore.user?.profileImageUrl" :src="authStore.user.profileImageUrl" :alt="authStore.user?.fullName || authStore.user?.username || 'User'" />
                <AvatarFallback class="bg-blue-600 text-white font-medium">
                  {{ userInitials }}
                </AvatarFallback>
              </Avatar>
            </div>
            <div class="min-w-0 flex-1">
              <p class="font-medium text-gray-900 dark:text-white truncate">{{ authStore.user?.fullName || authStore.user?.username || 'User' }}</p>
              <p class="text-sm text-gray-500 dark:text-gray-400 truncate">{{ authStore.user?.email || '' }}</p>
            </div>
          </div>

          <!-- Auth buttons for authenticated users -->
          <div v-if="authStore.isAuthenticated" class="px-4 space-y-3">
            <router-link
              v-if="authStore.isAdmin"
              to="/dashboard"
              class="flex items-center justify-center w-full px-4 py-2.5 rounded-lg bg-blue-600 text-white font-medium hover:bg-blue-700 transition-colors"
              @click="isMenuOpen = false"
            >
              <UserCog class="h-5 w-5 mr-2" />
              Dashboard
            </router-link>
            <button
              @click="mobileLogout"
              class="flex items-center justify-center w-full px-4 py-2.5 rounded-lg border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-300 font-medium hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
            >
              <LogOut class="h-5 w-5 mr-2" />
              Sign out
            </button>
          </div>

          <!-- Auth buttons for guests -->
          <div v-else class="px-4 space-y-3">
            <a
              href="#"
              class="flex items-center justify-center w-full px-4 py-2.5 rounded-lg border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-300 font-medium hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
              @click="isMenuOpen = false"
            >
              <Download class="h-5 w-5 mr-2" />
              Download Resume
            </a>
            <div class="grid grid-cols-2 gap-3 mt-3">
              <router-link
                to="/login"
                class="flex items-center justify-center px-4 py-2.5 rounded-lg border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-300 font-medium hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors"
                @click="isMenuOpen = false"
              >
                Sign in
              </router-link>
              <router-link
                to="/register"
                class="flex items-center justify-center px-4 py-2.5 rounded-lg bg-blue-600 text-white font-medium hover:bg-blue-700 transition-colors"
                @click="isMenuOpen = false"
              >
                Sign up
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Hero Section -->
    <section id="hero" class="pb-20 px-6 md:px-8 lg:px-12 relative overflow-hidden" aria-labelledby="hero-heading">
      <!-- Main content anchor for skip link -->
      <span id="main-content" class="sr-only" aria-hidden="true"></span>
      <!-- Decorative background elements -->
      <DecorativeShapes />

      <!-- Hero content -->
      <div class="container mx-auto flex flex-col md:flex-row items-center max-w-7xl relative z-10">
        <div class="md:w-1/2 mb-10 md:mb-0">
          <h1 id="hero-heading" class="text-4xl md:text-5xl font-bold text-gray-800 dark:text-white mb-4">
            Hi, I'm {{ portfolio.name }}
          </h1>
          <h2 class="text-2xl md:text-3xl font-semibold text-blue-600 dark:text-blue-400 mb-6">
            <span>{{ displayedTitle }}</span>
            <span class="animate-blink" aria-hidden="true">|</span>
            <!-- Hidden text for screen readers to announce the full title immediately -->
            <span class="sr-only">{{ TITLE_TEXT }}</span>
          </h2>
          <div id="about">
            <p class="text-gray-600 dark:text-gray-300 text-lg mb-8 max-w-lg">
              {{ portfolio.about }}
            </p>
          </div>
          <div class="flex items-center text-gray-600 dark:text-gray-300 mb-8">
            <MapPin class="h-5 w-5 mr-2 text-blue-600 dark:text-blue-400" />
            <span>{{ portfolio.location }}</span>
          </div>
          <div class="flex space-x-4">
            <a :href="portfolio.social.github" target="_blank" class="p-2 bg-gray-200 dark:bg-gray-700 rounded-full hover:bg-blue-100 dark:hover:bg-blue-900 transition-colors">
              <Github class="h-5 w-5 text-gray-700 dark:text-gray-300" />
            </a>
            <a :href="portfolio.social.linkedin" target="_blank" class="p-2 bg-gray-200 dark:bg-gray-700 rounded-full hover:bg-blue-100 dark:hover:bg-blue-900 transition-colors">
              <Linkedin class="h-5 w-5 text-gray-700 dark:text-gray-300" />
            </a>
            <a :href="portfolio.social.twitter" target="_blank" class="p-2 bg-gray-200 dark:bg-gray-700 rounded-full hover:bg-blue-100 dark:hover:bg-blue-900 transition-colors">
              <Twitter class="h-5 w-5 text-gray-700 dark:text-gray-300" />
            </a>
            <a :href="`mailto:${portfolio.social.email}`" class="p-2 bg-gray-200 dark:bg-gray-700 rounded-full hover:bg-blue-100 dark:hover:bg-blue-900 transition-colors">
              <Mail class="h-5 w-5 text-gray-700 dark:text-gray-300" />
            </a>
          </div>
        </div>
        <div class="md:w-1/2 flex justify-center">
          <div class="relative">
            <div class="w-64 h-64 md:w-80 md:h-80 rounded-full overflow-hidden border-4 border-white dark:border-gray-800 shadow-lg">
              <img src="https://placehold.co/400x400/2c3e50/ffffff/png?text=JD" alt="Profile" class="w-full h-full object-cover" />
            </div>
            <div class="absolute -bottom-5 -right-5 bg-blue-600 text-white p-4 rounded-lg shadow-lg">
              <Briefcase class="h-8 w-8" />
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Skills Section -->
    <section id="skills" class="py-20 bg-gradient-to-b from-white to-gray-50 dark:from-gray-800 dark:to-gray-900 relative overflow-hidden">
      <!-- Decorative background elements -->
      <DecorativeShapes />

      <div class="container mx-auto px-6 md:px-8 lg:px-12 max-w-7xl relative z-10">
        <h2 class="text-3xl font-bold text-center text-gray-800 dark:text-white mb-12">
          My Skills
        </h2>

        <!-- Skills Carousel -->
        <Carousel
          class="w-full max-w-5xl mx-auto"
          :opts="{
            align: 'start',
            loop: true,
          }"
          :plugins="[Autoplay({
            delay: 2000,
          })]"
        >
          <CarouselContent class="-ml-1">
            <CarouselItem v-for="skill in portfolio.skills" :key="skill.name" class="pl-1 md:basis-1/2 lg:basis-1/3">
              <div class="p-1">
                <Card>
                  <CardContent class="flex aspect-square items-center justify-center p-6 bg-gradient-to-br from-orange-50 to-orange-100 dark:from-orange-900/20 dark:to-orange-800/20">
                    <div class="text-center w-full">
                      <div class="flex justify-center mb-4">
                        <div class="w-16 h-16 rounded-full bg-orange-100 dark:bg-orange-900/30 flex items-center justify-center">
                          <span class="text-xl font-bold text-orange-600 dark:text-orange-400">{{ skill.level }}%</span>
                        </div>
                      </div>
                      <h3 class="text-xl font-semibold text-gray-800 dark:text-white mb-3">{{ skill.name }}</h3>
                      <div class="w-full bg-gray-200 dark:bg-gray-600 rounded-full h-2.5 mt-2">
                        <div class="bg-orange-500 dark:bg-orange-400 h-2.5 rounded-full" :style="`width: ${skill.level}%`"></div>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </div>
            </CarouselItem>
          </CarouselContent>
          <CarouselPrevious />
          <CarouselNext />
        </Carousel>
      </div>
    </section>

    <!-- Featured Projects -->
    <section id="projects" class="py-20 bg-gradient-to-br from-gray-50 via-gray-50 to-blue-50 dark:from-gray-900 dark:via-gray-900 dark:to-blue-950 relative overflow-hidden">
      <!-- Decorative background elements -->
      <DecorativeShapes />

      <!-- Large semicircle decoration -->
      <div class="absolute -right-32 top-0 w-64 h-64 bg-blue-200/20 dark:bg-blue-800/10 rounded-full blur-2xl"></div>
      <div class="absolute -left-32 bottom-0 w-64 h-64 bg-purple-200/20 dark:bg-purple-800/10 rounded-full blur-2xl"></div>

      <div class="container mx-auto px-6 md:px-8 lg:px-12 max-w-7xl relative z-10">
        <h2 class="text-3xl font-bold text-center text-gray-800 dark:text-white mb-6">
          Featured Projects
        </h2>

        <!-- Project Filters -->
        <div class="flex flex-wrap justify-center mb-10">
          <div class="bg-white/80 dark:bg-gray-800/80 backdrop-blur-md rounded-full shadow-md p-1.5 flex flex-wrap justify-center border border-gray-100 dark:border-gray-700">
            <!-- Decorative dot in background -->
            <div class="absolute -z-10 w-32 h-32 bg-blue-400/10 dark:bg-blue-400/5 rounded-full blur-xl -translate-y-1/2 translate-x-1/4"></div>
            <div class="absolute -z-10 w-24 h-24 bg-purple-400/10 dark:bg-purple-400/5 rounded-full blur-xl translate-y-1/2 -translate-x-1/4"></div>
            <button
              v-for="category in projectCategories"
              :key="category.id"
              @click="setFilter(category.id)"
              class="px-4 py-2 text-sm font-medium rounded-full transition-all duration-300 whitespace-nowrap my-1 mx-1 relative overflow-hidden group"
              :class="{
                'bg-gradient-to-r from-blue-500 to-blue-600 text-white shadow-sm': category.active,
                'text-gray-700 dark:text-gray-300 hover:bg-gray-100/80 dark:hover:bg-gray-700/80': !category.active
              }"
            >
              <!-- Subtle shine effect for active buttons -->
              <span
                v-if="category.active"
                class="absolute inset-0 w-full h-full bg-gradient-to-r from-transparent via-white/20 to-transparent -translate-x-full group-hover:translate-x-full transition-transform duration-1000 ease-in-out"
              ></span>

              <span class="relative z-10">{{ category.name }}</span>
              <span
                v-if="category.id === 'all' && category.active"
                class="relative z-10 ml-1 text-xs bg-white/90 text-blue-600 rounded-full px-1.5 py-0.5 shadow-sm"
              >
                {{ portfolio.featuredProjects.length }}
              </span>
            </button>
          </div>
        </div>

        <!-- No projects message -->
        <div v-if="filteredProjects.length === 0" class="py-16 text-center">
          <div class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-gray-100 dark:bg-gray-700 mb-4">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-gray-500 dark:text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </div>
          <h3 class="text-xl font-medium text-gray-900 dark:text-white mb-2">No projects found</h3>
          <p class="text-gray-500 dark:text-gray-400 mb-6">There are no projects matching the selected filter.</p>
          <button @click="setFilter('all')" class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors">
            View all projects
          </button>
        </div>

        <!-- Projects Grid -->
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 relative">
          <!-- Animation overlay -->
          <div
            v-if="isChangingFilter"
            class="absolute inset-0 bg-white/20 dark:bg-gray-900/20 backdrop-blur-sm z-10 flex items-center justify-center"
          >
            <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
          </div>

          <div
            v-for="project in filteredProjects"
            :key="project.title"
            class="bg-white dark:bg-gray-800 rounded-lg overflow-hidden shadow-lg transition-all duration-500 hover:transform hover:scale-105 hover:shadow-xl animate-fade-in"
            :style="{ animationDelay: `${filteredProjects.indexOf(project) * 100}ms` }"
          >
            <div class="relative">
              <img :src="project.image" :alt="project.title" class="w-full h-48 object-cover" />
              <span
                class="absolute top-3 right-3 px-2 py-1 text-xs font-semibold rounded-full capitalize"
                :class="{
                  'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200': project.category === 'fullstack',
                  'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200': project.category === 'frontend',
                  'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200': project.category === 'backend',
                  'bg-amber-100 text-amber-800 dark:bg-amber-900 dark:text-amber-200': project.category === 'mobile'
                }"
              >
                {{ project.category }}
              </span>
            </div>
            <div class="p-6">
              <h3 class="text-xl font-bold text-gray-800 dark:text-white mb-2">{{ project.title }}</h3>
              <p class="text-gray-600 dark:text-gray-300 mb-4">{{ project.description }}</p>
              <div class="flex flex-wrap gap-2 mb-4">
                <span v-for="tech in project.technologies" :key="tech" class="px-2 py-1 bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-300 rounded text-xs font-medium">
                  {{ tech }}
                </span>
              </div>
              <div class="flex justify-between">
                <a :href="project.github" target="_blank" class="flex items-center text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400">
                  <Github class="h-4 w-4 mr-1" />
                  <span>Code</span>
                </a>
                <a :href="project.demo" target="_blank" class="flex items-center text-gray-700 dark:text-gray-300 hover:text-blue-600 dark:hover:text-blue-400">
                  <ExternalLink class="h-4 w-4 mr-1" />
                  <span>Demo</span>
                </a>
              </div>
            </div>
          </div>
        </div>
        <div class="text-center mt-12">
          <Button>
            View All Projects
          </Button>
        </div>
      </div>
    </section>

    <!-- Experience Section -->
    <section
      id="experience"
      class="py-20 bg-gradient-to-tl from-white via-white to-blue-50 dark:from-gray-800 dark:via-gray-800 dark:to-blue-950/20 relative overflow-hidden"
      aria-labelledby="experience-heading"
    >
      <!-- Decorative background elements (aria-hidden to hide from screen readers) -->
      <DecorativeShapes aria-hidden="true" />

      <!-- Additional decorative elements -->
      <div
        aria-hidden="true"
        class="absolute top-1/3 right-0 w-32 h-64 bg-blue-100/30 dark:bg-blue-900/10 rounded-l-full blur-xl"
      ></div>

      <div class="container mx-auto px-6 md:px-8 lg:px-12 max-w-7xl relative z-10">
        <h2 id="experience-heading" class="text-3xl font-bold text-center text-gray-800 dark:text-white mb-12">
          Work Experience
        </h2>

        <!-- Experience Timeline - Mobile View (Stacked) -->
        <div class="md:hidden relative border-l-2 border-orange-400 dark:border-orange-600 ml-6 pl-8 mb-8 max-w-4xl mx-auto">
          <div
            v-for="(job, index) in portfolio.experience"
            :key="job.id"
            class="mb-12 relative"
          >
            <!-- Timeline dot with logo -->
            <div
              class="absolute -left-[3.25rem] top-0 w-10 h-10 rounded-full flex items-center justify-center"
              :class="job.current ? 'bg-orange-100 dark:bg-orange-900/30' : 'bg-gray-100 dark:bg-gray-800'"
            >
              <img
                :src="job.logo"
                :alt="`${job.company} logo`"
                class="w-8 h-8 rounded-full"
                loading="lazy"
              />
            </div>

            <!-- Experience card -->
            <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md border border-gray-100 dark:border-gray-700 overflow-hidden hover:shadow-lg transition-all duration-300">
              <div class="p-6">
                <div class="flex flex-col justify-between items-start gap-2 mb-4">
                  <div>
                    <h3 class="text-xl font-bold text-gray-800 dark:text-white mb-1">{{ job.position }}</h3>
                    <div class="flex items-center text-gray-600 dark:text-gray-400">
                      <Building2 class="h-4 w-4 mr-1" />
                      <span>{{ job.company }}</span>
                    </div>
                  </div>
                  <div>
                    <div class="flex items-center text-gray-600 dark:text-gray-400 mb-1">
                      <Calendar class="h-4 w-4 mr-1" />
                      <span>{{ job.startDate }} - {{ job.endDate }}</span>
                      <span
                        v-if="job.current"
                        class="ml-2 px-2 py-0.5 bg-orange-100 dark:bg-orange-900/30 text-orange-700 dark:text-orange-400 rounded-full text-xs font-medium"
                      >
                        Current
                      </span>
                    </div>
                    <div class="flex items-center">
                      <MapPin class="h-4 w-4 mr-1 text-gray-600 dark:text-gray-400" />
                      <span class="text-gray-600 dark:text-gray-400">{{ job.location }}</span>
                    </div>
                  </div>
                </div>

                <p class="text-gray-600 dark:text-gray-400 mb-4">{{ job.description }}</p>

                <div class="mb-4">
                  <h4 class="font-medium text-gray-800 dark:text-white mb-2">Key Responsibilities:</h4>
                  <ul class="list-disc pl-5 space-y-1">
                    <li
                      v-for="(responsibility, i) in job.responsibilities"
                      :key="i"
                      class="text-gray-600 dark:text-gray-400"
                    >
                      {{ responsibility }}
                    </li>
                  </ul>
                </div>

                <div class="flex flex-wrap gap-2">
                  <span
                    v-for="(tech, i) in job.technologies"
                    :key="i"
                    class="px-2 py-1 bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-300 rounded-md text-xs font-medium"
                  >
                    {{ tech }}
                  </span>
                </div>
              </div>
            </div>

            <!-- Connector line to next item -->
            <div
              v-if="index < portfolio.experience.length - 1"
              class="absolute h-12 w-0.5 bg-orange-400 dark:bg-orange-600 -left-[2.75rem] top-full"
            ></div>
          </div>
        </div>

        <!-- Experience Timeline - Desktop View (Alternating Left/Right) -->
        <div class="hidden md:block relative max-w-6xl mx-auto">
          <!-- Center timeline line -->
          <div class="absolute left-1/2 top-0 bottom-0 w-0.5 bg-orange-400 dark:bg-orange-600 transform -translate-x-1/2"></div>

          <div class="relative">
            <div
              v-for="(job, index) in portfolio.experience"
              :key="job.id"
              class="mb-16 relative grid grid-cols-7 items-center"
            >
              <!-- Left side (odd indexes) -->
              <div v-if="index % 2 === 0" class="col-span-3 pr-12">
                <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md border border-gray-100 dark:border-gray-700 overflow-hidden hover:shadow-lg transition-all duration-300">
                  <div class="p-6 flex flex-col items-start">
                    <!-- Header with company and position -->
                    <div class="w-full flex flex-col items-start gap-2 mb-4">
                      <div class="text-left">
                        <h3 class="text-xl font-bold text-gray-800 dark:text-white mb-1">{{ job.position }}</h3>
                        <div class="flex items-center text-gray-600 dark:text-gray-400">
                          <Building2 class="h-4 w-4 mr-1" />
                          <span>{{ job.company }}</span>
                        </div>
                      </div>
                      <div class="text-left">
                        <div class="flex items-center text-gray-600 dark:text-gray-400 mb-1">
                          <Calendar class="h-4 w-4 mr-1" />
                          <span>{{ job.startDate }} - {{ job.endDate }}</span>
                          <span
                            v-if="job.current"
                            class="ml-2 px-2 py-0.5 bg-orange-100 dark:bg-orange-900/30 text-orange-700 dark:text-orange-400 rounded-full text-xs font-medium"
                          >
                            Current
                          </span>
                        </div>
                        <div class="flex items-center">
                          <MapPin class="h-4 w-4 mr-1 text-gray-600 dark:text-gray-400" />
                          <span class="text-gray-600 dark:text-gray-400">{{ job.location }}</span>
                        </div>
                      </div>
                    </div>

                    <!-- Description -->
                    <div class="w-full mb-4">
                      <p class="text-gray-600 dark:text-gray-400 text-left">{{ job.description }}</p>
                    </div>

                    <!-- Responsibilities -->
                    <div class="w-full mb-4">
                      <h4 class="font-medium text-gray-800 dark:text-white mb-2 text-left">Key Responsibilities:</h4>
                      <ul class="list-disc pl-5 space-y-1">
                        <li
                          v-for="(responsibility, i) in job.responsibilities"
                          :key="i"
                          class="text-gray-600 dark:text-gray-400"
                        >
                          {{ responsibility }}
                        </li>
                      </ul>
                    </div>

                    <!-- Technologies -->
                    <div class="w-full">
                      <h4 class="font-medium text-gray-800 dark:text-white mb-2 text-left">Technologies:</h4>
                      <div class="flex flex-wrap gap-2">
                        <span
                          v-for="(tech, i) in job.technologies"
                          :key="i"
                          class="px-2 py-1 bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-300 rounded-md text-xs font-medium"
                        >
                          {{ tech }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Center timeline dot -->
              <div class="col-span-1 flex justify-center">
                <div
                  class="w-12 h-12 rounded-full flex items-center justify-center z-10 border-4 border-white dark:border-gray-800"
                  :class="job.current ? 'bg-orange-100 dark:bg-orange-900/30' : 'bg-gray-100 dark:bg-gray-800'"
                >
                  <img
                    :src="job.logo"
                    :alt="`${job.company} logo`"
                    class="w-8 h-8 rounded-full"
                    loading="lazy"
                  />
                </div>
              </div>

              <!-- Empty space for left side (even indexes) -->
              <div v-if="index % 2 === 1" class="col-span-3"></div>

              <!-- Right side (even indexes) -->
              <div v-if="index % 2 === 1" class="col-span-3 pl-12">
                <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md border border-gray-100 dark:border-gray-700 overflow-hidden hover:shadow-lg transition-all duration-300">
                  <div class="p-6">
                    <div class="flex flex-col items-start gap-2 mb-4">
                      <div>
                        <h3 class="text-xl font-bold text-gray-800 dark:text-white mb-1">{{ job.position }}</h3>
                        <div class="flex items-center text-gray-600 dark:text-gray-400">
                          <Building2 class="h-4 w-4 mr-1" />
                          <span>{{ job.company }}</span>
                        </div>
                      </div>
                      <div>
                        <div class="flex items-center text-gray-600 dark:text-gray-400 mb-1">
                          <Calendar class="h-4 w-4 mr-1" />
                          <span>{{ job.startDate }} - {{ job.endDate }}</span>
                        </div>
                        <div class="flex items-center">
                          <MapPin class="h-4 w-4 mr-1 text-gray-600 dark:text-gray-400" />
                          <span class="text-gray-600 dark:text-gray-400">{{ job.location }}</span>
                          <span
                            v-if="job.current"
                            class="ml-2 px-2 py-0.5 bg-orange-100 dark:bg-orange-900/30 text-orange-700 dark:text-orange-400 rounded-full text-xs font-medium"
                          >
                            Current
                          </span>
                        </div>
                      </div>
                    </div>

                    <p class="text-gray-600 dark:text-gray-400 mb-4">{{ job.description }}</p>

                    <div class="mb-4">
                      <h4 class="font-medium text-gray-800 dark:text-white mb-2">Key Responsibilities:</h4>
                      <ul class="list-disc pl-5 space-y-1">
                        <li
                          v-for="(responsibility, i) in job.responsibilities"
                          :key="i"
                          class="text-gray-600 dark:text-gray-400"
                        >
                          {{ responsibility }}
                        </li>
                      </ul>
                    </div>

                    <div class="w-full">
                      <h4 class="font-medium text-gray-800 dark:text-white mb-2">Technologies:</h4>
                      <div class="flex flex-wrap gap-2">
                        <span
                          v-for="(tech, i) in job.technologies"
                          :key="i"
                          class="px-2 py-1 bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-300 rounded-md text-xs font-medium"
                        >
                          {{ tech }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Empty space for right side (odd indexes) -->
              <div v-if="index % 2 === 0" class="col-span-3"></div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Contact Section -->
    <section id="contact" class="py-20 bg-gradient-to-b from-gray-50 to-white dark:from-gray-900 dark:to-gray-800 relative overflow-hidden">
      <!-- Decorative background elements -->
      <DecorativeShapes />

      <!-- Additional decorative elements -->
      <div class="absolute bottom-0 left-1/4 w-96 h-96 bg-gradient-to-t from-blue-100/20 to-transparent dark:from-blue-900/10 rounded-t-full blur-3xl"></div>

      <div class="container mx-auto px-6 md:px-8 lg:px-12 max-w-7xl relative z-10">
        <h2 class="text-3xl font-bold text-center text-gray-800 dark:text-white mb-12">
          Get In Touch
        </h2>
        <div class="max-w-3xl mx-auto bg-white dark:bg-gray-800 rounded-lg shadow-lg overflow-hidden">
          <div class="flex flex-col md:flex-row">
            <div class="md:w-1/3 bg-blue-600 dark:bg-blue-700 p-8 text-white">
              <h3 class="text-2xl font-bold mb-6">Contact Info</h3>
              <div class="space-y-4">
                <div class="flex items-center">
                  <Mail class="h-5 w-5 mr-3" />
                  <span>{{ portfolio.social.email }}</span>
                </div>
                <div class="flex items-center">
                  <MapPin class="h-5 w-5 mr-3" />
                  <span>{{ portfolio.location }}</span>
                </div>
                <div class="flex items-center">
                  <User class="h-5 w-5 mr-3" />
                  <span>Available for freelance</span>
                </div>
              </div>
              <div class="mt-8">
                <h4 class="text-lg font-semibold mb-4">Follow Me</h4>
                <div class="flex space-x-3">
                  <a :href="portfolio.social.github" target="_blank" class="p-2 bg-white bg-opacity-20 rounded-full hover:bg-opacity-30 transition-colors">
                    <Github class="h-5 w-5" />
                  </a>
                  <a :href="portfolio.social.linkedin" target="_blank" class="p-2 bg-white bg-opacity-20 rounded-full hover:bg-opacity-30 transition-colors">
                    <Linkedin class="h-5 w-5" />
                  </a>
                  <a :href="portfolio.social.twitter" target="_blank" class="p-2 bg-white bg-opacity-20 rounded-full hover:bg-opacity-30 transition-colors">
                    <Twitter class="h-5 w-5" />
                  </a>
                </div>
              </div>
            </div>
            <div class="md:w-2/3 p-8">
              <h3 class="text-2xl font-bold text-gray-800 dark:text-white mb-6">Send Me a Message</h3>
              <form class="space-y-4">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label class="block text-gray-700 dark:text-gray-300 mb-2">Name</label>
                    <input type="text" class="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white" />
                  </div>
                  <div>
                    <label class="block text-gray-700 dark:text-gray-300 mb-2">Email</label>
                    <input type="email" class="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white" />
                  </div>
                </div>
                <div>
                  <label class="block text-gray-700 dark:text-gray-300 mb-2">Subject</label>
                  <input type="text" class="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white" />
                </div>
                <div>
                  <label class="block text-gray-700 dark:text-gray-300 mb-2">Message</label>
                  <textarea rows="4" class="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white"></textarea>
                </div>
                <Button class="w-full">Send Message</Button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Footer -->
    <footer class="bg-gray-800 dark:bg-gray-900 text-white py-8">
      <div class="container mx-auto px-6 md:px-8 lg:px-12 max-w-7xl">
        <div class="flex flex-col md:flex-row justify-between items-center">
          <div class="mb-4 md:mb-0">
            <span class="text-2xl font-bold">Portfolio</span>
            <p class="text-gray-400 mt-2">© {{ new Date().getFullYear() }} {{ portfolio.name }}. All rights reserved.</p>
          </div>
          <div class="flex space-x-6">
            <a :href="portfolio.social.github" target="_blank" class="hover:text-blue-400 transition-colors">
              <Github class="h-5 w-5" />
            </a>
            <a :href="portfolio.social.linkedin" target="_blank" class="hover:text-blue-400 transition-colors">
              <Linkedin class="h-5 w-5" />
            </a>
            <a :href="portfolio.social.twitter" target="_blank" class="hover:text-blue-400 transition-colors">
              <Twitter class="h-5 w-5" />
            </a>
            <a :href="`mailto:${portfolio.social.email}`" class="hover:text-blue-400 transition-colors">
              <Mail class="h-5 w-5" />
            </a>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.animate-blink {
  animation: blink 1s step-end infinite;
}

@keyframes blink {
  from, to {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
}

/* Prevent body scrolling when mobile menu is open */
:deep(body.menu-open) {
  overflow: hidden;
}

/* Smooth scrolling for anchor links */
html {
  scroll-behavior: smooth;
}

/* Safe area padding for mobile devices */
.pb-safe {
  padding-bottom: env(safe-area-inset-bottom, 1rem);
}

/* Mobile menu animation */
@keyframes slideIn {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

/* Custom z-index values */
:deep(.z-100) {
  z-index: 100 !important;
}

/* Add padding to account for fixed navbar */
#hero {
  padding-top: 8rem;
}

/* Ensure sections have proper spacing */
section {
  position: relative;
  z-index: 1;
}

/* Animation for project cards */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fadeIn 0.5s ease-out forwards;
  opacity: 0;
}
</style>
