package cl.duoc.spa.spa_catalog_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Table(name = "services")
@AllArgsConstructor
@NoArgsConstructor
public class ServicioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // SKU visible en la web/móvil (RELAX30, CIRC45, etc.)
    @Column(nullable = false, unique = true, length = 20)
    private String sku;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String categoria;  // masajes, corporales, etc.

    @Column(nullable = false)
    private Double precio;     // CLP

    @Column(name = "duracion_min", nullable = false)
    private Integer duracionMin;  // minutos

    @Column(name = "image_url", length = 500)
    private String imageUrl;      // URL pública de la imagen

    @Column(name = "descripcion", length = 1000)
    private String descripcion;   // texto descriptivo del servicio

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

}
