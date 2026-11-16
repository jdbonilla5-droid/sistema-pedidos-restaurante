package com.grupo4.restaurante.repository;

import com.grupo4.restaurante.entity.Plato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatoRepository extends JpaRepository<Plato, Long> {

    List<Plato> findByCategoria_Id(Long categoriaId);

    List<Plato> findByDisponible(boolean disponible);

    boolean existsByCategoria_Id(Long categoriaId);
}