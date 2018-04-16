function initialize() {
    var select = document.getElementById("designation");
    for (var i = 0; i < select.length; i++) {
        select.remove(i);
    }
}

function update(champ) {
    initialize();
    var champSelected = document.getElementById(champ);
    var idSelected = champSelected.options[champSelected.selectedIndex].value;

    var request = new XMLHttpRequest();
    request.open("GET","ws/"+champ+"/"+idSelected,true);
    request.responseType="json";

    request.onload=function(){
        var response = this.response;
        changeFields(champ, response);
    }

    request.send();
}

function changeFields(champ, reponse) {
    var select = document.getElementById("designation");
    for (var i = 0; i < select.length; i++) {
        select.remove(i);
    }
    if (champ == "association") {
        if (Object.keys(reponse).length != 0) {
            document.getElementById("designation").removeAttribute("disabled");
            document.getElementById("quantity").removeAttribute("disabled");
            document.getElementsByClassName("btn")[0].removeAttribute("disabled");
            document.getElementById("quantity").setAttribute("placeholder", "Indiquez la quantité achetée");
            for (var element in reponse) {
                var option = document.createElement("option");
                option.text = reponse[element];
                option.value = element;
                select.add(option);
            }
        } else {
            document.getElementsByClassName("btn")[0].setAttribute("disabled", "");
            document.getElementById("designation").setAttribute("disabled", "");

            var option = document.createElement("option");
            option.text = "Aucun matériel existant pour cette association";
            select.add(option);

            var quantity = document.getElementById("quantity");
            quantity.setAttribute("disabled", "");
            quantity.setAttribute("placeholder", "");
        }
    }
}

