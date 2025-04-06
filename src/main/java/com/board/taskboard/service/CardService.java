package com.board.taskboard.service;

import com.board.taskboard.exceptions.CardBlockedException;
import com.board.taskboard.exceptions.CardFinishedException;
import com.board.taskboard.exceptions.EntityNotFoundException;
import com.board.taskboard.model.Block;
import com.board.taskboard.model.BoardColumn;
import com.board.taskboard.model.Card;
import com.board.taskboard.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.board.taskboard.dto.BoardColumnInfoDTO;
import java.sql.SQLException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static com.board.taskboard.model.BoardColumnKindEnum.CANCEL;
import static com.board.taskboard.model.BoardColumnKindEnum.FINAL;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private BlockService blockService;

    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public void delete(Long id) {
        cardRepository.deleteById(id);
    }

    public Optional<Card> findById(long id){
        return this.cardRepository.findById(id);
    }

    public void moveToNextColumn(final Long cardId, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException{
        var optional = cardRepository.findById(cardId);

        var dto = optional.orElseThrow(
            () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId))
        );

        List<Block> blocks = blockService.findByCardId(cardId);

        boolean blocked = blocks.stream()
                .anyMatch(c -> c.getBlockedAt() != null);

        if (blocked){
            var message = "O card %s está bloqueado, é necesário desbloquea-lo para mover".formatted(cardId);
            throw new CardBlockedException(message);
        }

        var currentColumn = boardColumnsInfo.stream()
                .filter(bc -> bc.id().equals(dto.getBoardColumn().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));

        if (currentColumn.kind().equals(FINAL)){
            throw new CardFinishedException("O card já foi finalizado");
        }
        var nextColumn = boardColumnsInfo.stream()
                .filter(bc -> bc.order() == currentColumn.order() + 1)
                .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));

        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setId(nextColumn.id());
        dto.setBoardColumn(boardColumn);
        cardRepository.save(dto);
    }

    public void cancel(final Long cardId, final Long cancelColumnId ,
                       final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException{
        var optional = cardRepository.findById(cardId);

        var dto = optional.orElseThrow(
                () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId))
        );

        List<Block> blocks = blockService.findByCardId(cardId);

        boolean blocked = blocks.stream()
                .anyMatch(c -> c.getBlockedAt() != null);

        if (blocked){
            var message = "O card %s está bloqueado, é necesário desbloquea-lo para mover".formatted(cardId);
            throw new CardBlockedException(message);
        }
        var currentColumn = boardColumnsInfo.stream()
                .filter(bc -> bc.id().equals(dto.getBoardColumn().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));
        if (currentColumn.kind().equals(FINAL)){
            throw new CardFinishedException("O card já foi finalizado");
        }
        boardColumnsInfo.stream()
                .filter(bc -> bc.order() == currentColumn.order() + 1)
                .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));

        BoardColumn boardColumn = new BoardColumn();
        boardColumn.setId(cancelColumnId);
        dto.setBoardColumn(boardColumn);
        cardRepository.save(dto);
    }

    public void block(final Long id, final String reason, final List<BoardColumnInfoDTO> boardColumnsInfo) throws SQLException {
        var optional = cardRepository.findById(id);

        var dto = optional.orElseThrow(
                () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(id))
        );

        List<Block> blocks = blockService.findByCardId(id);

        boolean blocked = blocks.stream()
                .anyMatch(c -> c.getBlockedAt() != null);

        if (blocked){
            var message = "O card %s já está bloqueado".formatted(id);
            throw new CardBlockedException(message);
        }

        var currentColumn = boardColumnsInfo.stream()
                .filter(bc -> bc.id().equals(dto.getBoardColumn().getId()))
                .findFirst()
                .orElseThrow();

        if (currentColumn.kind().equals(FINAL) || currentColumn.kind().equals(CANCEL)){
            var message = "O card está em uma coluna do tipo %s e não pode ser bloqueado"
                    .formatted(currentColumn.kind());
            throw new IllegalStateException(message);
        }

        Block block = new Block();
        block.setBlockedAt(OffsetDateTime.now());
        block.setBlockReason(reason);
        block.setCard(dto);
        blockService.save(block);
    }

    public void unblock(final Long id, final String reason) throws SQLException {
        var optional = cardRepository.findById(id);

        var dto = optional.orElseThrow(
                () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(id))
        );

        List<Block> blocks = blockService.findByCardId(id);

        boolean blocked = blocks.stream()
                .anyMatch(c -> c.getBlockedAt() != null);

        if (!blocked){
            var message = "O card %s não está bloqueado".formatted(id);
            throw new CardBlockedException(message);
        }

        Block block = blockService.findByChatIdAndUnblocked(id);
        block.setUnblockedAt(OffsetDateTime.now());
        block.setUnblockReason(reason);
        block.setCard(dto);
        blockService.save(block);
    }
}
