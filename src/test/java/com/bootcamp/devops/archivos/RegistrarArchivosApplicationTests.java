package com.bootcamp.devops.archivos;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RegistrarArchivosApplicationTests {

    private MockMvc mockMvc;
    private Path tempDir; // Directorio temporal para las pruebas
    private String tempFilePath; // Ruta temporal para el archivo

    @BeforeEach
    public void setup() throws IOException {
        // Crear un directorio temporal
        tempDir = Files.createTempDirectory("test-data");
        tempFilePath = tempDir.resolve("archivo.txt").toString();

        // Crear instancia del controlador y configurar la ruta
        ArchivoControlador archivoControlador = new ArchivoControlador();
        archivoControlador.setRutaArchivo(tempFilePath);

        // Configurar MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(archivoControlador).build();
    }

    @Test
    public void testEscribirArchivo() throws Exception {
        // Enviar una solicitud POST para escribir en el archivo
        mockMvc.perform(post("/mensaje/write")
                        .content("Mensaje de prueba")
                        .contentType("text/plain"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Mensaje escrito en el archivo")));

        // Verificar que el archivo fue creado
        assertTrue(Files.exists(Paths.get(tempFilePath)), "El archivo debería haberse creado.");
    }

    @Test
    public void testLeerArchivo() throws Exception {
        // Escribir contenido en el archivo temporal directamente
        Files.write(Paths.get(tempFilePath), "Mensaje de prueba\n".getBytes());

        // Enviar una solicitud GET para leer el archivo
        mockMvc.perform(get("/mensaje/read"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Mensaje de prueba")));
    }

    @Test
    public void testLeerArchivoSinContenido() throws Exception {
        // Asegurarse de que el archivo no tiene contenido
        Files.deleteIfExists(Paths.get(tempFilePath));

        // Enviar una solicitud GET para leer un archivo vacío o inexistente
        mockMvc.perform(get("/mensaje/read"))
                .andExpect(status().isOk())
                .andExpect(content().string("Error al leer el archivo o el archivo está vacío."));
    }

    @AfterEach
    public void cleanup() throws IOException {
        // Limpiar el directorio temporal después de cada prueba
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}