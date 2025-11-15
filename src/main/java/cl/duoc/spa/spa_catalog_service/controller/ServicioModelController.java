package cl.duoc.spa.spa_catalog_service.controller;


import cl.duoc.spa.spa_catalog_service.service.ServicioModelService;
import cl.duoc.spa.spa_catalog_service.model.ServicioModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ServicioModelController {

    private final ServicioModelService service;

    @GetMapping
    public List<ServicioModel> listAll() {
        return service.findAll();
    }

    @GetMapping("/active")
    public List<ServicioModel> listActive() {
        return service.findActive();
    }

    @GetMapping("/{sku}")
    public ServicioModel getBySku(@PathVariable UUID sku) {
        return service.findBySku(sku);
    }

    @PostMapping
    public ServicioModel create(@RequestBody ServicioModel servicio) {
        return service.save(servicio);
    }

    @PutMapping("/{sku}")
    public ServicioModel update(@PathVariable UUID sku, @RequestBody ServicioModel servicio) {
        servicio.setSku(sku);
        return service.save(servicio);
    }

    @DeleteMapping("/{sku}")
    public void delete(@PathVariable UUID sku) {
        service.delete(sku);
    }

}
