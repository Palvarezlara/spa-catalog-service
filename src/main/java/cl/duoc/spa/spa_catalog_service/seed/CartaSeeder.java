package cl.duoc.spa.spa_catalog_service.seed;

import cl.duoc.spa.spa_catalog_service.model.ServicioModel;
import cl.duoc.spa.spa_catalog_service.model.TerapeutaModel;
import cl.duoc.spa.spa_catalog_service.repository.IServicioModelRepository;
import cl.duoc.spa.spa_catalog_service.repository.ITerapeutaModelRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sincroniza la Carta de Servicios 2026 del SPA del Bosque con la base de datos.
 *
 * - Crea los servicios que no existen.
 * - Actualiza los que vienen de una carta anterior (cartaVersion < CARTA_VERSION).
 *   Las ediciones hechas luego desde el panel admin NO se pisan: el registro ya queda en la version vigente.
 * - Desactiva (no borra) servicios de cartas antiguas que ya no existen, para conservar el historial de ventas.
 * - Crea el equipo de terapeutas si la tabla esta vacia.
 *
 * Para la proxima carta: subir CARTA_VERSION y editar la lista.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "catalog.seed.enabled", havingValue = "true", matchIfMissing = true)
public class CartaSeeder implements ApplicationRunner {

    public static final int CARTA_VERSION = 2026;
    private static final Logger log = LoggerFactory.getLogger(CartaSeeder.class);

    private static final String MEDIA = "https://spa-del-bosque-media.s3.us-east-1.amazonaws.com/servicios/imagenes-servicios/";
    private static final String IMG_RELAX = MEDIA + "relajacion.webp";
    private static final String IMG_DESC = MEDIA + "descontracturante.webp";
    private static final String IMG_PIEDRAS = MEDIA + "piedras-calientes.webp";
    private static final String IMG_BOSQUE = MEDIA + "bosque.webp";
    private static final String IMG_OLIVO = MEDIA + "olivoterapia.webp";
    private static final String IMG_CHOCO = MEDIA + "chocolaterapia.webp";
    private static final String IMG_EXFO = MEDIA + "exfoliacion.webp";
    private static final String IMG_CIRC = MEDIA + "circuito.webp";
    private static final String IMG_SAUNA = MEDIA + "sauna.webp";
    private static final String IMG_RENO = MEDIA + "renovacion.webp";
    private static final String IMG_DULCE = MEDIA + "dulceaventura.webp";
    private static final String IMG_DAY = MEDIA + "dayuse.webp";
    private static final String IMG_NATIVO = MEDIA + "bosqueNativo.webp";
    private static final String IMG_MAGICO = MEDIA + "bosqueMagico.webp";
    private static final String IMG_AMIGAS = MEDIA + "EscapadaAmigas.webp";

    private final IServicioModelRepository servicios;
    private final ITerapeutaModelRepository terapeutas;

    /** Fila de la carta. */
    public record Item(String sku, String categoria, String nombre, int precio, int minutos,
                String img, String descripcion, boolean requiereTerapeuta, int personas) {}

    public static List<Item> carta() {
        String ninosNota = " Desde 4 a 12 años.";
        return List.of(
            // ---- Terapias (circuito y sauna: espacios compartidos) ----
            new Item("CIRC45", "circuitos", "Circuito de agua 45 min", 20000, 45, IMG_CIRC,
                "Espacio compartido con tres piscinas de diferentes temperaturas: 38 °C, 34 °C y 18 °C. Una de ellas cuenta con turbinas y burbujas que favorecen la distensión de la musculatura y la activación del sistema circulatorio. Uso permitido para mayores de 15 años.", false, 1),
            new Item("SAUNA30", "circuitos", "Sauna seco 30 min", 18000, 30, IMG_SAUNA,
                "Sauna seco de madera noble, con temperaturas entre los 70 °C y 80 °C y una humedad que no supera el 20 %.", false, 1),

            // ---- Masajes ----
            new Item("RELAX30", "masajes", "Masaje de relajación 30 min", 38000, 30, IMG_RELAX,
                "Masaje corporal suave orientado a la relajación del cuerpo y la mente.", true, 1),
            new Item("RELAX40", "masajes", "Masaje de relajación 40 min", 43000, 40, IMG_RELAX,
                "Masaje corporal suave orientado a la relajación del cuerpo y la mente.", true, 1),
            new Item("RELAX60", "masajes", "Masaje de relajación 60 min", 48000, 60, IMG_RELAX,
                "Masaje corporal suave orientado a la relajación del cuerpo y la mente.", true, 1),
            new Item("BOSQUE30", "masajes", "Masaje del bosque 30 min", 41000, 30, IMG_BOSQUE,
                "Masaje de la casa personalizado: diferentes técnicas y presiones que promueven la descarga de tensión acumulada, además de la relajación del cuerpo y mente.", true, 1),
            new Item("BOSQUE40", "masajes", "Masaje del bosque 40 min", 46000, 40, IMG_BOSQUE,
                "Masaje de la casa personalizado: diferentes técnicas y presiones que promueven la descarga de tensión acumulada, además de la relajación del cuerpo y mente.", true, 1),
            new Item("BOSQUE60", "masajes", "Masaje del bosque 60 min", 50000, 60, IMG_BOSQUE,
                "Masaje de la casa personalizado: diferentes técnicas y presiones que promueven la descarga de tensión acumulada, además de la relajación del cuerpo y mente.", true, 1),
            new Item("DESC30", "masajes", "Masaje descontracturante 30 min", 45000, 30, IMG_DESC,
                "Masaje de mayor presión enfocado en aliviar la tensión muscular localizada.", true, 1),
            new Item("DESC40", "masajes", "Masaje descontracturante 40 min", 50000, 40, IMG_DESC,
                "Masaje de mayor presión enfocado en aliviar la tensión muscular localizada.", true, 1),
            new Item("DESC60", "masajes", "Masaje descontracturante 60 min", 60000, 60, IMG_DESC,
                "Masaje de mayor presión enfocado en aliviar la tensión muscular localizada.", true, 1),
            new Item("DREN40", "masajes", "Drenaje linfático localizado 40 min", 45000, 40, IMG_RELAX,
                "Terapia enfocada a personas con retención de líquidos; la maniobra es de movimiento suave para dirigir este líquido a los ganglios. Se trabaja una zona específica.", true, 1),
            new Item("DREN60", "masajes", "Drenaje linfático completo 60 min", 53000, 60, IMG_RELAX,
                "Terapia enfocada a personas con retención de líquidos; la maniobra es de movimiento suave para dirigir este líquido a los ganglios. Se trabaja el cuerpo completo.", true, 1),
            new Item("EMBA40", "masajes", "Masaje para embarazadas 40 min", 46000, 40, IMG_RELAX,
                "Masaje pensado para acompañar a las futuras mamás, adaptado a su comodidad.", true, 1),
            new Item("BRUX30", "masajes", "Masaje anti bruxismo 30 min", 40000, 30, IMG_RELAX,
                "Masaje enfocado en la relajación de la zona de mandíbula, cuello y cabeza.", true, 1),
            new Item("NINOS20", "masajes", "Masaje para niños 20 min", 28000, 20, IMG_RELAX,
                "Masaje suave y breve pensado para niños." + ninosNota, true, 1),
            new Item("REFLEX40", "masajes", "Reflexología podal 40 min", 48000, 40, IMG_RELAX,
                "La reflexología se basa en el estímulo de áreas reflejas. Es una terapia natural que mediante técnicas manuales estimula terminales nerviosas y energéticas de los pies.", true, 1),
            new Item("PIED60", "masajes", "Piedras calientes 60 min", 60000, 60, IMG_PIEDRAS,
                "Masaje corporal con piedras volcánicas calientes y aromaterapia que despierta los sentidos. Siguiendo los canales energéticos del cuerpo se logra la relajación completa de la musculatura.", true, 1),
            new Item("PINDAS50", "masajes", "Masaje con pindas 50 min", 60000, 50, IMG_PIEDRAS,
                "Terapia de relajación profunda que mezcla técnicas de presión, arrastre y distensión muscular, combinado con el efecto relajante de las hierbas incorporadas y la aromaterapia concentrada en las pindas.", true, 1),

            // ---- Tratamientos corporales ----
            new Item("OLIVO100", "corporales", "Olivoterapia 100 min", 91000, 100, IMG_OLIVO,
                "Comienza con una exfoliación con pulpa de aceitunas, sal de mar y aceite de oliva; continúa con una nutrición al cabello desde la raíz a las puntas con aceite de coco; luego de una refrescante ducha, termina con un Masaje del Bosque de 60 minutos para abrir los sentidos.", true, 1),
            new Item("CHOCO90", "corporales", "Chocolaterapia 90 min", 91000, 90, IMG_CHOCO,
                "Terapia de chocolate exclusiva: exfoliación de cacao y café para revitalizar la piel, seguida de un relajante masaje de 40 minutos con crema de chocolate, y un masaje craneal con hidratación capilar.", true, 1),
            new Item("EXFO30", "corporales", "Exfoliación corporal 30 min", 45000, 30, IMG_EXFO,
                "Pulido de la piel que retira las células muertas, favoreciendo los procesos de renovación y dejándola suave y saludable.", true, 1),

            // ---- Programas individuales ----
            new Item("RENO145", "individuales", "Programa Renovación 145 min", 106000, 145, IMG_RENO,
                "Incluye: Circuito de agua 45 minutos y Olivoterapia 100 minutos.", true, 1),
            new Item("DULCE135", "individuales", "Programa Dulce aventura en el Bosque 135 min", 106000, 135, IMG_DULCE,
                "Incluye: Circuito de agua 45 minutos y Chocolaterapia 90 minutos.", true, 1),
            new Item("DAY135", "individuales", "Programa Day Use 135 min", 120000, 135, IMG_DAY,
                "Incluye: Circuito de agua 45 min, Exfoliación corporal 30 min y Piedras calientes 60 min.", true, 1),

            // ---- Programas en parejas (valor por pareja) ----
            new Item("NAT115", "parejas", "Programa Bosque nativo 115 min (pareja)", 150000, 115, IMG_NATIVO,
                "Valor para dos personas. Incluye: Sauna 30 minutos, Circuito de aguas 45 minutos y Masaje del bosque 40 minutos.", true, 2),
            new Item("MAG135", "parejas", "Programa Bosque mágico 135 min (pareja)", 200000, 135, IMG_MAGICO,
                "Valor para dos personas. Incluye: Sauna 30 minutos, Circuito de aguas 45 minutos, Reflexología podal 30 minutos y Masaje de relajación 30 minutos.", true, 2),

            // ---- Escapada de amigas (4 personas) ----
            new Item("AMIG115", "escapada-amigas", "Programa Escapada de amigas 115 min (4 personas)", 296000, 115, IMG_AMIGAS,
                "Valor para cuatro personas. Incluye: Sauna 30 minutos, Circuito de aguas 45 minutos, Masaje del bosque 40 minutos, tabla para compartir y botella de espumante.", true, 4)
        );
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        syncServicios();
        syncTerapeutas();
    }

    private void syncServicios() {
        List<Item> carta = carta();
        Set<String> skusCarta = new HashSet<>();
        int creados = 0, actualizados = 0, desactivados = 0;

        for (int i = 0; i < carta.size(); i++) {
            Item it = carta.get(i);
            skusCarta.add(it.sku());
            ServicioModel s = servicios.findBySku(it.sku()).orElse(null);
            boolean nuevo = s == null;
            if (nuevo) {
                s = new ServicioModel();
                s.setSku(it.sku());
            } else if (s.getCartaVersion() != null && s.getCartaVersion() >= CARTA_VERSION) {
                continue; // ya sincronizado: respetar ediciones del panel admin
            }
            s.setCategoria(it.categoria());
            s.setNombre(it.nombre());
            s.setPrecio((double) it.precio());
            s.setDuracionMin(it.minutos());
            s.setImageUrl(it.img());
            s.setDescripcion(it.descripcion());
            s.setRequiereTerapeuta(it.requiereTerapeuta());
            s.setPersonas(it.personas());
            s.setOrden(i + 1);
            s.setActivo(true);
            s.setCartaVersion(CARTA_VERSION);
            servicios.save(s);
            if (nuevo) creados++; else actualizados++;
        }

        for (ServicioModel s : servicios.findAll()) {
            boolean desactualizado = s.getCartaVersion() == null || s.getCartaVersion() < CARTA_VERSION;
            if (desactualizado && !skusCarta.contains(s.getSku()) && Boolean.TRUE.equals(s.getActivo())) {
                s.setActivo(false);
                servicios.save(s);
                desactivados++;
            }
        }
        log.info("Carta {} sincronizada: {} creados, {} actualizados, {} desactivados",
                CARTA_VERSION, creados, actualizados, desactivados);
    }

    private void syncTerapeutas() {
        if (terapeutas.count() > 0) return;
        String fotos = "https://spa-del-bosque-media.s3.us-east-1.amazonaws.com/terapeutas/";
        String[][] equipo = {
            {"Viviana Schiappacasse", fotos + "vivianaSchipp.webp"},
            {"Pamela Malicett", null},
            {"Rosa Cuevas", null},
            {"Damari Campos", null},
            {"Alicia Meza", fotos + "terapeuta1.webp"},
        };
        for (int i = 0; i < equipo.length; i++) {
            TerapeutaModel t = new TerapeutaModel();
            t.setNombre(equipo[i][0]);
            t.setFotoUrl(equipo[i][1]);
            t.setActivo(true);
            t.setOrden(i + 1);
            terapeutas.save(t);
        }
        log.info("Equipo de terapeutas inicial creado ({} personas)", equipo.length);
    }
}
