function validarPasswords() {
    /* Obtenemos los valores de los campos de contraseña */
    var password = document.getElementById('password').value;
    var confirmPassword = document.getElementById('confirmPassword').value;

    if (password === '' && confirmPassword === '') {
        return true;
    }

    /* Verificamos que las contraseñas coincidan */
    if (password !== confirmPassword) {
        mostrarMensaje("Error", "Las contraseñas no coinciden.", "error");
        return false;
    }

    return true;
}

function actualizarUsuario() {
    var formID = "updateForm";
    if (validarPasswords()) {
        showLoader();
        var form = document.getElementById(formID);
        var formData = new FormData(form);
        var url = form.getAttribute("action");

        var xmlhttp = new XMLHttpRequest();
        xmlhttp.open("POST", url, true);
        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState === 4) {
                hideLoader();
                if (xmlhttp.status === 200) {
                    var response = JSON.parse(xmlhttp.responseText);
                    mostrarMensaje(response.titulo, response.mensaje, response.tipo);
                    if (response.tipo !== "error") {
                        setTimeout(function() {
                            var linkURL = "/logout";
                            window.location.href = linkURL;
                        }, 5400);
                    }
                } else {
                    mostrarMensaje("Error en el servidor", "Error al enviar la solicitud: " + xmlhttp.statusText, "error");
                }
            }
        };
        xmlhttp.send(formData);
    }
}