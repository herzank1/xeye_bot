/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.explorer;

import com.monge.tbotboot.objects.FileType;
import static com.monge.xeye.explorer.DriveExplorer.ROOT_PATH;
import com.monge.xeye.xeye.objects.Xfile;
import com.monge.xeye.xeye.utils.Utils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.Data;

/**
 *
 * @author DeliveryExpress
 */
public class VirtualFile {

    String path;
    String hash;
    Xfile xfile;

    public VirtualFile(Xfile xfile) {

        if (xfile == null) {
            throw new java.lang.NullPointerException("xfile no puede ser nullo en este cosntructor");
        }

        if (xfile.getType().equals(FileType.FOLDER)) {
            this.path = xfile.getPath();
        } else {
            this.xfile = xfile;
            this.path = xfile.getPath();
        }

        /*Generamos el hash*/
        this.getHash();

    }

    /**
     * *
     *
     * @param path
     * @param xfile si es nullo sera un directorio
     */
    public VirtualFile(String path) {
        if (path == null) {
            throw new java.lang.NullPointerException("path no puede ser nullo en este cosntructor");
        }

        if (!SharedUtilities.isDirectory(path)) {
            throw new java.lang.NullPointerException("Este es un path invalido, debe terinar con /");
        }
        this.path = path;
        this.getHash();
    }

    /**
     * Obtiene el directorio padre del path actual.
     *
     * @return La ruta del directorio padre, o null si no tiene.
     */
    public String getParent() {

        return SharedUtilities.getParent(path);
    }
    
    
    
    public VirtualFile getParentVf(){
        return DriveExplorer.getVirtualDirectoryByPath(getParent());
    }

    /**
     * Verifica si hay un nivel superior a la ruta actual de un usuario.
     *
     * @param user El ID del usuario.
     * @return true si hay un nivel superior, false si está en la raíz.
     */
    public boolean hasUpLevel() {
        return !ROOT_PATH.equals(getParent());
    }
    
    public boolean isRootFolder(){
    return ROOT_PATH.equals(this.path);
    }

    public boolean isDirectoy() {
        return xfile == null || path.endsWith("/");
    }

    public String getHash() {
        if (hash == null || hash.isEmpty()) {
            hash = SharedUtilities.stringToMD5(this.path);
        }
        return hash;

    }

    public Xfile getXfile() {
        return this.xfile;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDirectory() {
        return this.xfile == null || this.path.endsWith("/");
    }

    public String getName() {
       return SharedUtilities.getName(path);
    }

}
