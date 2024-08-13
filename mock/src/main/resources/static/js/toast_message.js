function toast({
                   status = 1,
                   message = ""
               }) {
    const app = document.querySelector('.wrap');
    const wrap = document.createElement('div');
    if (status == 0) {
        wrap.innerHTML = `
            <div class="alert alert-unsignin">
            <i class="check-icon fas fa-check-circle icon-unsignin"></i>
            <div class="content">
                <h3 class="content-head">Lỗi!</h3>
                <p class="content-sub">${message || 'Tài khoản của bạn không tồn tại'}</p>
            </div>
            <div class = "icon">
            <i class="close-icon fas fa-times"></i></div>
        </div>
            `;
    } else {
        wrap.innerHTML = `
            <div class="alert alert-signin">
            <i class="check-icon fas fa-check-circle icon-signin"></i>
            <div class="content">
                <h3 class="content-head">Thành công!</h3>
                <p class="content-sub">${message || 'Bạn đã đăng kí thành công.'}</p>
            </div>
            <div class = "icon">
            <i class="close-icon fas fa-times"></i></div>
        </div>
            `;
    }

    app.appendChild(wrap);
    //Tắt auto
    const autoclose = setTimeout(function () {
        app.removeChild(wrap);
    }, 3000)
    //Tắt bằng cách click
    wrap.onclick = function (e) {
        console.log(e.target);

        if (e.target.closest('.icon')) {
            app.removeChild(wrap);
            clearTimeout(autoclose);
        }
    }
}

function showalertdk() {
    toast({
        status: 1,
        message: "Test demo succes"
    });
}
function showalertdk1() {
    toast({
        status: 0,
        message: "Test demo failed"
    });
}