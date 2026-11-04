package cl.duoc.spa.spa_catalog_service.dto;

import jakarta.validation.constraints.*;

public record BlogRequest(
        @NotBlank @Size(max = 120) @Pattern(regexp = "^[a-z0-9-]+$", message = "slug en minusculas, numeros y guiones")
        String slug,
        @NotBlank @Size(max = 200) String titulo,
        @Size(max = 100) String categoria,
        @Size(max = 100) String autor,
        @Size(max = 300) String resumen,
        @Size(max = 60000) String contenido,
        @Size(max = 500) @Pattern(regexp = "^(https://.*)?$", message = "debe ser una URL https") String imageUrl,
        Boolean publicado,
        Boolean destacado
) {}
