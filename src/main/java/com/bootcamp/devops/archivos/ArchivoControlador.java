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

    private String rutaArchivo = "data/archivo.txt"; // Ruta por defecto

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/write")
    public String escribirArchivo(@RequestBody String mensaje) {
        try {
            // Crear el directorio si no existe
            File archivo = new File(rutaArchivo);
            archivo.getParentFile().mkdirs();

            // Escribir el mensaje con la fecha
            String mensajeConFecha = String.format("[%s] %s", LocalDateTime.now().format(FORMATO_FECHA_HORA), mensaje);
            try (FileWriter writer = new FileWriter(archivo, true)) {
                writer.write(mensajeConFecha + "\n");
                return "Mensaje escrito en el archivo: " + mensajeConFecha;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al escribir en el archivo.";
        }
    }

    @GetMapping("/read")
    public String leerArchivo() {
        try {
            return new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al leer el archivo o el archivo está vacío.";
        }
    }
}
