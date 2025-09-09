package Service;


import DAO.TaskDAO;
import model.Task;

import java.util.List;

public class TaskService {
    private TaskDAO taskDAO;

    public TaskService() {
        this.taskDAO = new TaskDAO();
    }

    public List<Task> getAllTasks() {
        return taskDAO.getAll();
    }

    public List<Task> getTasksByProject(int projectId) {
        return taskDAO.getByProjectId(projectId);
    }

    public List<Task> getTasksByUser(int userId) {
        return taskDAO.getByUserId(userId);
    }

    public List<Task> getTasksByStatus(String status) {
        return taskDAO.getByStatus(status);
    }

    public Task getTaskById(int id) {
        return taskDAO.getById(id);
    }

    public boolean createTask(Task task) {
        return taskDAO.insert(task);
    }

    public boolean updateTask(Task task) {
        return taskDAO.update(task);
    }

    public boolean deleteTask(int id) {
        return taskDAO.delete(id);
    }

    public boolean updateTaskStatus(int taskId, String newStatus, int changedBy, String comment) {
        return taskDAO.updateStatus(taskId, newStatus, changedBy, comment);
    }

    public List<Task> searchTasks(String searchTerm) {
        return taskDAO.search(searchTerm);
    }
}