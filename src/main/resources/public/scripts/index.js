// Make sure the "How It Works" button scrolls to the section
document.addEventListener('DOMContentLoaded', function() {
    const howItWorksBtn = document.querySelector('.btn-secondary');
    const howItWorksSection = document.getElementById('how-it-works');

    howItWorksBtn.addEventListener('click', function(e) {
        e.preventDefault();
        howItWorksSection.scrollIntoView({
            behavior: 'smooth'
        });
    });
});