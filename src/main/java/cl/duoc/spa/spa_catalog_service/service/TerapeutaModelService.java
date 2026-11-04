package cl.duoc.spa.spa_catalog_service.service;

import cl.duoc.spa.spa_catalog_service.dto.TerapeutaRequest;
import cl.duoc.spa.spa_catalog_service.exception.ConflictException;
import cl.duoc.spa.spa_catalog_service.exception.NotFoundException;
import cl.duoc.spa.spa_catalog_service.model.TerapeutaModel;
import cl.duoc.spa.spa_catalog_service.repository.ITerapeutaModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TerapeutaModelService {

    private final ITerapeutaModelRepository repo;

    public List<TerapeutaModel> findAll(boolean incluirInactivos) {
        return incluirInactivos ? repo.findAllByOrderByOrdenAscNombreAsc()
                                : repo.findByActivoTrueOrderByOrdenAscNombreAsc();
    }

    public TerapeutaModel findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Terapeuta no encontrado"));
    }

    @Transactional
    public TerapeutaModel create(TerapeutaRequest r) {
        if (repo.existsByNombreIgnoreCase(r.nombre().trim())) {
            throw new ConflictException("Ya existe un terapeuta con ese nombre");
        }
        TerapeutaModel t = new TerapeutaModel();
        apply(t, r);
        return repo.save(t);
    }

    @Transactional
    public TerapeutaModel update(Long id, TerapeutaRequest r) {
        TerapeutaModel t = findById(id);
        apply(t, r);
        return repo.save(t);
    }

    /** Se desactiva en vez de borrar: las reservas historicas siguen referenciando al terapeuta. */
    @Transactional
    public TerapeutaModel setActivo(Long id, boolean activo) {
        TerapeutaModel t = findById(id);
        t.setActivo(activo);
        return repo.save(t);
    }

    private void apply(TerapeutaModel t, TerapeutaRequest r) {
        t.setNombre(r.nombre().trim());
        t.setFotoUrl(r.fotoUrl() == null || r.fotoUrl().isBlank() ? null : r.fotoUrl().trim());
        t.setEspecialidad(r.especialidad());
        t.setActivo(r.activo() == null ? Boolean.TRUE : r.activo());
        if (r.orden() != null) t.setOrden(r.orden());
    }
}
