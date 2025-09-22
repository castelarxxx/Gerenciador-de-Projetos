package DAO;





import model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {


    public boolean insert(Task task) {
        String sql = "INSERT INTO tasks (titulo, descricao, projeto_id, responsavel_id, status, " +
                "data_inicio_prevista, data_fim_prevista, data_inicio_real, data_fim_real, prioridade) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, task.getTitulo());
            stmt.setString(2, task.getDescricao());
            stmt.setInt(3, task.getProjetoId());

            if (task.getResponsavelId() != null) {
                stmt.setInt(4, task.getResponsavelId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setString(5, task.getStatus());

            if (task.getDataInicioPrevista() != null) {
                stmt.setDate(6, Date.valueOf(task.getDataInicioPrevista()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            if (task.getDataFimPrevista() != null) {
                stmt.setDate(7, Date.valueOf(task.getDataFimPrevista()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            if (task.getDataInicioReal() != null) {
                stmt.setDate(8, Date.valueOf(task.getDataInicioReal()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            if (task.getDataFimReal() != null) {
                stmt.setDate(9, Date.valueOf(task.getDataFimReal()));
            } else {
                stmt.setNull(9, Types.DATE);
            }

            stmt.setString(10, task.getPrioridade());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        task.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir tarefa: " + e.getMessage());
        }
        return false;
    }

    public Task getById(int id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        Task task = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    task = extractTaskFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefa: " + e.getMessage());
        }
        return task;
    }

    public List<Task> getAll() {
        String sql = "SELECT * FROM tasks ORDER BY criado_em DESC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefas: " + e.getMessage());
        }
        return tasks;
    }

    public List<Task> getByProjectId(int projectId) {
        String sql = "SELECT * FROM tasks WHERE projeto_id = ? ORDER BY criado_em DESC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(extractTaskFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefas por projeto: " + e.getMessage());
        }
        return tasks;
    }

    public List<Task> getByUserId(int userId) {
        String sql = "SELECT * FROM tasks WHERE responsavel_id = ? ORDER BY criado_em DESC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(extractTaskFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefas por usuário: " + e.getMessage());
        }
        return tasks;
    }

    public List<Task> getByStatus(String status) {
        String sql = "SELECT * FROM tasks WHERE status = ? ORDER BY criado_em DESC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(extractTaskFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefas por status: " + e.getMessage());
        }
        return tasks;
    }

    public boolean update(Task task) {
        String sql = "UPDATE tasks SET titulo = ?, descricao = ?, projeto_id = ?, responsavel_id = ?, " +
                "status = ?, data_inicio_prevista = ?, data_fim_prevista = ?, data_inicio_real = ?, " +
                "data_fim_real = ?, prioridade = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, task.getTitulo());
            stmt.setString(2, task.getDescricao());
            stmt.setInt(3, task.getProjetoId());

            if (task.getResponsavelId() != null) {
                stmt.setInt(4, task.getResponsavelId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setString(5, task.getStatus());

            if (task.getDataInicioPrevista() != null) {
                stmt.setDate(6, Date.valueOf(task.getDataInicioPrevista()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            if (task.getDataFimPrevista() != null) {
                stmt.setDate(7, Date.valueOf(task.getDataFimPrevista()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            if (task.getDataInicioReal() != null) {
                stmt.setDate(8, Date.valueOf(task.getDataInicioReal()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            if (task.getDataFimReal() != null) {
                stmt.setDate(9, Date.valueOf(task.getDataFimReal()));
            } else {
                stmt.setNull(9, Types.DATE);
            }

            stmt.setString(10, task.getPrioridade());
            stmt.setInt(11, task.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tarefa: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar tarefa: " + e.getMessage());
        }
        return false;
    }

    public boolean updateStatus(int taskId, String newStatus, int changedBy, String comment) {
        // Primeiro, obtém o status atual para registrar no histórico
        Task task = getById(taskId);
        if (task == null) return false;

        String oldStatus = task.getStatus();

        // Atualiza o status da tarefa
        String updateSql = "UPDATE tasks SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, taskId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Registra no histórico
                String historySql = "INSERT INTO task_status_history (task_id, status_antigo, status_novo, alterado_por, comentario) " +
                        "VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement historyStmt = conn.prepareStatement(historySql)) {
                    historyStmt.setInt(1, taskId);
                    historyStmt.setString(2, oldStatus);
                    historyStmt.setString(3, newStatus);
                    historyStmt.setInt(4, changedBy);
                    historyStmt.setString(5, comment);

                    historyStmt.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status da tarefa: " + e.getMessage());
        }
        return false;
    }

    public List<Task> search(String searchTerm) {
        String sql = "SELECT * FROM tasks WHERE titulo LIKE ? OR descricao LIKE ? ORDER BY criado_em DESC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeTerm = "%" + searchTerm + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(extractTaskFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tarefas: " + e.getMessage());
        }
        return tasks;
    }

    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setTitulo(rs.getString("titulo"));
        task.setDescricao(rs.getString("descricao"));
        task.setProjetoId(rs.getInt("projeto_id"));

        int responsavelId = rs.getInt("responsavel_id");
        if (!rs.wasNull()) {
            task.setResponsavelId(responsavelId);
        }

        task.setStatus(rs.getString("status"));

        Date dataInicioPrevista = rs.getDate("data_inicio_prevista");
        if (dataInicioPrevista != null) {
            task.setDataInicioPrevista(dataInicioPrevista.toLocalDate());
        }

        Date dataFimPrevista = rs.getDate("data_fim_prevista");
        if (dataFimPrevista != null) {
            task.setDataFimPrevista(dataFimPrevista.toLocalDate());
        }

        Date dataInicioReal = rs.getDate("data_inicio_real");
        if (dataInicioReal != null) {
            task.setDataInicioReal(dataInicioReal.toLocalDate());
        }

        Date dataFimReal = rs.getDate("data_fim_real");
        if (dataFimReal != null) {
            task.setDataFimReal(dataFimReal.toLocalDate());
        }

        task.setPrioridade(rs.getString("prioridade"));
        task.setCriadoEm(rs.getTimestamp("criado_em").toLocalDateTime());

        Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
        if (atualizadoEm != null) {
            task.setAtualizadoEm(atualizadoEm.toLocalDateTime());
        }

        return task;
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar tarefas por status: " + e.getMessage());
        }
        return 0;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM tasks";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao contar todas as tarefas: " + e.getMessage());
        }
        return 0;
    }
}
