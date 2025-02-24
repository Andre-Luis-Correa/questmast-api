package com.questmast.questmast.core.selectionprocesstest.mapper;

import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SelectionProcessTestMapper {

    SelectionProcessTest convert();
}
