/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.objects;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.monge.tbotboot.objects.FileType;
import com.monge.tbotboot.objects.TelegramFile;
import com.monge.xeye.explorer.DriveExplorer;
import com.monge.xeye.explorer.SharedUtilities;
import com.monge.xeye.xeye.utils.Utils;
import com.monge.xsqlite.xsqlite.BaseDao;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author DeliveryExpress Los archivos de telegram generan un id y diferente
 * cuando se envia a otro usuario o Bot es necesario que bots y que id esta
 * relacionado con el archivo.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Xfile extends BaseDao {

    @DatabaseField(id = true)
    String id;

    @DatabaseField
    String fileName;

    @DatabaseField
    String details;

    @DatabaseField
    String type;

    /*preview img telegram file Id*/
    @DatabaseField
    String previewsMirrors;

    /*ruta digital en el servidor, sin nombre del archivo*/
    @DatabaseField
    String path;
    /*Enlaces o bots que contienen el archivo*/
    @DatabaseField
    String fileMirrors;

    public Xfile() {
        this.id = Utils.generateXeyeUIDD();
        this.fileMirrors = new Gson().toJson(new HashMap<>());
        this.previewsMirrors = new Gson().toJson(new HashMap<>());
    }

    /**
     * *
     *
     * @param document
     * @param mirror
     */
    public Xfile(TelegramFile file) {
        this.id = Utils.generateXeyeUIDD();
        this.fileName = normalizeName(file.getFileName()) + "." + file.getType().toLowerCase();
        this.type = file.getType();
        addFileMirror(file.getFileId(), file.getBot());
        /*root por default*/
        this.path = SharedUtilities.createPath(DriveExplorer.ROOT_PATH, fileName);
        this.previewsMirrors = new Gson().toJson(new HashMap<>());

    }

    /**
     * *
     *
     * @param document
     * @param mirror
     */
    public Xfile(TelegramFile file, String path) {
        this.id = Utils.generateXeyeUIDD();
        this.fileName = normalizeName(file.getFileName()) + "." + file.getType().toLowerCase();
        this.type = file.getType();
        addFileMirror(file.getFileId(), file.getBot());
        /*root por default*/
        this.path = SharedUtilities.createPath(path, fileName);
        this.previewsMirrors = new Gson().toJson(new HashMap<>());

    }

    /**
     * *
     * use this to create directory
     *
     * @param path
     */
    public Xfile(String path) {
        this.id = Utils.generateXeyeUIDD();
        this.type = FileType.FOLDER;
        this.path = path;
        this.fileName = SharedUtilities.getName(path);

    }

    public void addFileMirror(String fileId, String mirror) {
        HashMap<String, String> fileMirrors1 = getFileMirrors();
        if (fileMirrors1 == null) {
            fileMirrors1 = new HashMap<>();
        }

        fileMirrors1.put(mirror, fileId);
        setFileMirrors(fileMirrors1);
    }

    public void addPrewMirror(String fileId, String mirror) {
        HashMap<String, String> prevMirrors1 = getPrevMirrors();
        if (prevMirrors1 == null) {
            prevMirrors1 = new HashMap<>();
        }

        prevMirrors1.put(mirror, fileId);
        setPrevMirrors(prevMirrors1);
    }

    public HashMap<String, String> getFileMirrors() {

        Gson gson = new Gson();

        return gson.fromJson(fileMirrors, HashMap.class);
    }

    public void setFileMirrors(HashMap<String, String> hashMap) {
        Gson gson = new Gson();
        this.fileMirrors = gson.toJson(hashMap);
    }

    public HashMap<String, String> getPrevMirrors() {

        Gson gson = new Gson();

        return gson.fromJson(previewsMirrors, HashMap.class);
    }

    public void setPrevMirrors(HashMap<String, String> hashMap) {
        Gson gson = new Gson();

        this.previewsMirrors = gson.toJson(hashMap);
    }

    public void removePreview(String botUserNname) {

        try {
            HashMap<String, String> prevMirrors = this.getPrevMirrors();
            prevMirrors.remove(botUserNname);
            this.setPrevMirrors(prevMirrors);
            this.update();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    
     public void removefileMirror(String botUserNname) {

        try {
            HashMap<String, String> fileMirrors = this.getFileMirrors();
            fileMirrors.remove(botUserNname);
            this.setFileMirrors(fileMirrors);
            this.update();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    
    
    

    /**
     * *
     *
     * @param botUserName
     * @return file Id of specific bot
     */
    public String getFileId(String botUserName) {
        return getFileMirrors().get(botUserName);
    }

    public String getPreview(String botUserName) {
        return getPrevMirrors().get(botUserName);

    }

    public boolean hasPreview(String botUserName) {
        try {
            return getPreview(botUserName) != null;
        } catch (java.lang.NullPointerException e) {
            return false;
        }

    }

    /**
     * *
     *
     * @param botUserName
     * @return true if Bot has the file Id of this File
     */
    public boolean existFileId(String botUserName) {
        return getFileId(botUserName) != null;
    }

    public boolean isDirectory() {
        return this.type.equals(FileType.FOLDER);

    }

    public TelegramFile getPreviewAsTelegramFile(String botUserName) {
        String previewId = getPreview(botUserName);

        TelegramFile tf = new TelegramFile();
        tf.setFileId(previewId);
        tf.setBot(botUserName);
        tf.setFileName("prev_" + this.fileName);
        tf.setType(FileType.IMAGE);
        tf.setDetails("preview");

        return tf;
    }

    public TelegramFile getAsTelegramFile(String botUserName) {
        TelegramFile tf = new TelegramFile();
        tf.setBot(botUserName);
        tf.setFileName(this.fileName);
        tf.setType(this.type);
        tf.setDetails(this.details);
        tf.setFileId(this.getFileId(botUserName));
        return tf;
    }

    public String getData() {
        return "[name]: " + this.fileName + "\n"
                + "[id]: " + this.id + "\n"
                + "[details]: " + this.details + "\n"
                + "Available on -> \n"
                + getBotsMirrors();
    }

    private String getBotsMirrors() {
        String mirrors = "";
        HashMap<String, String> fileMirrors1 = this.getFileMirrors();
        for (Map.Entry<String, String> entry : fileMirrors1.entrySet()) {
            mirrors += "🎬 " + entry.getKey() + "\n";
        }

        return mirrors;

    }

    private String normalizeName(String input) {
        // Reemplaza los caracteres "." y "/"
        String normalized = input.replaceAll("[./]", "");

        // Divide el texto en palabras utilizando el espacio como delimitador
        String[] words = normalized.split("\\s+");

        // Limita a un máximo de 5 palabras
        if (words.length > 5) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                result.append(words[i]).append(" ");
            }
            // Elimina el último espacio extra
            return result.toString().trim();
        }

        // Si ya tiene 5 o menos palabras, simplemente las une de nuevo
        return normalized;
    }

    public void updatePath(String path) {
        if(SharedUtilities.isValidPath(path)){
            this.path = SharedUtilities.createPath(path, fileName);
            
        }
   
    }

}
