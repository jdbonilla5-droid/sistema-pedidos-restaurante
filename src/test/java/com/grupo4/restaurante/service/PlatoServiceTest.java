package com.grupo4.restaurante.service;

import com.grupo4.restaurante.dto.PlatoRequest;
import com.grupo4.restaurante.dto.PlatoResponse;
import com.grupo4.restaurante.entity.Categoria;
import com.grupo4.restaurante.entity.Plato;
import com.grupo4.restaurante.exception.RecursoNoEncontradoException;
import com.grupo4.restaurante.repository.CategoriaRepository;
import com.grupo4.restaurante.repository.PlatoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoServiceTest {

    @Mock
    private PlatoRepository platoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private PlatoService platoService;

    @Test
    void crearPlatoConCategoriaExistenteDebeGuardarSusDatos() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Platos principales");

        when(categoriaRepository.findById(2L))
                .thenReturn(Optional.of(categoria));

        when(platoRepository.save(any(Plato.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        PlatoRequest request = new PlatoRequest(
                "  Pollo a la plancha  ",
                "Pollo con arroz",
                new BigDecimal("12.50"),
                true,
                2L
        );

        PlatoResponse respuesta = platoService.crear(request);

        assertAll(
                () -> assertEquals(
                        "Pollo a la plancha", respuesta.nombre()
                ),
                () -> assertEquals(
                        new BigDecimal("12.50"), respuesta.precio()
                ),
                () -> assertTrue(respuesta.disponible()),
                () -> assertEquals(
                        "Platos principales", respuesta.categoriaNombre()
                )
        );

        verify(platoRepository).save(any(Plato.class));
    }

    @Test
    void crearPlatoConCategoriaInexistenteDebeRechazarlo() {
        when(categoriaRepository.findById(999L))
                .thenReturn(Optional.empty());

        PlatoRequest request = new PlatoRequest(
                "Pollo a la plancha",
                "Pollo con arroz",
                new BigDecimal("12.50"),
                true,
                999L
        );

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> platoService.crear(request)
        );

        verify(platoRepository, never()).save(any(Plato.class));
    }

    @Test
    void actualizarPlatoDebeCambiarPrecioYDisponibilidad() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Platos principales");

        Plato plato = new Plato();
        plato.setNombre("Pollo a la plancha");
        plato.setPrecio(new BigDecimal("12.50"));
        plato.setDisponible(true);
        plato.setCategoria(categoria);

        when(platoRepository.findById(1L))
                .thenReturn(Optional.of(plato));

        when(categoriaRepository.findById(2L))
                .thenReturn(Optional.of(categoria));

        when(platoRepository.save(any(Plato.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        PlatoRequest request = new PlatoRequest(
                "Pollo a la plancha",
                "Pollo con arroz",
                new BigDecimal("13.50"),
                false,
                2L
        );

        PlatoResponse respuesta = platoService.actualizar(1L, request);

        assertAll(
                () -> assertEquals(
                        new BigDecimal("13.50"), respuesta.precio()
                ),
                () -> assertFalse(respuesta.disponible()),
                () -> assertSame(categoria, plato.getCategoria())
        );

        verify(platoRepository).save(plato);
    }
}