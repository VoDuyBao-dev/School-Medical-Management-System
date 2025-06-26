document.addEventListener('DOMContentLoaded', () => {
    const pagination = document.getElementById('pagination');
    const tableBody = document.getElementById('tableBody');
    const rowsPerPage = 10; // Số dòng hiển thị mỗi trang
    let rows = Array.from(tableBody.getElementsByTagName('tr'));
    const totalPages = Math.ceil(rows.length / rowsPerPage);
    let currentPage = 1; // Trang hiện tại mặc định

    // Hàm tạo phân trang động
    function createPagination() {
        pagination.innerHTML = '';
        for (let i = 1; i <= totalPages; i++) {
            const link = document.createElement('a');
            link.href = '#';
            link.textContent = i;
            link.dataset.page = i;
            if (i === currentPage) link.classList.add('current');
            pagination.appendChild(link);
        }
        // Thêm nút Next
        const nextLink = document.createElement('a');
        nextLink.href = '#';
        nextLink.id = 'nextPage';
        nextLink.textContent = 'Next';
        if (currentPage === totalPages) nextLink.style.display = 'none';
        pagination.appendChild(nextLink);
    }

    // Hàm hiển thị các dòng theo trang
    function displayRows(page) {
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        rows.forEach(row => row.style.display = 'none');
        rows.slice(start, end).forEach(row => row.style.display = '');
    }

    // Xử lý sự kiện click
    pagination.addEventListener('click', (e) => {
        e.preventDefault();
        const link = e.target;
        if (link.tagName === 'A') {
            const page = parseInt(link.dataset.page);
            if (page) {
                currentPage = page;
            } else if (link.id === 'nextPage' && currentPage < totalPages) {
                currentPage++;
            }
            displayRows(currentPage);
            createPagination(); // Cập nhật lại phân trang
        }
    });

    // Khởi tạo khi tải trang
    createPagination();
    displayRows(currentPage);
});