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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID sku;
    private String category;
    private String name;
    private Integer durationMin;
    private Double price;
    private Double commission;
    @Column(length = 500)
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    private Boolean active = true;



}
