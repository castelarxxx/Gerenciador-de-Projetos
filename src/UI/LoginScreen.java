package UI;



import Service.AuthService;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancel;

    private AuthService authService;

    public LoginScreen() {
        authService = new AuthService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Gerenciamento de Projetos - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblLogin = new JLabel("Login:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(lblLogin, constraints);

        txtLogin = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(txtLogin, constraints);

        JLabel lblPassword = new JLabel("Senha:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(lblPassword, constraints);

        txtPassword = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        panel.add(txtPassword, constraints);

        btnLogin = new JButton("Login");
        btnCancel = new JButton("Cancelar");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnCancel);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        panel.add(buttonPanel, constraints);

        add(panel);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    private void login() {
        String login = txtLogin.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, preencha todos os campos.",
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = authService.login(login, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    "Login realizado com sucesso! Bem-vindo, " + user.getNomeCompleto(),
                    "Login Bem-sucedido",
                    JOptionPane.INFORMATION_MESSAGE);

         
            dispose();
            openDashboard(user);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Login ou senha incorretos.",
                    "Erro de Login",
                    JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtLogin.requestFocus();
        }
    }

    private void openDashboard(User user) {
        
        JOptionPane.showMessageDialog(null,
                "Dashboard seria aberto aqui para: " + user.getNomeCompleto(),
                "Dashboard",
                JOptionPane.INFORMATION_MESSAGE);

        System.exit(0);
    }
}
