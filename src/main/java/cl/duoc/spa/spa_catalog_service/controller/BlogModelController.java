package cl.duoc.spa.spa_catalog_service.controller;

import cl.duoc.spa.spa_catalog_service.model.BlogModel;
import cl.duoc.spa.spa_catalog_service.service.BlogModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog/blogs")
@CrossOrigin(origins = "*") // o pones tu dominio real si deseas
public class BlogModelController {

    private final BlogModelService blogService;

    @GetMapping
    public ResponseEntity<List<BlogModel>> getAll() {
        return ResponseEntity.ok(blogService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogModel> getById(@PathVariable Long id) {
        return blogService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BlogModel> create(@RequestBody BlogModel blog) {
        return ResponseEntity.ok(blogService.create(blog));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogModel> update(@PathVariable Long id, @RequestBody BlogModel blog) {
        return ResponseEntity.ok(blogService.update(id, blog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH publicar/borrador
    @PatchMapping("/{id}/estado")
    public ResponseEntity<BlogModel> toggleEstado(
            @PathVariable Long id,
            @RequestBody EstadoDTO body
    ) {
        return ResponseEntity.ok(blogService.togglePublicado(id, body.publicado()));
    }

    // PATCH destacado
    @PatchMapping("/{id}/destacado")
    public ResponseEntity<BlogModel> toggleDestacado(
            @PathVariable Long id,
            @RequestBody DestacadoDTO body
    ) {
        return ResponseEntity.ok(blogService.toggleDestacado(id, body.destacado()));
    }

    // DTO internos
    public record EstadoDTO(Boolean publicado) {}
    public record DestacadoDTO(Boolean destacado) {}
}
