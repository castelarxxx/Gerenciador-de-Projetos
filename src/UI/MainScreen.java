package UI;


import Service.ProjectService;
import Service.TaskService;
import Service.TeamService;
import Service.UserService;
import model.Project;
import model.Task;
import model.User;

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


        projectReportItem.addActionListener(e -> gerarRelatorioAndamentoProjetos());
        performanceReportItem.addActionListener(e -> gerarRelatorioDesempenhoColaboradores());
        riskReportItem.addActionListener(e -> gerarRelatorioProjetosRisco());

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

        int projetosAtivos = getProjetosAtivosCount();
        int tarefasPendentes = getTarefasPendentesCount();
        int totalEquipes = getTotalEquipesCount();
        int colaboradoresAtivos = getColaboradoresAtivosCount();
        int totalProjetos = getTotalProjetosCount();
        int projetosConcluidos = getProjetosConcluidosCount();
        int tarefasConcluidas = getTarefasConcluidasCount();
        int tarefasEmAndamento = getTarefasEmAndamentoCount();

        JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        List<Project> projetos = getProjetosFromDatabase();

        ProjectTableModel tableModel = new ProjectTableModel(projetos);
        JTable table = new JTable(tableModel);

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


    private void atualizarTabela(ProjectTableModel tableModel) {
        List<Project> novosProjetos = getProjetosFromDatabase();
        tableModel.setProjects(novosProjetos);
        tableModel.fireTableDataChanged();
        JOptionPane.showMessageDialog(this,
                "Lista de projetos atualizada!",
                "Atualização",
                JOptionPane.INFORMATION_MESSAGE);
    }


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


    private void gerarRelatorioAndamentoProjetos() {
        try {
            ProjectService projectService = new ProjectService();
            List<Project> projetos = projectService.getAllProjects();

            RelatorioAndamentoProjetosDialog dialog = new RelatorioAndamentoProjetosDialog(this, projetos);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao gerar relatório de andamento: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    class RelatorioAndamentoProjetosDialog extends JDialog {
        private List<Project> projetos;

        public RelatorioAndamentoProjetosDialog(JFrame parent, List<Project> projetos) {
            super(parent, "Relatório de Andamento de Projetos", true);
            this.projetos = projetos;
            initComponents();
        }

        private void initComponents() {
            setSize(800, 600);
            setLocationRelativeTo(getParent());

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel titleLabel = new JLabel("Relatório de Andamento de Projetos", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            mainPanel.add(titleLabel, BorderLayout.NORTH);

            String[] columnNames = {"Projeto", "Status", "Progresso", "Tarefas", "Início", "Término", "Dias Restantes"};
            Object[][] data = gerarDadosRelatorio();

            JTable table = new JTable(data, columnNames);
            table.setDefaultRenderer(Object.class, new RelatorioTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


                    return c;
                }
            });

            JScrollPane scrollPane = new JScrollPane(table);
            mainPanel.add(scrollPane, BorderLayout.CENTER);


            JPanel resumoPanel = criarPainelResumo();
            mainPanel.add(resumoPanel, BorderLayout.SOUTH);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton exportarButton = new JButton("Exportar para CSV");
            JButton fecharButton = new JButton("Fechar");

            exportarButton.addActionListener(e -> exportarParaCSV(data, columnNames));
            fecharButton.addActionListener(e -> dispose());

            buttonPanel.add(exportarButton);
            buttonPanel.add(fecharButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            add(mainPanel);
        }

        private Object[][] gerarDadosRelatorio() {
            TaskService taskService = new TaskService();
            Object[][] data = new Object[projetos.size()][7];

            for (int i = 0; i < projetos.size(); i++) {
                Project projeto = projetos.get(i);
                List<Task> tarefas = taskService.getTasksByProject(projeto.getId());

                int totalTarefas = tarefas.size();
                int tarefasConcluidas = (int) tarefas.stream()
                        .filter(t -> "concluida".equals(t.getStatus()))
                        .count();


                long diasRestantes = projeto.getDataTerminoPrevista() != null ?
                        java.time.temporal.ChronoUnit.DAYS.between(
                                java.time.LocalDate.now(),
                                projeto.getDataTerminoPrevista()
                        ) : 0;

                data[i][0] = projeto.getNome();
                data[i][1] = getStatusDisplay(projeto.getStatus());
                data[i][3] = tarefasConcluidas + "/" + totalTarefas;
                data[i][4] = projeto.getDataInicio();
                data[i][5] = projeto.getDataTerminoPrevista();
                data[i][6] = diasRestantes > 0 ? diasRestantes + " dias" : "Expirado";
            }

            return data;
        }

        private JPanel criarPainelResumo() {
            JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
            panel.setBorder(BorderFactory.createTitledBorder("Resumo Geral"));

            long projetosAtivos = projetos.stream()
                    .filter(p -> "em_andamento".equals(p.getStatus()))
                    .count();

            long projetosConcluidos = projetos.stream()
                    .filter(p -> "concluido".equals(p.getStatus()))
                    .count();

            long projetosAtrasados = projetos.stream()
                    .filter(p -> p.getDataTerminoPrevista() != null &&
                            p.getDataTerminoPrevista().isBefore(java.time.LocalDate.now()) &&
                            !"concluido".equals(p.getStatus()))
                    .count();

            panel.add(criarCardResumo("Total Projetos", String.valueOf(projetos.size()), Color.BLUE));
            panel.add(criarCardResumo("Em Andamento", String.valueOf(projetosAtivos), Color.ORANGE));
            panel.add(criarCardResumo("Concluídos", String.valueOf(projetosConcluidos), Color.GREEN));
            panel.add(criarCardResumo("Atrasados", String.valueOf(projetosAtrasados), Color.RED));

            return panel;
        }

        private JPanel criarCardResumo(String titulo, String valor, Color cor) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(cor));
            card.setBackground(Color.WHITE);

            JLabel tituloLabel = new JLabel(titulo, SwingConstants.CENTER);
            JLabel valorLabel = new JLabel(valor, SwingConstants.CENTER);
            valorLabel.setFont(new Font("Arial", Font.BOLD, 18));
            valorLabel.setForeground(cor);

            card.add(tituloLabel, BorderLayout.NORTH);
            card.add(valorLabel, BorderLayout.CENTER);

            return card;
        }

        private String getStatusDisplay(String status) {
            switch (status) {
                case "planejado":
                    return "📋 Planejado";
                case "em_andamento":
                    return "🚀 Em Andamento";
                case "concluido":
                    return "✅ Concluído";
                case "cancelado":
                    return "❌ Cancelado";
                default:
                    return status;
            }
        }

        private void exportarParaCSV(Object[][] data, String[] columnNames) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Salvar Relatório CSV");
            fileChooser.setSelectedFile(new java.io.File("relatorio_andamento_projetos.csv"));

            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Arquivos CSV (*.csv)", "csv"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();

                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new java.io.File(file.getParent(), file.getName() + ".csv");
                }

                try (java.io.OutputStreamWriter writer =
                             new java.io.OutputStreamWriter(
                                     new java.io.FileOutputStream(file), "UTF-8")) {

                    writer.write('\uFEFF');

                    writer.write(String.join(";", columnNames));
                    writer.write("\n");

                    for (Object[] linha : data) {
                        for (int i = 0; i < linha.length; i++) {
                            if (i > 0) writer.write(";");

                            Object celula = linha[i];
                            String valor = celula != null ? celula.toString() : "";

                            valor = formatarValorCSV(valor);
                            writer.write(valor);
                        }
                        writer.write("\n");
                    }

                    writer.flush();

                    JOptionPane.showMessageDialog(this,
                            "Relatório exportado com sucesso!\nArquivo: " + file.getAbsolutePath(),
                            "Exportação Concluída",
                            JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao exportar relatório: " + e.getMessage(),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }

        private String formatarValorCSV(String valor) {
            if (valor == null) return "";

            valor = valor.replaceAll("[📋🚀✅❌⭐👍⚠️🔴🟡🟠🔵]", "").trim();

            if (valor.isEmpty()) {
                valor = valor.replaceAll("[📋🚀✅❌⭐👍⚠️🔴🟡🟠🔵]", "").trim();
                if (valor.isEmpty()) return "";
            }

            valor = valor.replace("\"", "\"\"");
            valor = valor.replace(";", ",");
            valor = valor.replace("\n", " ").replace("\r", " ");

            // Colocar entre aspas se necessário
            if (valor.contains(" ") || valor.contains(",") || valor.contains("\"") || valor.contains(";")) {
                valor = "\"" + valor + "\"";
            }

            return valor;
        }

    }

    private void gerarRelatorioDesempenhoColaboradores() {
        try {
            UserService userService = new UserService();
            TaskService taskService = new TaskService();

            List<User> colaboradores = userService.getUsersByRole(3);
            RelatorioDesempenhoDialog dialog = new RelatorioDesempenhoDialog(this, colaboradores, taskService);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao gerar relatório de desempenho: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    class RelatorioDesempenhoDialog extends JDialog {
        private List<User> colaboradores;
        private TaskService taskService;

        public RelatorioDesempenhoDialog(JFrame parent, List<User> colaboradores, TaskService taskService) {
            super(parent, "Relatório de Desempenho de Colaboradores", true);
            this.colaboradores = colaboradores;
            this.taskService = taskService;
            initComponents();
        }

        private void initComponents() {
            setSize(900, 600);
            setLocationRelativeTo(getParent());

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Título
            JLabel titleLabel = new JLabel("Relatório de Desempenho de Colaboradores", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            mainPanel.add(titleLabel, BorderLayout.NORTH);


            // Tabela de desempenho
            String[] columnNames = {"Colaborador", "Cargo", "Tarefas Totais", "Concluídas", "Em Andamento",
                    "Atrasadas", "Taxa Conclusão", "Produtividade"};
            Object[][] data = gerarDadosDesempenho();

            JTable table = new JTable(data, columnNames);
            table.setDefaultRenderer(Object.class, new DesempenhoTableCellRenderer());

            JScrollPane scrollPane = new JScrollPane(table);
            mainPanel.add(scrollPane, BorderLayout.CENTER);


            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton fecharButton = new JButton("Fechar");
            fecharButton.addActionListener(e -> dispose());
            buttonPanel.add(fecharButton);

            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            add(mainPanel);
        }

        private Object[][] gerarDadosDesempenho() {
            Object[][] data = new Object[colaboradores.size()][8];

            for (int i = 0; i < colaboradores.size(); i++) {
                User colaborador = colaboradores.get(i);
                List<Task> tarefas = taskService.getTasksByUser(colaborador.getId());

                int totalTarefas = tarefas.size();
                int concluidas = (int) tarefas.stream()
                        .filter(t -> "concluida".equals(t.getStatus()))
                        .count();
                int emAndamento = (int) tarefas.stream()
                        .filter(t -> "em_execucao".equals(t.getStatus()))
                        .count();
                int atrasadas = (int) tarefas.stream()
                        .filter(t -> t.getDataFimPrevista() != null &&
                                t.getDataFimPrevista().isBefore(java.time.LocalDate.now()) &&
                                !"concluida".equals(t.getStatus()))
                        .count();

                double taxaConclusao = totalTarefas > 0 ? (concluidas * 100.0) / totalTarefas : 0;
                String produtividade = calcularProdutividade(taxaConclusao);

                data[i][0] = colaborador.getNomeCompleto();
                data[i][1] = colaborador.getCargo();
                data[i][2] = totalTarefas;
                data[i][3] = concluidas;
                data[i][4] = emAndamento;
                data[i][5] = atrasadas;
                data[i][6] = String.format("%.1f%%", taxaConclusao);
                data[i][7] = produtividade;
            }

            java.util.Arrays.sort(data, (a, b) -> {
                double taxaA = Double.parseDouble(((String) a[6]).replace("%", ""));
                double taxaB = Double.parseDouble(((String) b[6]).replace("%", ""));
                return Double.compare(taxaB, taxaA);
            });

            return data;
        }

        private String calcularProdutividade(double taxaConclusao) {
            if (taxaConclusao >= 90) return "⭐ Excelente";
            if (taxaConclusao >= 75) return "👍 Boa";
            if (taxaConclusao >= 50) return "⚠️ Regular";
            return "❌ Baixa";
        }
    }


    private void gerarRelatorioProjetosRisco() {
        try {
            ProjectService projectService = new ProjectService();
            List<Project> projetosRisco = projectService.getProjectsAtRisk();

            RelatorioRiscoDialog dialog = new RelatorioRiscoDialog(this, projetosRisco);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao gerar relatório de projetos em risco: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    class RelatorioRiscoDialog extends JDialog {
        private List<Project> projetosRisco;

        public RelatorioRiscoDialog(JFrame parent, List<Project> projetosRisco) {
            super(parent, "Relatório de Projetos em Risco", true);
            this.projetosRisco = projetosRisco;
            initComponents();
        }

        private void initComponents() {
            setSize(700, 500);
            setLocationRelativeTo(getParent());

            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Título
            JLabel titleLabel = new JLabel("Projetos em Risco de Atraso", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            titleLabel.setForeground(Color.RED);
            mainPanel.add(titleLabel, BorderLayout.NORTH);

            if (projetosRisco.isEmpty()) {
                JLabel semRiscosLabel = new JLabel("🎉 Nenhum projeto em risco identificado!", SwingConstants.CENTER);
                semRiscosLabel.setFont(new Font("Arial", Font.BOLD, 14));
                semRiscosLabel.setForeground(Color.GREEN);
                mainPanel.add(semRiscosLabel, BorderLayout.CENTER);
            } else {
                // Tabela de projetos em risco
                String[] columnNames = {"Projeto", "Gerente", "Data Término", "Dias de Atraso",
                        "Progresso", "Nível de Risco"};
                Object[][] data = gerarDadosRisco();

                JTable table = new JTable(data, columnNames);
                table.setDefaultRenderer(Object.class, new RiscoTableCellRenderer());

                JScrollPane scrollPane = new JScrollPane(table);
                mainPanel.add(scrollPane, BorderLayout.CENTER);

                // Painel de alertas
                JPanel alertaPanel = criarPainelAlerta();
                mainPanel.add(alertaPanel, BorderLayout.SOUTH);
            }

            JButton fecharButton = new JButton("Fechar");
            fecharButton.addActionListener(e -> dispose());

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(fecharButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            add(mainPanel);
        }

        private Object[][] gerarDadosRisco() {
            TaskService taskService = new TaskService();
            UserService userService = new UserService();
            Object[][] data = new Object[projetosRisco.size()][6];

            for (int i = 0; i < projetosRisco.size(); i++) {
                Project projeto = projetosRisco.get(i);
                List<Task> tarefas = taskService.getTasksByProject(projeto.getId());

                int totalTarefas = tarefas.size();
                int tarefasConcluidas = (int) tarefas.stream()
                        .filter(t -> "concluida".equals(t.getStatus()))
                        .count();
                double progresso = totalTarefas > 0 ? (tarefasConcluidas * 100.0) / totalTarefas : 0;

                long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(
                        projeto.getDataTerminoPrevista(),
                        java.time.LocalDate.now()
                );

                String nivelRisco = calcularNivelRisco(diasAtraso, progresso);
                String nomeGerente = userService.getUserById(projeto.getGerenteId()).getNomeCompleto();

                data[i][0] = projeto.getNome();
                data[i][1] = nomeGerente;
                data[i][2] = projeto.getDataTerminoPrevista();
                data[i][3] = diasAtraso + " dias";
                data[i][4] = String.format("%.1f%%", progresso);
                data[i][5] = nivelRisco;
            }

            return data;
        }

        private String calcularNivelRisco(long diasAtraso, double progresso) {
            if (diasAtraso > 30 || progresso < 25) return "🔴 Crítico";
            if (diasAtraso > 15 || progresso < 50) return "🟡 Alto";
            if (diasAtraso > 7 || progresso < 75) return "🟠 Médio";
            return "🔵 Baixo";
        }

        private JPanel criarPainelAlerta() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createTitledBorder("⚠️ Alertas de Risco"));
            panel.setBackground(new Color(255, 240, 240));

            JTextArea alertaArea = new JTextArea();
            alertaArea.setEditable(false);
            alertaArea.setBackground(new Color(255, 240, 240));
            alertaArea.setText("Os projetos listados estão em risco de atraso. " +
                    "Recomenda-se revisar os cronogramas e alocar recursos adicionais conforme necessário.");
            panel.add(alertaArea, BorderLayout.CENTER);
            return panel;
        }
    }
}