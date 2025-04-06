package com.board.taskboard.ui;

import com.board.taskboard.model.Board;
import com.board.taskboard.model.BoardColumn;
import com.board.taskboard.model.BoardColumnKindEnum;
import com.board.taskboard.service.BlockService;
import com.board.taskboard.service.BoardColumnService;
import com.board.taskboard.service.BoardService;
import com.board.taskboard.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.board.taskboard.model.BoardColumnKindEnum.INITIAL;
import static com.board.taskboard.model.BoardColumnKindEnum.PENDING;
import static com.board.taskboard.model.BoardColumnKindEnum.CANCEL;
import static com.board.taskboard.model.BoardColumnKindEnum.FINAL;

@Component
public class MainMenu {
    @Autowired
    private BoardService boardService;

    @Autowired
    private CardService cardService;

    @Autowired
    private BoardColumnService boardColumnService;

    @Autowired
    private BlockService blockService;

    Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() throws SQLException {
        System.out.println("Bem vindo ao gerenciador de boards, escolha a opção desejada");
        var option = -1;
        while (true) {
            System.out.println("1 - Criar um novo board");
            System.out.println("2 - Selecionar um board existente");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");
            option = scanner.nextInt();
            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção inválida, informe uma opção do menu");
            }
        }
    }

    private void createBoard() throws SQLException {
        var entity = new Board();
        System.out.println("Informe o nome do seu board");
        entity.setName(scanner.next());

        System.out.println("Seu board terá colunas além das 3 padrões? Se sim informe quantas, senão digite '0'");
        var additionalColumns = scanner.nextInt();

        List<BoardColumn> columns = new ArrayList<>();

        System.out.println("Informe o nome da coluna inicial do board");
        var initialColumnName = scanner.next();
        var initialColumn = createColumn(initialColumnName, INITIAL, 0);
        initialColumn.setBoard(entity);
        columns.add(initialColumn);

        for (int i = 0; i < additionalColumns; i++) {
            System.out.println("Informe o nome da coluna de tarefa pendente do board");
            var pendingColumnName = scanner.next();
            var pendingColumn = createColumn(pendingColumnName, PENDING, i + 1);
            pendingColumn.setBoard(entity);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final");
        var finalColumnName = scanner.next();
        var finalColumn = createColumn(finalColumnName, FINAL, additionalColumns + 1);
        finalColumn.setBoard(entity);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna de cancelamento do board");
        var cancelColumnName = scanner.next();
        var cancelColumn = createColumn(cancelColumnName, CANCEL, additionalColumns + 2);
        cancelColumn.setBoard(entity);
        columns.add(cancelColumn);

        entity.setBoardColumns(columns);
        boardService.save(entity);
    }

    private void selectBoard() throws SQLException {
        System.out.println("Informe o id do board que deseja selecionar");
        var id = scanner.nextLong();
        var optional = boardService.findById(id);
        optional.ifPresentOrElse(
                b -> new BoardMenu(boardService,
                        cardService,
                        boardColumnService,
                        blockService, b).execute(),
                () -> System.out.printf("Não foi encontrado um board com id %s\n", id)
        );
    }

    private void deleteBoard() throws SQLException {
        System.out.println("Informe o id do board que será excluido");
        var id = scanner.nextLong();
        if (boardService.delete(id)){
            System.out.printf("O board %s foi excluido\n", id);
        } else {
            System.out.printf("Não foi encontrado um board com id %s\n", id);
        }
    }

    private BoardColumn createColumn(final String name, final BoardColumnKindEnum kind, final int order){
        var boardColumn = new BoardColumn();
        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setOrder(order);
        return boardColumn;
    }
}
