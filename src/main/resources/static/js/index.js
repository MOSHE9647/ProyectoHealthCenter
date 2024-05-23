document.addEventListener("DOMContentLoaded", function() {
    // Obtener todos los enlaces del menú
    var menuLinks = document.querySelectorAll('.sidemenu a, #adminLink');

    // Obtener el contenedor principal
    var mainContainer = document.querySelector('main');

    // Eliminar la clase 'active' de todos los enlaces del menú
    menuLinks.forEach(function(link) {
        link.classList.remove('active');
    });

    // Inicializar los enlaces del menú
    initializeMenuLinks(mainContainer, menuLinks);
});

function initializeMenuLinks(mainContainer, menuLinks) {
    // Aplicar una animación al mostrar el contenido
    fadeIn(mainContainer);

    // Iterar sobre cada enlace del menú
    for (var i = 1; i < menuLinks.length; i++) {
        var link = menuLinks[i];

        // Agregar un evento de clic a cada enlace
        link.addEventListener('click', function(event) {
            // Prevenir el comportamiento predeterminado del enlace
            event.preventDefault();

            // Agregar la clase 'active' al enlace que se ha hecho click
            this.classList.add('active');

            // Obtener la URL del enlace
            var url = this.getAttribute('href');

            // Realizar la solicitud AJAX
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.open("GET", url, true);

            xmlhttp.onreadystatechange = function() {
                if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                    // Actualizar el contenido de page-content con la respuesta del servidor
                    mainContainer.innerHTML = xmlhttp.responseText;

                    // Aplicar una animación al mostrar el contenido
                    mainContainer.style.opacity = 0;

                    // Obtener todos los elementos con la clase 'scriptJS'
                    var scriptElements = document.querySelectorAll('.scriptJS');

                    // Iterar sobre los elementos de script y agregarlos al documento
                    scriptElements.forEach(function(scriptElement) {
                        var script = document.createElement('script');
                        script.src = scriptElement.getAttribute('src');
                        document.body.appendChild(script);
                    });
                    
                    initializeConfigLinks();
                    fadeIn(mainContainer);
                }
            };

            xmlhttp.send();
        });
    }
}

function initializeConfigLinks() {
    var pageLinks = document.querySelectorAll('.pag');

    for (var i = 0; i < pageLinks.length; i++) {
        var link = pageLinks[i];
        
        link.addEventListener('click', function(event) {
            event.preventDefault();

            var url = this.getAttribute('href');

            var regex = /\/config\/([^\/]+)/;
            var resultado = url.match(regex);
            if (resultado !== null) {
                var tableName = resultado[1];

                var secondRegex = /([^?]+)/;
                var secondResult = tableName.match(secondRegex);
                if (secondResult !== null) {
                    tableName = secondResult[1];
                }

                alert(tableName);

                var tableContainerID = "table-" + tableName;
                doAJAX(url, tableContainerID);
            } else {
                mostrarMensaje("Error en la solicitud", "No se encontró el patrón en la URL.", "error");
            }
        });
    }
}

function doAJAX(url, containerID) {
    var tableContainer = document.getElementById(containerID);
    if (!tableContainer) {
        mostrarMensaje("Error", "El contenedor de la tabla no existe.", "error");
        return;
    }

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open("GET", url, true);

    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState === 4) {
            if (xmlhttp.status === 200) {
                tableContainer.innerHTML = xmlhttp.responseText;
            } else {
                mostrarMensaje("Error en el servidor", "Error al obtener la respuesta del servidor: " + xmlhttp.statusText, "error");
            }
        }
    };

    xmlhttp.onerror = function() {
        mostrarMensaje("Error de red", "No se pudo completar la solicitud debido a un error de red.", "error");
    };

    xmlhttp.send();
}


// Muestra el loader
function showLoader() {
    document.getElementById("loader").style.display = "flex";
}

// Oculta el loader
function hideLoader() {
    document.getElementById("loader").style.display = "none";
}

// Función para realizar una transición de desvanecimiento
function fadeIn(element) {
    var opacity = 0;
    var timer = setInterval(function() {
        if (opacity >= 1) {
            clearInterval(timer);
        }
        element.style.opacity = opacity;
        opacity += 0.08; // Ajusta la velocidad de la transición
    }, 20); // Ajusta la frecuencia de actualización de la opacidad
}