document.addEventListener('DOMContentLoaded', function() {
    // Update avatar with first letter as the user types their name
    const displayNameInput = document.getElementById('display-name');
    const avatarCircle = document.querySelector('.avatar-circle');

    displayNameInput.addEventListener('input', function() {
        if (this.value.trim()) {
            avatarCircle.textContent = this.value.trim()[0].toUpperCase();
        } else {
            avatarCircle.textContent = '?';
        }
    });

    // "Skip for now" button functionality
    const skipButton = document.querySelector('.btn-outline');
    skipButton.addEventListener('click', function() {
        window.location.href = 'messaging.html'; // Redirect to the messaging interface
    });
});