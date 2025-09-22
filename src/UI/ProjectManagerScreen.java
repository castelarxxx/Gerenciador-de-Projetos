package UI;

import Service.ProjectService;
import model.Project;
import Service.UserService;
import model.User;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ProjectManagerScreen extends JFrame {
    private ProjectService projectService;
    private UserService userService;
    private JTable projectTable;

    public ProjectManagerScreen() {
        this.projectService = new ProjectService();
        this.userService = new UserService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Projetos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Novo Projeto");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");


        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);


        projectTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(projectTable);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Configura ações dos botões
        addButton.addActionListener(this::addProject);
        editButton.addActionListener(this::editProject);
        deleteButton.addActionListener(this::deleteProject);
        refreshTable();
    }

    private void refreshTable() {
        List<Project> projects = projectService.getAllProjects();
        ProjectTableModel model = new ProjectTableModel(projects);
        projectTable.setModel(model);
    }

    private void addProject(ActionEvent e) {
        ProjectFormDialog dialog = new ProjectFormDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refreshTable();
            JOptionPane.showMessageDialog(this, "Projeto adicionado com sucesso!");
        }
    }

    private void editProject(ActionEvent e) {
        int selectedRow = projectTable.getSelectedRow();
        if (selectedRow >= 0) {
            ProjectTableModel model = (ProjectTableModel) projectTable.getModel();
            Project project = model.getProjectAt(selectedRow);

            ProjectFormDialog dialog = new ProjectFormDialog(this, project);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Projeto atualizado com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione um projeto para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteProject(ActionEvent e) {
        int selectedRow = projectTable.getSelectedRow();
        if (selectedRow >= 0) {
            ProjectTableModel model = (ProjectTableModel) projectTable.getModel();
            Project project = model.getProjectAt(selectedRow);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o projeto '" + project.getNome() + "'?\n" +
                            "Esta ação também excluirá todas as tarefas associadas.",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (projectService.deleteProject(project.getId())) {
                    JOptionPane.showMessageDialog(this, "Projeto excluído com sucesso.");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir projeto. Verifique se não há dependências.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione um projeto para excluir.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}


class ProjectFormDialog extends JDialog {
    private ProjectService projectService;
    private UserService userService;
    private Project project;
    private boolean saved = false;

    private JTextField nomeField;
    private JTextArea descricaoArea;
    private JComboBox<String> statusCombo;
    private JComboBox<User> gerenteCombo;
    private JTextField dataInicioField;
    private JTextField dataTerminoField;

    public ProjectFormDialog(JFrame parent, Project project) {
        super(parent, true);
        this.projectService = new ProjectService();
        this.userService = new UserService();
        this.project = project;

        initComponents();
        if (project != null) {
            fillForm();
        }
    }

    private void initComponents() {
        setTitle(project == null ? "Novo Projeto" : "Editar Projeto");
        setSize(500, 400);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("Descrição:"));
        descricaoArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(descricaoArea);
        formPanel.add(scrollPane);

        formPanel.add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(new String[]{
                "planejado", "em_andamento", "concluido", "cancelado"
        });
        formPanel.add(statusCombo);

        formPanel.add(new JLabel("Gerente:"));
        List<User> gerentes = userService.getUsersByRole(1); // Gerentes (role_id = 2)
        gerenteCombo = new JComboBox<>(gerentes.toArray(new User[0]));
        gerenteCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    User user = (User) value;
                    setText(user.getNomeCompleto() + " (" + user.getEmail() + ")");
                }
                return this;
            }
        });
        formPanel.add(gerenteCombo);

        formPanel.add(new JLabel("Data Início (YYYY-MM-DD):"));
        dataInicioField = new JTextField();
        formPanel.add(dataInicioField);

        formPanel.add(new JLabel("Data Término Prevista (YYYY-MM-DD):"));
        dataTerminoField = new JTextField();
        formPanel.add(dataTerminoField);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Salvar");
        JButton cancelButton = new JButton("Cancelar");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);


        saveButton.addActionListener(this::saveProject);
        cancelButton.addActionListener(e -> dispose());
    }

    private void fillForm() {
        nomeField.setText(project.getNome());
        descricaoArea.setText(project.getDescricao());
        statusCombo.setSelectedItem(project.getStatus());

        if (project.getDataInicio() != null) {
            dataInicioField.setText(project.getDataInicio().toString());
        }

        if (project.getDataTerminoPrevista() != null) {
            dataTerminoField.setText(project.getDataTerminoPrevista().toString());
        }


        if (project.getGerenteId() > 0) {
            for (int i = 0; i < gerenteCombo.getItemCount(); i++) {
                User user = gerenteCombo.getItemAt(i);
                if (user.getId() == project.getGerenteId()) {
                    gerenteCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void saveProject(ActionEvent e) {

        if (nomeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome do projeto é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Project projectToSave;
            if (project == null) {
                projectToSave = new Project();
            } else {
                projectToSave = project;
            }

            projectToSave.setNome(nomeField.getText().trim());
            projectToSave.setDescricao(descricaoArea.getText().trim());
            projectToSave.setStatus((String) statusCombo.getSelectedItem());


            User selectedGerente = (User) gerenteCombo.getSelectedItem();
            if (selectedGerente != null) {
                projectToSave.setGerenteId(selectedGerente.getId());
            }


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (!dataInicioField.getText().trim().isEmpty()) {
                projectToSave.setDataInicio(LocalDate.parse(dataInicioField.getText().trim(), formatter));
            }

            if (!dataTerminoField.getText().trim().isEmpty()) {
                projectToSave.setDataTerminoPrevista(LocalDate.parse(dataTerminoField.getText().trim(), formatter));
            }

            boolean success;
            if (project == null) {
                success = projectService.createProject(projectToSave);
            } else {
                success = projectService.updateProject(projectToSave);
            }

            if (success) {
                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar projeto. Verifique os dados.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Formato de data inválido. Use o formato YYYY-MM-DD.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
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
            case 2: return getStatusDisplay(project.getStatus());
            case 3: return project.getDataInicio() != null ? project.getDataInicio().toString() : "Não definida";
            case 4: return project.getDataTerminoPrevista() != null ? project.getDataTerminoPrevista().toString() : "Não definida";
            case 5: return getGerenteDisplay(project.getGerenteId());
            default: return null;
        }
    }

    private String getStatusDisplay(String status) {
        if (status == null) return "Não definido";

        switch (status) {
            case "planejado": return "📋 Planejado";
            case "em_andamento": return "🚀 Em Andamento";
            case "concluido": return "✅ Concluído";
            case "cancelado": return "❌ Cancelado";
            default: return status;
        }
    }

    private String getGerenteDisplay(int gerenteId) {
        if (gerenteId <= 0) return "Não atribuído";

        try {
            UserService userService = new UserService();
            User gerente = userService.getUserById(gerenteId);
            return gerente != null ? gerente.getNomeCompleto() : "ID: " + gerenteId;
        } catch (Exception e) {
            return "ID: " + gerenteId;
        }
    }

    public Project getProjectAt(int row) {
        return projects.get(row);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void setProjects(List<Project> novosProjetos) {
    }
}