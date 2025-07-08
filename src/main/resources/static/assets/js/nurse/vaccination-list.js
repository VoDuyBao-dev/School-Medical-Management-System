// static/assets/js/nurse/vaccination-list.js
document.addEventListener("DOMContentLoaded", () => {
    const selectAll  = document.getElementById("selectAll");
    const checkboxes = document.querySelectorAll(".studentCheckbox");
    const form       = document.querySelector("form");

    /* ---------- 1. Chọn / bỏ chọn tất cả ---------- */
    if(selectAll){
        selectAll.addEventListener("change",()=>{
            checkboxes.forEach(cb=>{
                cb.checked = selectAll.checked;
                toggleRowHighlight(cb);
            });
        });
    }

    /* ---------- 2. Cập nhật trạng thái selectAll ---------- */
    checkboxes.forEach(cb=>{
        cb.addEventListener("change",()=>{
            selectAll.checked=[...checkboxes].every(b=>b.checked);
            toggleRowHighlight(cb);
        });
    });

    function toggleRowHighlight(cb){
        const row=cb.closest("tr");
        if(cb.checked){
            row.style.background="#fde3ef";
        }else{
            row.style.background="#fff";
        }
    }

    /* ---------- 3. Kiểm tra & xác nhận khi gửi ---------- */
    if(form){
        form.addEventListener("submit",e=>{
            if(![...checkboxes].some(cb=>cb.checked)){
                e.preventDefault();
                showToast("Vui lòng chọn ít nhất một học sinh!", "error");
                return;
            }
            if(!confirm("Bạn chắc chắn muốn gửi kết quả đến phụ huynh?")){
                e.preventDefault();
            }else{
                showToast("Đang gửi kết quả...", "info");
            }
        });
    }

    /* ---------- 4. Hàm toast ---------- */
    function showToast(message,type="info"){
        const container=document.getElementById("toast-container");
        if(!container) return alert(message); // fallback

        const toast=document.createElement("div");
        toast.className=`toast toast-${type}`;
        toast.textContent=message;
        container.appendChild(toast);

        // chuyển sang trạng thái hiện
        setTimeout(()=>toast.classList.add("show"),10);

        // tự ẩn sau 4s
        setTimeout(()=>{
            toast.classList.remove("show");
            setTimeout(()=>toast.remove(),400);
        },4000);
    }
});
