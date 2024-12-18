
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.has('error') && urlParams.get('error') === 'email') {
        Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Cette adresse email est déjà utilisée. Veuillez en choisir une autre.',
            confirmButtonText: 'OK'
        });
    }
    else if (urlParams.has('error') && urlParams.get('error') === 'other') {
        Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Une erreur est survenue. Veuillez réessayer.',
            confirmButtonText: 'OK'
        });
    }

    const passwordButton = document.getElementById('togglePassword');
    passwordButton.addEventListener('mousedown', function() {
        togglePasswordVisibility('password', this);
    });

    passwordButton.addEventListener('mouseup', function() {
        togglePasswordVisibility('password', this);
    });

    const confirmPasswordButton = document.getElementById('toggleConfirmPassword');
    confirmPasswordButton.addEventListener('mousedown', function() {
        togglePasswordVisibility('confirm_password', this);
    });

    confirmPasswordButton.addEventListener('mouseup', function() {
        togglePasswordVisibility('confirm_password', this);
    });

    document.getElementById('registrationForm').addEventListener('submit', function(event) {
        if (!checkPassword()) {
            event.preventDefault();
        }
    });
    
});

/**
 * Fonction qui vérifie que les mots de passe saisis dans les champs "password" et "confirm_password" sont identiques.
 * @returns 
 */
function checkPassword()
{
    var password = document.getElementById("password").value;
    var confirm_password = document.getElementById("confirm_password").value;
    if (password !== confirm_password) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Les mots de passe ne correspondent pas. Veuillez réessayer.',
        });
        return false;
    }
    return true;
}

/**
 * Fonction qui permet de basculer entre le type de champ "password" et "text".
 * @param {*} inputId 
 * @param {*} button 
 */
function togglePasswordVisibility(inputId, button) {
    const input = document.getElementById(inputId);
    const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
    input.setAttribute('type', type);
}