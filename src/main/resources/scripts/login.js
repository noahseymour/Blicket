document.addEventListener('DOMContentLoaded', function() {
    const toggleButton = document.getElementById('toggle-visibility');
    const privateKeyField = document.getElementById('private-key');
    const eyeIcon = toggleButton.querySelector('.eye-icon');

    toggleButton.addEventListener('click', function() {
        if (privateKeyField.classList.contains('hidden-text')) {
            privateKeyField.classList.remove('hidden-text');
            eyeIcon.classList.add('hidden');
        } else {
            privateKeyField.classList.add('hidden-text');
            eyeIcon.classList.remove('hidden');
        }
    });
});