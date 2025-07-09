document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("studentForm");
    form.addEventListener("submit", (e) => {
        const name = form.querySelector('input[name="fullName"]').value.trim();
        const address = form.querySelector('input[name="address"]').value.trim();
        const className = form.querySelector('input[name="className"]').value.trim();

        if (!name || !address || !className) {
            alert("Vui lòng điền đầy đủ thông tin bắt buộc.");
            e.preventDefault();
        }
    });
});
