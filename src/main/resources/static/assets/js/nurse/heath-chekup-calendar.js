let currentDate = new Date();
let selectedDay = null;

function renderCalendar() {
    const calendar = document.getElementById('calendar');
    calendar.innerHTML = ''; // Xóa lịch cũ

    // Tiêu đề các ngày trong tuần
    const days = ['Chủ nhật', 'Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7'];
    days.forEach(day => {
        const dayHeader = document.createElement('div');
        dayHeader.classList.add('day-header');
        dayHeader.textContent = day;
        calendar.appendChild(dayHeader);
    });

    // Lấy thông tin tháng hiện tại
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const totalDays = lastDay.getDate();
    const firstDayIndex = firstDay.getDay(); // 0 (CN) đến 6 (T7)

    // Thêm các ngày trống trước ngày 1
    for (let i = 0; i < firstDayIndex; i++) {
        const emptyDay = document.createElement('div');
        calendar.appendChild(emptyDay);
    }

    // Thêm các ngày trong tháng
    for (let day = 1; day <= totalDays; day++) {
        const dayElement = document.createElement('div');
        dayElement.classList.add('day');
        dayElement.textContent = day;
        if (day === new Date().getDate() && month === new Date().getMonth() && year === new Date().getFullYear()) {
            dayElement.classList.add('selected'); // Đánh dấu ngày hiện tại
            selectedDay = dayElement;
            document.getElementById('date').value = `${day}/${month + 1}/${year}`;
        }
        dayElement.addEventListener('click', () => selectDay(dayElement));
        calendar.appendChild(dayElement);
    }

    // Cập nhật tháng năm hiển thị
    document.getElementById('current-month-year').textContent = `Tháng ${month + 1}/${year}`;
}

function changeMonth(delta) {
    currentDate.setMonth(currentDate.getMonth() + delta);
    renderCalendar();
}

function selectDay(dayElement) {
    if (selectedDay) {
        selectedDay.classList.remove('selected');
    }
    dayElement.classList.add('selected');
    selectedDay = dayElement;
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth() + 1;
    const day = dayElement.textContent;
    document.getElementById('date').value = `${day}/${month}/${year}`;
}

document.getElementById('checkupForm').addEventListener('submit', function(event) {
    event.preventDefault();
    if (selectedDay && document.getElementById('name').value && document.getElementById('time').value) {
        alert(`Đặt lịch thành công!\nHọ tên: ${document.getElementById('name').value}\nNgày: ${document.getElementById('date').value}\nGiờ: ${document.getElementById('time').value}`);
    } else {
        alert('Vui lòng chọn ngày, nhập họ tên và giờ khám!');
    }
});

// Tải lịch khi trang mở
window.onload = renderCalendar;