package com.alexdev.mappers;

import com.alexdev.dto.response.MarkDTO;
import com.alexdev.entities.Mark;

public class MarkMapper {

    public static MarkDTO entityToDTO(Mark mark) {

        if (mark == null) {
            return null;
        }
        return new MarkDTO(mark.getId(), mark.getName());
    }
}
