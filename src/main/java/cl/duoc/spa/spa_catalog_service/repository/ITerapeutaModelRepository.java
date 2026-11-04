package cl.duoc.spa.spa_catalog_service.repository;

import cl.duoc.spa.spa_catalog_service.model.TerapeutaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITerapeutaModelRepository extends JpaRepository<TerapeutaModel, Long> {
    List<TerapeutaModel> findByActivoTrueOrderByOrdenAscNombreAsc();
    List<TerapeutaModel> findAllByOrderByOrdenAscNombreAsc();
    boolean existsByNombreIgnoreCase(String nombre);
}
