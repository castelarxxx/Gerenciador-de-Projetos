package DAO;

import model.Project;

import  java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {
    public boolean insert(Project project) {
        String sql = "INSERT INTO projects (nome, descricao, data_inicio, data_termino_prevista, status, gerente_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, project.getNome());
            stmt.setString(2, project.getDescricao());

            if (project.getDataInicio() != null) {
                stmt.setDate(3, Date.valueOf(project.getDataInicio()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            if (project.getDataTerminoPrevista() != null) {
                stmt.setDate(4, Date.valueOf(project.getDataTerminoPrevista()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setString(5, project.getStatus());

            if (project.getGerenteId() > 0) {
                stmt.setInt(6, project.getGerenteId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        project.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir projeto: " + e.getMessage());
        }
        return false;
    }

    public Project getById(int id) {
        String sql = "SELECT * FROM projects WHERE id = ?";
        Project project = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    project = extractProjectFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projeto: " + e.getMessage());
        }
        return project;
    }

    public List<Project> getAll() {
        String sql = "SELECT * FROM projects ORDER BY criado_em DESC";
        List<Project> projects = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                projects.add(extractProjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos: " + e.getMessage());
        }
        return projects;
    }

    public List<Project> getByManagerId(int managerId) {
        String sql = "SELECT * FROM projects WHERE gerente_id = ? ORDER BY criado_em DESC";
        List<Project> projects = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, managerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    projects.add(extractProjectFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos por gerente: " + e.getMessage());
        }
        return projects;
    }

    public List<Project> getByStatus(String status) {
        String sql = "SELECT * FROM projects WHERE status = ? ORDER BY criado_em DESC";
        List<Project> projects = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    projects.add(extractProjectFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos por status: " + e.getMessage());
        }
        return projects;
    }

    public boolean update(Project project) {
        String sql = "UPDATE projects SET nome = ?, descricao = ?, data_inicio = ?, " +
                "data_termino_prevista = ?, data_termino_real = ?, status = ?, gerente_id = ? " +
                "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, project.getNome());
            stmt.setString(2, project.getDescricao());

            if (project.getDataInicio() != null) {
                stmt.setDate(3, Date.valueOf(project.getDataInicio()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            if (project.getDataTerminoPrevista() != null) {
                stmt.setDate(4, Date.valueOf(project.getDataTerminoPrevista()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            if (project.getDataTerminoReal() != null) {
                stmt.setDate(5, Date.valueOf(project.getDataTerminoReal()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            stmt.setString(6, project.getStatus());

            if (project.getGerenteId() > 0) {
                stmt.setInt(7, project.getGerenteId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            stmt.setInt(8, project.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar projeto: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM projects WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar projeto: " + e.getMessage());
        }
        return false;
    }

    public boolean updateStatus(int projectId, String newStatus, int changedBy, String comment) {
        Project project = getById(projectId);
        if (project == null) return false;

        String oldStatus = project.getStatus();

        String updateSql = "UPDATE projects SET status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, projectId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                String historySql = "INSERT INTO project_status_history (project_id, status_antigo, status_novo, alterado_por, comentario) " +
                        "VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement historyStmt = conn.prepareStatement(historySql)) {
                    historyStmt.setInt(1, projectId);
                    historyStmt.setString(2, oldStatus);
                    historyStmt.setString(3, newStatus);
                    historyStmt.setInt(4, changedBy);
                    historyStmt.setString(5, comment);

                    historyStmt.executeUpdate();
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do projeto: " + e.getMessage());
        }
        return false;
    }

    public List<Project> getProjectsAtRisk() {
        String sql = "SELECT * FROM projects WHERE data_termino_prevista < CURDATE() " +
                "AND status NOT IN ('concluido', 'cancelado') ORDER BY data_termino_prevista ASC";
        List<Project> projects = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                projects.add(extractProjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos em risco: " + e.getMessage());
        }
        return projects;
    }

    public double calculateProgress(int projectId) {
        String sql = "SELECT COUNT(*) as total, " +
                "SUM(CASE WHEN status = 'concluida' THEN 1 ELSE 0 END) as concluidas " +
                "FROM tasks WHERE projeto_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    int concluidas = rs.getInt("concluidas");

                    if (total > 0) {
                        return (concluidas * 100.0) / total;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao calcular progresso do projeto: " + e.getMessage());
        }
        return 0.0;
    }

    public List<Project> search(String searchTerm) {
        String sql = "SELECT * FROM projects WHERE nome LIKE ? OR descricao LIKE ? ORDER BY criado_em DESC";
        List<Project> projects = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeTerm = "%" + searchTerm + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    projects.add(extractProjectFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar projetos: " + e.getMessage());
        }
        return projects;
    }

    private Project extractProjectFromResultSet(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getInt("id"));
        project.setNome(rs.getString("nome"));
        project.setDescricao(rs.getString("descricao"));

        Date dataInicio = rs.getDate("data_inicio");
        if (dataInicio != null) {
            project.setDataInicio(dataInicio.toLocalDate());
        }

        Date dataTerminoPrevista = rs.getDate("data_termino_prevista");
        if (dataTerminoPrevista != null) {
            project.setDataTerminoPrevista(dataTerminoPrevista.toLocalDate());
        }

        Date dataTerminoReal = rs.getDate("data_termino_real");
        if (dataTerminoReal != null) {
            project.setDataTerminoReal(dataTerminoReal.toLocalDate());
        }

        project.setStatus(rs.getString("status"));
        project.setGerenteId(rs.getInt("gerente_id"));
        project.setCriadoEm(rs.getTimestamp("criado_em").toLocalDateTime());

        Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
        if (atualizadoEm != null) {
            project.setAtualizadoEm(atualizadoEm.toLocalDateTime());
        }

        return project;
    }
}
