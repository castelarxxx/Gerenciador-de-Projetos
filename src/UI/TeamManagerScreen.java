package UI;

import Service.TeamService;
import model.Team;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TeamManagerScreen extends JFrame {
    private TeamService teamService;
    private JTable teamTable;

    public TeamManagerScreen() {
        this.teamService = new TeamService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Equipes");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de botões
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

        // Tabela de equipes
        teamTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(teamTable);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Configura ações dos botões
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
        JOptionPane.showMessageDialog(this, "Funcionalidade de adicionar equipe será implementada aqui.");
    }

    private void editTeam(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidade de editar equipe será implementada aqui.");
    }

    private void deleteTeam(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidade de excluir equipe será implementada aqui.");
    }

    private void manageTeamMembers(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidade de gerenciar membros será implementada aqui.");
    }
}

// Modelo de tabela para equipes
class TeamTableModel extends AbstractTableModel {
    private List<Team> teams;
    private String[] columnNames = {"ID", "Nome", "Descrição", "Data Criação"};

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
            case 3: return team.getCriadoEm();
            default: return null;
        }
    }

    public Team getTeamAt(int row) {
        return teams.get(row);
    }
}