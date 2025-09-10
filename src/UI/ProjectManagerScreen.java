package UI;


import Service.ProjectService;
import model.Project;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ProjectManagerScreen extends JFrame {
    private ProjectService projectService;
    private JTable projectTable;

    public ProjectManagerScreen() {
        this.projectService = new ProjectService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Projetos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Novo Projeto");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");
        JButton refreshButton = new JButton("Atualizar");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        projectTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(projectTable);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        addButton.addActionListener(this::addProject);
        editButton.addActionListener(this::editProject);
        deleteButton.addActionListener(this::deleteProject);
        refreshButton.addActionListener(e -> refreshTable());

        refreshTable();
    }

    private void refreshTable() {
        List<Project> projects = projectService.getAllProjects();
        ProjectTableModel model = new ProjectTableModel(projects);
        projectTable.setModel(model);
    }

    private void addProject(ActionEvent e) {
       
        JOptionPane.showMessageDialog(this, "Funcionalidade de adicionar projeto será implementada aqui.");
    }

    private void editProject(ActionEvent e) {
        
        JOptionPane.showMessageDialog(this, "Funcionalidade de editar projeto será implementada aqui.");
    }

    private void deleteProject(ActionEvent e) {
       
        JOptionPane.showMessageDialog(this, "Funcionalidade de excluir projeto será implementada aqui.");
    }
}


class ProjectTableModel extends AbstractTableModel {
    private List<Project> projects;
    private String[] columnNames = {"ID", "Nome", "Descrição", "Status", "Início", "Término Previsto", "Gerente"};

    public ProjectTableModel(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public int getRowCount() {
        return projects.size();
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
        Project project = projects.get(rowIndex);
        switch (columnIndex) {
            case 0: return project.getId();
            case 1: return project.getNome();
            case 2: return project.getDescricao();
            case 3: return project.getStatus();
            case 4: return project.getDataInicio();
            case 5: return project.getDataTerminoPrevista();
            case 6: return "Gerente " + project.getGerenteId();
            default: return null;
        }
    }

    public Project getProjectAt(int row) {
        return projects.get(row);
    }
}
