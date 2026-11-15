package group4.ec.edu.espe.pedidosrestaurante;

import group4.ec.edu.espe.pedidosrestaurante.entity.*;
import group4.ec.edu.espe.pedidosrestaurante.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

public class DatosDePrueba {

    @Bean
    CommandLineRunner cargar(PlatoRepository platos, PedidoRepository pedidos) {
        return args -> {
            if (platos.count() > 0) return;
            platos.save(plato("Encebollado", "5.00", true));    // id 1
            platos.save(plato("Hornado", "8.50", true));        // id 2
            platos.save(plato("Ceviche (agotado)", "7.00", false)); // id 3 NO disponible
            pedidos.save(new Pedido());                         // pedido 1
            pedidos.save(new Pedido());                         // pedido 2
        };
    }

    private Plato plato(String nombre, String precio, boolean disponible) {
        Plato p = new Plato();
        p.setNombre(nombre);
        p.setPrecio(new BigDecimal(precio));
        p.setDisponible(disponible);
        return p;
    }
}
