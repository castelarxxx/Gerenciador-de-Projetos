package DAO;

import DAO.DBConnection;
import model.Team;
import model.TeamMember;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {

    public boolean insert(Team team) {
        String sql = "INSERT INTO teams (nome, descricao) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, team.getNome());
            stmt.setString(2, team.getDescricao());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        team.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir equipe: " + e.getMessage());
        }
        return false;
    }

    public Team getById(int id) {
        String sql = "SELECT * FROM teams WHERE id = ?";
        Team team = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    team = extractTeamFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar equipe: " + e.getMessage());
        }
        return team;
    }

    public List<Team> getAll() {
        String sql = "SELECT * FROM teams ORDER BY nome";
        List<Team> teams = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                teams.add(extractTeamFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar equipes: " + e.getMessage());
        }
        return teams;
    }

    public boolean update(Team team) {
        String sql = "UPDATE teams SET nome = ?, descricao = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, team.getNome());
            stmt.setString(2, team.getDescricao());
            stmt.setInt(3, team.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar equipe: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM teams WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar equipe: " + e.getMessage());
        }
        return false;
    }

    public List<Team> search(String searchTerm) {
        String sql = "SELECT * FROM teams WHERE nome LIKE ? OR descricao LIKE ? ORDER BY nome";
        List<Team> teams = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeTerm = "%" + searchTerm + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teams.add(extractTeamFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar equipes: " + e.getMessage());
        }
        return teams;
    }

    public boolean addMember(int teamId, int userId, String papel) {
        String sql = "INSERT INTO team_members (team_id, user_id, papel_no_time) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teamId);
            stmt.setInt(2, userId);
            stmt.setString(3, papel);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar membro à equipe: " + e.getMessage());
        }
        return false;
    }

    public boolean removeMember(int teamId, int userId) {
        String sql = "DELETE FROM team_members WHERE team_id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teamId);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover membro da equipe: " + e.getMessage());
        }
        return false;
    }

    public boolean updateMemberRole(int teamId, int userId, String novoPapel) {
        String sql = "UPDATE team_members SET papel_no_time = ? WHERE team_id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoPapel);
            stmt.setInt(2, teamId);
            stmt.setInt(3, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar papel do membro: " + e.getMessage());
        }
        return false;
    }

    public List<TeamMember> getMembers(int teamId) {
        String sql = "SELECT * FROM team_members WHERE team_id = ?";
        List<TeamMember> members = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teamId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TeamMember member = new TeamMember();
                    member.setTeamId(rs.getInt("team_id"));
                    member.setUserId(rs.getInt("user_id"));
                    member.setPapel(rs.getString("papel_no_time"));
                    member.setCriadoEm(rs.getTimestamp("criado_em").toLocalDateTime());

                    Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
                    if (atualizadoEm != null) {
                        member.setAtualizadoEm(atualizadoEm.toLocalDateTime());
                    }

                    members.add(member);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar membros da equipe: " + e.getMessage());
        }
        return members;
    }

    //   public List<User> getUsersInTeam(int teamId) {
    //       String sql = "SELECT u.* FROM users u " +
    //             "INNER JOIN team_members tm ON u.id = tm.user_id " +
    //         "WHERE tm.team_id = ? AND u.ativo = TRUE";
    //   List<User> users = new ArrayList<>();

    //try (Connection conn = DBConnection.getConnection();
    //   PreparedStatement stmt = conn.prepareStatement(sql)) {

    //stmt.setInt(1, teamId);
    //try (ResultSet rs = stmt.executeQuery()) {
    //  while (rs.next()) {
    //    User user = new User();
    //  user.setId(rs.getInt("id"));
    //user.setNomeCompleto(rs.getString("nome_completo"));
    //user.setEmail(rs.getString("email"));
    //   user.setCargo(rs.getString("cargo"));
    //    user.setLogin(rs.getString("login"));
    //       users.add(user);
    //     }
    //    }
    //  } catch (SQLException e) {
    //       System.err.println("Erro ao buscar usuários da equipe: " + e.getMessage());
    //    }
    //    return users;
    //   }

    public List<Team> getUserTeams(int userId) {
        String sql = "SELECT t.* FROM teams t " +
                "INNER JOIN team_members tm ON t.id = tm.team_id " +
                "WHERE tm.user_id = ?";
        List<Team> teams = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teams.add(extractTeamFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar equipes do usuário: " + e.getMessage());
        }
        return teams;
    }

    public boolean isUserInTeam(int userId, int teamId) {
        String sql = "SELECT COUNT(*) FROM team_members WHERE user_id = ? AND team_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, teamId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar se usuário está na equipe: " + e.getMessage());
        }
        return false;
    }

    public boolean assignToProject(int teamId, int projectId, String papel, LocalDate inicio, LocalDate fim) {
        String sql = "INSERT INTO project_teams (project_id, team_id, papel_no_projeto, alocado_de, alocado_ate) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);
            stmt.setInt(2, teamId);
            stmt.setString(3, papel);
            stmt.setDate(4, Date.valueOf(inicio));
            stmt.setDate(5, Date.valueOf(fim));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atribuir equipe ao projeto: " + e.getMessage());
        }
        return false;
    }

    public boolean removeFromProject(int teamId, int projectId) {
        String sql = "DELETE FROM project_teams WHERE team_id = ? AND project_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teamId);
            stmt.setInt(2, projectId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover equipe do projeto: " + e.getMessage());
        }
        return false;
    }

    public List<Team> getTeamsByProject(int projectId) {
        String sql = "SELECT t.* FROM teams t " +
                "INNER JOIN project_teams pt ON t.id = pt.team_id " +
                "WHERE pt.project_id = ?";
        List<Team> teams = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teams.add(extractTeamFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar equipes do projeto: " + e.getMessage());
        }
        return teams;
    }

    private Team extractTeamFromResultSet(ResultSet rs) throws SQLException {
        Team team = new Team();
        team.setId(rs.getInt("id"));
        team.setNome(rs.getString("nome"));
        team.setDescricao(rs.getString("descricao"));
        team.setCriadoEm(rs.getTimestamp("criado_em").toLocalDateTime());

        Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
        if (atualizadoEm != null) {
            team.setAtualizadoEm(atualizadoEm.toLocalDateTime());
        }

        return team;
    }
}
