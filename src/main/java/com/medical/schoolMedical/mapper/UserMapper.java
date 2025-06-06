package com.medical.schoolMedical.mapper;

import com.medical.schoolMedical.dto.UserDTO;
import com.medical.schoolMedical.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
}
