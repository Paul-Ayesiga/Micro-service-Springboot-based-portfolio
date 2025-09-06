import api from './api';

export const skillsApi = {
  // Get all skills
  getAllSkills() {
    return api.get('/public/skills');
  },

  // Get skill by ID
  getSkillById(id) {
    return api.get(`/public/skills/${id}`);
  },

  // Get skills by category
  getSkillsByCategory(category) {
    return api.get(`/public/skills/category/${category}`);
  },

  // Get skills by proficiency level
  getSkillsByLevel(level) {
    return api.get(`/public/skills/level/${level}`);
  },

  // Create a new skill (admin only)
  createSkill(skillData) {
    return api.post('/admin/skills', skillData);
  },

  // Update a skill (admin only)
  updateSkill(id, skillData) {
    return api.put(`/admin/skills/${id}`, skillData);
  },

  // Delete a skill (admin only)
  deleteSkill(id) {
    return api.delete(`/admin/skills/${id}`);
  }
};
