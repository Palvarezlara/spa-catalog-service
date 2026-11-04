package cl.duoc.spa.spa_catalog_service.service;

import cl.duoc.spa.spa_catalog_service.dto.ServicioRequest;
import cl.duoc.spa.spa_catalog_service.exception.ConflictException;
import cl.duoc.spa.spa_catalog_service.exception.NotFoundException;
import cl.duoc.spa.spa_catalog_service.model.ServicioModel;
import cl.duoc.spa.spa_catalog_service.repository.IServicioModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioModelService {

    private final IServicioModelRepository repo;

    private static final Comparator<ServicioModel> POR_ORDEN = Comparator
            .comparing((ServicioModel s) -> s.getOrden() == null ? Integer.MAX_VALUE : s.getOrden())
            .thenComparing(ServicioModel::getNombre);

    /** @param incluirInactivos true solo para administradores */
    public List<ServicioModel> findAll(boolean incluirInactivos) {
        return repo.findAll().stream()
                .filter(s -> incluirInactivos || Boolean.TRUE.equals(s.getActivo()))
                .sorted(POR_ORDEN)
                .toList();
    }

    public Optional<ServicioModel> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<ServicioModel> findBySku(String sku) {
        return repo.findBySku(sku);
    }

    @Transactional
    public ServicioModel create(ServicioRequest r) {
        if (repo.existsBySku(r.sku())) {
            throw new ConflictException("Ya existe un servicio con el SKU " + r.sku());
        }
        ServicioModel s = new ServicioModel();
        apply(s, r);
        return repo.save(s);
    }

    @Transactional
    public ServicioModel update(Long id, ServicioRequest r) {
        ServicioModel s = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado"));
        if (!s.getSku().equals(r.sku()) && repo.existsBySku(r.sku())) {
            throw new ConflictException("Ya existe un servicio con el SKU " + r.sku());
        }
        apply(s, r);
        return repo.save(s);
    }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Servicio no encontrado");
        }
        repo.deleteById(id);
    }

    @Transactional
    public ServicioModel toggleActivo(Long id, boolean activo) {
        ServicioModel s = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado"));
        s.setActivo(activo);
        return repo.save(s);
    }

    private void apply(ServicioModel s, ServicioRequest r) {
        s.setSku(r.sku().trim().toUpperCase());
        s.setNombre(r.nombre().trim());
        s.setCategoria(r.categoria());
        s.setPrecio(r.precio());
        s.setDuracionMin(r.duracionMin());
        s.setImageUrl(r.imageUrl() == null || r.imageUrl().isBlank() ? null : r.imageUrl().trim());
        s.setDescripcion(r.descripcion());
        s.setActivo(r.activo() == null ? Boolean.TRUE : r.activo());
        s.setRequiereTerapeuta(r.requiereTerapeuta() == null ? Boolean.TRUE : r.requiereTerapeuta());
        s.setPersonas(r.personas() == null ? 1 : r.personas());
        if (r.orden() != null) s.setOrden(r.orden());
    }
}
