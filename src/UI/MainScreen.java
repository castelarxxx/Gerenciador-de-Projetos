package UI;


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


        JLabel titleLabel = new JLabel("Dashboard - Visão Geral", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        metricsPanel.add(createMetricCard("Projetos Ativos", "15"));
        metricsPanel.add(createMetricCard("Tarefas Pendentes", "42"));
        metricsPanel.add(createMetricCard("Equipes", "5"));
        metricsPanel.add(createMetricCard("Colaboradores", "28"));

        panel.add(metricsPanel, BorderLayout.CENTER);

        return panel;
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