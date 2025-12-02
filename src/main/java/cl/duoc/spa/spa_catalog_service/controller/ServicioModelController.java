package cl.duoc.spa.spa_catalog_service.controller;


import cl.duoc.spa.spa_catalog_service.service.ServicioModelService;
import cl.duoc.spa.spa_catalog_service.model.ServicioModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/servicios")
@RequiredArgsConstructor
public class ServicioModelController {

    private final ServicioModelService servicioService;


    @GetMapping
    public ResponseEntity<List<ServicioModel>> getAll() {
        return ResponseEntity.ok(servicioService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ServicioModel> getById(@PathVariable Long id) {
        return servicioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<ServicioModel> create(@RequestBody ServicioModel servicio) {
        return ResponseEntity.ok(servicioService.create(servicio));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ServicioModel> update(@PathVariable Long id,
                                                @RequestBody ServicioModel servicio) {
        return ResponseEntity.ok(servicioService.update(id, servicio));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicioService.delete(id);
        return ResponseEntity.noContent().build();
    }


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
