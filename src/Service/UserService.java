package Service;


import DAO.UserDAO;
import model.User;

import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public List<User> getAllUsers() {
        return userDAO.getAll();
    }

    public List<User> searchUsers(String term) {
        // Implementar busca por nome, email ou login
        return userDAO.getAll(); // Simplificado para o exemplo
    }

    public boolean createUser(User user) {
        return userDAO.insert(user);
    }

    public boolean updateUser(User user) {
        return userDAO.update(user);
    }

    public boolean deleteUser(int id) {
        return userDAO.delete(id);
    }

    public User getUserById(int id) {
        return userDAO.getById(id);
    }

    public User getUserByLogin(String login) {
        return userDAO.getByLogin(login);
    }
}