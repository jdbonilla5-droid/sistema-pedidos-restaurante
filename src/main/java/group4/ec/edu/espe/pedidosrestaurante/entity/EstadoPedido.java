package group4.ec.edu.espe.pedidosrestaurante.entity;

public enum EstadoPedido {
    PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO;

    public boolean puedeCambiarA(EstadoPedido nuevo) {
        return switch (this) {
            case PENDIENTE -> nuevo == EN_PREPARACION || nuevo == CANCELADO;
            case EN_PREPARACION -> nuevo == LISTO || nuevo == CANCELADO;
            case LISTO -> nuevo == ENTREGADO;
            case ENTREGADO, CANCELADO -> false;
        };
    }

    public boolean admiteModificaciones() {
        return this == PENDIENTE || this == EN_PREPARACION;
    }
}
