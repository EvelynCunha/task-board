package com.board.taskboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board_column")
public class BoardColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    @Column(name = "id")
    private Long id;

    @Setter
    @Getter
    @Column(name = "name")
    private String name;

    @Setter
    @Getter
    @Column(name = "column_order")
    private int order;

    @Setter
    @Getter
    @Column(name = "kind")
    private BoardColumnKindEnum kind;

    @Setter
    @Getter
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "board_id")
    private Board board = new Board();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "boardColumn", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Getter
    @Setter
    private List<Card> cards = new ArrayList<>();
}
