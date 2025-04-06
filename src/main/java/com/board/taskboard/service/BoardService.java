package com.board.taskboard.service;

import com.board.taskboard.model.Board;
import com.board.taskboard.model.Task;
import com.board.taskboard.repository.BoardRepository;
import com.board.taskboard.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    public List<Board> getAll() {
        return boardRepository.findAll();
    }

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public boolean delete(Long id) {
        boardRepository.deleteById(id);
        return true;
    }

    public Optional<Board> findById(long id) {
        return boardRepository.findById(id);
    }
}
