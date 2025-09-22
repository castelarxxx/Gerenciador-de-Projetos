package UI;

import Service.TaskService;
import Service.ProjectService;
import Service.UserService;
import model.Task;
import model.Project;
import model.User;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TaskManagerScreen extends JFrame {
    private TaskService taskService;
    private ProjectService projectService;
    private UserService userService;
    private JTable taskTable;

    public TaskManagerScreen() {
        this.taskService = new TaskService();
        this.projectService = new ProjectService();
        this.userService = new UserService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Tarefas");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Nova Tarefa");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");
        JButton refreshButton = new JButton("Atualizar");
        JButton changeStatusButton = new JButton("Alterar Status");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(changeStatusButton);

        // Tabela de tarefas
        taskTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(taskTable);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Configura ações dos botões
        addButton.addActionListener(this::addTask);
        editButton.addActionListener(this::editTask);
        deleteButton.addActionListener(this::deleteTask);
        refreshButton.addActionListener(e -> refreshTable());
        changeStatusButton.addActionListener(this::changeTaskStatus);

        refreshTable();
    }

    private void refreshTable() {
        List<Task> tasks = taskService.getAllTasks();
        TaskTableModel model = new TaskTableModel(tasks);
        taskTable.setModel(model);
    }

    private void addTask(ActionEvent e) {
        TaskFormDialog dialog = new TaskFormDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refreshTable();
            JOptionPane.showMessageDialog(this, "Tarefa adicionada com sucesso!");
        }
    }

    private void editTask(ActionEvent e) {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            TaskTableModel model = (TaskTableModel) taskTable.getModel();
            Task task = model.getTaskAt(selectedRow);

            TaskFormDialog dialog = new TaskFormDialog(this, task);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Tarefa atualizada com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma tarefa para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTask(ActionEvent e) {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            TaskTableModel model = (TaskTableModel) taskTable.getModel();
            Task task = model.getTaskAt(selectedRow);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir a tarefa '" + task.getTitulo() + "'?",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (taskService.deleteTask(task.getId())) {
                    JOptionPane.showMessageDialog(this, "Tarefa excluída com sucesso.");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir tarefa.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma tarefa para excluir.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void changeTaskStatus(ActionEvent e) {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow >= 0) {
            TaskTableModel model = (TaskTableModel) taskTable.getModel();
            Task task = model.getTaskAt(selectedRow);

            TaskStatusDialog dialog = new TaskStatusDialog(this, task);
            dialog.setVisible(true);
            if (dialog.isUpdated()) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Status da tarefa atualizado com sucesso!");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma tarefa para alterar o status.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}

// Diálogo de formulário para tarefas
class TaskFormDialog extends JDialog {
    private TaskService taskService;
    private ProjectService projectService;
    private UserService userService;
    private Task task;
    private boolean saved = false;

    private JTextField tituloField;
    private JTextArea descricaoArea;
    private JComboBox<Project> projetoCombo;
    private JComboBox<User> responsavelCombo;
    private JComboBox<String> prioridadeCombo;
    private JTextField dataInicioField;
    private JTextField dataFimField;

    public TaskFormDialog(JFrame parent, Task task) {
        super(parent, true);
        this.taskService = new TaskService();
        this.projectService = new ProjectService();
        this.userService = new UserService();
        this.task = task;

        initComponents();
        if (task != null) {
            fillForm();
        }
    }

    private void initComponents() {
        setTitle(task == null ? "Nova Tarefa" : "Editar Tarefa");
        setSize(500, 450);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        formPanel.add(new JLabel("Título:"));
        tituloField = new JTextField();
        formPanel.add(tituloField);

        formPanel.add(new JLabel("Descrição:"));
        descricaoArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(descricaoArea);
        formPanel.add(scrollPane);

        formPanel.add(new JLabel("Projeto:"));
        List<Project> projetos = projectService.getAllProjects();
        projetoCombo = new JComboBox<>(projetos.toArray(new Project[0]));
        projetoCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Project) {
                    Project project = (Project) value;
                    setText(project.getNome() + " (" + project.getStatus() + ")");
                }
                return this;
            }
        });
        formPanel.add(projetoCombo);

        formPanel.add(new JLabel("Responsável:"));
        List<User> usuarios = userService.getActiveUsers();
        responsavelCombo = new JComboBox<>(usuarios.toArray(new User[0]));
        responsavelCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    User user = (User) value;
                    setText(user.getNomeCompleto() + " (" + user.getCargo() + ")");
                }
                return this;
            }
        });
        formPanel.add(responsavelCombo);

        formPanel.add(new JLabel("Prioridade:"));
        prioridadeCombo = new JComboBox<>(new String[]{"baixa", "media", "alta", "urgente"});
        prioridadeCombo.setSelectedItem("media");
        formPanel.add(prioridadeCombo);

        formPanel.add(new JLabel("Data Início Prevista (YYYY-MM-DD):"));
        dataInicioField = new JTextField();
        formPanel.add(dataInicioField);

        formPanel.add(new JLabel("Data Fim Prevista (YYYY-MM-DD):"));
        dataFimField = new JTextField();
        formPanel.add(dataFimField);

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
        saveButton.addActionListener(this::saveTask);
        cancelButton.addActionListener(e -> dispose());
    }

    private void fillForm() {
        tituloField.setText(task.getTitulo());
        descricaoArea.setText(task.getDescricao());
        prioridadeCombo.setSelectedItem(task.getPrioridade());

        if (task.getDataInicioPrevista() != null) {
            dataInicioField.setText(task.getDataInicioPrevista().toString());
        }

        if (task.getDataFimPrevista() != null) {
            dataFimField.setText(task.getDataFimPrevista().toString());
        }

        // Selecionar o projeto atual
        for (int i = 0; i < projetoCombo.getItemCount(); i++) {
            Project project = projetoCombo.getItemAt(i);
            if (project.getId() == task.getProjetoId()) {
                projetoCombo.setSelectedIndex(i);
                break;
            }
        }

        // Selecionar o responsável atual
        if (task.getResponsavelId() != null) {
            for (int i = 0; i < responsavelCombo.getItemCount(); i++) {
                User user = responsavelCombo.getItemAt(i);
                if (user.getId() == task.getResponsavelId()) {
                    responsavelCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void saveTask(ActionEvent e) {
        // Validação
        if (tituloField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Título da tarefa é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (projetoCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um projeto.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Task taskToSave;
            if (task == null) {
                taskToSave = new Task();
            } else {
                taskToSave = task;
            }

            taskToSave.setTitulo(tituloField.getText().trim());
            taskToSave.setDescricao(descricaoArea.getText().trim());
            taskToSave.setPrioridade((String) prioridadeCombo.getSelectedItem());

            // Obter projeto selecionado
            Project selectedProject = (Project) projetoCombo.getSelectedItem();
            taskToSave.setProjetoId(selectedProject.getId());

            // Obter responsável selecionado
            User selectedResponsavel = (User) responsavelCombo.getSelectedItem();
            if (selectedResponsavel != null) {
                taskToSave.setResponsavelId(selectedResponsavel.getId());
            }

            // Converter datas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (!dataInicioField.getText().trim().isEmpty()) {
                LocalDate dataInicio = LocalDate.parse(dataInicioField.getText().trim(), formatter);
                taskToSave.setDataInicioPrevista(dataInicio);

                // Validar data fim
                if (!dataFimField.getText().trim().isEmpty()) {
                    LocalDate dataFim = LocalDate.parse(dataFimField.getText().trim(), formatter);
                    if (dataFim.isBefore(dataInicio)) {
                        JOptionPane.showMessageDialog(this,
                                "Data de fim não pode ser anterior à data de início.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    taskToSave.setDataFimPrevista(dataFim);
                }
            }

            boolean success;
            if (task == null) {
                success = taskService.createTask(taskToSave);
            } else {
                success = taskService.updateTask(taskToSave);
            }

            if (success) {
                saved = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar tarefa. Verifique os dados.",
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

// Diálogo para alterar status da tarefa
class TaskStatusDialog extends JDialog {
    private TaskService taskService;
    private Task task;
    private boolean updated = false;

    private JComboBox<String> statusCombo;
    private JTextArea comentarioArea;
    private JLabel infoLabel;

    public TaskStatusDialog(JFrame parent, Task task) {
        super(parent, true);
        this.taskService = new TaskService();
        this.task = task;

        initComponents();
    }

    private void initComponents() {
        setTitle("Alterar Status da Tarefa");
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Informações da tarefa
        infoLabel = new JLabel("<html><b>Tarefa:</b> " + task.getTitulo() +
                "<br><b>Status Atual:</b> " + task.getStatus() + "</html>");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Formulário de status
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));

        formPanel.add(new JLabel("Novo Status:"));
        statusCombo = new JComboBox<>(new String[]{"pendente", "em_execucao", "concluida", "cancelada"});
        statusCombo.setSelectedItem(task.getStatus());
        formPanel.add(statusCombo);

        formPanel.add(new JLabel("Comentário:"));
        comentarioArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(comentarioArea);
        formPanel.add(scrollPane);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Atualizar Status");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(this::updateStatus);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(infoLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void updateStatus(ActionEvent e) {
        String novoStatus = (String) statusCombo.getSelectedItem();
        String comentario = comentarioArea.getText().trim();

        if (novoStatus.equals(task.getStatus())) {
            JOptionPane.showMessageDialog(this,
                    "O status selecionado é o mesmo que o atual.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Supondo que temos um usuário logado (em um sistema real, pegaria do contexto de autenticação)
            int usuarioLogadoId = 1; // ID do usuário admin por padrão

            if (taskService.updateTaskStatus(task.getId(), novoStatus, usuarioLogadoId, comentario)) {
                updated = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erro ao atualizar status da tarefa.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao processar: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isUpdated() {
        return updated;
    }
}

// Modelo de tabela para tarefas (versão robusta)
class TaskTableModel extends AbstractTableModel {
    private List<Task> tasks;
    private String[] columnNames = {"ID", "Título", "Descrição", "Projeto", "Responsável", "Status", "Prioridade", "Início Previsto", "Término Previsto"};

    public TaskTableModel(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
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
        Task task = tasks.get(rowIndex);
        switch (columnIndex) {
            case 0: return task.getId();
            case 1: return task.getTitulo();
            case 2: return task.getDescricao() != null ?
                    (task.getDescricao().length() > 50 ? task.getDescricao().substring(0, 47) + "..." : task.getDescricao()) : "";
            case 3: return "Projeto ID: " + task.getProjetoId();
            case 4: return task.getResponsavelId() != null ? "User ID: " + task.getResponsavelId() : "Não atribuído";
            case 5: return getStatusDisplay(task.getStatus());
            case 6: return getPrioridadeDisplay(task.getPrioridade());
            case 7: return task.getDataInicioPrevista();
            case 8: return task.getDataFimPrevista();
            default: return null;
        }
    }

    private String getStatusDisplay(String status) {
        if (status == null) return "❓ Não definido";

        switch (status) {
            case "pendente": return "⏳ Pendente";
            case "em_execucao": return "🚀 Em Execução";
            case "concluida": return "✅ Concluída";
            case "cancelada": return "❌ Cancelada";
            default: return status;
        }
    }

    private String getPrioridadeDisplay(String prioridade) {
        if (prioridade == null) return "⚪ Não definida";

        switch (prioridade) {
            case "baixa": return "🔵 Baixa";
            case "media": return "🟡 Média";
            case "alta": return "🔴 Alta";
            case "urgente": return "⚡ Urgente";
            default: return prioridade;
        }
    }

    public Task getTaskAt(int row) {
        return tasks.get(row);
    }
}