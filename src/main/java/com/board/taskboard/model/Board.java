package com.board.taskboard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.board.taskboard.model.BoardColumnKindEnum.CANCEL;
import static com.board.taskboard.model.BoardColumnKindEnum.INITIAL;

@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    @Column(name = "id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Getter
    @Setter
    private List<BoardColumn> boardColumns = new ArrayList<>();

    private BoardColumn getFilteredColumn(Predicate<BoardColumn> filter){
        return boardColumns.stream()
                .filter(filter)
                .findFirst().orElseThrow();
    }

    public BoardColumn getInitialColumn(){
        return getFilteredColumn(bc -> bc.getKind().equals(INITIAL));
    }

    public BoardColumn getCancelColumn(){
        return getFilteredColumn(bc -> bc.getKind().equals(CANCEL));
    }
}
