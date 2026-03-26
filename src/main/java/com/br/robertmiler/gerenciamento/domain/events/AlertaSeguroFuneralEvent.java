package com.br.robertmiler.gerenciamento.domain.events;

import org.springframework.context.ApplicationEvent;

/**
 * Evento disparado pelo AssociadoService quando o status de um associado é
 * alterado para INATIVO_FALECIMENTO.
 *
 * Listeners podem escutar este evento com @EventListener para, por exemplo,
 * notificar a seguradora, enviar e-mail de alerta à ADM ou registrar protocolo.
 */
public class AlertaSeguroFuneralEvent extends ApplicationEvent {

    private final String cpfAssociado;

    public AlertaSeguroFuneralEvent(Object source, String cpfAssociado) {
        super(source);
        this.cpfAssociado = cpfAssociado;
    }

    public String getCpfAssociado() {
        return cpfAssociado;
    }
}
