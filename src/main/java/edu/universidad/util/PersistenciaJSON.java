package edu.universidad.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Sistema de persistencia CORREGIDO usando archivos JSON
 * SOLUCIONA: Creación automática de carpetas y archivos
 */
public class PersistenciaJSON {

    private static final String CARPETA_DATOS = "datos";
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Configurar el mapper
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // ✅ IGNORAR CAMPOS DESCONOCIDOS (COMPATIBILIDAD CON JSONs VIEJOS)
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // ✅ CREAR CARPETA SI NO EXISTE (CORREGIDO)
        crearCarpetaDatos();
    }

    /**
     * ✅ NUEVO: Crea la carpeta datos si no existe
     */
    private static void crearCarpetaDatos() {
        try {
            Path path = Paths.get(CARPETA_DATOS);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("✅ Carpeta '" + CARPETA_DATOS + "' creada exitosamente");
            }
        } catch (IOException e) {
            System.err.println("❌ Error al crear carpeta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * ✅ MEJORADO: Guarda una lista de objetos en un archivo JSON
     */
    public static <T> boolean guardar(String archivo, List<T> datos) {
        try {
            // Asegurar que la carpeta existe
            crearCarpetaDatos();

            File file = new File(CARPETA_DATOS, archivo);

            // Crear el archivo si no existe
            if (!file.exists()) {
                file.createNewFile();
            }

            mapper.writeValue(file, datos);
            System.out.println("✅ Guardado: " + archivo + " (" + datos.size() + " elementos)");
            return true;
        } catch (IOException e) {
            System.err.println("❌ Error al guardar " + archivo + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ✅ MEJORADO: Guarda un mapa en un archivo JSON
     */
    public static <K, V> boolean guardarMapa(String archivo, Map<K, V> datos) {
        try {
            crearCarpetaDatos();

            File file = new File(CARPETA_DATOS, archivo);

            if (!file.exists()) {
                file.createNewFile();
            }

            mapper.writeValue(file, datos);
            System.out.println("✅ Guardado: " + archivo + " (" + datos.size() + " elementos)");
            return true;
        } catch (IOException e) {
            System.err.println("❌ Error al guardar " + archivo + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ✅ MEJORADO: Carga una lista de objetos desde un archivo JSON
     */
    public static <T> List<T> cargarLista(String archivo, Class<T> clase) {
        try {
            crearCarpetaDatos();

            File file = new File(CARPETA_DATOS, archivo);

            if (!file.exists()) {
                System.out.println("ℹ️ Archivo no existe: " + archivo + " (creando lista vacía)");
                // ✅ CREAR ARCHIVO VACÍO
                guardar(archivo, new ArrayList<>());
                return new ArrayList<>();
            }

            // Verificar si el archivo está vacío
            if (file.length() == 0) {
                System.out.println("ℹ️ Archivo vacío: " + archivo);
                return new ArrayList<>();
            }

            T[] array = (T[]) mapper.readValue(file,
                    mapper.getTypeFactory().constructArrayType(clase));

            List<T> lista = new ArrayList<>(Arrays.asList(array));
            System.out.println("✅ Cargado: " + archivo + " (" + lista.size() + " elementos)");
            return lista;

        } catch (IOException e) {
            System.err.println("❌ Error al cargar " + archivo + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * ✅ MEJORADO: Carga un mapa desde un archivo JSON
     */
    public static <K, V> Map<K, V> cargarMapa(String archivo, Class<K> claseKey, Class<V> claseValue) {
        try {
            crearCarpetaDatos();

            File file = new File(CARPETA_DATOS, archivo);

            if (!file.exists()) {
                System.out.println("ℹ️ Archivo no existe: " + archivo + " (creando mapa vacío)");
                guardarMapa(archivo, new HashMap<>());
                return new HashMap<>();
            }

            if (file.length() == 0) {
                System.out.println("ℹ️ Archivo vacío: " + archivo);
                return new HashMap<>();
            }

            Map<K, V> mapa = mapper.readValue(file,
                    mapper.getTypeFactory().constructMapType(HashMap.class, claseKey, claseValue));

            System.out.println("✅ Cargado: " + archivo + " (" + mapa.size() + " elementos)");
            return mapa;

        } catch (IOException e) {
            System.err.println("❌ Error al cargar " + archivo + ": " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
     * ✅ NUEVO: Verifica si un archivo existe
     */
    public static boolean existeArchivo(String archivo) {
        File file = new File(CARPETA_DATOS, archivo);
        return file.exists();
    }

    /**
     * ✅ NUEVO: Elimina un archivo
     */
    public static boolean eliminarArchivo(String archivo) {
        try {
            File file = new File(CARPETA_DATOS, archivo);
            if (file.exists()) {
                boolean eliminado = file.delete();
                if (eliminado) {
                    System.out.println("✅ Archivo eliminado: " + archivo);
                }
                return eliminado;
            }
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar " + archivo + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * ✅ NUEVO: Lista todos los archivos JSON en la carpeta
     */
    public static List<String> listarArchivos() {
        List<String> archivos = new ArrayList<>();
        File carpeta = new File(CARPETA_DATOS);

        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] files = carpeta.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    archivos.add(file.getName());
                }
            }
        }

        return archivos;
    }

    /**
     * ✅ NUEVO: Obtiene información de los archivos
     */
    public static void mostrarEstadisticas() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   ESTADÍSTICAS DE PERSISTENCIA         ║");
        System.out.println("╚════════════════════════════════════════╝");

        File carpeta = new File(CARPETA_DATOS);

        if (!carpeta.exists()) {
            System.out.println("⚠️ La carpeta de datos no existe");
            return;
        }

        System.out.println("📂 Ubicación: " + carpeta.getAbsolutePath());
        System.out.println("📊 Archivos encontrados:");

        List<String> archivos = listarArchivos();

        if (archivos.isEmpty()) {
            System.out.println("   (vacío)");
        } else {
            for (String archivo : archivos) {
                File file = new File(carpeta, archivo);
                long tamaño = file.length();
                System.out.println(String.format("   • %s (%.2f KB)", archivo, tamaño / 1024.0));
            }
        }

        System.out.println("═══════════════════════════════════════════\n");
    }
}