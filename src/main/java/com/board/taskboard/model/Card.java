package com.board.taskboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    @Column(name = "id")
    private Long id;

    @Setter
    @Getter
    @Column(name = "title")
    private String title;

    @Setter
    @Getter
    @Column(name = "card_description")
    private String description;

    @Setter
    @Getter
    @ManyToOne
    private BoardColumn boardColumn = new BoardColumn();

    @Setter
    @Getter
    @OneToMany
    private List<Block> blocks = new ArrayList<>();
}
