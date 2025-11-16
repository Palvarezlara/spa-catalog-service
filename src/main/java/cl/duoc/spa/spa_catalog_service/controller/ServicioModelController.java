package cl.duoc.spa.spa_catalog_service.controller;


import cl.duoc.spa.spa_catalog_service.service.ServicioModelService;
import cl.duoc.spa.spa_catalog_service.model.ServicioModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalog/servicios")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ServicioModelController {

    private final ServicioModelService servicioService;

    // GET /api/v1/spadelbosque/servicios
    @GetMapping
    public ResponseEntity<List<ServicioModel>> getAll() {
        return ResponseEntity.ok(servicioService.findAll());
    }

    // GET /api/v1/spadelbosque/servicios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ServicioModel> getById(@PathVariable Long id) {
        return servicioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/v1/spadelbosque/servicios
    @PostMapping
    public ResponseEntity<ServicioModel> create(@RequestBody ServicioModel servicio) {
        return ResponseEntity.ok(servicioService.create(servicio));
    }

    // PUT /api/v1/spadelbosque/servicios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ServicioModel> update(@PathVariable Long id,
                                                @RequestBody ServicioModel servicio) {
        return ResponseEntity.ok(servicioService.update(id, servicio));
    }

    // DELETE /api/v1/spadelbosque/servicios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/v1/spadelbosque/servicios/{id}/estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ServicioModel> toggleEstado(
            @PathVariable Long id,
            @RequestBody EstadoDTO body
    ) {
        return ResponseEntity.ok(servicioService.toggleActivo(id, body.activo()));
    }

    // DTO para el PATCH
    public record EstadoDTO(Boolean activo) {}

}
