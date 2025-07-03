package com.bitlab.project.mapper;

import com.bitlab.project.model.dto.UserDto;
import com.bitlab.project.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    User toUser(UserDto userDto);

    UserDto toUserDto (User user);

}