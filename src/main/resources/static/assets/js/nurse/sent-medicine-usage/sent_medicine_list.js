document.addEventListener("DOMContentLoaded", function () {
    const rows = document.querySelectorAll("tbody tr");
    rows.forEach(row => {
        row.addEventListener("mouseover", () => {
            row.style.backgroundColor = "#f1f1f1";
        });
        row.addEventListener("mouseout", () => {
            row.style.backgroundColor = "";
        });
    });
});
