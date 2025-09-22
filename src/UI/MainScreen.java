package UI;


import Service.ProjectService;
import Service.TaskService;
import Service.TeamService;
import Service.UserService;
import model.Project;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends JFrame {

    public MainScreen() {
        initComponents();
        setupWindow();
    }

    private void initComponents() {

        setTitle("Sistema de Gerenciamento de Projetos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);


        JMenuBar menuBar = new JMenuBar();


        JMenu fileMenu = new JMenu("Opções");
        JMenuItem exitItem = new JMenuItem("Sair");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);


        JMenu cadastroMenu = new JMenu("Cadastros");
        JMenuItem usersItem = new JMenuItem("Usuários");
        JMenuItem projectsItem = new JMenuItem("Projetos");
        JMenuItem teamsItem = new JMenuItem("Equipes");
        JMenuItem tasksItem = new JMenuItem("Tarefas");

        usersItem.addActionListener(e -> openUserManager());
        projectsItem.addActionListener(e -> openProjectManager());
        teamsItem.addActionListener(e -> openTeamManager());
        tasksItem.addActionListener(e -> openTaskManager());

        cadastroMenu.add(usersItem);
        cadastroMenu.add(projectsItem);
        cadastroMenu.add(teamsItem);
        cadastroMenu.add(tasksItem);


        JMenu reportsMenu = new JMenu("Relatórios");
        JMenuItem projectReportItem = new JMenuItem("Andamento de Projetos");
        JMenuItem performanceReportItem = new JMenuItem("Desempenho de Colaboradores");
        JMenuItem riskReportItem = new JMenuItem("Projetos em Risco");

        reportsMenu.add(projectReportItem);
        reportsMenu.add(performanceReportItem);
        reportsMenu.add(riskReportItem);


        menuBar.add(fileMenu);
        menuBar.add(cadastroMenu);
        menuBar.add(reportsMenu);

        setJMenuBar(menuBar);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel dashboardPanel = createDashboardPanel();
        tabbedPane.addTab("Dashboard", dashboardPanel);


        JPanel projectsPanel = createProjectsPanel();
        tabbedPane.addTab("Projetos", projectsPanel);

        add(tabbedPane);
    }

    private void setupWindow() {
        setVisible(true);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Título
        JLabel titleLabel = new JLabel("Dashboard - Visão Geral", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Buscar dados reais do banco
        int projetosAtivos = getProjetosAtivosCount();
        int tarefasPendentes = getTarefasPendentesCount();
        int totalEquipes = getTotalEquipesCount();
        int colaboradoresAtivos = getColaboradoresAtivosCount();
        int totalProjetos = getTotalProjetosCount();
        int projetosConcluidos = getProjetosConcluidosCount();
        int tarefasConcluidas = getTarefasConcluidasCount();
        int tarefasEmAndamento = getTarefasEmAndamentoCount();

        // Painel com métricas
        JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Métricas principais com dados reais
        metricsPanel.add(createMetricCard("Projetos Ativos", String.valueOf(projetosAtivos),
                "Total: " + totalProjetos + " | Concluídos: " + projetosConcluidos, Color.BLUE));

        metricsPanel.add(createMetricCard("Tarefas Pendentes", String.valueOf(tarefasPendentes),
                "Em andamento: " + tarefasEmAndamento + " | Concluídas: " + tarefasConcluidas, Color.ORANGE));

        metricsPanel.add(createMetricCard("Equipes", String.valueOf(totalEquipes),
                "Equipes cadastradas no sistema", Color.GREEN));

        metricsPanel.add(createMetricCard("Colaboradores", String.valueOf(colaboradoresAtivos),
                "Usuários ativos no sistema", Color.MAGENTA));

        panel.add(metricsPanel, BorderLayout.CENTER);

        // Adicionar botão de atualização
        JButton refreshButton = new JButton("🔄 Atualizar Dashboard");
        refreshButton.addActionListener(e -> refreshDashboard(metricsPanel));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private int getProjetosAtivosCount() {
        try {
            ProjectService projectService = new ProjectService();
            return projectService.getActiveProjectsCount();
        } catch (Exception e) {
            System.err.println("Erro ao buscar projetos ativos: " + e.getMessage());
            return 0;
        }
    }

    private int getTarefasPendentesCount() {
        try {
            TaskService taskService = new TaskService();
            return taskService.getPendingTasksCount();
        } catch (Exception e) {
            System.err.println("Erro ao buscar tarefas pendentes: " + e.getMessage());
            return 0;
        }
    }

    private int getTotalEquipesCount() {
        try {
            TeamService teamService = new TeamService();
            return teamService.getTotalTeamsCount();
        } catch (Exception e) {
            System.err.println("Erro ao buscar total de equipes: " + e.getMessage());
            return 0;
        }
    }

    private int getColaboradoresAtivosCount() {
        try {
            UserService userService = new UserService();
            return userService.getActiveUsersCount();
        } catch (Exception e) {
            System.err.println("Erro ao buscar colaboradores ativos: " + e.getMessage());
            return 0;
        }
    }

    private int getTotalProjetosCount() {
        try {
            ProjectService projectService = new ProjectService();
            return projectService.getTotalProjectsCount();
        } catch (Exception e) {
            System.err.println("Erro ao buscar total de projetos: " + e.getMessage());
            return 0;
        }
    }

    private int getProjetosConcluidosCount() {
        try {
            ProjectService projectService = new ProjectService();
            return projectService.getCompletedProjectsCount();
        } catch (Exception e) {
            System.err.println("Erro ao buscar projetos concluídos: " + e.getMessage());
            return 0;
        }
    }

    private int getTarefasConcluidasCount() {
        try {
            TaskService taskService = new TaskService();
            return taskService.getCompletedTasksCount();
        } catch (Exception e) {
            System.err.println("Erro ao buscar tarefas concluídas: " + e.getMessage());
            return 0;
        }
    }

    private int getTarefasEmAndamentoCount() {
        try {
            TaskService taskService = new TaskService();
            return taskService.getInProgressTasksCount();
        } catch (Exception e) {
            System.err.println("Erro ao buscar tarefas em andamento: " + e.getMessage());
            return 0;
        }
    }

    // Método para criar cards de métricas com subtítulo
    private JPanel createMetricCard(String title, String value, String subtitle, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.brighter(), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.DARK_GRAY);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(color);

        JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        subtitleLabel.setForeground(Color.GRAY);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(subtitleLabel, BorderLayout.SOUTH);

        // Efeito hover (opcional)
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(color.brighter().brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
            }
        });

        return card;
    }

    // Método para atualizar o dashboard
    private void refreshDashboard(JPanel metricsPanel) {
        // Buscar dados atualizados
        int projetosAtivos = getProjetosAtivosCount();
        int tarefasPendentes = getTarefasPendentesCount();
        int totalEquipes = getTotalEquipesCount();
        int colaboradoresAtivos = getColaboradoresAtivosCount();
        int totalProjetos = getTotalProjetosCount();
        int projetosConcluidos = getProjetosConcluidosCount();
        int tarefasConcluidas = getTarefasConcluidasCount();
        int tarefasEmAndamento = getTarefasEmAndamentoCount();

        // Limpar painel atual
        metricsPanel.removeAll();

        // Adicionar cards atualizados
        metricsPanel.add(createMetricCard("Projetos Ativos", String.valueOf(projetosAtivos),
                "Total: " + totalProjetos + " | Concluídos: " + projetosConcluidos, Color.BLUE));

        metricsPanel.add(createMetricCard("Tarefas Pendentes", String.valueOf(tarefasPendentes),
                "Em andamento: " + tarefasEmAndamento + " | Concluídas: " + tarefasConcluidas, Color.ORANGE));

        metricsPanel.add(createMetricCard("Equipes", String.valueOf(totalEquipes),
                "Equipes cadastradas no sistema", Color.GREEN));

        metricsPanel.add(createMetricCard("Colaboradores", String.valueOf(colaboradoresAtivos),
                "Usuários ativos no sistema", Color.MAGENTA));

        // Atualizar a interface
        metricsPanel.revalidate();
        metricsPanel.repaint();

        JOptionPane.showMessageDialog(this, "Dashboard atualizado com sucesso!",
                "Atualização", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createMetricCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(new Color(0, 100, 200));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Novo Projeto");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");
        JButton refreshButton = new JButton("Atualizar");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Buscar projetos do banco de dados
        List<Project> projetos = getProjetosFromDatabase();

        // Criar modelo de tabela com dados reais
        ProjectTableModel tableModel = new ProjectTableModel(projetos);
        JTable table = new JTable(tableModel);

        // Configurar a tabela
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);


        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Configurar ações dos botões
        addButton.addActionListener(e -> adicionarProjeto(tableModel));
        editButton.addActionListener(e -> editarProjeto(table, tableModel));
        deleteButton.addActionListener(e -> excluirProjeto(table, tableModel));
        refreshButton.addActionListener(e -> atualizarTabela(tableModel));

        return panel;
    }

    // Método para buscar projetos do banco
    private List<Project> getProjetosFromDatabase() {
        try {
            ProjectService projectService = new ProjectService();
            return projectService.getAllProjects();
        } catch (Exception e) {
            System.err.println("Erro ao buscar projetos: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar projetos do banco de dados.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    // Método para atualizar a tabela
    private void atualizarTabela(ProjectTableModel tableModel) {
        List<Project> novosProjetos = getProjetosFromDatabase();
        tableModel.setProjects(novosProjetos);
        tableModel.fireTableDataChanged();
        JOptionPane.showMessageDialog(this,
                "Lista de projetos atualizada!",
                "Atualização",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Ação de adicionar projeto
    private void adicionarProjeto(ProjectTableModel tableModel) {
        ProjectFormDialog dialog = new ProjectFormDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            atualizarTabela(tableModel);
        }
    }

    // Ação de editar projeto
    private void editarProjeto(JTable table, ProjectTableModel tableModel) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Project projeto = tableModel.getProjectAt(selectedRow);
            ProjectFormDialog dialog = new ProjectFormDialog(this, projeto);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                atualizarTabela(tableModel);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Selecione um projeto para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    // Ação de excluir projeto
    private void excluirProjeto(JTable table, ProjectTableModel tableModel) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Project projeto = tableModel.getProjectAt(selectedRow);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o projeto '" + projeto.getNome() + "'?\n" +
                            "Esta ação também excluirá todas as tarefas associadas.",
                    "Confirmar Exclusão",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ProjectService projectService = new ProjectService();
                    if (projectService.deleteProject(projeto.getId())) {
                        JOptionPane.showMessageDialog(this, "Projeto excluído com sucesso.");
                        atualizarTabela(tableModel);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Erro ao excluir projeto.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir projeto: " + e.getMessage(),
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

    private void openUserManager() {
        UserManagerScreen userManager = new UserManagerScreen();
        userManager.setVisible(true);
    }

    private void openProjectManager() {
        ProjectManagerScreen projectManager = new ProjectManagerScreen();
        projectManager.setVisible(true);
    }

    private void openTeamManager() {
        TeamManagerScreen teamManager = new TeamManagerScreen();
        teamManager.setVisible(true);
    }

    private void openTaskManager() {
        TaskManagerScreen taskManager = new TaskManagerScreen();
        taskManager.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainScreen();
        });
    }
}