package cl.duoc.spa.spa_catalog_service.service;

import cl.duoc.spa.spa_catalog_service.repository.IServicioModelRepository;
import cl.duoc.spa.spa_catalog_service.model.ServicioModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor  //Reemplaza al @Autowired
public class ServicioModelService {

    private final IServicioModelRepository repo;

    // Obtener todos
    public List<ServicioModel> findAll() {
        return repo.findAll();
    }

    // Obtener uno por id
    public Optional<ServicioModel> findById(Long id) {
        return repo.findById(id);
    }

    // Crear
    public ServicioModel create(ServicioModel servicio) {
        // aquí podrías validar SKU único si quieres
        return repo.save(servicio);
    }

    // Actualizar
    public ServicioModel update(Long id, ServicioModel data) {
        ServicioModel servicio = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        servicio.setSku(data.getSku());
        servicio.setNombre(data.getNombre());
        servicio.setCategoria(data.getCategoria());
        servicio.setPrecio(data.getPrecio());
        servicio.setDuracionMin(data.getDuracionMin());
        servicio.setImageUrl(data.getImageUrl());
        servicio.setDescripcion(data.getDescripcion());
        servicio.setActivo(data.getActivo());

        return repo.save(servicio);
    }

    // Eliminar
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // Cambiar estado activo/inactivo
    public ServicioModel toggleActivo(Long id, boolean activo) {
        ServicioModel servicio = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        servicio.setActivo(activo);
        return repo.save(servicio);
    }

}
