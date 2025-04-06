package com.board.taskboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "block")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    @Column(name = "id")
    private Long id;

    @Setter
    @Getter
    @Column(name = "blocked_at")
    private OffsetDateTime blockedAt;

    @Setter
    @Getter
    @Column(name = "block_reason")
    private String blockReason;

    @Setter
    @Getter
    @Column(name = "unblocked_at")
    private OffsetDateTime unblockedAt;

    @Setter
    @Getter
    @Column(name = "unblocked_reason")
    private String unblockReason;

    @Getter
    @Setter
    @ManyToOne
    public Card card;
}
