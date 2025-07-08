package com.medical.schoolMedical.controller.schoolNurse;

import com.medical.schoolMedical.entities.SchoolNurse;
import com.medical.schoolMedical.entities.SentMedicine;
import com.medical.schoolMedical.entities.SentMedicineUsage;
import com.medical.schoolMedical.exceptions.BusinessException;
import com.medical.schoolMedical.exceptions.ErrorCode;
import com.medical.schoolMedical.service.SchoolNurseService;
import com.medical.schoolMedical.service.SentMedicineService;
import com.medical.schoolMedical.service.SentMedicineUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/nurse/sentMedicineUsage")
public class MedicineUsageController {
    @Autowired
    private SentMedicineService sentMedicineService;

    @Autowired
    private SentMedicineUsageService usageService;

    @Autowired
    private SchoolNurseService schoolNurseService;

    // 1. Danh sách thuốc phụ huynh đã gửi

    @GetMapping("/sent")
    public String listAllSentMedicines(Model model) {
        List<SentMedicine> sentList = sentMedicineService.getAll();
        model.addAttribute("sentList", sentList);
        return "nurse/sent-medicine-usage/sent_medicine_list";
    }

    @GetMapping("/use/{id}")
    public String showUsageForm(@PathVariable Long id, Model model) {
        //Tạo đối tượng usage mới
        SentMedicineUsage usage = new SentMedicineUsage();

        //Lấy thông tin gửi thuốc
        SentMedicine sentMedicine = sentMedicineService.getById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SENT_MEDICINE_NOT_FOUND));


        //Gán sentMedicine vào usage
        usage.setSentMedicine(sentMedicine);

        //Tách danh sách thuốc thành List<String>
        List<String> medicineOptions = Arrays.stream(sentMedicine.getMedicineList().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());


        model.addAttribute("usage", usage);
        model.addAttribute("medicineOptions", medicineOptions);
        return "nurse/sent-medicine-usage/sent_medicine_usage_form";
    }

    @PostMapping("/use")
    public String submitUsage(@ModelAttribute SentMedicineUsage usage,
                              @AuthenticationPrincipal UserDetails userDetails) {
        //Lấy username từ userDetails
        String username = userDetails.getUsername();

        //Tìm SchoolNurse từ schoolNurseService
        SchoolNurse nurse = schoolNurseService.findNurseByUsername(username);

        if (nurse == null) {
            throw new RuntimeException("Không tìm thấy nhân viên y tế đăng nhập.");
        }

        //Gán SchoolNurse cho usage
        usage.setSchoolNurse(nurse);

        usageService.create(usage);
        return "redirect:/nurse/sentMedicineUsage/sent";
    }



    //2. Lịch sử liều thuốc đã cho uống
    @GetMapping("/usage-history")
    public String usageHistory(Model model) {
        List<SentMedicineUsage> usageList = usageService.getAll();
        model.addAttribute("usageList", usageList);
        return "nurse/sent-medicine-usage/medicine_usage_history";
    }

}
