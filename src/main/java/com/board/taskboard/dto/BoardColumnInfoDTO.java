package com.board.taskboard.dto;

import com.board.taskboard.model.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id, int order, BoardColumnKindEnum kind) {
}
