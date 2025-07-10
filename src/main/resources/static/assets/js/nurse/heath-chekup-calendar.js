let currentDate = new Date();
let selectedDay = null;

function renderCalendar() {
    const calendar = document.getElementById('calendar');
    calendar.innerHTML = ''; // Xóa lịch cũ

    const days = ['Chủ nhật', 'Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7'];
    days.forEach(day => {
        const dayHeader = document.createElement('div');
        dayHeader.classList.add('day-header');
        dayHeader.textContent = day;
        calendar.appendChild(dayHeader);
    });

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const totalDays = lastDay.getDate();
    const firstDayIndex = firstDay.getDay();

    for (let i = 0; i < firstDayIndex; i++) {
        const emptyDay = document.createElement('div');
        calendar.appendChild(emptyDay);
    }

    for (let day = 1; day <= totalDays; day++) {
        const dayElement = document.createElement('div');
        dayElement.classList.add('day');
        dayElement.textContent = day;
        if (day === new Date().getDate() && month === new Date().getMonth() && year === new Date().getFullYear()) {
            dayElement.classList.add('selected');
            selectedDay = dayElement;
            const mm = String(month + 1).padStart(2, '0');
            const dd = String(day).padStart(2, '0');
            document.getElementById('date').value = `${year}-${mm}-${dd}`;
        }
        dayElement.addEventListener('click', () => selectDay(dayElement));
        calendar.appendChild(dayElement);
    }

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
    const month = String(currentDate.getMonth() + 1).padStart(2, '0');
    const day = String(dayElement.textContent).padStart(2, '0');
    document.getElementById('date').value = `${year}-${month}-${day}`;
}
document.getElementById('checkupForm').addEventListener('submit', function(event) {
    const className = document.getElementById('classId').value;
    const date = document.getElementById('date').value;
    const time = document.getElementById('time').value;

    if (!selectedDay || !className || !time) {
        event.preventDefault();
        alert('Vui lòng chọn ngày, lớp và giờ khám!');
    }
});

window.onload = renderCalendar;
