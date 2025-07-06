document.addEventListener("DOMContentLoaded", () => {
    const alerts = document.querySelectorAll(".notice-success, .alert, .notice-error");
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = "opacity 0.5s ease-out";
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 4000); // 4 giây
    });
});