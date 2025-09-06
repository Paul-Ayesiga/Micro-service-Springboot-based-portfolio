import api from './api';

export const projectsApi = {
  // Get all projects
  getAllProjects() {
    return api.get('/public/projects');
  },

  // Get featured projects
  getFeaturedProjects() {
    return api.get('/public/projects/featured');
  },

  // Get project by ID
  getProjectById(id) {
    return api.get(`/public/projects/${id}`);
  },

  // Get projects by category
  getProjectsByCategory(category) {
    return api.get(`/public/projects/category/${category}`);
  },

  // Get projects by technology
  getProjectsByTechnology(technology) {
    return api.get(`/public/projects/technology/${technology}`);
  },

  // Create a new project (admin only)
  createProject(projectData) {
    return api.post('/admin/projects', projectData);
  },

  // Update a project (admin only)
  updateProject(id, projectData) {
    return api.put(`/admin/projects/${id}`, projectData);
  },

  // Delete a project (admin only)
  deleteProject(id) {
    return api.delete(`/admin/projects/${id}`);
  }
};
