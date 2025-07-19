// Este script só será carregado para usuários logados
document.addEventListener('DOMContentLoaded', () => {
    // Abre a conexão com nosso endpoint SSE
    const eventSource = new EventSource('/notifications/subscribe');

    // Fica ouvindo por mensagens com o nome 'status-update'
    eventSource.addEventListener('status-update', function(event) {
        console.log("Notificação recebida:", event.data);

        const alertContainer = document.getElementById('global-alert-container');
        if (alertContainer) {
            // Cria o HTML do alerta do Bootstrap
            const alertHTML = `
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    <i class="bi bi-info-circle-fill me-2"></i> ${event.data}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            `;
            // Adiciona o novo alerta ao container
            alertContainer.innerHTML = alertHTML;
        }
    });

    eventSource.onerror = function(err) {
        console.error("Conexão SSE falhou.", err);
        eventSource.close();
    };
});