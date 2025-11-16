package cl.duoc.spa.spa_catalog_service.service;

import cl.duoc.spa.spa_catalog_service.model.BlogModel;
import cl.duoc.spa.spa_catalog_service.repository.IBlogModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogModelService {
    private final IBlogModelRepository blogRepository;

    // Obtener todos
    public List<BlogModel> findAll() {
        return blogRepository.findAll();
    }

    // Obtener uno
    public Optional<BlogModel> findById(Long id) {
        return blogRepository.findById(id);
    }

    // Crear
    public BlogModel create(BlogModel blog) {
        // si se crea en estado publicado y no tiene fecha => asignamos
        if (blog.getPublicado() && blog.getFechaPublicacion() == null) {
            blog.setFechaPublicacion(LocalDateTime.now());
        }
        return blogRepository.save(blog);
    }

    // Editar
    public BlogModel update(Long id, BlogModel up) {
        BlogModel blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog no encontrado"));

        blog.setSlug(up.getSlug());
        blog.setTitulo(up.getTitulo());
        blog.setCategoria(up.getCategoria());
        blog.setAutor(up.getAutor());
        blog.setResumen(up.getResumen());
        blog.setContenido(up.getContenido());
        blog.setImageUrl(up.getImageUrl());
        blog.setPublicado(up.getPublicado());
        blog.setDestacado(up.getDestacado());

        // Manejo de publicación
        if (up.getPublicado() && blog.getFechaPublicacion() == null) {
            blog.setFechaPublicacion(LocalDateTime.now());
        }

        return blogRepository.save(blog);
    }

    // Eliminar
    public void delete(Long id) {
        blogRepository.deleteById(id);
    }

    // Cambiar estado publicado/borrador
    public BlogModel togglePublicado(Long id, boolean publicado) {
        BlogModel blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog no encontrado"));

        blog.setPublicado(publicado);

        if (publicado && blog.getFechaPublicacion() == null) {
            blog.setFechaPublicacion(LocalDateTime.now());
        }
        return blogRepository.save(blog);
    }

    // Cambiar destacado
    public BlogModel toggleDestacado(Long id, boolean destacado) {
        BlogModel blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog no encontrado"));

        blog.setDestacado(destacado);
        return blogRepository.save(blog);
    }

}
