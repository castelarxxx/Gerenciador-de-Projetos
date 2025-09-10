import DAO.PasswordHasher;
import DAO.UserDAO;
import UI.LoginScreen;
import model.User;



public class MainApp {
    public static void main(String[] args) {
        
        UserDAO userDAO = new UserDAO();
        User admin = userDAO.getByLogin("admin");

        if (admin == null) {
            admin = new User(
                    "Administrador",
                    "000.000.000-00",
                    "admin@empresa.com",
                    "Administrador",
                    "admin",
                    PasswordHasher.hashPassword("admin123"),
                    1 
            );
            userDAO.insert(admin);
            System.out.println("Usuário admin criado: login=admin, senha=admin123");
        }

        java.awt.EventQueue.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
}
