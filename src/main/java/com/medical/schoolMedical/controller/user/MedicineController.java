package com.medical.schoolMedical.controller.user;

import com.medical.schoolMedical.entities.Medicine;
import com.medical.schoolMedical.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/nurse/medicines")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;

    // 1. Xem danh sách thuốc
    @GetMapping
    public String listMedicines(Model model) {
        List<Medicine> medicines = medicineService.getAllMedicines();
        model.addAttribute("medicines", medicines);
        return "nurse/medicine/list";
    }

    // 2. Hiển thị form thêm thuốc
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("medicine", new Medicine());
        return "nurse/medicine/form";
    }

    // 3. Lưu thuốc mới
    @PostMapping("/save")
    public String saveMedicine(@ModelAttribute("medicine") Medicine medicine, RedirectAttributes redirectAttributes) {


        // Kiểm tra nếu là thêm mới
        boolean isNew = (medicine.getId() == null);

        // Kiểm tra trùng tên (bỏ qua khoảng trắng và chữ hoa)
        String normalizedName = medicine.getName().trim();

        if (isNew) {
            // Kiểm tra trùng tên khi thêm mới
            if (medicineService.existsByName(normalizedName)) {
                redirectAttributes.addFlashAttribute("error", "Thuốc với tên \"" + normalizedName + "\" đã tồn tại.");
                return "redirect:/nurse/medicines/new";
            }
        } else {
            if (medicineService.isNameTakenByOtherId(normalizedName, medicine.getId())) {
                redirectAttributes.addFlashAttribute("error", "Tên thuốc \"" + normalizedName + "\" đã được sử dụng bởi một thuốc khác.");
                return "redirect:/nurse/medicines/edit/" + medicine.getId();
            }
        }

        // Nếu là cập nhật (đã có ID), giữ nguyên entryDate cũ
        if (medicine.getId() != null && medicine.getId() != 0) {
            Medicine existing = medicineService.getMedicineById(medicine.getId());
            medicine.setEntryDate(existing.getEntryDate());
        }

        medicine.setName(normalizedName);
        medicineService.saveMedicine(medicine); // sử dụng service để lưu luôn
        System.out.println("DEBUG: Đã lưu thành công thuốc, ID = " + medicine.getId());

        redirectAttributes.addFlashAttribute("success", isNew ? "Thêm thuốc thành công." : "Cập nhật thuốc thành công.");

        return "redirect:/nurse/medicines";
    }



    // 4. Hiển thị form chỉnh sửa thuốc
    @GetMapping("/edit/{id}")
    public String editMedicine(@PathVariable Long id, Model model) {
        Medicine medicine = medicineService.getMedicineById(id);
        model.addAttribute("medicine", medicine);
        return "nurse/medicine/form";
    }

    // 5. Xóa thuốc
    @GetMapping("/delete/{id}")
    public String deleteMedicine(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        medicineService.deleteMedicine(id);
        redirectAttributes.addFlashAttribute("success", "Xóa thuốc thành công!");
        return "redirect:/nurse/medicines";
    }

}
