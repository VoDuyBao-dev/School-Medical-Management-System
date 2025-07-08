document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("#vaccinationScheduleForm");

    const vaccineInput = document.querySelector("#vaccineType");
    const injectionInput = document.querySelector("#injectionDate");
    const ageInput = document.querySelector("#recommendedAge");

    form.addEventListener("submit", (e) => {
        let hasError = false;

        clearError(vaccineInput);
        clearError(injectionInput);
        clearError(ageInput);

        // Vaccine type bắt buộc
        if (!vaccineInput.value.trim()) {
            showError(vaccineInput, "Loại vắc xin bắt buộc");
            hasError = true;
        }

        // Injection date bắt buộc và phải lớn hơn hiện tại
        if (!injectionInput.value) {
            showError(injectionInput, "Ngày giờ tiêm bắt buộc");
            hasError = true;
        } else {
            const selected = new Date(injectionInput.value);
            const now = new Date();
            if (selected <= now) {
                showError(injectionInput, "Ngày giờ tiêm phải lớn hơn hiện tại");
                hasError = true;
            }
        }

        // Độ tuổi: không âm, không 0 nếu có nhập
        const ageValue = ageInput.value.trim();
        if (ageValue) {
            const ageNumber = parseInt(ageValue);
            if (isNaN(ageNumber) || ageNumber <= 0) {
                showError(ageInput, "Độ tuổi khuyến nghị phải lớn hơn 0");
                hasError = true;
            }
        }

        if (hasError) {
            e.preventDefault();
        }
    });

    function showError(el, message) {
        el.classList.add("is-invalid");
        let feedback = document.createElement("div");
        feedback.className = "invalid-feedback";
        feedback.innerText = message;
        el.parentNode.appendChild(feedback);
    }

    function clearError(el) {
        el.classList.remove("is-invalid");
        const feedback = el.parentNode.querySelector(".invalid-feedback");
        if (feedback) feedback.remove();
    }
});
