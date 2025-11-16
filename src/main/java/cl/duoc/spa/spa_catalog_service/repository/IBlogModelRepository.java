package cl.duoc.spa.spa_catalog_service.repository;

import cl.duoc.spa.spa_catalog_service.model.BlogModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBlogModelRepository extends JpaRepository<BlogModel, Long> {
    boolean existsBySlug(String slug);
}
