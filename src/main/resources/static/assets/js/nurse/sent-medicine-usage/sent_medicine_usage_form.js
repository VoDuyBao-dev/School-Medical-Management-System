document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("usageForm");
    const medicineSelect = document.getElementById("medicineName");
    const dosageInput = document.getElementById("dosage");

    form.addEventListener("submit", (e) => {
        if (!medicineSelect.value) {
            alert("Vui lòng chọn thuốc.");
            e.preventDefault();
            medicineSelect.focus();
            return;
        }
        if (!dosageInput.value.trim()) {
            alert("Vui lòng nhập liều dùng.");
            e.preventDefault();
            dosageInput.focus();
        }
    });
});
