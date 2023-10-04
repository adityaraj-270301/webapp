package com.csye6225.assignment2.model;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Assignment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private Integer num_of_attempts;

    @Column(nullable = false)
    private Date deadline;

    @Column(nullable = false)
    private Date assignment_created;

    @Column(nullable = false)
    private Date assignment_updated;

    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "user_id")
    // private UserAccount userAccount;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getNum_of_attempts() {
        return num_of_attempts;
    }

    public void setNum_of_attempts(Integer num_of_attempts) {
        this.num_of_attempts = num_of_attempts;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getAssignment_created() {
        return assignment_created;
    }

    public void setAssignment_created(Date assignment_created) {
        this.assignment_created = assignment_created;
    }

    public Date getAssignment_updated() {
        return assignment_updated;
    }

    public void setAssignment_updated(Date assignment_updated) {
        this.assignment_updated = assignment_updated;
    }

    // public UserAccount getUserAccount() {
    // return userAccount;
    // }

    // public void setUserAccount(UserAccount userAccount) {
    // this.userAccount = userAccount;
    // }

}
