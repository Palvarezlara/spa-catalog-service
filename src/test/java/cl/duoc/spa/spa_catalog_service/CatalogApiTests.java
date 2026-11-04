package cl.duoc.spa.spa_catalog_service;

import cl.duoc.spa.spa_catalog_service.repository.IServicioModelRepository;
import cl.duoc.spa.spa_catalog_service.seed.CartaSeeder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogApiTests {

    @Autowired MockMvc mvc;
    @Autowired IServicioModelRepository servicios;

    private static final String VALID = """
        {"sku":"TEST1","nombre":"Servicio de prueba","categoria":"masajes","precio":10000,"duracionMin":30,
         "descripcion":"x","activo":true}
        """;

    @AfterEach
    void limpiar() {
        servicios.findBySku("TEST1").ifPresent(servicios::delete);
    }

    @Test
    void carta2026QuedaSembrada() {
        long activos = servicios.findAll().stream().filter(s -> Boolean.TRUE.equals(s.getActivo())).count();
        assertEquals(CartaSeeder.carta().size(), activos);
        assertEquals(60000.0, servicios.findBySku("DESC60").orElseThrow().getPrecio());
        assertEquals(2, servicios.findBySku("NAT115").orElseThrow().getPersonas());
        assertEquals(false, servicios.findBySku("CIRC45").orElseThrow().getRequiereTerapeuta());
    }

    @Test
    void listadoPublicoNoRequiereToken() throws Exception {
        mvc.perform(get("/api/v1/catalog/servicios")).andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(CartaSeeder.carta().size()));
        mvc.perform(get("/api/v1/catalog/terapeutas")).andExpect(status().isOk())
           .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void escrituraSinTokenEs401() throws Exception {
        mvc.perform(post("/api/v1/catalog/servicios").contentType(MediaType.APPLICATION_JSON).content(VALID))
           .andExpect(status().isUnauthorized());
    }

    @Test
    void escrituraComoClienteEs403() throws Exception {
        mvc.perform(post("/api/v1/catalog/servicios").contentType(MediaType.APPLICATION_JSON).content(VALID)
                .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(() -> "ROLE_CLIENTE")))
           .andExpect(status().isForbidden());
    }

    @Test
    void adminCreaYValida() throws Exception {
        var admin = SecurityMockMvcRequestPostProcessors.jwt().authorities(() -> "ROLE_ADMIN");
        mvc.perform(post("/api/v1/catalog/servicios").contentType(MediaType.APPLICATION_JSON).content(VALID).with(admin))
           .andExpect(status().isCreated());
        // SKU duplicado -> 409
        mvc.perform(post("/api/v1/catalog/servicios").contentType(MediaType.APPLICATION_JSON).content(VALID).with(admin))
           .andExpect(status().isConflict());
        // payload invalido -> 400
        mvc.perform(post("/api/v1/catalog/servicios").contentType(MediaType.APPLICATION_JSON)
                .content("{\"sku\":\"\",\"nombre\":\"\",\"categoria\":\"hack\",\"precio\":-1,\"duracionMin\":1}").with(admin))
           .andExpect(status().isBadRequest());
    }

    @Test
    void serviciosInactivosSoloLosVeElAdmin() throws Exception {
        var s = servicios.findBySku("RELAX30").orElseThrow();
        s.setActivo(false);
        servicios.save(s);
        try {
            String publico = mvc.perform(get("/api/v1/catalog/servicios")).andReturn().getResponse().getContentAsString();
            assertTrue(!publico.contains("\"RELAX30\""));
            String admin = mvc.perform(get("/api/v1/catalog/servicios")
                    .with(SecurityMockMvcRequestPostProcessors.jwt().authorities(() -> "ROLE_ADMIN")))
                    .andReturn().getResponse().getContentAsString();
            assertTrue(admin.contains("\"RELAX30\""));
        } finally {
            s.setActivo(true);
            servicios.save(s);
        }
    }
}
