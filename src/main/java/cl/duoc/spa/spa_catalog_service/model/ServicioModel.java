package cl.duoc.spa.spa_catalog_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // false para circuito de agua y sauna (espacios compartidos, controlados por aforo)
    @Column(name = "requiere_terapeuta")
    private Boolean requiereTerapeuta = true;

    // Personas incluidas en el valor (1 individual, 2 parejas, 4 escapada de amigas)
    private Integer personas = 1;

    // Orden de aparicion segun la carta
    private Integer orden;

    // Version de la carta con la que se sincronizo este registro (ver CartaSeeder)
    @Column(name = "carta_version")
    private Integer cartaVersion;

}
