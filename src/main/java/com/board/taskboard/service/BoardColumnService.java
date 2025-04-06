package com.board.taskboard.service;

import com.board.taskboard.model.Block;
import com.board.taskboard.model.Board;
import com.board.taskboard.model.BoardColumn;
import com.board.taskboard.repository.BlockRepository;
import com.board.taskboard.repository.BoardColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardColumnService {
    @Autowired
    private BoardColumnRepository boardColumnRepository;

    public List<BoardColumn> getAll() {
        return boardColumnRepository.findAll();
    }

    public BoardColumn save(BoardColumn boardColumn) {
        return boardColumnRepository.save(boardColumn);
    }

    public void delete(Long id) {
        boardColumnRepository.deleteById(id);
    }

    public Optional<BoardColumn> findById(long id){
        return boardColumnRepository.findById(id);
    }

    public List<BoardColumn> findByBoardId(long boardId){
        List<BoardColumn> columns = boardColumnRepository.findAll();

        return columns.stream()
                .filter(x -> x.getBoard().getId().equals(boardId))
                .collect(Collectors.toList());
    }
}
