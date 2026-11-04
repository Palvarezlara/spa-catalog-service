package cl.duoc.spa.spa_catalog_service.service;

import cl.duoc.spa.spa_catalog_service.dto.BlogRequest;
import cl.duoc.spa.spa_catalog_service.exception.ConflictException;
import cl.duoc.spa.spa_catalog_service.exception.NotFoundException;
import cl.duoc.spa.spa_catalog_service.model.BlogModel;
import cl.duoc.spa.spa_catalog_service.repository.IBlogModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogModelService {
    private final IBlogModelRepository blogRepository;

    /** @param incluirBorradores true solo para administradores */
    public List<BlogModel> findAll(boolean incluirBorradores) {
        return blogRepository.findAll().stream()
                .filter(b -> incluirBorradores || Boolean.TRUE.equals(b.getPublicado()))
                .toList();
    }

    public Optional<BlogModel> findById(Long id) {
        return blogRepository.findById(id);
    }

    @Transactional
    public BlogModel create(BlogRequest r) {
        if (blogRepository.existsBySlug(r.slug())) {
            throw new ConflictException("Ya existe un blog con ese slug");
        }
        BlogModel blog = new BlogModel();
        apply(blog, r);
        return blogRepository.save(blog);
    }

    @Transactional
    public BlogModel update(Long id, BlogRequest r) {
        BlogModel blog = blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Blog no encontrado"));
        if (!blog.getSlug().equals(r.slug()) && blogRepository.existsBySlug(r.slug())) {
            throw new ConflictException("Ya existe un blog con ese slug");
        }
        apply(blog, r);
        return blogRepository.save(blog);
    }

    @Transactional
    public void delete(Long id) {
        if (!blogRepository.existsById(id)) {
            throw new NotFoundException("Blog no encontrado");
        }
        blogRepository.deleteById(id);
    }

    @Transactional
    public BlogModel togglePublicado(Long id, boolean publicado) {
        BlogModel blog = blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Blog no encontrado"));
        blog.setPublicado(publicado);
        if (publicado && blog.getFechaPublicacion() == null) {
            blog.setFechaPublicacion(LocalDateTime.now());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    public BlogModel toggleDestacado(Long id, boolean destacado) {
        BlogModel blog = blogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Blog no encontrado"));
        blog.setDestacado(destacado);
        return blogRepository.save(blog);
    }

    private void apply(BlogModel b, BlogRequest r) {
        b.setSlug(r.slug());
        b.setTitulo(r.titulo().trim());
        b.setCategoria(r.categoria());
        b.setAutor(r.autor());
        b.setResumen(r.resumen());
        b.setContenido(r.contenido());
        b.setImageUrl(r.imageUrl() == null || r.imageUrl().isBlank() ? null : r.imageUrl().trim());
        boolean publicado = Boolean.TRUE.equals(r.publicado());
        b.setPublicado(publicado);
        b.setDestacado(Boolean.TRUE.equals(r.destacado()));
        if (publicado && b.getFechaPublicacion() == null) {
            b.setFechaPublicacion(LocalDateTime.now());
        }
    }
}
