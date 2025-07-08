function showModal() {
    const modal = document.getElementById('modal');
    const modalTitle = document.getElementById('modal-title');

    modal.style.display = 'flex';
}

function closeModal() {
    const modal = document.getElementById('modal');
    modal.style.display = 'none';
}

function filterAppointments() {
    const filterValue = document.getElementById('filterStatus').value.toLowerCase();
    const rows = document.querySelectorAll('#appointmentsTable tbody tr');

    rows.forEach(row => {
        const status = row.cells[5].innerText.trim().toLowerCase();
        if (filterValue === '' || status === filterValue) {
            row.style.display = '';
        } else {
            row.style.display = 'none';
        }
    });
}

// Tải trang, áp dụng filter mặc định
document.addEventListener('DOMContentLoaded', () => {
    filterAppointments();
});

// Đóng modal khi nhấn ngoài modal
window.addEventListener('click', (event) => {
    const modal = document.getElementById('modal');
    if (event.target === modal) {
        modal.style.display = 'none';
    }
});
// So sánh thời gian để áp dụng lớp upcoming/past
document.addEventListener('DOMContentLoaded', () => {
    const now = new Date('2025-07-03 09:08'); // Thời gian hiện tại
    const rows = document.querySelectorAll('#appointmentsTable tbody tr');

    rows.forEach(row => {
        const scheduledTime = new Date(row.cells[3].textContent); // Cột thời gian hẹn
        if (scheduledTime < now) {
            row.classList.add('past');
            row.classList.remove('upcoming');
        } else {
            row.classList.add('upcoming');
            row.classList.remove('past');
        }
    });

    filterAppointments(); // Áp dụng filter mặc định
});

//createReview
// Đặt thời gian tạo mặc định là hiện tại (11:48 PM +07, 02/07/2025)
document.addEventListener('DOMContentLoaded', () => {
    const now = new Date();
    const createdAtInput = document.getElementById('createdAt');
    const formattedDate = now.toISOString().slice(0, 16); // Định dạng yyyy-MM-ddTHH:mm
    createdAtInput.value = formattedDate;

    // Xử lý thời gian lịch hẹn, đặt tối thiểu là hiện tại
    const scheduledTimeInput = document.getElementById('scheduled_time');
    scheduledTimeInput.min = formattedDate;

    // Xử lý submit form (có thể mở rộng để validate)
    document.getElementById('appointmentForm').addEventListener('submit', (e) => {
        const studentId = document.getElementById('student_id').value;
        const parentId = document.getElementById('parent_id').value;
        const nurseId = document.getElementById('school_nurse_id').value;
        if (!studentId || !parentId || !nurseId) {
            e.preventDefault();
            alert('Vui lòng điền đầy đủ mã học sinh, phụ huynh và y tế!');
        }
    });
});

//confirm of parent
function printVoucher() {
    window.print();
}

// Đặt thời gian tạo và hẹn mặc định gần với hiện tại (09:58 PM +07, 03/07/2025)
document.addEventListener('DOMContentLoaded', () => {
    const now = new Date();
    const formattedDate = now.toLocaleString('vi-VN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
    }).replace(/\//g, '-').replace(/, /g, ' ');

    // Nếu cần, có thể cập nhật thời gian tạo hoặc hẹn động (hiện tại dùng tĩnh)
    // document.querySelector('.info-group:nth-child(5) span').textContent = formattedDate;
});