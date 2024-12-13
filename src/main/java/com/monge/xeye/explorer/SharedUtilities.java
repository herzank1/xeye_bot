/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.explorer;

import static com.monge.xeye.explorer.DriveExplorer.ROOT_PATH;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author DeliveryExpress
 */
public class SharedUtilities {
    
    public static boolean isValidPath(String path){
         if (path == null || path.isEmpty()) {
            return false;
        }
        
        return path.startsWith(ROOT_PATH)&&path.endsWith("/");
    }

    public static boolean isDirectory(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("El path no puede ser nulo o vacío.");
        }

        // Normaliza el path reemplazando backslashes por slashes
        path = path.replace("\\", "/");

        // Verifica si el path termina con un slash, indicando que es un directorio
        if (path.endsWith("/")) {
            return true;
        }

        // Busca si hay una extensión de archivo (punto seguido de caracteres al final)
        int lastDotIndex = path.lastIndexOf(".");
        int lastSlashIndex = path.lastIndexOf("/");

        // Si hay un punto después del último slash, probablemente es un archivo
        return lastDotIndex <= lastSlashIndex || lastDotIndex == -1;
    }

    /**
     * Calcula el hash MD5 de un string.
     *
     * @param input El string de entrada.
     * @return El hash MD5 del string.
     */
    public static String stringToMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular el hash MD5", e);
        }
    }

    public static String createPath(String path, String fileName) {
        path = path.replaceAll("\\\\", "/").replaceAll("/+$", ""); // Normalizar la ruta base
        if (fileName == null || fileName.isEmpty()) {
            return path + "/"; // Ruta de carpeta
        }
        return path + "/" + fileName; // Ruta de archivo
    }

    /**
 * Obtiene el directorio padre del path actual.
 *
 * @param path La ruta de la cual obtener el directorio padre.
 * @return La ruta del directorio padre, o null si no tiene.
 */
public static String getParent(String path) {
    if (path == null || path.isEmpty()) {
        return null; // Manejo de error para entradas nulas o vacías
    }

    // Normaliza el path eliminando barras finales redundantes excepto en la raíz
    String normalizedPath = path.replaceAll("/+$", "");
    
    // Encuentra el índice del último separador
    int lastSlashIndex = normalizedPath.lastIndexOf('/');

    // Si no hay un separador o el separador está al principio, es la raíz o no tiene padre
    if (lastSlashIndex <= 0) {
        return ROOT_PATH; // Devuelve null si es raíz o no tiene padre
    }

    // Extrae el directorio padre y asegura que termine con una barra
    return normalizedPath.substring(0, lastSlashIndex) + "/";
}


    public static String getName(String path) {
        if (path == null || path.isEmpty()) {
            return null; // Manejo de errores si el path es nulo o vacío
        }

        // Reemplaza cualquier backslash con un slash para unificar los separadores
        path = path.replace("\\", "/");

        // Remueve cualquier separador al final del path para evitar nombres vacíos
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        // Encuentra el último separador
        int lastSeparatorIndex = path.lastIndexOf("/");

        // Si no se encuentra el separador, regresa el path completo
        if (lastSeparatorIndex == -1) {
            return path;
        }

        // Devuelve el segmento después del último separador
        return path.substring(lastSeparatorIndex + 1);
    }

    public static ArrayList<String> extractFolders(String path) {
        ArrayList<String> folders = new ArrayList<>();

        if (path == null || path.isEmpty()) {
            return folders; // Retorna una lista vacía si el path es nulo o vacío
        }

        // Reemplaza cualquier backslash con slash para uniformidad
        path = path.replace("\\", "/");

        // Asegúrate de que termina con un separador si no es un archivo
        if (!path.endsWith("/")) {
            path = path + "/";
        }

        // Mientras haya separadores, recorta el path y añádelo a la lista
        while (path.contains("/") && !path.equals("/")) {
            folders.add(path);

            // Elimina el último segmento del path
            int lastSlashIndex = path.lastIndexOf("/", path.length() - 2);
            path = path.substring(0, lastSlashIndex + 1);
        }

        // Añade la raíz si no está incluida
        if (!folders.contains("/") && !path.isEmpty()) {
            folders.add(path);
        }

        return folders;
    }
    
  

}
