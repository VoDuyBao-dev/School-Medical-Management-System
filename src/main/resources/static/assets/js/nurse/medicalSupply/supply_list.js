document.addEventListener("DOMContentLoaded", () => {
    const searchInput = document.getElementById("searchInput");
    const tbody       = document.getElementById("supplyTable");

    // --- Tìm kiếm theo tên vật tư ---
    searchInput?.addEventListener("input", () => {
        const kw = searchInput.value.trim().toLowerCase();
        Array.from(tbody.rows).forEach(row => {
            const name = row.cells[1].textContent.toLowerCase();
            row.style.display = name.includes(kw) ? "" : "none";
        });
    });

    // --- Ẩn thông báo sau 4s (nếu có alert nào hiện) ---
    const alerts = document.querySelectorAll(".alert, .notice-success, .notice-error");
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = "opacity 0.5s ease-out";
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 4000);
    });
});
