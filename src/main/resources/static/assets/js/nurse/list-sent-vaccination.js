document.addEventListener("DOMContentLoaded", () => {
    // Tự động ẩn thông báo sau 5 giây
    const alerts = document.querySelectorAll(".alert");
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = "opacity 0.5s ease-out";
            alert.style.opacity = "0";
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });
    const table = document.getElementById("vaccinationTable");
    const tbody = table.querySelector("tbody");
    const rows = Array.from(tbody.querySelectorAll("tr"))
        .filter(row => !row.querySelector("td").hasAttribute("colspan")); // bỏ dòng trống

    rows.sort((a, b) => {
        const dateA = a.children[1].textContent.trim(); // cột "Ngày tiêm"
        const timeA = a.children[2].textContent.trim(); // cột "Giờ tiêm"
        const dateB = b.children[1].textContent.trim();
        const timeB = b.children[2].textContent.trim();

        const dateTimeA = new Date(`${convertDate(dateA)} ${timeA}`);
        const dateTimeB = new Date(`${convertDate(dateB)} ${timeB}`);
        return dateTimeA - dateTimeB;
    });

    // Clear bảng và gắn lại theo thứ tự mới
    rows.forEach((row, index) => {
        row.children[0].textContent = index + 1; // cập nhật STT
        tbody.appendChild(row); // thêm lại vào bảng
    });

    // Chuyển "dd/MM/yyyy" => "yyyy-MM-dd"
    function convertDate(str) {
        const [d, m, y] = str.split('/');
        return `${y}-${m}-${d}`;
    }
});
