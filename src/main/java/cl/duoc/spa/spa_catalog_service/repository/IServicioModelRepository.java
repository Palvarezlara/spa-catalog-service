package cl.duoc.spa.spa_catalog_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.spa.spa_catalog_service.model.ServicioModel;
import java.util.List;

public interface IServicioModelRepository extends JpaRepository<ServicioModel, Integer> {
    List<ServicioModel> findByActiveTrue();
}
