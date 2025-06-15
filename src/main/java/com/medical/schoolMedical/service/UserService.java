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

import java.util.Optional;

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

    @Autowired
    PasswordEncoder passwordEncoder;


    public UserDTO signUp(UserDTO userDTO) {
        if(userRepository.existsByUsername(userDTO.getUsername())){
            throw new BusinessException(ErrorCode.USERNAME_EXISTS);
        }

        User user = userMapper.toUser(userDTO);

        //        Xử dụng thuật toán hash BCrypt để hash password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try{
            return userMapper.toUserDTO(createUser(user));
        }catch (Exception ex){
            //            Bắt các lỗi ngoài ý muốn
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }


    }

    public void validateUserInput(UserDTO userDTO) {
        String password = userDTO.getPassword().trim();
        if (password.contains("<script>") || password.matches(".*[<>\"'].+")) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
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


    //Kiểm tra coi đã đăng nhập chưa
    public boolean checkLogin(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    /*public User checkLogin(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);

        }
    }*/







}
