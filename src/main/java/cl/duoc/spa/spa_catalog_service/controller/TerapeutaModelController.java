package cl.duoc.spa.spa_catalog_service.controller;

import cl.duoc.spa.spa_catalog_service.dto.TerapeutaRequest;
import cl.duoc.spa.spa_catalog_service.model.TerapeutaModel;
import cl.duoc.spa.spa_catalog_service.service.TerapeutaModelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog/terapeutas")
public class TerapeutaModelController {

    private final TerapeutaModelService service;

    /** Publico: terapeutas activos (para elegir en la agenda). ADMIN: todos. */
    @GetMapping
    public ResponseEntity<List<TerapeutaModel>> getAll(Authentication auth) {
        return ResponseEntity.ok(service.findAll(AuthUtil.isAdmin(auth)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerapeutaModel> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TerapeutaModel> create(@Valid @RequestBody TerapeutaRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TerapeutaModel> update(@PathVariable Long id, @Valid @RequestBody TerapeutaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TerapeutaModel> estado(@PathVariable Long id, @RequestBody EstadoDTO body) {
        return ResponseEntity.ok(service.setActivo(id, Boolean.TRUE.equals(body.activo())));
    }

    public record EstadoDTO(Boolean activo) {}
}
