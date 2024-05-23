function initializeToast() {
    const toast = document.querySelector(".toast");
    const closeIcon = document.querySelector(".close");
    const progress = document.querySelector(".progress");
    let timer1, timer2;

    toast.classList.add("active");
    progress.classList.add("active");

    timer1 = setTimeout(() => {
        toast.classList.remove("active");
    }, 5000); // 1s = 1000 milliseconds

    timer2 = setTimeout(() => {
        progress.classList.remove("active");
    }, 5300);

    closeIcon.addEventListener("click", () => {
        toast.classList.remove("active");

        setTimeout(() => {
            progress.classList.remove("active");
        }, 300);

        clearTimeout(timer1);
        clearTimeout(timer2);
    });
}

function mostrarMensaje(titulo, mensaje, tipo) {
    var icono = "";
    if (tipo === 'success') {
        icono = 'la-check';
    } else if (tipo === 'error') {
        icono = 'la-times';
    }

    var notificationHTML = `
        <div id="toastNotification" class="toast">
            <div class="toast-content">
                <i class="fas fa-solid las ${icono} check ${tipo}"></i>
                <div class="message">
                    <span class="text text-1">${titulo}</span>
                    <span class="text text-2">${mensaje}</span>
                </div>
            </div>
            <i class="fa-solid las la-times close"></i>
            <div class="progress ${tipo}"></div>
        </div>
    `;
    var notificationContainer = document.getElementById('notification-container');
    notificationContainer.innerHTML = notificationHTML;
    initializeToast();
}