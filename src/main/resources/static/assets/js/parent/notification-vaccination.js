document.addEventListener("DOMContentLoaded", () => {
    // Tự ẩn alert sau 4 giây
    document.querySelectorAll(".alert").forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 4000);
    });
});
