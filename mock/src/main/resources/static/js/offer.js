document.addEventListener('DOMContentLoaded', function() {
    const exportBtn = document.getElementById('exportBtn');
    const exportPopup = document.getElementById('exportPopup');
    const exportCancelBtn = document.getElementById('exportCancelBtn');

    exportBtn.addEventListener('click', function() {
        console.log('Export button clicked'); // Debug log
        exportPopup.style.display = 'flex';
    });

    exportCancelBtn.addEventListener('click', function() {
        exportPopup.style.display = 'none';
    });

    document.getElementById('exportForm').addEventListener('submit', function(event) {
        var dateFrom = document.getElementById('dateFrom').value;
        var dateTo = document.getElementById('dateTo').value;
        console.log(dateFrom, dateTo)
        if (dateFrom > dateTo) {
            event.preventDefault();
            alert('From Date needs to be earlier than To Date');
        }
    });

    var successAlert = document.getElementById('successAlert');
    if (successAlert) {
        setTimeout(function() {
            successAlert.style.transition = 'opacity 0.5s';
            successAlert.style.opacity = '0';
            setTimeout(function() {
                successAlert.style.display = 'none';
            }, 500);
        }, 1500);
    }
});

