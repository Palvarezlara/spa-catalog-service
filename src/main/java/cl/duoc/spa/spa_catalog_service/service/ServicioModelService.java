package cl.duoc.spa.spa_catalog_service.service;

import cl.duoc.spa.spa_catalog_service.repository.IServicioModelRepository;
import cl.duoc.spa.spa_catalog_service.model.ServicioModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor  //Reemplaza al @Autowired
public class ServicioModelService {

    private final IServicioModelRepository repo;

    public List<ServicioModel> findAll() {
        return repo.findAll();
    }

    public List<ServicioModel> findActive() {
        return repo.findByActiveTrue();
    }

    public ServicioModel findBySku(UUID sku) {
        return repo.findById(sku).orElse(null);
    }

    public ServicioModel save(ServicioModel servicio) {
        return repo.save(servicio);
    }

    public void delete(UUID sku) {
        repo.deleteById(sku);
    }

}
