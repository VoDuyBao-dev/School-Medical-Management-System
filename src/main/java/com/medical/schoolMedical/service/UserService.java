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
import org.springframework.transaction.annotation.Transactional;

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
            ex.printStackTrace(); //kiểm tra ếu lôỗi là user_id null
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

    @Transactional
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


    @PostConstruct
    public void initAdmin() {
        if (userRepository.findByUsername("admin") == null) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin")); // Mã hoá mật khẩu
            adminUser.setRole(Role.ADMIN);
            userRepository.save(adminUser);

            // Tạo và lưu admin vào bảng admins
            Admin admin = new Admin();
            admin.setUser(adminUser);
            admin.setFullName("Admin");
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
    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    // Xóa mềm
    public void softDeleteUser(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setDeleted(true);
            userRepository.save(user);
        }
    }

    // Tìm theo username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //Tìm kiếm admin
    public Admin findAdminByUsername(String username) {
        return adminRepository.findByUser_Username(username)
                .orElse(null);
    }

    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }


}
