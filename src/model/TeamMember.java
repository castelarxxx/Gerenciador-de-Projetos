package model;

import java.time.LocalDateTime;

public class TeamMember {
    private int teamId;
    private int userId;
    private String papel;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public TeamMember() {}

    public TeamMember(int teamId, int userId, String papel) {
        this.teamId = teamId;
        this.userId = userId;
        this.papel = papel;
    }

    // Getters e Setters
    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}