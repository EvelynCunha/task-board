package com.board.taskboard.service;

import com.board.taskboard.model.Block;
import com.board.taskboard.model.Board;
import com.board.taskboard.model.BoardColumn;
import com.board.taskboard.repository.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlockService {
    @Autowired
    private BlockRepository blockRepository;

    public List<Block> getAll() {
        return blockRepository.findAll();
    }

    public Block save(Block block) {
        return blockRepository.save(block);
    }

    public void delete(Long id) {
        blockRepository.deleteById(id);
    }

    public Optional<Block> findById(long id){
        return blockRepository.findById(id);
    }

    public List<Block> findByCardId(long cardId){
        List<Block> blocks = blockRepository.findAll();

        return blocks.stream()
                .filter(x -> x.getCard().getId().equals(cardId))
                .collect(Collectors.toList());
    }

    public Block findByChatIdAndUnblocked(long cardId){
        List<Block> blocks = blockRepository.findAll();

        return blocks.stream()
                .filter(x -> x.getCard().getId().equals(cardId)
                        && x.getUnblockedAt() == null)
                .findFirst()
                .orElse(null);
    }
}
