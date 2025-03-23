document.addEventListener('DOMContentLoaded', function() {
    const displayNameInput = document.getElementById('display-name');
    const avatarCircle = document.querySelector('.avatar-circle');

    displayNameInput.addEventListener('input', function() {
        if (this.value.trim()) {
            avatarCircle.textContent = this.value.trim()[0].toUpperCase();
        } else {
            avatarCircle.textContent = '?';
        }
    });

    const skipButton = document.querySelector('.btn-outline');
    skipButton.addEventListener('click', function() {
        window.location.href = 'messaging.html'; // Redirect to the messaging interface
    });
});