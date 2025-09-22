package UI;


import Service.ProjectService;
import Service.TaskService;
import Service.TeamService;
import Service.UserService;

import javax.swing.*;
import java.awt.*;

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


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Novo Projeto");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Nome", "Status", "Início", "Término Previsto", "Gerente"};
        Object[][] data = {
                {1, "Sistema de Gestão", "Em Andamento", "2023-01-15", "2023-06-30", "João Silva"},
                {2, "Site Corporativo", "Concluído", "2023-02-01", "2023-04-15", "Maria Santos"},
                {3, "App Mobile", "Planejado", "2023-05-01", "2023-09-30", "Carlos Oliveira"}
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
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