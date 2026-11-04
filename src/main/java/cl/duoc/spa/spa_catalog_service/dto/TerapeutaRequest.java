package cl.duoc.spa.spa_catalog_service.dto;

import jakarta.validation.constraints.*;

public record TerapeutaRequest(
        @NotBlank @Size(max = 120) String nombre,
        @Size(max = 500) @Pattern(regexp = "^(https://.*)?$", message = "debe ser una URL https") String fotoUrl,
        @Size(max = 200) String especialidad,
        Boolean activo,
        Integer orden
) {}
