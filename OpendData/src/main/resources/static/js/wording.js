//afficher le tableau si il y a des données sinon le csv 
document.addEventListener("DOMContentLoaded", function() {
    var table = document.getElementById("csvTable");
    var pdfDiv = document.getElementById("pdf");
    var showPdfBtn = document.getElementById("showpdf");

    // si le tableau est vide on affiche le pdf
    if(table != null){
        if (table.rows.length === 1) {
            table.style.display = "none";
            if(pdfDiv != null){
            pdfDiv.style.display = "block";
        }
        } else {
            table.style.display = "table";
            if(pdfDiv != null){
            pdfDiv.style.display = "none";
            }
        }
    }

        

    var showTableBtn = document.getElementById("showTableBtn");
    var showPdfBtn = document.getElementById("showpdf");

    if (showTableBtn) {
        document.getElementById("showTableBtn").addEventListener("click", function() {
            var table = document.getElementById("csvTable");
            var pdfDiv = document.getElementById("pdf");
            var showPdfBtn = document.getElementById("showpdf");
        
            // Affiche le tableau et masque le PDF et desactive le bouton csv et active le bouton pdf
            if (table.style.display === "none" || table.style.display === "") {
                table.style.display = "table";
                showPdfBtn.disabled = false;
                showTableBtn.disabled = true;
                if(pdfDiv != null){
                    pdfDiv.style.display = "none";
                }
            } else {
                table.style.display = "none";
                showTableBtn.disabled = false;
            }
            
        });
    }

    if (showTableBtn) {
        if(showPdfBtn != null){
            document.getElementById("showpdf").addEventListener("click", function() {
                var pdfDiv = document.getElementById("pdf");
                var table = document.getElementById("csvTable");
                var showTableBtn = document.getElementById("showTableBtn");
            
                // Affiche le PDF et masque le tableau et desactive le bouton pdf et active le bouton csv
                if (pdfDiv.style.display === "none" || pdfDiv.style.display === "") {
                    pdfDiv.style.display = "block";
                    table.style.display = "none";
                    showTableBtn.disabled = false;
                    showPdfBtn.disabled = true;
                } else {
                    pdfDiv.style.display = "none";
                    showPdfBtn.disabled = false;
                }
            });
        }
    }

    // récupération de la description de la licence et l'url et affichage lors du survol du input licence des informations
    var licenceInput = document.getElementById("detailsLicence");
    var licenceDescription = document.getElementById("licenceDescription");
    var licenceUrlData = document.getElementById("licenceUrl");

    if (licenceInput) {
        licenceInput.addEventListener("click", function() {
            console.log("test");
    
            var licenceName = licenceInput.value;
    
            if (licenceDescription) {
                var licenceDesc = document.getElementById("licenceDescriptionShow");
    
                if (licenceDesc) {
                    licenceDesc.remove();
                }
                else {
                    var licenceDesc = document.createElement("div");
                    licenceDesc.textContent = "Description: " + licenceDescription.title;
                    licenceDesc.style.padding = "5px";
                    licenceDesc.style.backgroundColor = "#f8f9fa";
                    licenceDesc.style.border = "1px solid #ccc";
                    licenceDesc.style.marginTop = "50px";
                    licenceDesc.style.position = "absolute";
                    licenceDesc.style.zIndex = "1000";
                    licenceDesc.setAttribute("id", "licenceDescriptionShow");

                    var licenceUrl = document.createElement("a");
                    licenceUrl.href = licenceUrlData.title;
                    licenceUrl.textContent = "URL: "
                    licenceUrl.target = "_blank"; 
                    licenceUrl.style.display = "block";
                    licenceUrl.style.marginTop = "5px";
                    licenceUrl.style.textDecoration = "underline";
                    licenceUrl.style.color = "#007bff";
                
                    licenceDesc.appendChild(licenceUrl);
                    licenceInput.parentElement.appendChild(licenceDesc);
                }
            }
        });
    
    }
});