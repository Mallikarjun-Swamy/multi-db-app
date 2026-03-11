package com.ms.mapper;

import com.ms.dto.request.UserRequestDto;
import com.ms.dto.response.UserResponseDto;
import com.ms.model.postgres.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestDto dto);

    UserResponseDto toDto(User user);

    List<UserResponseDto> toDtoList(List<User> users);

}
