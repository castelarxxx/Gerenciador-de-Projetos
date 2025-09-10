package Service;


import DAO.PasswordHasher;
import DAO.UserDAO;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private UserDAO userDAO;
    private Map<String, User> activeSessions;

    public AuthService() {
        this.userDAO = new UserDAO();
        this.activeSessions = new HashMap<>();
    }

    public User login(String login, String password) {
        User user = userDAO.getByLogin(login);

        if (user != null && user.isAtivo() &&
                PasswordHasher.verifyPassword(password, user.getSenhaHash())) {

            String sessionToken = generateSessionToken();
            activeSessions.put(sessionToken, user);

            return user;
        }
        return null;
    }

    public void logout(String sessionToken) {
        activeSessions.remove(sessionToken);
    }

    public boolean isUserLoggedIn(String sessionToken) {
        return activeSessions.containsKey(sessionToken);
    }

    public User getLoggedUser(String sessionToken) {
        return activeSessions.get(sessionToken);
    }

    public boolean hasPermission(String sessionToken, String requiredRole) {
        User user = getLoggedUser(sessionToken);
        if (user == null) return false;

        Map<Integer, String> roleMap = new HashMap<>();
        roleMap.put(1, "administrador");
        roleMap.put(2, "gerente");
        roleMap.put(3, "colaborador");

        String userRole = roleMap.get(user.getRoleId());

        switch (requiredRole) {
            case "admin":
                return "administrador".equals(userRole);
            case "gerente":
                return "administrador".equals(userRole) || "gerente".equals(userRole);
            case "colaborador":
                return true;
            default:
                return false;
        }
    }

    private String generateSessionToken() {
        return java.util.UUID.randomUUID().toString();
    }
}
