document.addEventListener("DOMContentLoaded", () => {
    const errorAlert = document.querySelector(".alert-danger");
    if (errorAlert) {
        setTimeout(() => {
            errorAlert.style.opacity = "0";
            setTimeout(() => errorAlert.remove(), 500);
        }, 4000);
    }
});
