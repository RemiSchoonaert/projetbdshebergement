function checkStudents() {
    var select = document.getElementById("student");
    if (select.length == 0) {
        select.setAttribute("disabled", "");
        document.getElementsByClassName("btn")[0].setAttribute("disabled", "");
    }
}
