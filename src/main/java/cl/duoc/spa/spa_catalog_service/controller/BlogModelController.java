package cl.duoc.spa.spa_catalog_service.controller;

import cl.duoc.spa.spa_catalog_service.dto.BlogRequest;
import cl.duoc.spa.spa_catalog_service.model.BlogModel;
import cl.duoc.spa.spa_catalog_service.service.BlogModelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog/blogs")
public class BlogModelController {

    private final BlogModelService blogService;

    /** Publico: solo publicados. ADMIN: tambien borradores. */
    @GetMapping
    public ResponseEntity<List<BlogModel>> getAll(Authentication auth) {
        return ResponseEntity.ok(blogService.findAll(AuthUtil.isAdmin(auth)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogModel> getById(@PathVariable Long id, Authentication auth) {
        return blogService.findById(id)
                .filter(b -> AuthUtil.isAdmin(auth) || Boolean.TRUE.equals(b.getPublicado()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BlogModel> create(@Valid @RequestBody BlogRequest request) {
        return ResponseEntity.status(201).body(blogService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogModel> update(@PathVariable Long id, @Valid @RequestBody BlogRequest request) {
        return ResponseEntity.ok(blogService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<BlogModel> toggleEstado(@PathVariable Long id, @RequestBody EstadoDTO body) {
        return ResponseEntity.ok(blogService.togglePublicado(id, Boolean.TRUE.equals(body.publicado())));
    }

    @PatchMapping("/{id}/destacado")
    public ResponseEntity<BlogModel> toggleDestacado(@PathVariable Long id, @RequestBody DestacadoDTO body) {
        return ResponseEntity.ok(blogService.toggleDestacado(id, Boolean.TRUE.equals(body.destacado())));
    }

    public record EstadoDTO(Boolean publicado) {}
    public record DestacadoDTO(Boolean destacado) {}
}
