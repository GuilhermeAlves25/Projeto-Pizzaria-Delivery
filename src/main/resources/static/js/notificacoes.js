
document.addEventListener('DOMContentLoaded', () => {

    const eventSource = new EventSource('/notifications/subscribe');


    eventSource.addEventListener('status-update', function(event) {
        console.log("Notificação recebida:", event.data);

        const alertContainer = document.getElementById('global-alert-container');
        if (alertContainer) {

            const alertHTML = `
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    <i class="bi bi-info-circle-fill me-2"></i> ${event.data}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            `;

            alertContainer.innerHTML = alertHTML;
        }
    });

    eventSource.onerror = function(err) {
        console.error("Conexão SSE falhou.", err);
        eventSource.close();
    };
});