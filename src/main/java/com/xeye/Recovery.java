/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.xeye;

import java.io.File;
import java.nio.file.Path;

/**
 *
 * @author DeliveryExpress
 */
public class Recovery {
    public static void main(String args[]){
        
       // Ruta absoluta de la carpeta donde están los archivos
        String directoryPath = "C:\\Users\\DeliveryExpress\\Documents\\NetBeansProjects\\xeye\\src\\main\\java\\com\\xeye";
        

        String newExtension = ".java";
        
       
        changeFileExtensions(directoryPath, newExtension);
    
    }
    
        public static void changeFileExtensions(String directoryPath , String newExtension) {
        // Obtén el directorio
        File directory = new File(directoryPath);
        
        // Verifica si es un directorio y existe
        if (directory.exists() && directory.isDirectory()) {
            // Obtén todos los archivos en el directorio
            File[] files = directory.listFiles();
            
            // Verifica si el directorio tiene archivos
            if (files != null) {
                for (File file : files) {
                    // Verifica si es un archivo y si tiene la extensión que buscamos
                    if (file.isFile()) {
                        // Genera el nuevo nombre de archivo con la nueva extensión
                        String newName = file.getName() + newExtension;
                        File newFile = new File(directory, newName);
                        
                        // Cambia el nombre del archivo
                        boolean renamed = file.renameTo(newFile);
                        
                        if (renamed) {
                            System.out.println("Archivo renombrado: " + file.getName() + " -> " + newName);
                        } else {
                            System.out.println("No se pudo renombrar el archivo: " + file.getName());
                        }
                    }
                }
            } else {
                System.out.println("El directorio está vacío o no es accesible.");
            }
        } else {
            System.out.println("El directorio no existe o no es válido.");
        }
    }
    
}
