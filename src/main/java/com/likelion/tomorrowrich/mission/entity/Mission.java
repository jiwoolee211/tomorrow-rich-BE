package com.likelion.tomorrowrich.mission.entity;

import com.likelion.tomorrowrich.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "missions")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate missionDate;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MissionType missionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MissionStatus status = MissionStatus.SCHEDULED;

    @Column(nullable = false)
    private Integer progress = 0;

    @Column(nullable = false)
    private Integer targetCount = 1;

    @Column(nullable = false)
    private Integer rewardPoint = 0;

    @Column(nullable = false)
    private Boolean pointReceived = false;

    private String targetCategoryCode;

    protected Mission() {
    }

    public Mission(
            User user,
            LocalDate missionDate,
            String title,
            String description,
            MissionType missionType,
            Integer targetCount,
            Integer rewardPoint
    ) {
        this.user = user;
        this.missionDate = missionDate;
        this.title = title;
        this.description = description;
        this.missionType = missionType;
        this.targetCount = targetCount;
        this.rewardPoint = rewardPoint;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getMissionDate() {
        return missionDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public MissionType getMissionType() {
        return missionType;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public Integer getProgress() {
        return progress;
    }

    public Integer getTargetCount() {
        return targetCount;
    }

    public Integer getRewardPoint() {
        return rewardPoint;
    }

    public Boolean getPointReceived() {
        return pointReceived;
    }

    public String getTargetCategoryCode() {
        return targetCategoryCode;
    }

    public void updateProgress(Integer progress) {
        this.progress = progress;
        if (progress >= targetCount && status != MissionStatus.COMPLETED) {
            this.status = MissionStatus.COMPLETABLE;
        }
    }

    public void complete() {
        this.status = MissionStatus.COMPLETED;
        this.pointReceived = true;
    }
}
