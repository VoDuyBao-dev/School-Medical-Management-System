package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.UserDTO;
import com.medical.schoolMedical.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);

    @Mapping(target = "id", ignore = true)
    User toUser(UserDTO userDTO);
}
