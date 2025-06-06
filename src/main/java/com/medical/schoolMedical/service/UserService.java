package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.UserDTO;
import com.medical.schoolMedical.entities.*;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.UserMapper;
import com.medical.schoolMedical.repositories.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class UserService {
    UserRepository userRepository;
    ParentRepositoty parentRepositoty;
    AdminRepository adminRepository;
    SchoolNurseRepository schoolNurseRepository;
    ManagerRepository managerRepository;
    UserMapper userMapper;
    public UserDTO signUp(UserDTO userDTO) {
        if(userRepository.existsByUsername(userDTO.getUsername())){
            throw new BusinessException(ErrorCode.USERNAME_EXISTS,"Người dùng đã tồn tại");
        }

        User user = userMapper.toUser(userDTO);

        //        Xử dụng thuật toán hash BCrypt để hash password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try{
            return userMapper.toUserDTO(createUser(user));
        }catch (Exception ex){
            //            Bắt các lỗi ngoài ý muốn
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Đăng ký thất bại, vui lòng thử lại");
        }


    }

    public void validateUserInput(UserDTO userDTO) {
        String password = userDTO.getPassword().trim();
        if (password.contains("<script>") || password.matches(".*[<>\"'].+")) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD, "Định dạng password không hợp lệ");
        }

        // gán lại username đã clean
        userDTO.setPassword(password);
    }

    public User createUser(User user){
            userRepository.save(user);
            switch (user.getRole()){
                case ADMIN:
                    Admin admin = new Admin();
                    admin.setUser(user);
                    adminRepository.save(admin);
                    break;
                case MANAGER:
                    Manager manager = new Manager();
                    manager.setUser(user);
                    managerRepository.save(manager);
                    break;
                case SCHOOL_NURSE:
                    SchoolNurse schoolNurse = new SchoolNurse();
                    schoolNurse.setUser(user);
                    schoolNurseRepository.save(schoolNurse);
                    break;
                case PARENT:
                    Parent parent = new Parent();
                    parent.setUser(user);
                    parentRepositoty.save(parent);
                    break;

            }
            return user;

    }


}
