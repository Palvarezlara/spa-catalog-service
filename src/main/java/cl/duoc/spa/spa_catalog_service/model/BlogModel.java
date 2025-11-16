package cl.duoc.spa.spa_catalog_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "blogs")
@AllArgsConstructor
@NoArgsConstructor
public class BlogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ej: "respiracion-consciente-5-minutos"
    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 100)
    private String categoria;

    @Column(length = 100)
    private String autor;

    // Resumen corto para listados
    @Column(length = 300)
    private String resumen;

    // Contenido completo (Markdown / HTML)
    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "publicado", nullable = false)
    private Boolean publicado = false;

    @Column(name = "destacado", nullable = false)
    private Boolean destacado = false;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;
}
