// Back to Top functionality
const backToTop = document.getElementById('backToTop');

// Show/hide button on scroll
window.addEventListener('scroll', () => {
    if (window.scrollY > 200) { // Show after scrolling 200px
        backToTop.style.display = 'block';
    } else {
        backToTop.style.display = 'none';
    }
});

// Scroll to top on click
backToTop.addEventListener('click', () => {
    window.scrollTo({
        top: 0,
        behavior: 'smooth' // Smooth scrolling
    });
});