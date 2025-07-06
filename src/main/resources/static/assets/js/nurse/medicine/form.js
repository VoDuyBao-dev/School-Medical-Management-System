document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("medicineForm");
    form.addEventListener("submit", e => {
        const name     = document.getElementById("name").value.trim();
        const unit     = document.getElementById("unit").value.trim();
        const quantity = document.getElementById("quantityInStock").value;
        const expiry   = document.getElementById("expiryDate").value;

        if (!name || !unit || !quantity || !expiry) {
            alert("Vui lòng điền đầy đủ thông tin.");
            e.preventDefault();
            return;
        }
        if (parseInt(quantity) < 0) {
            alert("Số lượng trong kho không được nhỏ hơn 0.");
            e.preventDefault();
        }
    });
});
