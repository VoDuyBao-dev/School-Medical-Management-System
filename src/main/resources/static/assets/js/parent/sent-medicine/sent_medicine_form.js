document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("medicineForm");

    form.addEventListener("submit", function (e) {
        const student = document.getElementById("student").value.trim();
        const medicineList = document.getElementById("medicineList").value.trim();
        const instructions = document.getElementById("usageInstructions").value.trim();

        if (!student || !medicineList || !instructions) {
            alert("Vui lòng nhập đầy đủ thông tin trước khi gửi thuốc.");
            e.preventDefault();
        }
    });
});
