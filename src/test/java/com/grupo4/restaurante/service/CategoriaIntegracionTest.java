package com.grupo4.restaurante.service;

import com.grupo4.restaurante.dto.CategoriaRequest;
import com.grupo4.restaurante.dto.CategoriaResponse;
import com.grupo4.restaurante.entity.Categoria;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoriaIntegracionTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private EntityManager entityManager;

    @Test
    void crearCategoriaDebePersistirSusDatos() {
        CategoriaRequest request = new CategoriaRequest(
                "  Bebidas de prueba  ",
                "Categoría para comprobar la persistencia"
        );

        CategoriaResponse respuesta = categoriaService.crear(request);

        // Envía los cambios a PostgreSQL y limpia la caché de entidades.
        entityManager.flush();
        entityManager.clear();

        Categoria guardada = entityManager.find(
                Categoria.class,
                respuesta.id()
        );

        assertNotNull(guardada);

        assertAll(
                () -> assertNotNull(guardada.getId()),
                () -> assertEquals(
                        "Bebidas de prueba",
                        guardada.getNombre()
                ),
                () -> assertEquals(
                        request.descripcion(),
                        guardada.getDescripcion()
                )
        );
    }
}