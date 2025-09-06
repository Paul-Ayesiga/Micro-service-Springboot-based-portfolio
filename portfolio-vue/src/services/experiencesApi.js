import api from './api';

export const experiencesApi = {
  // Get all experiences
  getAllExperiences() {
    return api.get('/public/experiences');
  },

  // Get current experiences
  getCurrentExperiences() {
    return api.get('/public/experiences/current');
  },

  // Get experience by ID
  getExperienceById(id) {
    return api.get(`/public/experiences/${id}`);
  },

  // Create a new experience (admin only)
  createExperience(experienceData) {
    return api.post('/admin/experiences', experienceData);
  },

  // Update an experience (admin only)
  updateExperience(id, experienceData) {
    return api.put(`/admin/experiences/${id}`, experienceData);
  },

  // Delete an experience (admin only)
  deleteExperience(id) {
    return api.delete(`/admin/experiences/${id}`);
  }
};
