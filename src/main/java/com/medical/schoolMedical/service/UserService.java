package com.medical.schoolMedical.service;

import com.medical.schoolMedical.dto.UserDTO;
import com.medical.schoolMedical.entities.*;
import com.medical.schoolMedical.enums.Role;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.mapper.UserMapper;
import com.medical.schoolMedical.repositories.*;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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


//    public String getRole(UserDTO userDTO){
//        try{
//            return userDTO.getRole().name();
//        }catch (NullPointerException ex){
//            throw new NullPointerException("Role is null");
//        }
//
//    }


    //Kiểm tra coi đã đăng nhập chưa
    public boolean checkLogin(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return true;
        } else {
            return false;
        }
    }


    @PostConstruct
    public void initAdmin() {
        if (userRepository.findByUsername("admin") == null) {
//            Taọ User admin
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin")); // mã hoá password
            user.setRole(Role.ADMIN); // sử dụng enum
            userRepository.save(user);
//            Lưu admin vào bảng admin
            Admin admin = new Admin();
            admin.setUser(user);
            adminRepository.save(admin);
            System.out.println("Admin mặc định đã được tạo: admin/admin");
        }
    }

    // Lấy tất cả user chưa bị xóa
    public List<User> findAllUsers() {
        return userRepository.findByIsDeletedFalse();
    }

    // Lưu hoặc cập nhật user
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // Tìm theo ID
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Xóa mềm
    public void softDeleteUser(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setDeleted(true);
            userRepository.save(user);
        }
    }

}
