
package Service;


import DAO.ProjectDAO;
import model.Project;

import java.util.List;

public class ProjectService {
    private ProjectDAO projectDAO;

    public ProjectService() {
        this.projectDAO = new ProjectDAO();
    }

    public List<Project> getAllProjects() {
        return projectDAO.getAll();
    }

    public Project getProjectById(int id) {
        return projectDAO.getById(id);
    }

    public List<Project> getProjectsByManager(int managerId) {
        return projectDAO.getByManagerId(managerId);
    }

    public List<Project> getProjectsByStatus(String status) {
        return projectDAO.getByStatus(status);
    }

    public boolean createProject(Project project) {
        // Validações de negócio
        if (project.getNome() == null || project.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do projeto é obrigatório");
        }

        if (project.getDataInicio() != null && project.getDataTerminoPrevista() != null &&
                project.getDataInicio().isAfter(project.getDataTerminoPrevista())) {
            throw new IllegalArgumentException("Data de início não pode ser após data de término prevista");
        }

        return projectDAO.insert(project);
    }

    public boolean updateProject(Project project) {
        return projectDAO.update(project);
    }

    public boolean deleteProject(int id) {
        return projectDAO.delete(id);
    }

    public boolean updateProjectStatus(int projectId, String newStatus, int changedBy, String comment) {
        return projectDAO.updateStatus(projectId, newStatus, changedBy, comment);
    }

    public List<Project> getProjectsAtRisk() {
        return projectDAO.getProjectsAtRisk();
    }

    public List<Project> searchProjects(String searchTerm) {
        return projectDAO.search(searchTerm);
    }

    public double getProjectProgress(int projectId) {
        return projectDAO.calculateProgress(projectId);
    }
}