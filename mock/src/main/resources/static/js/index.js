console.log("Hi anh em group 4")
document.addEventListener('DOMContentLoaded', () => {
    const logoutLink = document.getElementById('logoutLink');
    const confirmPopup = document.getElementById('confirmPopup');
    const confirmButton = confirmPopup.querySelector('.confirm');
    const cancelButton = confirmPopup.querySelector('.cancel');

    logoutLink.addEventListener('click', (event) => {
        event.preventDefault(); // Prevent the default action (navigation)
        confirmPopup.style.display = 'flex'; // Show the confirmation popup
    });

    confirmButton.addEventListener('click', () => {
        window.location.href = logoutLink.href; // Proceed with logout
    });

    cancelButton.addEventListener('click', () => {
        confirmPopup.style.display = 'none'; // Hide the confirmation popup
    });
});