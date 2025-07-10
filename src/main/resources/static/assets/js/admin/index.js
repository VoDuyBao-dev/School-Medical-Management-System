document.addEventListener('DOMContentLoaded', () => {
    // Lấy dữ liệu từ Thymeleaf
    const medicalData = [[/* labels */], [/* khám bệnh data */], [/* tiêm thuốc data */]];
    const eventData = [[/* labels */], [/* ca sự kiện data */]];

    // Giả định dữ liệu mẫu nếu không có từ backend
    if (!medicalData[0].length || !eventData[0].length) {
        eventData[0] = ['01/06', '03/06', '05/06', '07/06', '09/06','11/06', '13/06', '15/06', '17/06', '19/06','21/06', '23/06', '25/06', '27/06', '29/06' ];
        eventData[1] = [15, 20, 25, 18, 30, 12, 1, 8, 4, 13, 16, 20, 7, 2, 22];        // Ca sự kiện
    }

    // Biểu đồ thống kê khám bệnh & tiêm thuốc (Line Chart đôi)
    const ctx = document.getElementById('myChart').getContext('2d');

// Gradient cho khám sức khỏe (xanh)
    const gradientBlue = ctx.createLinearGradient(0, 0, 0, 400);
    gradientBlue.addColorStop(0, 'rgba(75, 192, 192, 0.4)');
    gradientBlue.addColorStop(1, 'rgba(75, 192, 192, 0)');

// Gradient cho chích thuốc (cam)
    const gradientOrange = ctx.createLinearGradient(0, 0, 0, 400);
    gradientOrange.addColorStop(0, 'rgba(255, 99, 132, 0.4)');
    gradientOrange.addColorStop(1, 'rgba(255, 159, 64, 0)');

    const data = {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [
            {
                label: 'Khám sức khỏe',
                data: [65, 59, 80, 81, 56, 55, 40],
                borderColor: 'rgb(75, 192, 192)',
                backgroundColor: 'rgba(75, 192, 192, 0.1)',
                fill: true,
                borderWidth: 3,
                tension: 0.4,
                pointBackgroundColor: '#fff',
                pointBorderColor: 'rgb(75, 192, 192)',
                pointRadius: 5,
            },
            {
                label: 'Tiêm thuốc',
                data: [28, 48, 40, 19, 86, 27, 90],
                borderColor: 'rgb(255, 99, 132)',
                backgroundColor: 'rgba(255, 99, 132, 0.1)',
                fill: true,
                borderWidth: 3,
                tension: 0.4,
                pointBackgroundColor: '#fff',
                pointBorderColor: 'rgb(255, 99, 132)',
                pointRadius: 5,
            }
        ]
    };

    const config = {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'Số lần Khám sức khỏe & Tiêm thuốc theo tháng'
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            animation: {
                duration: 1500,
                easing: 'easeOutQuart'
            }
        }
    };

    new Chart(ctx, config);

    // Biểu đồ thống kê ca sự kiện y tế (Bar Chart)
    const eventCtx = document.getElementById('eventStatsChart').getContext('2d');
    new Chart(eventCtx, {
        type: 'bar',
        data: {
            labels: eventData[0],
            datasets: [{
                label: 'Ca Sự Kiện Y Tế',
                data: eventData[1],
                backgroundColor: '#2ecc71',
                borderRadius: 5
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true,
                    title: { display: true, text: 'Số Ca' }
                },
                x: {
                    title: { display: true, text: 'Ngày' }
                }
            },
            plugins: {
                legend: { display: false }
            },
            animation: {
                duration: 1000,
                easing: 'easeInOutQuad'
            }
        }
    });
});

// newStudent
function switchTab(tab) {
    const buttons = document.querySelectorAll('.tab-button');
    buttons.forEach(btn => btn.classList.remove('active'));
    const activeButton = tab === 'week' ? buttons[0] : buttons[1];
    activeButton.classList.add('active');

    // Có thể thêm logic để tải dữ liệu khác cho từng tab nếu cần
}