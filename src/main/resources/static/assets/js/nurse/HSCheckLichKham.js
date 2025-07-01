document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('searchInput');
    const clearSearch = document.getElementById('clearSearch');
    const toggleList = document.getElementById('toggleList');
    const confirmedTable = document.getElementById('confirmedTable');
    const checkedTable = document.getElementById('checkedTable');
    const confirmedStudentTable = document.getElementById('confirmedStudentTable');
    const checkedStudentTable = document.getElementById('checkedStudentTable');
    const noData = document.getElementById('noData');
    const pagination = document.getElementById('pagination');
    const rowsPerPage = 3;
    let currentPage = 1;
    let isConfirmedList = true;

    const confirmedRows = Array.from(confirmedStudentTable.getElementsByTagName('tr'));
    const checkedRows = Array.from(checkedStudentTable.getElementsByTagName('tr'));

    // Toggle between lists
    toggleList.addEventListener('click', (e) => {
        e.preventDefault();
        isConfirmedList = !isConfirmedList;
        confirmedTable.classList.toggle('active', isConfirmedList);
        checkedTable.classList.toggle('active', !isConfirmedList);
        toggleList.textContent = isConfirmedList
            ? '→ Xem danh sách học sinh đã khám'
            : '→ Xem danh sách học sinh chưa được khám';
        toggleList.setAttribute('aria-label', isConfirmedList
            ? 'Xem danh sách học sinh đã khám'
            : 'Xem danh sách học sinh chưa được khám');
        searchInput.value = '';
        clearSearch.style.display = 'none';
        currentPage = 1;
        updateTable();
    });

    // Search functionality
    searchInput.addEventListener('input', () => {
        const query = searchInput.value.toLowerCase();
        clearSearch.style.display = query ? 'block' : 'none';
        const currentRows = isConfirmedList ? confirmedRows : checkedRows;
        currentRows.forEach(row => {
            const name = row.cells[0].textContent.toLowerCase();
            const className = row.cells[1].textContent.toLowerCase();
            row.style.display = (name.includes(query) || className.includes(query)) ? '' : 'none';
        });
        currentPage = 1;
        updateTable();
    });

    // Clear search
    clearSearch.addEventListener('click', () => {
        searchInput.value = '';
        clearSearch.style.display = 'none';
        const currentRows = isConfirmedList ? confirmedRows : checkedRows;
        currentRows.forEach(row => row.style.display = '');
        currentPage = 1;
        updateTable();
    });

    // Pagination functionality
    function updateTable() {
        const currentRows = isConfirmedList ? confirmedRows : checkedRows;
        const visibleRows = currentRows.filter(row => row.style.display !== 'none');
        const totalPages = Math.ceil(visibleRows.length / rowsPerPage);
        pagination.innerHTML = '';
        noData.style.display = visibleRows.length === 0 ? 'block' : 'none';
        confirmedTable.style.display = isConfirmedList && visibleRows.length > 0 ? 'table' : 'none';
        checkedTable.style.display = !isConfirmedList && visibleRows.length > 0 ? 'table' : 'none';

        if (totalPages === 0) return;

        for (let i = 1; i <= totalPages; i++) {
            const a = document.createElement('a');
            a.href = '#';
            a.textContent = i;
            a.setAttribute('aria-label', `Trang ${i}`);
            if (i === currentPage) a.classList.add('current');
            a.addEventListener('click', (e) => {
                e.preventDefault();
                currentPage = i;
                displayPage();
                updatePaginationLinks();
            });
            pagination.appendChild(a);
        }

        displayPage();
    }

    function updatePaginationLinks() {
        const links = pagination.getElementsByTagName('a');
        Array.from(links).forEach((link, index) => {
            link.classList.toggle('current', index + 1 === currentPage);
        });
    }

    function displayPage() {
        const currentRows = isConfirmedList ? confirmedRows : checkedRows;
        const visibleRows = currentRows.filter(row => row.style.display !== 'none');
        currentRows.forEach(row => row.style.display = 'none');
        const start = (currentPage - 1) * rowsPerPage;
        const end = start + rowsPerPage;
        visibleRows.slice(start, end).forEach(row => row.style.display = '');
    }

    // Close alert functionality
    document.querySelectorAll('.alert .close-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            btn.parentElement.style.display = 'none';
        });
    });

    // Smooth scroll for links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', (e) => {
            if (anchor.id !== 'toggleList') {
                e.preventDefault();
                document.querySelector(anchor.getAttribute('href')).scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });

    // Initialize table
    updateTable();
});

document.getElementById('selectAll').addEventListener('change', function () {
    const checkboxes = document.querySelectorAll('.studentCheckbox');
    checkboxes.forEach(cb => cb.checked = this.checked);
});