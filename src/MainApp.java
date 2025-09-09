import DAO.PasswordHasher;
import DAO.UserDAO;
import UI.LoginScreen;
import model.User;



public class MainApp {
    public static void main(String[] args) {
        // Verificar se existe um usuário admin, caso contrário criar um
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
                    1 // role_id para administrador
            );
            userDAO.insert(admin);
            System.out.println("Usuário admin criado: login=admin, senha=admin123");
        }

        // Iniciar a interface de login
        java.awt.EventQueue.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
}
