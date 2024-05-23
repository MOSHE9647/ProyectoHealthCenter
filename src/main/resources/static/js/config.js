function changePageSize(pageNumberID, pageSizeSelectID, tableViewLink, tableContID) {
    var pageNumber = document.getElementById(pageNumberID).value;
    if (pageNumber !== '-1') {
        const pageSize = document.getElementById(pageSizeSelectID).value;
        const currentPage = pageNumber;
        var url = tableViewLink + "?page=" + currentPage + "&size=" + pageSize;
        cargarVista(url, tableContID);
    }
}

function sortBy(pageNumberID, sortBySelectID, pageSizeSelectID, tableViewLink, tableContID) {
    var pageNumber = document.getElementById(pageNumberID).value;
    if (pageNumber !== '-1') {
        const sortBy = document.getElementById(sortBySelectID).value;
        const pageSize = document.getElementById(pageSizeSelectID).value;
        const currentPage = pageNumber;
        var url = tableViewLink + "?page=" + currentPage + "&size=" + pageSize + "&sort=" + sortBy;
        cargarVista(url, tableContID);
    }
}

function eliminarUsuario(userEmail, userType) {
    var confirmado = confirm("¿Está seguro de querer eliminar al usuario con correo '" + userEmail + "' del sistema?");
    if (confirmado) {
        showLoader();
        var form = document.getElementById('deleteForm');
        var csrfToken = form.querySelector("input[name='_csrf']").value;

        if (userType === 'Administrador') {
            userType = "usuario";
        }

        var url = "/api/v1/" + userType.toLowerCase() + "/delete?email=" + encodeURIComponent(userEmail);

        var xhttp = new XMLHttpRequest();
        xhttp.open("DELETE", url, true);

        // Agregar el token CSRF como encabezado personalizado
        xhttp.setRequestHeader("X-CSRF-TOKEN", csrfToken);
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4) {
                hideLoader();
                if (this.status == 200) {
                    var response = JSON.parse(xhttp.responseText);
                    mostrarMensaje(response.titulo, response.mensaje, response.tipo);
                    if (response.tipo !== 'error') {
                        setTimeout(function() {
                            cargarVista("config/users", "#table-users");
                        }, 5400);
                    }
                } else {
                    mostrarMensaje("Error interno", "Error al comunicarse con el servidor", "error");
                }
            }
        };
        xhttp.send();
    }
}

function editarUsuario(userEmail) {
    var url = "/config/edit-user?email=" + userEmail;
    alert(url);
}