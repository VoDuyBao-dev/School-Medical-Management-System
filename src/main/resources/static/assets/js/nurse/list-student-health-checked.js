// Chức năng "chọn tất cả"
document.addEventListener("DOMContentLoaded", () => {
    const selectAll = document.getElementById("selectAll");
    const checkboxes = document.querySelectorAll(".studentCheckbox");

    if (selectAll) {
        selectAll.addEventListener("change", () => {
            checkboxes.forEach(cb => cb.checked = selectAll.checked);
        });
    }

    // Tự ẩn alert sau 4 giây
    document.querySelectorAll(".alert").forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = "0";
            setTimeout(()=>alert.remove(), 500);
        }, 4000);
    });
});
