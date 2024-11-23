package com.bootcamp.devops.archivos;

import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/mensaje")
public class ArchivoControlador {

    private static final String RUTA_ARCHIVO = "/data/archivo.txt";
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/write")
    public String escribirArchivo(@RequestBody String mensaje) {
        String mensajeConFecha = String.format("[%s] %s", LocalDateTime.now().format(FORMATO_FECHA_HORA), mensaje);

        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO, true)) {
            writer.write(mensajeConFecha + "\n");
            return "Mensaje escrito en el archivo: " + mensajeConFecha;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al escribir en el archivo.";
        }
    }

    @GetMapping("/read")
    public String leerArchivo() {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
            return contenido.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al leer el archivo o el archivo está vacío.";
        }
    }
}
