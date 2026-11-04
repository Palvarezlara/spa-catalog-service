package cl.duoc.spa.spa_catalog_service.controller;

import cl.duoc.spa.spa_catalog_service.dto.ServicioRequest;
import cl.duoc.spa.spa_catalog_service.model.ServicioModel;
import cl.duoc.spa.spa_catalog_service.service.ServicioModelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/servicios")
@RequiredArgsConstructor
public class ServicioModelController {

    private final ServicioModelService servicioService;

    /** Publico: solo servicios activos. ADMIN (con token): tambien los inactivos. */
    @GetMapping
    public ResponseEntity<List<ServicioModel>> getAll(Authentication auth) {
        return ResponseEntity.ok(servicioService.findAll(AuthUtil.isAdmin(auth)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioModel> getById(@PathVariable Long id, Authentication auth) {
        return servicioService.findById(id)
                .filter(s -> AuthUtil.isAdmin(auth) || Boolean.TRUE.equals(s.getActivo()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Consulta por SKU (la usa el servicio de ventas para validar precios). */
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ServicioModel> getBySku(@PathVariable String sku, Authentication auth) {
        return servicioService.findBySku(sku.toUpperCase())
                .filter(s -> AuthUtil.isAdmin(auth) || Boolean.TRUE.equals(s.getActivo()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServicioModel> create(@Valid @RequestBody ServicioRequest request) {
        return ResponseEntity.status(201).body(servicioService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicioModel> update(@PathVariable Long id, @Valid @RequestBody ServicioRequest request) {
        return ResponseEntity.ok(servicioService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ServicioModel> toggleEstado(@PathVariable Long id, @RequestBody EstadoDTO body) {
        return ResponseEntity.ok(servicioService.toggleActivo(id, Boolean.TRUE.equals(body.activo())));
    }

    public record EstadoDTO(Boolean activo) {}
}
