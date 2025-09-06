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
  Bell,
  Globe,
  Key,
  Lock,
  Mail,
  Moon,
  Save,
  Settings,
  Shield,
  Sun,
  User,
  Users,
  UserPlus,
  UserCheck,
  UserX,
  KeyRound
} from 'lucide-vue-next';
import { ref } from 'vue';

// Sample user data
const user = ref({
  name: 'John Doe',
  email: 'john.doe@example.com',
  avatar: 'https://placehold.co/200/2c3e50/ffffff/png?text=JD',
  role: 'Administrator',
  theme: 'light',
  notifications: {
    email: true,
    browser: true,
    mobile: false
  },
  security: {
    twoFactor: false,
    lastPasswordChange: '3 months ago'
  }
});

// Sample roles data
const roles = ref([
  {
    id: 1,
    name: 'ADMIN',
    description: 'Full access to all resources',
    users: 2,
    permissions: [
      { id: 1, name: 'dashboard:view', description: 'View dashboard' },
      { id: 2, name: 'dashboard:edit', description: 'Edit dashboard' },
      { id: 3, name: 'projects:view', description: 'View projects' },
      { id: 4, name: 'projects:create', description: 'Create projects' },
      { id: 5, name: 'projects:edit', description: 'Edit projects' },
      { id: 6, name: 'projects:delete', description: 'Delete projects' },
      { id: 7, name: 'users:view', description: 'View users' },
      { id: 8, name: 'users:edit', description: 'Edit users' },
      { id: 9, name: 'roles:manage', description: 'Manage roles' }
    ]
  },
  {
    id: 2,
    name: 'EDITOR',
    description: 'Can edit content but cannot delete',
    users: 5,
    permissions: [
      { id: 1, name: 'dashboard:view', description: 'View dashboard' },
      { id: 3, name: 'projects:view', description: 'View projects' },
      { id: 4, name: 'projects:create', description: 'Create projects' },
      { id: 5, name: 'projects:edit', description: 'Edit projects' }
    ]
  },
  {
    id: 3,
    name: 'VIEWER',
    description: 'Read-only access to content',
    users: 12,
    permissions: [
      { id: 1, name: 'dashboard:view', description: 'View dashboard' },
      { id: 3, name: 'projects:view', description: 'View projects' }
    ]
  }
]);

// Sample users with roles
const usersWithRoles = ref([
  { id: 1, name: 'John Doe', email: 'john.doe@example.com', role: 'ADMIN' },
  { id: 2, name: 'Jane Smith', email: 'jane.smith@example.com', role: 'ADMIN' },
  { id: 3, name: 'Bob Johnson', email: 'bob.johnson@example.com', role: 'EDITOR' },
  { id: 4, name: 'Alice Williams', email: 'alice.williams@example.com', role: 'EDITOR' },
  { id: 5, name: 'Charlie Brown', email: 'charlie.brown@example.com', role: 'VIEWER' }
]);

// Sample all permissions
const allPermissions = ref([
  { id: 1, name: 'dashboard:view', description: 'View dashboard' },
  { id: 2, name: 'dashboard:edit', description: 'Edit dashboard' },
  { id: 3, name: 'projects:view', description: 'View projects' },
  { id: 4, name: 'projects:create', description: 'Create projects' },
  { id: 5, name: 'projects:edit', description: 'Edit projects' },
  { id: 6, name: 'projects:delete', description: 'Delete projects' },
  { id: 7, name: 'users:view', description: 'View users' },
  { id: 8, name: 'users:edit', description: 'Edit users' },
  { id: 9, name: 'roles:manage', description: 'Manage roles' },
  { id: 10, name: 'settings:view', description: 'View settings' },
  { id: 11, name: 'settings:edit', description: 'Edit settings' }
]);

// Selected role for editing
const selectedRole = ref(null);

// New role form
const newRole = ref({
  name: '',
  description: '',
  permissions: []
});

// Active tab
const activeTab = ref('profile');

// Active roles subtab
const activeRolesTab = ref('roles-list');
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
                <BreadcrumbPage>Settings</BreadcrumbPage>
              </BreadcrumbItem>
            </BreadcrumbList>
          </Breadcrumb>
        </div>
      </header>
      <div class="flex flex-1 flex-col p-4 pt-0 overflow-x-hidden">
        <div class="container mx-auto px-4 max-w-full">
          <h1 class="text-2xl font-bold mb-6">Settings</h1>

          <div class="flex flex-col md:flex-row gap-6">
            <!-- Settings Navigation -->
            <div class="w-full md:w-64">
              <Card>
                <CardContent class="p-0">
                  <nav>
                    <ul class="divide-y">
                      <li>
                        <button
                          @click="activeTab = 'profile'"
                          class="w-full flex items-center px-4 py-3 hover:bg-gray-50 transition-colors"
                          :class="activeTab === 'profile' ? 'bg-blue-50 text-blue-600 font-medium' : ''"
                        >
                          <User class="h-5 w-5 mr-3" />
                          <span>Profile</span>
                        </button>
                      </li>
                      <li>
                        <button
                          @click="activeTab = 'account'"
                          class="w-full flex items-center px-4 py-3 hover:bg-gray-50 transition-colors"
                          :class="activeTab === 'account' ? 'bg-blue-50 text-blue-600 font-medium' : ''"
                        >
                          <Mail class="h-5 w-5 mr-3" />
                          <span>Account</span>
                        </button>
                      </li>
                      <li>
                        <button
                          @click="activeTab = 'appearance'"
                          class="w-full flex items-center px-4 py-3 hover:bg-gray-50 transition-colors"
                          :class="activeTab === 'appearance' ? 'bg-blue-50 text-blue-600 font-medium' : ''"
                        >
                          <Sun class="h-5 w-5 mr-3" />
                          <span>Appearance</span>
                        </button>
                      </li>
                      <li>
                        <button
                          @click="activeTab = 'notifications'"
                          class="w-full flex items-center px-4 py-3 hover:bg-gray-50 transition-colors"
                          :class="activeTab === 'notifications' ? 'bg-blue-50 text-blue-600 font-medium' : ''"
                        >
                          <Bell class="h-5 w-5 mr-3" />
                          <span>Notifications</span>
                        </button>
                      </li>
                      <li>
                        <button
                          @click="activeTab = 'security'"
                          class="w-full flex items-center px-4 py-3 hover:bg-gray-50 transition-colors"
                          :class="activeTab === 'security' ? 'bg-blue-50 text-blue-600 font-medium' : ''"
                        >
                          <Shield class="h-5 w-5 mr-3" />
                          <span>Security</span>
                        </button>
                      </li>
                      <li>
                        <button
                          @click="activeTab = 'roles'"
                          class="w-full flex items-center px-4 py-3 hover:bg-gray-50 transition-colors"
                          :class="activeTab === 'roles' ? 'bg-blue-50 text-blue-600 font-medium' : ''"
                        >
                          <Users class="h-5 w-5 mr-3" />
                          <span>Roles & Permissions</span>
                        </button>
                      </li>
                    </ul>
                  </nav>
                </CardContent>
              </Card>
            </div>

            <!-- Settings Content -->
            <div class="flex-1">
              <!-- Profile Settings -->
              <Card v-if="activeTab === 'profile'" class="mb-6">
                <CardHeader>
                  <CardTitle class="flex items-center">
                    <User class="h-5 w-5 mr-2" />
                    Profile Information
                  </CardTitle>
                  <CardDescription>Update your profile information and public details</CardDescription>
                </CardHeader>
                <CardContent>
                  <div class="flex flex-col md:flex-row gap-6">
                    <div class="md:w-1/3 flex flex-col items-center">
                      <img :src="user.avatar" :alt="user.name" class="w-32 h-32 rounded-full mb-4" />
                      <Button variant="outline" size="sm">Change Avatar</Button>
                    </div>
                    <div class="md:w-2/3">
                      <div class="grid gap-4">
                        <div class="grid gap-2">
                          <label class="text-sm font-medium">Full Name</label>
                          <input
                            type="text"
                            :value="user.name"
                            class="px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                          />
                        </div>
                        <div class="grid gap-2">
                          <label class="text-sm font-medium">Email</label>
                          <input
                            type="email"
                            :value="user.email"
                            class="px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                          />
                        </div>
                        <div class="grid gap-2">
                          <label class="text-sm font-medium">Bio</label>
                          <textarea
                            rows="4"
                            placeholder="Write a short bio about yourself"
                            class="px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                          ></textarea>
                        </div>
                      </div>
                    </div>
                  </div>
                </CardContent>
                <CardFooter class="flex justify-end">
                  <Button class="gap-2">
                    <Save class="h-4 w-4" />
                    <span>Save Changes</span>
                  </Button>
                </CardFooter>
              </Card>

              <!-- Account Settings -->
              <Card v-if="activeTab === 'account'" class="mb-6">
                <CardHeader>
                  <CardTitle class="flex items-center">
                    <Mail class="h-5 w-5 mr-2" />
                    Account Settings
                  </CardTitle>
                  <CardDescription>Manage your account settings and preferences</CardDescription>
                </CardHeader>
                <CardContent>
                  <div class="grid gap-6">
                    <div class="grid gap-2">
                      <label class="text-sm font-medium">Email Address</label>
                      <input
                        type="email"
                        :value="user.email"
                        class="px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                      />
                    </div>
                    <div class="grid gap-2">
                      <label class="text-sm font-medium">Language</label>
                      <select class="px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                        <option value="en">English</option>
                        <option value="fr">French</option>
                        <option value="es">Spanish</option>
                        <option value="de">German</option>
                      </select>
                    </div>
                    <div class="grid gap-2">
                      <label class="text-sm font-medium">Time Zone</label>
                      <select class="px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                        <option value="utc">UTC</option>
                        <option value="est">Eastern Time (ET)</option>
                        <option value="pst">Pacific Time (PT)</option>
                        <option value="cet">Central European Time (CET)</option>
                      </select>
                    </div>
                  </div>
                </CardContent>
                <CardFooter class="flex justify-end">
                  <Button class="gap-2">
                    <Save class="h-4 w-4" />
                    <span>Save Changes</span>
                  </Button>
                </CardFooter>
              </Card>

              <!-- Appearance Settings -->
              <Card v-if="activeTab === 'appearance'" class="mb-6">
                <CardHeader>
                  <CardTitle class="flex items-center">
                    <Sun class="h-5 w-5 mr-2" />
                    Appearance
                  </CardTitle>
                  <CardDescription>Customize the appearance of the application</CardDescription>
                </CardHeader>
                <CardContent>
                  <div class="grid gap-6">
                    <div>
                      <h3 class="text-sm font-medium mb-3">Theme</h3>
                      <div class="grid grid-cols-3 gap-4">
                        <div
                          class="border rounded-lg p-4 flex flex-col items-center cursor-pointer hover:border-blue-500 transition-colors"
                          :class="user.theme === 'light' ? 'border-blue-500 bg-blue-50' : ''"
                          @click="user.theme = 'light'"
                        >
                          <Sun class="h-6 w-6 mb-2" />
                          <span class="text-sm">Light</span>
                        </div>
                        <div
                          class="border rounded-lg p-4 flex flex-col items-center cursor-pointer hover:border-blue-500 transition-colors"
                          :class="user.theme === 'dark' ? 'border-blue-500 bg-blue-50' : ''"
                          @click="user.theme = 'dark'"
                        >
                          <Moon class="h-6 w-6 mb-2" />
                          <span class="text-sm">Dark</span>
                        </div>
                        <div
                          class="border rounded-lg p-4 flex flex-col items-center cursor-pointer hover:border-blue-500 transition-colors"
                          :class="user.theme === 'system' ? 'border-blue-500 bg-blue-50' : ''"
                          @click="user.theme = 'system'"
                        >
                          <Settings class="h-6 w-6 mb-2" />
                          <span class="text-sm">System</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </CardContent>
                <CardFooter class="flex justify-end">
                  <Button class="gap-2">
                    <Save class="h-4 w-4" />
                    <span>Save Changes</span>
                  </Button>
                </CardFooter>
              </Card>

              <!-- Notifications Settings -->
              <Card v-if="activeTab === 'notifications'" class="mb-6">
                <CardHeader>
                  <CardTitle class="flex items-center">
                    <Bell class="h-5 w-5 mr-2" />
                    Notifications
                  </CardTitle>
                  <CardDescription>Manage your notification preferences</CardDescription>
                </CardHeader>
                <CardContent>
                  <div class="grid gap-6">
                    <div class="flex items-center justify-between">
                      <div>
                        <h3 class="font-medium">Email Notifications</h3>
                        <p class="text-sm text-gray-500">Receive notifications via email</p>
                      </div>
                      <label class="relative inline-flex items-center cursor-pointer">
                        <input type="checkbox" v-model="user.notifications.email" class="sr-only peer">
                        <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                      </label>
                    </div>
                    <div class="flex items-center justify-between">
                      <div>
                        <h3 class="font-medium">Browser Notifications</h3>
                        <p class="text-sm text-gray-500">Receive notifications in your browser</p>
                      </div>
                      <label class="relative inline-flex items-center cursor-pointer">
                        <input type="checkbox" v-model="user.notifications.browser" class="sr-only peer">
                        <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                      </label>
                    </div>
                    <div class="flex items-center justify-between">
                      <div>
                        <h3 class="font-medium">Mobile Notifications</h3>
                        <p class="text-sm text-gray-500">Receive notifications on your mobile device</p>
                      </div>
                      <label class="relative inline-flex items-center cursor-pointer">
                        <input type="checkbox" v-model="user.notifications.mobile" class="sr-only peer">
                        <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                      </label>
                    </div>
                  </div>
                </CardContent>
                <CardFooter class="flex justify-end">
                  <Button class="gap-2">
                    <Save class="h-4 w-4" />
                    <span>Save Changes</span>
                  </Button>
                </CardFooter>
              </Card>

              <!-- Security Settings -->
              <Card v-if="activeTab === 'security'" class="mb-6">
                <CardHeader>
                  <CardTitle class="flex items-center">
                    <Shield class="h-5 w-5 mr-2" />
                    Security
                  </CardTitle>
                  <CardDescription>Manage your security settings</CardDescription>
                </CardHeader>
                <CardContent>
                  <div class="grid gap-6">
                    <div>
                      <h3 class="font-medium mb-2">Change Password</h3>
                      <div class="grid gap-4">
                        <div class="grid gap-2">
                          <label class="text-sm font-medium">Current Password</label>
                          <input
                            type="password"
                            placeholder="••••••••"
                            class="px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                          />
                        </div>
                        <div class="grid gap-2">
                          <label class="text-sm font-medium">New Password</label>
                          <input
                            type="password"
                            placeholder="••••••••"
                            class="px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                          />
                        </div>
                        <div class="grid gap-2">
                          <label class="text-sm font-medium">Confirm New Password</label>
                          <input
                            type="password"
                            placeholder="••••••••"
                            class="px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                          />
                        </div>
                        <Button class="w-full md:w-auto">Update Password</Button>
                      </div>
                    </div>

                    <Separator />

                    <div class="flex items-center justify-between">
                      <div>
                        <h3 class="font-medium">Two-Factor Authentication</h3>
                        <p class="text-sm text-gray-500">Add an extra layer of security to your account</p>
                      </div>
                      <label class="relative inline-flex items-center cursor-pointer">
                        <input type="checkbox" v-model="user.security.twoFactor" class="sr-only peer">
                        <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                      </label>
                    </div>
                  </div>
                </CardContent>
              </Card>

              <!-- Roles & Permissions Settings -->
              <div v-if="activeTab === 'roles'">
                <!-- Roles & Permissions Tabs -->
                <div class="mb-6 border-b border-gray-200">
                  <div class="flex flex-wrap -mb-px">
                    <button
                      @click="activeRolesTab = 'roles-list'"
                      class="inline-block p-4 border-b-2 rounded-t-lg"
                      :class="activeRolesTab === 'roles-list' ? 'text-blue-600 border-blue-600' : 'border-transparent hover:text-gray-600 hover:border-gray-300'"
                    >
                      <div class="flex items-center">
                        <Users class="h-5 w-5 mr-2" />
                        <span>Roles</span>
                      </div>
                    </button>
                    <button
                      @click="activeRolesTab = 'users-roles'"
                      class="inline-block p-4 border-b-2 rounded-t-lg"
                      :class="activeRolesTab === 'users-roles' ? 'text-blue-600 border-blue-600' : 'border-transparent hover:text-gray-600 hover:border-gray-300'"
                    >
                      <div class="flex items-center">
                        <UserCheck class="h-5 w-5 mr-2" />
                        <span>User Assignments</span>
                      </div>
                    </button>
                    <button
                      @click="activeRolesTab = 'permissions'"
                      class="inline-block p-4 border-b-2 rounded-t-lg"
                      :class="activeRolesTab === 'permissions' ? 'text-blue-600 border-blue-600' : 'border-transparent hover:text-gray-600 hover:border-gray-300'"
                    >
                      <div class="flex items-center">
                        <KeyRound class="h-5 w-5 mr-2" />
                        <span>Permissions</span>
                      </div>
                    </button>
                  </div>
                </div>

                <!-- Roles List Tab -->
                <Card v-if="activeRolesTab === 'roles-list'" class="mb-6">
                  <CardHeader>
                    <CardTitle class="flex items-center justify-between">
                      <div class="flex items-center">
                        <Users class="h-5 w-5 mr-2" />
                        <span>Roles Management</span>
                      </div>
                      <Button size="sm" class="gap-2">
                        <UserPlus class="h-4 w-4" />
                        <span>Add Role</span>
                      </Button>
                    </CardTitle>
                    <CardDescription>Manage roles and their permissions</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div class="overflow-x-auto">
                      <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                          <tr>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Role Name</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Description</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Users</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Permissions</th>
                            <th scope="col" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                          </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                          <tr v-for="role in roles" :key="role.id" class="hover:bg-gray-50">
                            <td class="px-6 py-4 whitespace-nowrap">
                              <div class="font-medium text-gray-900">{{ role.name }}</div>
                            </td>
                            <td class="px-6 py-4">
                              <div class="text-sm text-gray-500">{{ role.description }}</div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                              <div class="text-sm text-gray-900">{{ role.users }}</div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                              <div class="text-sm text-gray-900">{{ role.permissions.length }}</div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                              <div class="flex justify-end space-x-2">
                                <button class="text-blue-600 hover:text-blue-900">Edit</button>
                                <button class="text-red-600 hover:text-red-900">Delete</button>
                              </div>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </CardContent>
                </Card>

                <!-- User Role Assignments Tab -->
                <Card v-if="activeRolesTab === 'users-roles'" class="mb-6">
                  <CardHeader>
                    <CardTitle class="flex items-center">
                      <UserCheck class="h-5 w-5 mr-2" />
                      <span>User Role Assignments</span>
                    </CardTitle>
                    <CardDescription>Assign roles to users</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div class="mb-4">
                      <div class="relative">
                        <input
                          type="text"
                          placeholder="Search users..."
                          class="w-full px-4 py-2 pl-10 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                        />
                        <div class="absolute left-3 top-2.5 text-gray-400">
                          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                          </svg>
                        </div>
                      </div>
                    </div>
                    <div class="overflow-x-auto">
                      <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                          <tr>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">User</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Current Role</th>
                            <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                          </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                          <tr v-for="user in usersWithRoles" :key="user.id" class="hover:bg-gray-50">
                            <td class="px-6 py-4 whitespace-nowrap">
                              <div class="flex items-center">
                                <div class="flex-shrink-0 h-10 w-10 rounded-full bg-gray-200 flex items-center justify-center text-gray-600">
                                  {{ user.name.charAt(0) }}
                                </div>
                                <div class="ml-4">
                                  <div class="font-medium text-gray-900">{{ user.name }}</div>
                                </div>
                              </div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                              <div class="text-sm text-gray-500">{{ user.email }}</div>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap">
                              <span
                                class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                                :class="{
                                  'bg-green-100 text-green-800': user.role === 'ADMIN',
                                  'bg-blue-100 text-blue-800': user.role === 'EDITOR',
                                  'bg-gray-100 text-gray-800': user.role === 'VIEWER'
                                }"
                              >
                                {{ user.role }}
                              </span>
                            </td>
                            <td class="px-6 py-4 whitespace-nowrap text-sm">
                              <select class="px-2 py-1 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
                                <option v-for="role in roles" :key="role.id" :value="role.name" :selected="role.name === user.role">{{ role.name }}</option>
                              </select>
                              <button class="ml-2 text-blue-600 hover:text-blue-900 text-sm">Update</button>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </CardContent>
                </Card>

                <!-- Permissions Tab -->
                <Card v-if="activeRolesTab === 'permissions'" class="mb-6">
                  <CardHeader>
                    <CardTitle class="flex items-center">
                      <KeyRound class="h-5 w-5 mr-2" />
                      <span>Permissions</span>
                    </CardTitle>
                    <CardDescription>View and manage available permissions</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div class="mb-6">
                      <h3 class="text-lg font-medium mb-2">Available Permissions</h3>
                      <p class="text-sm text-gray-500 mb-4">These permissions can be assigned to roles. Permissions determine what actions users can perform.</p>

                      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                        <div v-for="permission in allPermissions" :key="permission.id" class="border rounded-lg p-4 hover:bg-gray-50">
                          <div class="font-medium">{{ permission.name }}</div>
                          <div class="text-sm text-gray-500">{{ permission.description }}</div>
                        </div>
                      </div>
                    </div>

                    <Separator class="my-6" />

                    <div>
                      <h3 class="text-lg font-medium mb-2">Role Permissions</h3>
                      <p class="text-sm text-gray-500 mb-4">Select a role to view or edit its permissions:</p>

                      <div class="mb-4">
                        <select
                          class="w-full px-3 py-2 rounded-md border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                          v-model="selectedRole"
                        >
                          <option :value="null">Select a role</option>
                          <option v-for="role in roles" :key="role.id" :value="role">{{ role.name }}</option>
                        </select>
                      </div>

                      <div v-if="selectedRole" class="border rounded-lg p-4">
                        <h4 class="font-medium mb-2">Permissions for {{ selectedRole.name }}</h4>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-2 mb-4">
                          <div v-for="permission in selectedRole.permissions" :key="permission.id" class="flex items-center">
                            <input type="checkbox" :id="`perm-${permission.id}`" checked class="rounded text-blue-600 focus:ring-blue-500 h-4 w-4" />
                            <label :for="`perm-${permission.id}`" class="ml-2 text-sm">{{ permission.name }}</label>
                          </div>
                        </div>
                        <Button size="sm">Save Permissions</Button>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              </div>
            </div>
          </div>
        </div>
      </div>
    </SidebarInset>
  </SidebarProvider>
</template>
