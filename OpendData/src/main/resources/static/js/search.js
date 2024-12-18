document.addEventListener('DOMContentLoaded', function () {

    console.log('search.js loaded');

    const searchForm = document.getElementById('searchForm');
    const searchInput = document.getElementById('searchValue');
    const filtersForm = document.getElementById('filtersForm');
    const itemsPerPage = 16;
    let currentPage = 0;

    const allCards = Array.from(document.querySelectorAll(".flip-card"));
    let filteredCards = allCards;

    searchForm.addEventListener('submit', function (event) {
        event.preventDefault();
        filterAndDisplay();
    });

    filtersForm.addEventListener('submit', function (event) {
        event.preventDefault();
        filterAndDisplay();
    });

    showPage(currentPage, allCards, itemsPerPage);

    function filterAndDisplay() {
        const searchValue = searchInput.value.toLowerCase();
        const cards = document.querySelectorAll('.flip-card');

        // Récupère les valeurs des filtres
        const downloadMin = parseInt(document.getElementById('download').value, 10) || 0;
        const license = document.getElementById('license').value;
        const theme = document.getElementById('theme').value;
        const territory = document.getElementById('territory').value;
        const langue = document.getElementById('langue').value;

        // Récupère et parse la date entrée par l'utilisateur
        const modificationDateInput = document.getElementById('modification').value;
        const modificationDate = modificationDateInput ? new Date(modificationDateInput) : null;

        const csv = document.getElementById('csv').checked;
        const xml = document.getElementById('xml').checked;
        const pdf = document.getElementById('pdf').checked;

        cards.forEach(card => {
            const titleElement = card.querySelector('#blockTitle');
            const titleText = titleElement ? titleElement.textContent.toLowerCase() : '';

            const download = parseInt(card.querySelector('input[name="download"]').value, 10) || 0;
            const cardLicense = card.querySelector('input[name="license"]').value || '';
            const cardModification = card.querySelector('input[name="modification"]').value || '';
            const cardTheme = card.querySelector('input[name="theme"]').value || '';
            const cardTerritory = card.querySelector('input[name="territory"]').value || '';
            const cardLangue = card.querySelector('input[name="langue"]').value || '';
            const csvCheckbox = card.querySelector('input[name="csv"]').value;
            const xmlCheckbox = card.querySelector('input[name="xml"]').value;
            const pdfCheckbox = card.querySelector('input[name="pdf"]').value;

            // Parse la date de modification de la carte
            const cardDate = cardModification ? new Date(cardModification) : null;

            // Vérifie si la carte correspond aux critères
            const matchesSearch = titleText.includes(searchValue);
            const matchesDownload = download >= downloadMin;
            const matchesLicense = !license || cardLicense === license;
            const matchesTheme = !theme || cardTheme === theme;
            const matchesTerritory = !territory || cardTerritory === territory;
            const matchesLangue = !langue || cardLangue.includes(langue);
            const matchesDate = !modificationDate || (cardDate && cardDate >= modificationDate);
            const matchesCsv = !csv || csvCheckbox === "true";
            const matchesXml = !xml || xmlCheckbox === "true";
            const matchesPdf = !pdf || pdfCheckbox === "true";

            // Affiche ou masque la carte en fonction des critères
            if (matchesSearch && matchesDownload && matchesLicense &&
                matchesTheme && matchesTerritory && matchesLangue &&
                matchesDate && matchesCsv && matchesXml && matchesPdf) {
                card.style.display = '';
            } else {
                card.style.display = 'none';
            }
        });

        // Met à jour filteredCards avec les cartes affichées
        filteredCards = Array.from(cards).filter(card => card.style.display !== 'none');

        currentPage = 0;
        showPage(currentPage, filteredCards, itemsPerPage);
    }
});

/**
 * Affiche les éléments de la page donnée
 * @param {*} page      La page à afficher
 * @param {*} allCards  Toutes les cartes de données
 * @param {*} itemsPerPage  Nombre d'éléments par page
 */
function showPage(page, allCards, itemsPerPage) {
    const start = page * itemsPerPage;
    const end = start + itemsPerPage;
    allCards.forEach(card => card.style.display = 'none');
    allCards.slice(start, end).forEach(card => card.style.display = 'block');
    updatePagination(page, allCards, itemsPerPage);
}

/**
 * Met à jour les boutons de pagination en fonction de la page actuelle
 * @param {*} currentPage  La page actuelle
 * @param {*} allCards    Toutes les cartes de données
 * @param {*} itemsPerPage Nombre d'éléments par page
 */
function updatePagination(currentPage, allCards, itemsPerPage) {
    let totalPages = Math.ceil(allCards.length / itemsPerPage);
    let paginationContainer = document.querySelector(".pagination");
    paginationContainer.innerHTML = "";

    if (currentPage > 0) {
        let prevButton = document.createElement("button");
        prevButton.textContent = "Précédent";
        prevButton.classList.add("btn-call-to-action");
        prevButton.onclick = () => showPage(currentPage - 1, allCards, itemsPerPage);
        paginationContainer.appendChild(prevButton);
    }

    let pageInfo = document.createElement("span");
    pageInfo.textContent = `Page ${currentPage + 1} sur ${totalPages}`;
    paginationContainer.appendChild(pageInfo);

    if (currentPage < totalPages - 1) {
        let nextButton = document.createElement("button");
        nextButton.textContent = "Suivant";
        nextButton.classList.add("btn-call-to-action");
        nextButton.onclick = () => showPage(currentPage + 1, allCards, itemsPerPage);
        paginationContainer.appendChild(nextButton);
    }
}