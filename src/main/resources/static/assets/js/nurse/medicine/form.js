document.addEventListener("DOMContentLoaded", () => {
    const form     = document.getElementById("medicineForm");
    const nameInp  = document.getElementById("name");
    const unitInp  = document.getElementById("unit");
    const qtyInp   = document.getElementById("quantityInStock");
    const expInp   = document.getElementById("expiryDate");

    form.addEventListener("submit", (e) => {
        const name     = nameInp.value.trim();
        const unit     = unitInp.value.trim();
        const quantity = qtyInp.value;
        const expiry   = expInp.value;

        /* ─── Kiểm tra bắt buộc ─────────────────────────── */
        if (!name || !unit || !quantity || !expiry) {
            alert("Vui lòng điền đầy đủ thông tin.");
            e.preventDefault();
            return;
        }

        /* ─── Số lượng không âm ─────────────────────────── */
        if (parseInt(quantity, 10) < 0) {
            alert("Số lượng trong kho không được nhỏ hơn 0.");
            e.preventDefault();
            return;
        }

        /* ─── Hạn sử dụng phải từ hôm nay trở đi ─────────── */
        const today   = new Date();
        today.setHours(0,0,0,0);                    // 00:00 hôm nay
        const expDate = new Date(expiry);

        if (expDate < today) {
            alert("Hạn sử dụng phải lớn hơn hoặc bằng ngày hiện tại!");
            e.preventDefault();
        }
    });
});
