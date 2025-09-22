package UI;

import Service.TeamService;
import Service.UserService;
import model.Team;
import model.User;
import model.TeamMember;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TeamManagerScreen extends JFrame {
    private TeamService teamService;
    private UserService userService;
    private JTable teamTable;

    public TeamManagerScreen() {
        this.teamService = new TeamService();
        this.userService = new UserService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Equipes");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Nova Equipe");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");
        JButton refreshButton = new JButton("Atualizar");
        JButton manageMembersButton = new JButton("Gerenciar Membros");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(manageMembersButton);

        teamTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(teamTable);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        addButton.addActionListener(this::addTeam);
        editButton.addActionListener(this::editTeam);
        deleteButton.addActionListener(this::deleteTeam);
        refreshButton.addActionListener(e -> refreshTable());
        manageMembersButton.addActionListener(this::manageTeamMembers);

        refreshTable();
    }

    private void refreshTable() {
        List<Team> teams = teamService.getAllTeams();
        TeamTableModel model = new TeamTableModel(teams);
        teamTable.setModel(model);
    }

    private void addTeam(ActionEvent e) {
        TeamFormDialog dialog = new TeamFormDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refreshTable();
            JOptionPane.showMessageDialog(this, "Equipe criada com sucesso!");
        }
    }

    private void editTeam(ActionEvent e) {
        int selectedRow = teamTable.getSelectedRow();
        if (selectedRow >= 0) {
            TeamTableModel model = (TeamTableModel) teamTable.getModel();
            Team team = model.getTeamAt(selectedRow);

            TeamFormDialog dialog = new TeamFormDialog(this, team);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Equipe atualizada com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma equipe para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTeam(ActionEvent e) {
        int selectedRow = teamTable.getSelectedRow();
        if (selectedRow >= 0) {
            TeamTableModel model = (TeamTableModel) teamTable.getModel();
            Team team = model.getTeamAt(selectedRow);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir a equipe '" + team.getNome() + "'?\n" +
                            "Esta ação também removerá todos os membros da equipe.",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (teamService.deleteTeam(team.getId())) {
                    JOptionPane.showMessageDialog(this, "Equipe excluída com sucesso.");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir equipe. Verifique se não há projetos vinculados.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma equipe para excluir.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void manageTeamMembers(ActionEvent e) {
        int selectedRow = teamTable.getSelectedRow();
        if (selectedRow >= 0) {
            TeamTableModel model = (TeamTableModel) teamTable.getModel();
            Team team = model.getTeamAt(selectedRow);

            TeamMembersDialog dialog = new TeamMembersDialog(this, team);
            dialog.setVisible(true);
            if (dialog.isUpdated()) {
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma equipe para gerenciar membros.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}

class TeamFormDialog extends JDialog {
    private TeamService teamService;
    private Team team;
    private boolean saved = false;

    private JTextField nomeField;
    private JTextArea descricaoArea;

    public TeamFormDialog(JFrame parent, Team team) {
        super(parent, true);
        this.teamService = new TeamService();
        this.team = team;

        initComponents();
        if (team != null) {
            fillForm();
        }
    }

    private void initComponents() {
        setTitle(team == null ? "Nova Equipe" : "Editar Equipe");
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        formPanel.add(new JLabel("Nome da Equipe:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("Descrição:"));
        descricaoArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(descricaoArea);
        formPanel.add(scrollPane);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        saveButton.addActionListener(this::saveTeam);
        cancelButton.addActionListener(e -> dispose());
    }

    private void fillForm() {
        nomeField.setText(team.getNome());
        descricaoArea.setText(team.getDescricao());
    }

    private void saveTeam(ActionEvent e) {
        if (nomeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nome da equipe é obrigatório.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nomeField.getText().trim().length() < 3) {
            JOptionPane.showMessageDialog(this,
                    "Nome da equipe deve ter pelo menos 3 caracteres.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Team teamToSave;
            if (team == null) {
                teamToSave = new Team();
            } else {
                teamToSave = team;
            }

            teamToSave.setNome(nomeField.getText().trim());
            teamToSave.setDescricao(descricaoArea.getText().trim());

            boolean success;
            if (team == null) {
                success = teamService.createTeam(teamToSave);
            } else {
                success = teamService.updateTeam(teamToSave);
            }

            if (success) {
                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar equipe. Verifique se o nome já existe.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao processar dados: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }
}

class TeamMembersDialog extends JDialog {
    private TeamService teamService;
    private UserService userService;
    private Team team;
    private boolean updated = false;

    private JList<User> availableUsersList;
    private JList<User> teamMembersList;
    private DefaultListModel<User> availableUsersModel;
    private DefaultListModel<User> teamMembersModel;

    public TeamMembersDialog(JFrame parent, Team team) {
        super(parent, true);
        this.teamService = new TeamService();
        this.userService = new UserService();
        this.team = team;

        initComponents();
        loadUsers();
    }

    private void initComponents() {
        setTitle("Gerenciar Membros - " + team.getNome());
        setSize(600, 400);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel listsPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel availablePanel = new JPanel(new BorderLayout());
        availablePanel.setBorder(BorderFactory.createTitledBorder("Usuários Disponíveis"));

        availableUsersModel = new DefaultListModel<>();
        availableUsersList = new JList<>(availableUsersModel);
        availableUsersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        availableUsersList.setCellRenderer(new UserListCellRenderer());

        JScrollPane availableScroll = new JScrollPane(availableUsersList);
        availablePanel.add(availableScroll, BorderLayout.CENTER);

        JPanel membersPanel = new JPanel(new BorderLayout());
        membersPanel.setBorder(BorderFactory.createTitledBorder("Membros da Equipe"));

        teamMembersModel = new DefaultListModel<>();
        teamMembersList = new JList<>(teamMembersModel);
        teamMembersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        teamMembersList.setCellRenderer(new UserListCellRenderer());

        JScrollPane membersScroll = new JScrollPane(teamMembersList);
        membersPanel.add(membersScroll, BorderLayout.CENTER);

        listsPanel.add(availablePanel);
        listsPanel.add(membersPanel);

        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Adicionar →");
        JButton removeButton = new JButton("← Remover");
        JButton setLeaderButton = new JButton("Definir como Líder");

        addButton.addActionListener(e -> addMembersToTeam());
        removeButton.addActionListener(e -> removeMembersFromTeam());
        setLeaderButton.addActionListener(e -> setTeamLeader());

        actionPanel.add(addButton);
        actionPanel.add(removeButton);
        actionPanel.add(setLeaderButton);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> saveChanges());
        cancelButton.addActionListener(e -> dispose());

        controlPanel.add(saveButton);
        controlPanel.add(cancelButton);

        mainPanel.add(listsPanel, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadUsers() {
        List<User> allUsers = userService.getActiveUsers();
        List<User> teamMembers = teamService.getUsersInTeam(team.getId());

        availableUsersModel.clear();
        teamMembersModel.clear();

        for (User user : allUsers) {
            if (teamMembers.stream().anyMatch(member -> member.getId() == user.getId())) {
                teamMembersModel.addElement(user);
            } else {
                availableUsersModel.addElement(user);
            }
        }
    }

    private void addMembersToTeam() {
        List<User> selectedUsers = availableUsersList.getSelectedValuesList();
        if (selectedUsers.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Selecione usuários para adicionar à equipe.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (User user : selectedUsers) {
            if (teamService.addMemberToTeam(team.getId(), user.getId(), "Membro")) {
                availableUsersModel.removeElement(user);
                teamMembersModel.addElement(user);
            }
        }
        updated = true;
    }

    private void removeMembersFromTeam() {
        List<User> selectedUsers = teamMembersList.getSelectedValuesList();
        if (selectedUsers.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Selecione membros para remover da equipe.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover " + selectedUsers.size() + " membro(s) da equipe?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            for (User user : selectedUsers) {
                if (teamService.removeMemberFromTeam(team.getId(), user.getId())) {
                    teamMembersModel.removeElement(user);
                    availableUsersModel.addElement(user);
                }
            }
            updated = true;
        }
    }

    private void setTeamLeader() {
        User selectedUser = teamMembersList.getSelectedValue();
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um membro para definir como líder.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (teamService.updateMemberRole(team.getId(), selectedUser.getId(), "Líder")) {
            JOptionPane.showMessageDialog(this,
                    selectedUser.getNomeCompleto() + " definido como líder da equipe.",
                    "Líder Definido",
                    JOptionPane.INFORMATION_MESSAGE);
            updated = true;
        }
    }

    private void saveChanges() {
        dispose();
    }

    public boolean isUpdated() {
        return updated;
    }
}

class UserListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof User) {
            User user = (User) value;
            setText(user.getNomeCompleto() + " - " + user.getCargo());
            setToolTipText(user.getEmail());
        }
        return this;
    }
}

class TeamTableModel extends AbstractTableModel {
    private List<Team> teams;
    private String[] columnNames = {"ID", "Nome", "Descrição", "Data Criação", "Nº de Membros"};

    public TeamTableModel(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public int getRowCount() {
        return teams.size();
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
        Team team = teams.get(rowIndex);
        switch (columnIndex) {
            case 0: return team.getId();
            case 1: return team.getNome();
            case 2: return team.getDescricao();
            case 3: return team.getCriadoEm().toLocalDate().toString();
            case 4: return "Membros: " + getMemberCount(team.getId());
            default: return null;
        }
    }

    private int getMemberCount(int teamId) {
        TeamService teamService = new TeamService();
        return teamService.getUsersInTeam(teamId).size();
    }

    public Team getTeamAt(int row) {
        return teams.get(row);
    }
}