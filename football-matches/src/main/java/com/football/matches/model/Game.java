package com.football.matches.model;

import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private String opponentTeam;

    @ManyToOne
    @JoinColumn(name = "stadium_id", nullable = false)
    private Stadium stadium;

    @ManyToMany
    @JoinTable(
            name = "game_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<Player> players;

    private Integer attendance;

    @Enumerated(EnumType.STRING)
    private GameResult result;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status = GameStatus.SCHEDULED;

    public Game() {
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Game game = (Game) o;
        return getId() != null && Objects.equals(getId(), game.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
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

    public Stadium getStadium() {
        return this.stadium;
    }

    public List<Player> getPlayers() {
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

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public void setPlayers(List<Player> players) {
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

    public String toString() {
        return "Game(id=" + this.getId() + ", dateTime=" + this.getDateTime() + ", opponentTeam=" + this.getOpponentTeam() + ", stadium=" + this.getStadium() + ", attendance=" + this.getAttendance() + ", result=" + this.getResult() + ", status=" + this.getStatus() + ")";
    }
}
