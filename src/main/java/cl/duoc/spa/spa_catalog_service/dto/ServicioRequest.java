package cl.duoc.spa.spa_catalog_service.dto;

import jakarta.validation.constraints.*;

/** Datos aceptados al crear/editar un servicio (evita asignacion masiva sobre la entidad). */
public record ServicioRequest(
        @NotBlank @Size(max = 20) @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "solo letras, numeros, guion y guion bajo")
        String sku,
        @NotBlank @Size(max = 150) String nombre,
        @NotBlank @Pattern(regexp = "masajes|corporales|circuitos|individuales|parejas|escapada-amigas",
                message = "categoria no valida") String categoria,
        @NotNull @DecimalMin("0") @DecimalMax("5000000") Double precio,
        @NotNull @Min(5) @Max(600) Integer duracionMin,
        @Size(max = 500) @Pattern(regexp = "^(https://.*)?$", message = "debe ser una URL https") String imageUrl,
        @Size(max = 1000) String descripcion,
        Boolean activo,
        Boolean requiereTerapeuta,
        @Min(1) @Max(10) Integer personas,
        Integer orden
) {}
