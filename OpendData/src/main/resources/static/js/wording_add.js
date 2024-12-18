
document.addEventListener('DOMContentLoaded', function() {

    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error') && urlParams.get('error') === 'libelle') {
        Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Libellé déjà utilisé. Veuillez en choisir un autre.',
            confirmButtonText: 'OK'
        });
    }

    const form = document.getElementById('form_wording_add');
    form.addEventListener('submit', function(event) {
        if (!validateFiles()) {
            event.preventDefault();
        }
        const select = document.getElementById('sujetSelect');
        if (select.value === "") {
            event.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Erreur',
                text: 'Veuillez sélectionner un sujet.',
                confirmButtonText: 'OK'
            });
        }
    });

    const fileUpload = document.getElementById('fileUpload');
    fileUpload.addEventListener('change', function() {
        displayFileName(this);
    });

    const fileUpload1 = document.getElementById('fileUpload1');
    fileUpload1.addEventListener('change', function() {
        displayFileName(this);
    });

    const fileUpload3 = document.getElementById('fileUpload3');
    fileUpload3.addEventListener('change', function() {
        displayFileName(this);
    });

});

/**
 * Affiche le nom du fichier sélectionné
 * @param {*} input 
 */
function displayFileName(input) {
    let fileNameDisplay = input.parentNode.querySelector(".file-name");
    fileNameDisplay.textContent = input.files.length > 0 ? input.files[0].name : '';
}

/**
 * Vérifie que l'utilisateur a sélectionné au moins un fichier
 * @returns 
 */
function validateFiles() {
    const pdfFile = document.getElementById('fileUpload').files.length;
    const xmlFile = document.getElementById('fileUpload1').files.length;
    const csvFile = document.getElementById('fileUpload3').files.length;

    if (pdfFile === 0 && xmlFile === 0 && csvFile === 0) {
        alert("Veuillez sélectionner au moins un fichier.");
        return false; // Empêche l'envoi du formulaire
    }
    return true; // Autorise l'envoi du formulaire
}