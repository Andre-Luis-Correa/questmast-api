package com.questmast.questmast.core.authentication.user.mapper;

import com.questmast.questmast.core.authentication.user.domain.dto.UserDTO;
import com.questmast.questmast.core.authentication.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "mainEmail")
    UserDTO convertUserToUserDTO(User user);
}
