package com.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity(name = "chunk_infos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChunkInfo {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "chunk_data_id")
    private ChunkData data;

    @NotNull
    @OneToOne
    @JoinColumn(name = "from_user_id")
    private User from;

    @NotNull
    @OneToOne
    @JoinColumn(name = "to_user_id")
    private User to;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;

    private String comment;

    public ChunkInfo(Long id, User from, User to, Date expiration, String comment) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.createdAt = new Date();
        this.expiration = expiration;
        this.comment = comment;
    }
}
