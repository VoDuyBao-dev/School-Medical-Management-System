package com.medical.schoolMedical.controller.manager;

import com.medical.schoolMedical.enums.Gender;
import com.medical.schoolMedical.service.ParentService;
import com.medical.schoolMedical.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/manager/students")
public class ManagerStudentController {
    @Autowired
    private ParentService parentService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/create")
    public String showCreateStudentForm(Model model) {
        model.addAttribute("parentList", parentService.getAllParents());
        return "manager/student_form";
    }

    @PostMapping("/create")
    public String createStudent(@RequestParam String fullName,
                                @RequestParam Gender gender,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthDate,
                                @RequestParam String address,
                                @RequestParam String className,
                                @RequestParam Long parentId) {
        studentService.createStudent(fullName, gender, birthDate, address, className, parentId);
        return "redirect:/manager/students/list";
    }

    @GetMapping("/list")
    public String listStudents(Model model) {
        model.addAttribute("studentList", studentService.getAllStudents());
        return "manager/student_list";
    }
}
