package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "from_user_id")
    private User from;

    @NotNull
    private String email;

    @NotNull
    private String type;

    private String content;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Notification(Long id, User from, String email, String type, String content) {
        this.id = id;
        this.from = from;
        this.email = email;
        this.createdAt = new Date();
        this.type = type;
        this.content = content;
    }

}
