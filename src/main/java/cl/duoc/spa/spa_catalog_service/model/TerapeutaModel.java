package cl.duoc.spa.spa_catalog_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Terapeuta del SPA. Se usa en la agenda de reservas (modal "Quien prefieres que te atienda?")
 * y mas adelante en la plataforma de escritorio.
 * Solo se publican datos profesionales minimos (nombre y foto): sin datos personales de contacto.
 */
@Entity
@Data
@Table(name = "terapeutas")
@AllArgsConstructor
@NoArgsConstructor
public class TerapeutaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String nombre;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(length = 200)
    private String especialidad;

    @Column(nullable = false)
    private Boolean activo = true;

    private Integer orden;
}
