console.log("login.js chargé");

document.addEventListener('DOMContentLoaded', function() {
    // Vérification si un paramètre 'error' existe dans l'URL
    const urlParams = new URLSearchParams(window.location.search);
    
    // Si le paramètre 'error' existe et vaut 'email', affiche l'erreur
    if (urlParams.has('error') && urlParams.get('error') === 'invalid') {
        Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Les informations de connexion fournies sont incorrectes. Veuillez vérifier votre email et votre mot de passe.',
            confirmButtonText: 'OK'
        });
    }

    // Fonction pour afficher/masquer le mot de passe
    const togglePasswordButton = document.getElementById('toggleConfirmPassword');
    
    // Ajout de l'événement pour afficher le mot de passe lors du "mousedown"
    togglePasswordButton.addEventListener('mousedown', function() {
        togglePasswordVisibility('form6Example54');
    });

    // Réaction lors du relâchement du bouton "mouseup" pour cacher le mot de passe
    togglePasswordButton.addEventListener('mouseup', function() {
        togglePasswordVisibility('form6Example54');
    });

    // Pour les mobiles : lors du glissement hors du bouton tout en maintenant le clic
    togglePasswordButton.addEventListener('mouseleave', function() {
        togglePasswordVisibility('form6Example54', false);
    });
});

/**
 * Fonction pour basculer la visibilité du mot de passe
 * @param {*} inputId ID de l'élément input de type 'password'
 */
function togglePasswordVisibility(inputId) {
    const input = document.getElementById(inputId);
    const currentType = input.getAttribute('type');
    const newType = currentType === 'password' ? 'text' : 'password';
    
    input.setAttribute('type', newType);
}
