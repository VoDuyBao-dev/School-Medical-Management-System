package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.ParentDTO;
import com.medical.schoolMedical.dto.UserDTO;
import com.medical.schoolMedical.entities.Parent;
import com.medical.schoolMedical.entities.User;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.ParentMapper;
import com.medical.schoolMedical.mapper.UserMapper;
import com.medical.schoolMedical.repositories.ParentRepositoty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ParentService {

    ParentRepositoty parentRepositoty;
    UserMapper userMapper;
    ParentMapper parentMapper;

    public ParentDTO getParentbyId(Long userId) {
        Parent parent = parentRepositoty.findByUser_Id(userId).orElseThrow(() -> new BusinessException(ErrorCode.PARENT_NOT_EXISTS));
        return parentMapper.toParentDTO(parent);
    }
}
