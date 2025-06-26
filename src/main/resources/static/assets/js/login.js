document.addEventListener('DOMContentLoaded', function() {
    const togglePassword = document.getElementById('togglePassword');
    const password = document.getElementById('password');
    const toggleConfirmPassword = document.getElementById('toggleConfirmPassword');
    const confirmPassword = document.getElementById('confirm-password');

    // Toggle hiển thị/ẩn mật khẩu
    if (togglePassword && password) {
        togglePassword.addEventListener('click', function () {
            const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
            password.setAttribute('type', type);
            this.classList.toggle('fa-eye-slash');
        });
    }

    // Toggle hiển thị/ẩn xác nhận mật khẩu
    if (toggleConfirmPassword && confirmPassword) {
        toggleConfirmPassword.addEventListener('click', function () {
            const type = confirmPassword.getAttribute('type') === 'password' ? 'text' : 'password';
            confirmPassword.setAttribute('type', type);
            this.classList.toggle('fa-eye-slash');
        });
    }

    // Mở modal OTP
    function openModal() {
        const otpModal = document.getElementById("otpModal");
        if (otpModal) {
            otpModal.style.display = "flex";
        }
    }

    // Đóng modal OTP
    function closeModal() {
        const otpModal = document.getElementById("otpModal");
        if (otpModal) {
            otpModal.style.display = "none";
        }
    }

    // Xử lý submit OTP
    function submitOTP() {
        const otpInputs = document.querySelectorAll('.otp-inputs input');
        const otp = Array.from(otpInputs).map(input => input.value).join('');
        if (otp && otp.length === otpInputs.length) {
            alert("Mã OTP đã được gửi: " + otp);
            closeModal();
        } else {
            alert("Vui lòng nhập đầy đủ mã OTP!");
        }
    }

    // Đóng modal khi click bên ngoài
    window.onclick = function(event) {
        const modal = document.getElementById("otpModal");
        if (event.target === modal) {
            closeModal();
        }
    };

    // Tự động chuyển sang ô input tiếp theo
    const otpInputs = document.querySelectorAll('.otp-inputs input');
    if (otpInputs.length > 0) {
        otpInputs.forEach((input, index, inputs) => {
            input.addEventListener('input', (e) => {
                if (e.inputType !== 'deleteContentBackward' && input.value.length === 1) {
                    if (index < inputs.length - 1) {
                        inputs[index + 1].focus();
                    }
                }
            });

            input.addEventListener('keydown', (e) => {
                if (e.key === 'Backspace' && !input.value && index > 0) {
                    inputs[index - 1].focus();
                }
            });
        });
    }

    // Xử lý gửi lại mã OTP bằng AJAX
    const resendOtpBtn = document.getElementById('resendOtpBtn');
    if (resendOtpBtn) {
        resendOtpBtn.addEventListener('click', function() {
            const resendBtn = this;
            resendBtn.disabled = true;
            resendBtn.innerText = 'Đang gửi...';

            fetch('/resend-otp', {
                method: 'POST',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
                .then(response => response.text())
                .then(text => {
                    try {
                        const data = JSON.parse(text);
                        console.log("✅ JSON đã parse:", data);
                        const message = document.getElementById('resendMessage');
                        if (message) {
                            if (data.success) {
                                message.innerText = '✅ Mã OTP đã được gửi lại thành công!';
                                message.style.color = 'green';
                            } else {
                                message.innerText = `❌ ${data.error}`;
                                message.style.color = 'red';
                            }
                            message.style.display = 'block';
                        }
                    } catch (e) {
                        console.error("❌ Lỗi khi parse JSON:", e, text);
                    }
                })
                .catch(error => {
                    console.error("❌ Lỗi khi xử lý phản hồi:", error);
                })
                .finally(() => {
                    resendBtn.disabled = false;
                    resendBtn.innerText = 'Gửi lại mã OTP';
                });
        });
    }

    // Gán các hàm vào global scope để gọi từ HTML
    window.openModal = openModal;
    window.closeModal = closeModal;
    window.submitOTP = submitOTP;
});