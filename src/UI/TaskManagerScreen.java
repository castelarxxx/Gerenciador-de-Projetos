package UI;

import Service.TaskService;
import model.Task;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TaskManagerScreen extends JFrame {
    private TaskService taskService;
    private JTable taskTable;

    public TaskManagerScreen() {
        this.taskService = new TaskService();
        initComponents();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Tarefas");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        taskTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(taskTable);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

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
        JOptionPane.showMessageDialog(this, "Funcionalidade de adicionar tarefa será implementada aqui.");
    }

    private void editTask(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidade de editar tarefa será implementada aqui.");
    }

    private void deleteTask(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidade de excluir tarefa será implementada aqui.");
    }

    private void changeTaskStatus(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidade de alterar status será implementada aqui.");
    }
}

class TaskTableModel extends AbstractTableModel {
    private List<Task> tasks;
    private String[] columnNames = {"ID", "Título", "Descrição", "Projeto", "Status", "Prioridade"};

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
            case 2: return task.getDescricao();
            case 3: return "Projeto " + task.getProjetoId();
            case 4: return task.getStatus() != null ? task.getStatus() : "pendente";
            case 5: return task.getPrioridade() != null ? task.getPrioridade() : "media";
            default: return null;
        }
    }

    public Task getTaskAt(int row) {
        return tasks.get(row);
    }
}