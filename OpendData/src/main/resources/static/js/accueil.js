// Script pour l'animation des statistiques
document.addEventListener('DOMContentLoaded', function() {
    const counters = document.querySelectorAll('.stat-number');
    counters.forEach(counter => {
        counter.innerText = '0';

        const updateCounter = () => {
            const target = +counter.getAttribute('data-target');
            const count = +counter.innerText;
            const increment = target / 200;

            if (count < target) {
                counter.innerText = `${Math.ceil(count + increment)}`;
                setTimeout(updateCounter, 10);
            } else {
                counter.innerText = target;
            }
        };

        updateCounter();
    });
});
// Création d'un graphique interactif avec Chart.js
const ctx = document.getElementById('dataChart').getContext('2d');
const dataChart = new Chart(ctx, {
    type: 'bar',  // Type de graphique : bar, line, pie, etc.
    data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [{
            label: 'Téléchargements de jeux de données',
            data: [120, 190, 300, 500, 200, 300],
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});
// Initialiser la carte avec Leaflet.js centrée sur Dijon
var map = L.map('map').setView([47.3220, 5.0415], 10);  // Coordonnées pour Dijon

// Ajouter la couche OpenStreetMap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
}).addTo(map);


// Ajouter un marqueur à la carte pour Dijon
L.marker([47.3220, 5.0415]).addTo(map)  // Coordonnées pour Dijon
    .bindPopup('Données géographiques de Dijon.')
    .openPopup();


