package UI;


import Service.UserService;
import model.User;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class UserManagerScreen extends JFrame {
    private UserService userService;
    private JTable userTable;
    private JTextField searchField;

    public UserManagerScreen() {
        this.userService = new UserService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Usuários");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de busca
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        JButton searchButton = new JButton("Buscar");

        searchPanel.add(new JLabel("Buscar usuário:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Novo Usuário");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");
        JButton refreshButton = new JButton("Atualizar");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Tabela de usuários
        userTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Adiciona componentes ao painel principal
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Configura ações dos botões
        addButton.addActionListener(this::addUser);
        editButton.addActionListener(this::editUser);
        deleteButton.addActionListener(this::deleteUser);
        refreshButton.addActionListener(e -> refreshTable());
        searchButton.addActionListener(e -> searchUsers());

        // Carrega dados iniciais
        refreshTable();
    }

    private void refreshTable() {
        java.util.List<User> users = userService.getAllUsers();
        UserTableModel model = new UserTableModel(users);
        userTable.setModel(model);
    }

    private void searchUsers() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            java.util.List<User> users = userService.searchUsers(searchTerm);
            UserTableModel model = new UserTableModel(users);
            userTable.setModel(model);
        } else {
            refreshTable();
        }
    }

    private void addUser(ActionEvent e) {
        UserFormDialog dialog = new UserFormDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refreshTable();
        }
    }

    private void editUser(ActionEvent e) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            UserTableModel model = (UserTableModel) userTable.getModel();
            User user = model.getUserAt(selectedRow);

            UserFormDialog dialog = new UserFormDialog(this, user);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteUser(ActionEvent e) {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            UserTableModel model = (UserTableModel) userTable.getModel();
            User user = model.getUserAt(selectedRow);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o usuário " + user.getNomeCompleto() + "?",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (userService.deleteUser(user.getId())) {
                    JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso.");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir usuário.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário para excluir.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}

// Modelo de tabela para usuários
class UserTableModel extends AbstractTableModel {
    private java.util.List<User> users;
    private String[] columnNames = {"ID", "Nome", "CPF", "E-mail", "Cargo", "Login", "Perfil", "Ativo"};

    public UserTableModel(java.util.List<User> users) {
        this.users = users;
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = users.get(rowIndex);
        switch (columnIndex) {
            case 0: return user.getId();
            case 1: return user.getNomeCompleto();
            case 2: return user.getCpf();
            case 3: return user.getEmail();
            case 4: return user.getCargo();
            case 5: return user.getLogin();
            case 6: return getRoleName(user.getRoleId());
            case 7: return user.isAtivo() ? "Sim" : "Não";
            default: return null;
        }
    }

    private String getRoleName(int roleId) {
        switch (roleId) {
            case 1: return "Administrador";
            case 2: return "Gerente";
            case 3: return "Colaborador";
            default: return "Desconhecido";
        }
    }

    public User getUserAt(int row) {
        return users.get(row);
    }
}