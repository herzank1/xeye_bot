package com.monge.xeye.explorer;

import com.monge.xeye.xeye.objects.Xfile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Clase que simula un explorador de archivos manipulando una base de datos. Los
 * "paths" son representaciones lógicas y no rutas reales del disco duro. Xfile
 * tambien representan carpetas
 */
public class DriveExplorer {
    
    public static final String ROOT_PATH = "XEYE:/";
    public static final String FOLDER_SEPARATOR = "/";

    /**
     * Cache de rutas actuales por usuario (userId -> path).
     */
    public static Map<String, String> usersCurrentsPath = new HashMap<>();

    /**
     * Cache de archivos por ruta absoluta (path -> Xfile).
     */
    public static List<VirtualFile> files = new ArrayList<>();

    /**
     * Inicializa la estructura cargando los archivos desde la base de datos.
     */
    public static void init() {
        List<Xfile> readAll = Xfile.readAll(Xfile.class);
        for (Xfile file : readAll) {
            VirtualFile ve = new VirtualFile(file);
            processFile(ve);
        }
        
        for (VirtualFile f : files) {
       //     System.out.println(f.getPath() + " -> " + f.getHash());
        }

        //  System.out.println("Archivos y directorios cargados " + readAll.size());
    }
    
    private static void processFile(VirtualFile ve) {
        
        if (!virtualFileExist(ve.getPath())) {
            files.add(ve);
            // System.out.println("File loaded -> " + ve.getPath() + " - hash -> " + ve.getHash());

        }
        
        ArrayList<String> extractFolders = SharedUtilities.extractFolders(ve.getParent());

        //   System.out.println("extractFolders " + extractFolders);
        for (String path : extractFolders) {
            if (!directoryExist(path)) {
                
                VirtualFile folder = new VirtualFile(path);
                files.add(folder);
                //   System.out.println("Folder added -> " + folder.getPath() + " - hash -> " + folder.getHash());

            }
            
        }
        
    }
    
    public static VirtualFile getRoot() {
        return getVirtualDirectoryByPath(ROOT_PATH);
    }
    
    public static boolean directoryExist(String path) {
        
        if (!SharedUtilities.isDirectory(path)) {
            throw new java.lang.IllegalArgumentException("Path invalido!");
        }
        return getVirtualDirectoryByPath(path) != null;
    }
    
    public static VirtualFile getVirtualDirectoryByPath(String path) {
        VirtualFile get = getVirtualFileByPath(path);
        if (get != null && get.isDirectory()) {
            return get;
        } else {
            return null;
        }
        
    }
    
    public static VirtualFile getVirtualDirectoryByHash(String path) {
        VirtualFile get = getVirtualFileByHash(path);
        if (get != null && get.isDirectory()) {
            return get;
        } else {
            return null;
        }
        
    }
    
    public static boolean virtualFileExist(String path) {
        return getVirtualFileByPath(path) != null;
    }
    
    
    /***
     * Regresa el VirtualFile de Xfile por id
     * @param id
     * @return 
     */
    public static VirtualFile getVirtualFileByXid(String id) {
        return files.stream().filter(c -> c.getXfile().getId().equals(id)).findFirst().orElse(null);
    }
    
    public static VirtualFile getVirtualFileByPath(String path) {
        return files.stream().filter(c -> c.getPath().equals(path)).findFirst().orElse(null);
    }
    
    public static VirtualFile getVirtualFileByHash(String path) {
        return files.stream().filter(c -> c.getHash().equals(path)).findFirst().orElse(null);
    }
    
    public static List<VirtualFile> readDirectory(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("El path no puede ser nulo o vacío.");
        }

        // Normalizar el path para que termine con "/"
        if (!path.endsWith("/")) {
            path = path + "/";
        }

        // Colección temporal para almacenar resultados
        List<VirtualFile> result = new ArrayList<>();

        // Iterar sobre la lista de archivos
        for (VirtualFile file : files) {
            if (file.getParent().equals(path) && !file.getPath().equals(ROOT_PATH)) {
                result.add(file);
            }
        }
        
        
    
                 files.sort(Comparator.comparing(VirtualFile::getName, DriveExplorer::naturalCompare));

       // result.sort(Comparator.comparing(VirtualFile::getName));
        
        return result;
    }

    /**
     * Asigna la ruta actual de un usuario.
     *
     * @param user El ID del usuario.
     * @param path La nueva ruta.
     */
    public static void setCurrentUserPath(String user, String path) {
        usersCurrentsPath.put(user, path);
    }

    /**
     * Obtiene la ruta actual de un usuario.
     *
     * @param userId El ID del usuario.
     * @return La ruta actual del usuario.
     */
    public static String getUserCurrentPath(String userId) {
        return usersCurrentsPath.computeIfAbsent(userId, k -> ROOT_PATH);
    }

    /**
     * Crea una nueva carpeta en una ruta.
     *
     * @param path La ruta donde se creará la carpeta.
     * @param name El nombre de la carpeta.
     */
    public static VirtualFile createFolder(String path, String name) {
        
        String createPath = SharedUtilities.createPath(path, name);
        Xfile xfile = new Xfile(createPath);
        xfile.create();
        
        VirtualFile ve = new VirtualFile(createPath);
        
        processFile(ve);
        
        return ve;
    }

    /**
     * Recarga los datos desde la base de datos.
     */
    public static void reload() {
        init();
    }
    
    public static void moveFileTo(Xfile xfile, String newPath) {

        // Verificar si la nueva ruta existe y es válida
        if (!directoryExist(newPath)) {
            throw new IllegalArgumentException("La nueva ruta no es válida o no existe.");
        }

        // Actualizar el Xfile con la nueva ruta (sin modificar el nombre del archivo)
        xfile.setPath(newPath);
        xfile.update();
        
    }
    
          // Método para comparación natural
    public static int naturalCompare(String s1, String s2) {
        Pattern pattern = Pattern.compile("(\\d+|\\D+)"); // Divide en números y no números
        Matcher m1 = pattern.matcher(s1);
        Matcher m2 = pattern.matcher(s2);

        while (m1.find() && m2.find()) {
            String part1 = m1.group();
            String part2 = m2.group();

            // Compara partes numéricas como números
            if (part1.matches("\\d+") && part2.matches("\\d+")) {
                int num1 = Integer.parseInt(part1);
                int num2 = Integer.parseInt(part2);
                if (num1 != num2) {
                    return Integer.compare(num1, num2);
                }
            } else {
                // Compara partes no numéricas lexicográficamente
                int cmp = part1.compareTo(part2);
                if (cmp != 0) {
                    return cmp;
                }
            }
        }

        // Si una cadena tiene más partes que la otra
        return Boolean.compare(m1.find(), m2.find());
    }
    
}
