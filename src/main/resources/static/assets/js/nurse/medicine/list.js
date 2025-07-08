document.addEventListener("DOMContentLoaded", () => {
    /* ===== 1. TỰ ẨN THÔNG BÁO SAU 4S ===== */
    const alerts = document.querySelectorAll(".notice-success, .alert, .notice-error");
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = "opacity 0.5s ease-out";
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 4000);   // 4 giây
    });

    /* ===== 2. TÌM KIẾM THEO TÊN THUỐC ===== */
    const searchInput = document.getElementById("searchInput");
    const tbody       = document.getElementById("medicineTable");

    searchInput?.addEventListener("input", () => {
        const kw = searchInput.value.trim().toLowerCase();
        Array.from(tbody.rows).forEach(r => {
            const name = r.cells[1].textContent.toLowerCase();
            r.style.display = name.includes(kw) ? "" : "none";
        });
    });
});
