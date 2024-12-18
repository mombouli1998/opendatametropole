document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    if (params.has('success') && params.get('success') === 'password') {
        Swal.fire({
            icon: 'success',
            title: 'Succès',
            text: 'Mot de passe mis à jour avec succès',
            confirmButtonText: 'OK'
        });
    } else if (params.has('error') && params.get('error') === 'password') {
        Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Ancien mot de passe incorrect',
            confirmButtonText: 'OK'
        });
    } else if (params.has('error') && params.get('error') === 'samePassword') {
        Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Le nouveau mot de passe ne peut pas être identique à l\'ancien. Veuillez entrer un mot de passe différent.',
            confirmButtonText: 'OK'
        });
    }
    else if(params.has('success')&& params.get('success')==='email'){
        Swal.fire({
            icon:'success',
            title:'Succès',
            text:'Adresse Email mis à jour avec succès',
            confirmButtonText: 'OK'
        })
    }
    else if (params.has('error') && params.get('error') === 'email') {
        Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Adresse email déja lié à un compte',
            confirmButtonText: 'OK'
        });
    }
});

/**
 * Fonction javascript qui permettre l'affichage ou non du mot de passe
 * @param {*} inputId 
 * @param {*} button
 */
function togglePasswordVisibility(inputId, button) {
    const input = document.getElementById(inputId);
    const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
    input.setAttribute('type', type);
}

/**
 * Fonction javascript qui va récupérer les données entrées par l'utilisateur pour le changement d'adresse mail
 * Puis elle va appeler via la méthode POST la page /update-email
 */
function updateEmail(){
    const currentEmail = document.getElementById('currentEmail').value;
    const newEmail = document.getElementById('newEmail').value;
    const currentPassword = document.getElementById('currentPassword').value;

    if(newEmail && currentPassword){
        fetch('/account/update-email',{
            method :'POST',
            headers:{'Content-Type':'application/json',},
            body:JSON.stringify({currentEmail:currentEmail,newEmail:newEmail,currentPassword:currentPassword}),
        })
        .catch(error => console.error('Erreur : ',error));  
    }
    else{
        alert('Veuillez remplir tous les champs');
    }

}