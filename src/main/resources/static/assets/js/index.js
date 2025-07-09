document.addEventListener('DOMContentLoaded', () => {
    // Accordion (Tài liệu)
    const accordionTitles = document.querySelectorAll('.accordion-title');
    accordionTitles.forEach(title => {
        title.addEventListener('click', () => {
            const item = title.parentElement;
            const content = item.querySelector('.accordion-content');
            const icon = title.querySelector('i');

            item.classList.toggle('active');
            if (item.classList.contains('active')) {
                content.style.display = 'block';
                content.style.opacity = '0';
                setTimeout(() => {
                    content.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
                    content.style.opacity = '1';
                    content.style.transform = 'perspective(500px) translateZ(0)';
                }, 50);
                icon.classList.remove('fa-plus');
                icon.classList.add('fa-minus');
            } else {
                content.style.opacity = '0';
                content.style.transform = 'perspective(500px) translateZ(-10px)';
                setTimeout(() => {
                    content.style.display = 'none';
                }, 600);
                icon.classList.remove('fa-minus');
                icon.classList.add('fa-plus');
            }
        });
    });

    // FAQ (giữ nguyên nếu có)
    const faqQuestions = document.querySelectorAll('.faq-question');
    faqQuestions.forEach(question => {
        question.addEventListener('click', () => {
            const item = question.parentElement;
            const answer = item.querySelector('.faq-answer');
            const icon = question.querySelector('i');

            item.classList.toggle('active');
            if (item.classList.contains('active')) {
                answer.style.display = 'block';
                answer.style.opacity = '0';
                setTimeout(() => {
                    answer.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
                    answer.style.opacity = '1';
                    answer.style.transform = 'perspective(500px) translateZ(0)';
                }, 50);
                icon.classList.remove('fa-plus');
                icon.classList.add('fa-minus');
            } else {
                answer.style.opacity = '0';
                answer.style.transform = 'perspective(500px) translateZ(-10px)';
                setTimeout(() => {
                    answer.style.display = 'none';
                }, 600);
                icon.classList.remove('fa-minus');
                icon.classList.add('fa-plus');
            }
        });
    });

    // Cập nhật thời gian thực (giữ nguyên nếu có)
    function updateTime() {
        const timeElement = document.getElementById('current-time');
        if (timeElement) {
            const now = new Date();
            timeElement.textContent = now.toLocaleString('en-US', {
                hour: '2-digit',
                minute: '2-digit',
                hour12: true,
                day: '2-digit',
                month: '2-digit',
                year: 'numeric'
            }) + ' +07';
        }
    }
    updateTime();
    setInterval(updateTime, 60000);
});