function updateLink() {
    var select = document.getElementById("stuff");
    var lien = document.getElementById("disponibility");
    var idSelected = select.options[select.selectedIndex].value;
    lien.setAttribute("href", "disponibilite?materiel="+idSelected+"&periode=0");
}

