document.addEventListener("DOMContentLoaded", () => {
    // Auto-hide alerts after 4s
    document.querySelectorAll(".alert").forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 4000);
    });
});
