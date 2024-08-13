document.addEventListener('DOMContentLoaded', function() {
    var cancelOfferBtn = document.getElementById('cancelOfferBtn');
    if (cancelOfferBtn) {
        cancelOfferBtn.addEventListener('click', function(event) {
            event.preventDefault();
            document.getElementById('confirmCancelPopup').style.display = 'flex';
        });
    }

    document.getElementById('confirmNo').addEventListener('click', function() {
        document.getElementById('confirmCancelPopup').style.display = 'none';
    });

    var rejectOfferBtn = document.getElementById('rejectOfferBtn');
    if (rejectOfferBtn) {
        rejectOfferBtn.addEventListener('click', function(event) {
            event.preventDefault();
            console.log("hello")
            document.getElementById('rejectModal').style.display = 'flex';
        });
    }

    document.getElementById('rejectCancelBtn').addEventListener('click', function() {
        document.getElementById('rejectModal').style.display = 'none';
    })
});