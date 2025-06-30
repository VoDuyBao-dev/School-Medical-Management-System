package com.medical.schoolMedical.controller.user;

import com.medical.schoolMedical.entities.HealthRecord;
import com.medical.schoolMedical.service.HealthRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/school_nurse/health-record")
@RequiredArgsConstructor
public class NurseHealthRecordController {
    @Autowired
    private HealthRecordService healthRecordService;

    @GetMapping
    public String listHealthRecord(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<HealthRecord> records = (keyword == null || keyword.isEmpty())
                ? healthRecordService.getAll()
                : healthRecordService.searchByStudentName(keyword);

        model.addAttribute("records", records);
        model.addAttribute("keyword", keyword);
        return "nurse/health-records/health_record_list";
    }

    /*@GetMapping("/view/{id}")
    public String viewDetail(@PathVariable Long id, Model model) {

        HealthRecord record = healthRecordService.getById(id);

        model.addAttribute("records", record);
        return "nurse/health-records/health_record_view";
    }*/

    @GetMapping("/view/{id}")
    public String viewDetail(@PathVariable Long id, Model model) {

        Optional<HealthRecord> optionalRecord = healthRecordService.findByIdWithStudentAndParent(id);

        if (optionalRecord.isEmpty()) {
            return "redirect:/school_nurse/health-records";
        }

        model.addAttribute("record", optionalRecord.get());
        return "nurse/health-records/health_record_view";
    }


}
