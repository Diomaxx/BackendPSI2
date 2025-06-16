package com.psi2.donaciones;

import com.psi2.donaciones.dto.DonanteDto;
import com.psi2.donaciones.dto.ProductoDto;
import com.psi2.donaciones.service.serviceimpl.InventarioExternoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@SpringBootTest
class Integracion {

    @Autowired
    private InventarioExternoServiceImpl inventarioService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testConsultarInventario_mocked() {
        String mockJson = "[{\n" +
                "  \"id_articulo\": 4,\n" +
                "  \"nombre_articulo\": \"Abrigo\",\n" +
                "  \"descripcion\": \"Abrigo de lana para invierno\",\n" +
                "  \"nombre_unidad\": \"Pieza\",\n" +
                "  \"medida_abreviada\": \"Pza\",\n" +
                "  \"cantidad_estimada_por_persona\": 1,\n" +
                "  \"total_restante\": 161\n" +
                "}]";

        mockServer.expect(requestTo("https://backenddonaciones.onrender.com/api/inventario/stock"))
                .andRespond(withSuccess(mockJson, MediaType.APPLICATION_JSON));

        List<ProductoDto> productos = inventarioService.consultarInventario();

        assertNotNull(productos, "La lista de productos no debe ser null");
        assertEquals(1, productos.size(), "Debe haber exactamente un producto");

        ProductoDto producto = productos.get(0);
        assertEquals(4, Integer.valueOf(producto.getId_articulo()));
        assertEquals("Abrigo", producto.getNombre_articulo());
        assertEquals("Pza", producto.getMedida_abreviada());
        assertEquals(161, producto.getTotal_restante());

        mockServer.verify();
    }


    @Test
    void testObtenerDonantesPorCodigo_mocked() {
        String codigo = "ABC123";

        String mockJson = "[\n" +
                "  {\n" +
                "    \"id_donante\": 2,\n" +
                "    \"nombres\": \"María\",\n" +
                "    \"apellido_paterno\": \"Fernández\",\n" +
                "    \"apellido_materno\": \"Ruiz\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id_donante\": 4,\n" +
                "    \"nombres\": \"Ricardo\",\n" +
                "    \"apellido_paterno\": \"Pacheco\",\n" +
                "    \"apellido_materno\": \"Ortiz\"\n" +
                "  }\n" +
                "]";

        mockServer.expect(requestTo("https://backenddonaciones.onrender.com/api/paquetes/donantes/" + codigo))
                .andRespond(withSuccess(mockJson, MediaType.APPLICATION_JSON));

        List<DonanteDto> donantes = inventarioService.obtenerDonantesPorCodigo(codigo);

        assertNotNull(donantes);
        assertEquals(2, donantes.size());

        DonanteDto d1 = donantes.get(0);
        assertEquals("María", d1.getNombres());
        assertEquals("Fernández", d1.getApellido_paterno());

        DonanteDto d2 = donantes.get(1);
        assertEquals("Ricardo", d2.getNombres());
        assertEquals("Ortiz", d2.getApellido_materno());

        mockServer.verify();
    }

    @Test
    void testConsultarProducto_mocked() {
        String idProducto = "9";

        String mockJson = "{\n" +
                "  \"id_articulo\": 9,\n" +
                "  \"nombre_articulo\": \"Lentejas\",\n" +
                "  \"descripcion\": \"Lentejas secas de calidad\",\n" +
                "  \"nombre_unidad\": \"Kilogramo\",\n" +
                "  \"medida_abreviada\": \"kg\",\n" +
                "  \"total_restante\": 201,\n" +
                "  \"cantidad_estimada_por_persona\": 0.2\n" +
                "}";

        mockServer.expect(requestTo("https://backenddonaciones.onrender.com/api/inventario/stock/articulo/" + idProducto))
                .andRespond(withSuccess(mockJson, MediaType.APPLICATION_JSON));

        ProductoDto producto = inventarioService.consultarProducto(idProducto);

        assertNotNull(producto);
        assertEquals("Lentejas", producto.getNombre_articulo());
        assertEquals(201, producto.getTotal_restante());
        assertEquals("kg", producto.getMedida_abreviada());

        mockServer.verify();
    }
}

