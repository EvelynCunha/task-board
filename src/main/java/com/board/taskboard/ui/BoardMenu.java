package com.board.taskboard.ui;

import com.board.taskboard.dto.BoardColumnInfoDTO;
import com.board.taskboard.model.Board;
import com.board.taskboard.model.BoardColumn;
import com.board.taskboard.model.Card;
import com.board.taskboard.service.BlockService;
import com.board.taskboard.service.BoardColumnService;
import com.board.taskboard.service.BoardService;
import com.board.taskboard.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class BoardMenu {
    private CardService cardService;
    private BoardColumnService boardColumnService;
    private BlockService blockService;
    private Board board;
    private BoardService boardService;

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public BoardMenu(
            BoardService boardService,
            CardService cardService,
            BoardColumnService boardColumnService,
            BlockService blockService,
            Board board) {
        this.board = board;
        this.blockService = blockService;
        this.cardService = cardService;
        this.boardColumnService = boardColumnService;
        this.boardService = boardService;
    }

    public void execute() {
        try {
            System.out.printf("Bem vindo ao board %s, selecione a operação desejada\n", board.getId());
            var option = -1;
            while (option != 9) {
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Ver board");
                System.out.println("7 - Ver coluna com cards");
                System.out.println("8 - Ver card");
                System.out.println("9 - Voltar para o menu anterior um card");
                System.out.println("10 - Sair");
                option = scanner.nextInt();
                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando para o menu anterior");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida, informe uma opção do menu");
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            System.exit(0);
        }
    }

    private void createCard() throws SQLException{
        var card = new Card();
        System.out.println("Informe o título do card");
        card.setTitle(scanner.next());
        System.out.println("Informe a descrição do card");
        card.setDescription(scanner.next());
        if(board.getBoardColumns().isEmpty()){
            List<BoardColumn> columns = boardColumnService.findByBoardId(board.getId());
            System.out.println("SIZE: " + columns.size());
            board.setBoardColumns(columns);
        }
        card.setBoardColumn(board.getInitialColumn());
        cardService.save(card);
    }

    private void moveCardToNextColumn() throws SQLException {
        System.out.println("Informe o id do card que deseja mover para a próxima coluna");
        var cardId = scanner.nextLong();
        var boardColumnsInfo = board.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
        cardService.moveToNextColumn(cardId, boardColumnsInfo);
    }

    private void blockCard() throws SQLException {
        System.out.println("Informe o id do card que será bloqueado");
        var cardId = scanner.nextLong();
        System.out.println("Informe o motivo do bloqueio do card");
        var reason = scanner.next();
        var boardColumnsInfo = board.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
        cardService.block(cardId, reason, boardColumnsInfo);
    }

    private void unblockCard() throws SQLException {
        System.out.println("Informe o id do card que será desbloqueado");
        var cardId = scanner.nextLong();
        System.out.println("Informe o motivo do desbloqueio do card");
        var reason = scanner.next();
        cardService.unblock(cardId, reason);
    }

    private void cancelCard() throws SQLException {
        System.out.println("Informe o id do card que deseja mover para a coluna de cancelamento");
        var cardId = scanner.nextLong();
        var cancelColumn = board.getCancelColumn();
        var boardColumnsInfo = board.getBoardColumns().stream()
                .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();
        cardService.cancel(cardId, cancelColumn.getId(), boardColumnsInfo);
    }

    private void showBoard() throws SQLException {
        var optional = boardService.findById(board.getId());
        optional.ifPresent(b -> {
            System.out.printf("Board [%s,%s]\n", b.getId(), b.getName());
            b.getBoardColumns().forEach(c ->
                    System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n", c.getName(), c.getKind(), c.getCards().size())
            );
        });
    }

    private void showColumn() throws SQLException {
        var columnsIds = board.getBoardColumns().stream().map(BoardColumn::getId).toList();
        var selectedColumnId = -1L;
        while (!columnsIds.contains(selectedColumnId)){
            System.out.printf("Escolha uma coluna do board %s pelo id\n", board.getName());
            board.getBoardColumns().forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));
            selectedColumnId = scanner.nextLong();
        }
        var column = boardColumnService.findById(selectedColumnId);
        column.ifPresent(co -> {
            System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
            co.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescrição: %s",
                    ca.getId(), ca.getTitle(), ca.getDescription()));
        });
    }

    private void showCard() throws SQLException {
        System.out.println("Informe o id do card que deseja visualizar");
        var selectedCardId = scanner.nextLong();
        cardService.findById(selectedCardId)
                .ifPresentOrElse(
                        c -> {
                            System.out.printf("Card %s - %s.\n", c.getId(), c.getTitle());
                            System.out.printf("Descrição: %s\n", c.getDescription());
                        },
                        () -> System.out.printf("Não existe um card com o id %s\n", selectedCardId));
    }
}