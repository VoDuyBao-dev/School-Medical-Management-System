document.addEventListener("DOMContentLoaded", () => {
    // Ẩn thông báo sau 4 giây
    document.querySelectorAll(".alert").forEach(alert => {
        setTimeout(() => {
            alert.style.transition = "opacity 0.5s";
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 4000);
    });

    // Lọc tìm kiếm (client-side) nếu muốn
    const searchInput = document.getElementById("searchInput");
    const tbody       = document.getElementById("recordTable");

    searchInput?.addEventListener("input", () => {
        const kw = searchInput.value.trim().toLowerCase();
        Array.from(tbody.rows).forEach(row => {
            const name = row.cells[1].textContent.toLowerCase();
            row.style.display = name.includes(kw) ? "" : "none";
        });
    });
});
