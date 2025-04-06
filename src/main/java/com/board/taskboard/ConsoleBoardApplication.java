package com.board.taskboard;

import com.board.taskboard.service.BlockService;
import com.board.taskboard.service.BoardColumnService;
import com.board.taskboard.service.BoardService;
import com.board.taskboard.service.CardService;
import com.board.taskboard.ui.MainMenu;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;


import java.sql.SQLException;

@SpringBootApplication
public class ConsoleBoardApplication {
    public static void main(String[] args) throws SQLException {
        ApplicationContext context = new SpringApplicationBuilder(ConsoleBoardApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);;

        //BlockService blockService = context.getBean(BlockService.class);
        //CardService cardService = context.getBean(CardService.class);
        //BoardService boardService = context.getBean(BoardService.class);
        //BoardColumnService boardColumnService = context.getBean(BoardColumnService.class);

        MainMenu mainMenu = context.getBean(MainMenu.class);
        mainMenu.execute();
    }
}
