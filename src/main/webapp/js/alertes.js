function displayMessage() {
    var href = window.location.href;

    var action = "";
    var message = "";

    for (var index = 0; index < href.length; index++) {
        if (href[index] == "?") {
            index++;
            while (href[index] != "=" && index < href.length) {
                action += href[index];
                index++;
            }
            index++;
            while (index < href.length) {
                message += href[index];
                index++;
            }
        }
    }

    var alerte;

    switch (action) {
        case "connexion" :
            alerte = document.getElementById("alerteConnexionFail");
            break;
        case "inscription" :
            alerte = document.getElementById("alerteInscriptionSuccess");
            break;
        case "changepwd" :
            switch (message) {
                case "fail" :
                    alerte = document.getElementById("alerteChangePwdFail");
                    break;
                case "success" :
                    alerte = document.getElementById("alerteChangePwdSuccess");
                    break;
            }
            break;
        case "deletion" :
            switch (message) {
                case "impossible" :
                    alerte = document.getElementById("alerteDeletionImpossible");
                    break;
                case "success" :
                    alerte = document.getElementById("alerteDeletionSuccess");
                    break;
                case "unauthorized" :
                    alerte = document.getElementById("alerteDeletionUnauthorized");
                    break;
                case "fail" :
                    alerte = document.getElementById("alerteDeletionFail");
                    break;
            }
            break;
        case "emprunt" :
            switch (message) {
                case "success" :
                    alerte = document.getElementById("alerteEmpruntSuccess");
                    break;
                case "impossible" :
                    alerte = document.getElementById("alerteEmpruntImpossible");
                    break;
            }
            break;
        case "calendarerror" :
            alerte = document.getElementById("alerteCalendarError");
            break;
        case "achat" :
            switch (message) {
                case "success" :
                    alerte = document.getElementById("alerteAchatSuccess");
                    break;
                case "fail" :
                    alerte = document.getElementById("alerteAchatFail");
                    break;
            }
            break;
        case "error" :
            switch (message) {
                case "period" :
                    alerte = document.getElementById("alerteErrorPeriod");
                    break;
                case "quantity" :
                    alerte = document.getElementById("alerteErrorQuantity");
                    break;
                case "data" :
                    alerte = document.getElementById("alerteErrorData");
                    break;
                case "phone" :
                    alerte = document.getElementById("alerteErrorPhone");
                    break;
                case "mail" :
                    alerte = document.getElementById("alerteErrorMail");
                    break;
                case "pwd" :
                    alerte = document.getElementById("alerteErrorPwd");
                    break;
                case "existingmail" :
                    alerte = document.getElementById("alerteExistingMail");
                    break;
                case "firstname" :
                    alerte = document.getElementById("alerteErrorFirstName");
                    break;
                case "lastname" :
                    alerte = document.getElementById("alerteErrorLastName");
                    break;
            }
            break;
        case "approval" :
            switch (message) {
                case "notapproved" :
                    alerte = document.getElementById("alerteReglementNotApproved");
                    break;
                case "fail" :
                    alerte = document.getElementById("alerteReglementFail");
                    break;
            }
            break;
        case "validation" :
            switch (message) {
                case "success" :
                    alerte = document.getElementById("alerteValidationSuccess");
                    break;
                case "fail" :
                    alerte = document.getElementById("alerteValidationFail");
                    break;
            }
            break;
        case "promotion" :
            switch (message) {
                case "success" :
                    alerte = document.getElementById("alertePromotionSuccess");
                    break;
                case "fail" :
                    alerte = document.getElementById("alertePromotionFail");
                    break;
            }
            break;
        case "termination" :
            switch (message) {
                case "success" :
                    alerte = document.getElementById("alerteTerminationSuccess");
                    break;
                case "fail" :
                    alerte = document.getElementById("alerteTerminationFail");
                    break;
            }
            break;
    }

    if (alerte != null) {
        var classes = alerte.getAttribute("class");
        var indexHideField = classes.indexOf("hide-field");
        var nvllesClasses = classes.substring(0, indexHideField - 1) + classes.substring(indexHideField + 10);
        alerte.setAttribute("class", nvllesClasses);
    }
}

function loading(button) {
    var classes = button.getAttribute("class");
    button.setAttribute("class", classes+" disabled");
    button.innerHTML = "<i class=\"fa fa-circle-o-notch fa-spin\"></i>";
}

window.onload = function () {
    displayMessage();
    var completeName = document.location.href;
    var page = "";
    var index = completeName.lastIndexOf("/") + 1;
    var letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    while (letters.indexOf(completeName[index]) > -1) {
        page += completeName[index];
        index++;
    }
    if (page == "inventaire" || page == "demandes" || page == "empruntencours") {
        var table = document.getElementById("tabletodisplay");
        var alerte = document.getElementById("emptytable").textContent;

        if (alerte == "") {
            var classes = table.getAttribute("class");
            var indexHideField = classes.indexOf("hide-field");
            var nvllesClasses = classes.substring(0, indexHideField - 1) + classes.substring(indexHideField + 10);
            table.setAttribute("class", nvllesClasses);
        }
    }
    switch (page) {
        case "emprunt" :
            updateLink();
            break;
        case "sousresponsable" :
            checkStudents();
            break;
        case "achatmaterielexistant" :
            update("association");
            break;
    }

    var email = document.getElementById("email");

    var button = document.getElementsByClassName("btn")[0];
    if (button != null) {
        button.onclick = function () {
            if (email == null) {
                loading(button);
            } else {
                var temp = document.getElementById("email").value;
                if (temp.length > 0 && temp.indexOf("@") > 0 && temp.indexOf("@") != temp.length - 1) {
                    loading(button);
                }
            }
        }
    }

}

