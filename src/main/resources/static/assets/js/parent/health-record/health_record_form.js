document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("healthForm");
    const visionInput = document.getElementById("vision");

    form.addEventListener("submit", function (e) {
        const vision = parseFloat(visionInput.value);

        if (isNaN(vision) || vision < 0 || vision > 10) {
            alert("Thị lực phải nằm trong khoảng từ 0 đến 10.");
            visionInput.focus();
            e.preventDefault();
            return false;
        }

        // Optional: Kiểm tra các trường required theo JS nếu muốn mạnh tay hơn
        const requiredFields = form.querySelectorAll("[required]");
        for (const field of requiredFields) {
            if (!field.value.trim()) {
                alert("Vui lòng nhập đầy đủ thông tin bắt buộc.");
                field.focus();
                e.preventDefault();
                return false;
            }
        }
    });
});
