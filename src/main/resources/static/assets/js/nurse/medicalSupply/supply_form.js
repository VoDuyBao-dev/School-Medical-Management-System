document.addEventListener("DOMContentLoaded",()=>{
    const form=document.getElementById("supplyForm");
    form.addEventListener("submit",e=>{
        const name=document.getElementById("name").value.trim();
        const qty=document.getElementById("quantityInStock").value;
        if(!name||!qty){
            alert("Vui lòng điền đầy đủ thông tin.");
            e.preventDefault();
            return;
        }
        if(parseInt(qty)<0){
            alert("Số lượng không được âm.");
            e.preventDefault();
        }
    });
});
