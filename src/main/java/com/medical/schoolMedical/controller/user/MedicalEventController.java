package com.medical.schoolMedical.controller.user;


import com.medical.schoolMedical.dto.MedicalEventDTO;
import com.medical.schoolMedical.entities.MedicalEvent;
import com.medical.schoolMedical.entities.SchoolNurse;
import com.medical.schoolMedical.entities.Student;
import com.medical.schoolMedical.entities.User;
import com.medical.schoolMedical.security.CustomUserDetails;
import com.medical.schoolMedical.service.MedicalEventService;
import com.medical.schoolMedical.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/school_nurse/medical-events")
@RequiredArgsConstructor
public class MedicalEventController {


    @Autowired
    private MedicalEventService medicalEventService;

    @Autowired
    private UserService userService;

    // Danh sách sự kiện y tế

    /*@GetMapping
    public String viewAllEvents(@AuthenticationPrincipal CustomUserDetails currentUser,
                                Model model) {
        SchoolNurse nurse = userService.findNurseByUsername(currentUser.getUsername());
        List<MedicalEvent> events = medicalEventService.findByNurseId(nurse.getId());

        model.addAttribute("events", events);
        return "nurse/list";
    }*/

    @GetMapping
    public String viewAllEvents(@AuthenticationPrincipal CustomUserDetails currentUser,
                                Model model) {
        SchoolNurse nurse = userService.findNurseByUsername(currentUser.getUsername());
        List<MedicalEvent> events = medicalEventService.findByNurseId(nurse.getId());

        List<MedicalEventDTO> dtoList = events.stream().map(event -> {
            MedicalEventDTO dto = new MedicalEventDTO();
            dto.setId(event.getId());
            dto.setStudentFullName(event.getStudent().getFullName());
            dto.setEventTime(event.getEventTime());
            dto.setLocation(event.getLocation());
            dto.setDescription(event.getDescription());
            return dto;
        }).toList();

        model.addAttribute("events", dtoList);
        return "nurse/list";
    }


    //Trang form thêm sự kiện y tế
    /*@GetMapping("/create")
    public String showCreateForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("students", userService.getAllStudents()); // Danh sách học sinh
        model.addAttribute("event", new MedicalEvent());

        return "nurse/create";
    }*/

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("students", userService.getAllStudents());
        model.addAttribute("eventDTO", new MedicalEventDTO());
        return "nurse/create";
    }



    //Xử lý lưu sự kiện
    /*@PostMapping("/save")
    public String saveMedicalEvent(@ModelAttribute("event") MedicalEvent event,
                                   @AuthenticationPrincipal CustomUserDetails currentUser,
                                   RedirectAttributes redirectAttributes) {
        SchoolNurse nurse = userService.findNurseByUsername(currentUser.getUsername());
        User createdBy = userService.findByUsername(currentUser.getUsername());

        event.setSchoolNurse(nurse);
        event.setUser(createdBy);

        medicalEventService.saveMedicalEvent(event);
        redirectAttributes.addFlashAttribute("success", "Ghi nhận sự kiện thành công!");
        return "redirect:/school_nurse/medical-events";
    }*/

    @PostMapping("/save")
    public String saveMedicalEvent(@ModelAttribute("eventDTO") MedicalEventDTO dto,
                                   @AuthenticationPrincipal CustomUserDetails currentUser,
                                   RedirectAttributes redirectAttributes) {
        Student student = userService.findStudentById(dto.getStudentId());
        SchoolNurse nurse = userService.findNurseByUsername(currentUser.getUsername());
        User createdBy = userService.findByUsername(currentUser.getUsername());

        MedicalEvent event = new MedicalEvent();
        event.setStudent(student);
        event.setSchoolNurse(nurse);
        event.setUser(createdBy);
        event.setLocation(dto.getLocation());
        event.setDescription(dto.getDescription());
        event.setInitial_treatment(dto.getInitialTreatment());
        event.setFinal_treatment(dto.getFinalTreatment());
        event.setNotes(dto.getNotes());

        medicalEventService.saveMedicalEvent(event);
        redirectAttributes.addFlashAttribute("success", "Ghi nhận sự kiện thành công!");
        return "redirect:/school_nurse/medical-events";
    }



    //Chi tiết một sự kiện
    /*@GetMapping("/{id}")
    public String viewEventDetail(@PathVariable Long id, Model model) {
        MedicalEvent event = medicalEventService.findMedicalEventById(id);
        model.addAttribute("event", event);
        return "nurse/detail";
    }*/

    @GetMapping("/{id}")
    public String viewEventDetail(@PathVariable Long id, Model model) {
        MedicalEvent event = medicalEventService.findMedicalEventById(id);

        // Chuyển đổi sang DTO
        MedicalEventDTO dto = new MedicalEventDTO();
        dto.setId(event.getId());
        dto.setStudentFullName(event.getStudent().getFullName());
        dto.setEventTime(event.getEventTime());
        dto.setLocation(event.getLocation());
        dto.setDescription(event.getDescription());
        dto.setInitialTreatment(event.getInitial_treatment());
        dto.setFinalTreatment(event.getFinal_treatment());
        dto.setNotes(event.getNotes());
        dto.setNurseFullName(event.getSchoolNurse().getFullName());


        model.addAttribute("event", dto);
        return "nurse/detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        MedicalEvent event = medicalEventService.findMedicalEventById(id);
        MedicalEventDTO dto = medicalEventService.convertToDto(event);

        model.addAttribute("event", dto);
        model.addAttribute("students", userService.getAllStudents());
        return "nurse/edit";
    }


    @PostMapping("/update")
    public String updateEvent(@ModelAttribute("event") MedicalEventDTO dto,
                              RedirectAttributes redirectAttributes) {
        medicalEventService.updateFromDto(dto);
        redirectAttributes.addFlashAttribute("success", "Cập nhật sự kiện thành công!");
        return "redirect:/school_nurse/medical-events";
    }


}
