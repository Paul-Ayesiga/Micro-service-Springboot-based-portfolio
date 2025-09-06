import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/LandingPage.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { guestOnly: true }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/RegisterView.vue'),
      meta: { guestOnly: true }
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('../views/DashboardView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/projects',
      name: 'projects',
      component: () => import('../views/ProjectsView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/skills',
      name: 'skills',
      component: () => import('../views/SkillsView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/experience',
      name: 'experience',
      component: () => import('../views/ExperienceView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/settings',
      name: 'settings',
      component: () => import('../views/SettingsView.vue'),
      meta: { requiresAuth: true, requiresAdmin: true }
    },
  ],
})

// Navigation guard
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()

  // Initialize auth store if not already initialized
  if (!authStore.initialized) {
    await authStore.initialize()
  }

  console.log('Navigation guard - Auth state:', {
    isAuthenticated: authStore.isAuthenticated,
    isAdmin: authStore.isAdmin,
    route: to.name
  })

  // Only redirect admin users to dashboard after login
  if (authStore.isAuthenticated && authStore.isAdmin) {
    // Only redirect from login/register pages to dashboard
    if ((to.name === 'login' || to.name === 'register') && to.name !== 'logout') {
      console.log('Admin user detected on login/register page, redirecting to dashboard')
      next({ name: 'dashboard' })
      return
    }
  }

  // Check if route is for guests only (login, register)
  if (to.matched.some(record => record.meta.guestOnly)) {
    if (authStore.isAuthenticated) {
      // Redirect admin users to dashboard, others to home
      if (authStore.isAdmin) {
        next({ name: 'dashboard' })
      } else {
        next({ name: 'home' })
      }
      return
    }
  }

  // Check if route requires authentication
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!authStore.isAuthenticated) {
      // Redirect to login if not authenticated
      next({ name: 'login' })
      return
    }

    // Check if route requires admin role
    if (to.matched.some(record => record.meta.requiresAdmin) && !authStore.isAdmin) {
      // Redirect to home if not admin
      next({ name: 'home' })
      return
    }
  }

  next()
})

export default router
