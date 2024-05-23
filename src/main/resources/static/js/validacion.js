function validarFormulario(formID) {
    /* Verificamos que todos los campos estén completos: */
    var form = document.getElementById(formID);
    var inputs = form.querySelectorAll("input[required], select[required]");

    for (var i = 0; i < inputs.length; i++) {
        if (!inputs[i].value.trim()) {
            var valorInput = inputs[i].name;
            if (valorInput.startsWith('direccion')) {
                valorInput = valorInput.replace(/^direccion\./, '');
            }
            mostrarMensaje("Error", "El campo '" + valorInput + "' no puede estar vacío.", "error");
            return false;
        }
    }

    return true;
}

function validarPasswords() {
    /* Obtenemos los valores de los campos de contraseña */
    var password = document.getElementById('password').value;
    var confirmPassword = document.getElementById('confirmPassword').value;

    /* Verificamos que los campos de contraseña no estén vacíos */
    if (password === "") {
        mostrarMensaje("Error", "El campo 'Contraseña' no puede estar vacío", "error");
        return false;
    }
    if (confirmPassword === "") {
        mostrarMensaje("Error", "Debe confirmar la contraseña introducida", "error");
        return false;
    }

    /* Verificamos que las contraseñas coincidan */
    if (password !== confirmPassword) {
        mostrarMensaje("Error", "Las contraseñas no coinciden.", "error");
        return false;
    }

    // Si llegamos a este punto, las contraseñas son iguales y no están vacías
    return true;
}