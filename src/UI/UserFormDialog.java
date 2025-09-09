package UI;

import DAO.PasswordHasher;
import Service.UserService;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserFormDialog extends JDialog {
    private UserService userService;
    private User user;
    private boolean saved = false;

    private JTextField nomeField;
    private JTextField cpfField;
    private JTextField emailField;
    private JTextField cargoField;
    private JTextField loginField;
    private JPasswordField senhaField;
    private JComboBox<String> perfilCombo;
    private JCheckBox ativoCheck;

    public UserFormDialog(JFrame parent, User user) {
        super(parent, true);
        this.userService = new UserService();
        this.user = user;

        initComponents();
        if (user != null) {
            fillForm();
        }
    }

    private void initComponents() {
        setTitle(user == null ? "Novo Usuário" : "Editar Usuário");
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));

        formPanel.add(new JLabel("Nome Completo:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        formPanel.add(cpfField);

        formPanel.add(new JLabel("E-mail:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Cargo:"));
        cargoField = new JTextField();
        formPanel.add(cargoField);

        formPanel.add(new JLabel("Login:"));
        loginField = new JTextField();
        formPanel.add(loginField);

        formPanel.add(new JLabel("Senha:"));
        senhaField = new JPasswordField();
        formPanel.add(senhaField);

        formPanel.add(new JLabel("Perfil:"));
        perfilCombo = new JComboBox<>(new String[]{"Administrador", "Gerente", "Colaborador"});
        formPanel.add(perfilCombo);

        formPanel.add(new JLabel("Ativo:"));
        ativoCheck = new JCheckBox();
        ativoCheck.setSelected(true);
        formPanel.add(ativoCheck);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Ações dos botões
        saveButton.addActionListener(this::saveUser);
        cancelButton.addActionListener(e -> dispose());
    }

    private void fillForm() {
        nomeField.setText(user.getNomeCompleto());
        cpfField.setText(user.getCpf());
        emailField.setText(user.getEmail());
        cargoField.setText(user.getCargo());
        loginField.setText(user.getLogin());
        perfilCombo.setSelectedIndex(user.getRoleId() - 1);
        ativoCheck.setSelected(user.isAtivo());
    }

    private void saveUser(ActionEvent e) {
        // Validação dos campos
        if (nomeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user == null && new String(senhaField.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Senha é obrigatória para novo usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User userToSave;
            if (user == null) {
                // Novo usuário
                userToSave = new User();
                userToSave.setSenhaHash(PasswordHasher.hashPassword(new String(senhaField.getPassword())));
            } else {
                // Usuário existente
                userToSave = user;
                // Se a senha foi alterada, fazer hash da nova senha
                if (!new String(senhaField.getPassword()).isEmpty()) {
                    userToSave.setSenhaHash(PasswordHasher.hashPassword(new String(senhaField.getPassword())));
                }
            }

            userToSave.setNomeCompleto(nomeField.getText().trim());
            userToSave.setCpf(cpfField.getText().trim());
            userToSave.setEmail(emailField.getText().trim());
            userToSave.setCargo(cargoField.getText().trim());
            userToSave.setLogin(loginField.getText().trim());
            userToSave.setRoleId(perfilCombo.getSelectedIndex() + 1);
            userToSave.setAtivo(ativoCheck.isSelected());

            boolean success;
            if (user == null) {
                success = userService.createUser(userToSave);
            } else {
                success = userService.updateUser(userToSave);
            }

            if (success) {
                saved = true;
                JOptionPane.showMessageDialog(this, "Usuário salvo com sucesso.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }
}