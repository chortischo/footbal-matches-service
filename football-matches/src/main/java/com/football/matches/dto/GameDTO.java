package com.football.matches.dto;

import com.football.matches.model.GameResult;
import com.football.matches.model.GameStatus;

import java.time.LocalDateTime;
import java.util.List;

public class GameDTO {
    private Long id;
    private LocalDateTime dateTime;
    private String opponentTeam;
    private StadiumDTO stadium;
    private List<PlayerDTO> players;
    private Integer attendance;
    private GameResult result;
    private GameStatus status;

    public GameDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public String getOpponentTeam() {
        return this.opponentTeam;
    }

    public StadiumDTO getStadium() {
        return this.stadium;
    }

    public List<PlayerDTO> getPlayers() {
        return this.players;
    }

    public Integer getAttendance() {
        return this.attendance;
    }

    public GameResult getResult() {
        return this.result;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setOpponentTeam(String opponentTeam) {
        this.opponentTeam = opponentTeam;
    }

    public void setStadium(StadiumDTO stadium) {
        this.stadium = stadium;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }

    public void setAttendance(Integer attendance) {
        this.attendance = attendance;
    }

    public void setResult(GameResult result) {
        this.result = result;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof GameDTO)) return false;
        final GameDTO other = (GameDTO) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$dateTime = this.getDateTime();
        final Object other$dateTime = other.getDateTime();
        if (this$dateTime == null ? other$dateTime != null : !this$dateTime.equals(other$dateTime)) return false;
        final Object this$opponentTeam = this.getOpponentTeam();
        final Object other$opponentTeam = other.getOpponentTeam();
        if (this$opponentTeam == null ? other$opponentTeam != null : !this$opponentTeam.equals(other$opponentTeam))
            return false;
        final Object this$stadium = this.getStadium();
        final Object other$stadium = other.getStadium();
        if (this$stadium == null ? other$stadium != null : !this$stadium.equals(other$stadium)) return false;
        final Object this$players = this.getPlayers();
        final Object other$players = other.getPlayers();
        if (this$players == null ? other$players != null : !this$players.equals(other$players)) return false;
        final Object this$attendance = this.getAttendance();
        final Object other$attendance = other.getAttendance();
        if (this$attendance == null ? other$attendance != null : !this$attendance.equals(other$attendance))
            return false;
        final Object this$result = this.getResult();
        final Object other$result = other.getResult();
        if (this$result == null ? other$result != null : !this$result.equals(other$result)) return false;
        final Object this$status = this.getStatus();
        final Object other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof GameDTO;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $dateTime = this.getDateTime();
        result = result * PRIME + ($dateTime == null ? 43 : $dateTime.hashCode());
        final Object $opponentTeam = this.getOpponentTeam();
        result = result * PRIME + ($opponentTeam == null ? 43 : $opponentTeam.hashCode());
        final Object $stadium = this.getStadium();
        result = result * PRIME + ($stadium == null ? 43 : $stadium.hashCode());
        final Object $players = this.getPlayers();
        result = result * PRIME + ($players == null ? 43 : $players.hashCode());
        final Object $attendance = this.getAttendance();
        result = result * PRIME + ($attendance == null ? 43 : $attendance.hashCode());
        final Object $result = this.getResult();
        result = result * PRIME + ($result == null ? 43 : $result.hashCode());
        final Object $status = this.getStatus();
        result = result * PRIME + ($status == null ? 43 : $status.hashCode());
        return result;
    }

    public String toString() {
        return "GameDTO(id=" + this.getId() + ", dateTime=" + this.getDateTime() + ", opponentTeam=" + this.getOpponentTeam() + ", stadium=" + this.getStadium() + ", players=" + this.getPlayers() + ", attendance=" + this.getAttendance() + ", result=" + this.getResult() + ", status=" + this.getStatus() + ")";
    }
}