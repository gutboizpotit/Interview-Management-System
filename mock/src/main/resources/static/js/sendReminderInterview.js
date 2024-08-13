$(document).ready(function() {
    $('#sendReminderButton').on('click', function() {
        console.log("Confirm Cancel Button Clicked");
        $('#sendReminderForm').submit();
        $('#sendReminderModal').modal('hide');
    });

    setTimeout(function() {
        $('.alert-success, .alert-danger').fadeOut('slow');
    }, 5000);
});

document.addEventListener('DOMContentLoaded', function() {
    var toast = document.getElementById('toast');

    if (toast) {
        var message = document.getElementById('toast-content').textContent;

        console.log(message)

        if (message.includes("successfully!")) {
            toast.className = 'toast show success';
        } else if (message.includes("Fail")) {
            toast.className = 'toast show error';
        } else {
            toast.className = 'toast show';
        }

        toast.classList.add('show');
        setTimeout(function() {
            toast.classList.remove('show');
        }, 3000);
    }
});