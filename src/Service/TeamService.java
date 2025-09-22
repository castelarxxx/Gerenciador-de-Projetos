package Service;


import DAO.TeamDAO;
import model.Team;
import model.TeamMember;
import model.User;

import java.util.List;

public class TeamService {
    private TeamDAO teamDAO;

    public TeamService() {
        this.teamDAO = new TeamDAO();
    }

    public List<Team> getAllTeams() {
        return teamDAO.getAll();
    }

    public Team getTeamById(int id) {
        return teamDAO.getById(id);
    }

    public boolean createTeam(Team team) {
        if (team.getNome() == null || team.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da equipe é obrigatório");
        }
        return teamDAO.insert(team);
    }

    public boolean updateTeam(Team team) {
        return teamDAO.update(team);
    }

    public boolean deleteTeam(int id) {
        return teamDAO.delete(id);
    }

    public List<Team> searchTeams(String searchTerm) {
        return teamDAO.search(searchTerm);
    }

    public boolean addMemberToTeam(int teamId, int userId, String papel) {
        return teamDAO.addMember(teamId, userId, papel);
    }

    public boolean removeMemberFromTeam(int teamId, int userId) {
        return teamDAO.removeMember(teamId, userId);
    }

    public boolean updateMemberRole(int teamId, int userId, String novoPapel) {
        return teamDAO.updateMemberRole(teamId, userId, novoPapel);
    }

    public List<TeamMember> getTeamMembers(int teamId) {
        return teamDAO.getMembers(teamId);
    }

    //  public List<User> getUsersInTeam(int teamId) {
    //    return teamDAO.getUsersInTeam(teamId);
    //}

    public List<Team> getUserTeams(int userId) {
        return teamDAO.getUserTeams(userId);
    }

    public boolean isUserInTeam(int userId, int teamId) {
        return teamDAO.isUserInTeam(userId, teamId);
    }

    // Operações com projetos
    public boolean assignTeamToProject(int teamId, int projectId, String papel, java.time.LocalDate inicio, java.time.LocalDate fim) {
        return teamDAO.assignToProject(teamId, projectId, papel, inicio, fim);
    }

    public boolean removeTeamFromProject(int teamId, int projectId) {
        return teamDAO.removeFromProject(teamId, projectId);
    }

    public List<Team> getProjectTeams(int projectId) {
        return teamDAO.getTeamsByProject(projectId);
    }

    public List<User> getUsersInTeam(int teamId) {
        return teamDAO.getUsersInTeam(teamId);
    }

    public int getTotalTeamsCount() {
        return teamDAO.countAll();
    }
}
