package cl.duoc.spa.spa_catalog_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.spa.spa_catalog_service.model.ServicioModel;
import org.springframework.stereotype.Repository;


@Repository
public interface IServicioModelRepository extends JpaRepository<ServicioModel, Long> {
    boolean existsBySku(String sku);
}
