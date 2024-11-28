/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.objects;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.monge.tbotboot.objects.FileType;
import com.monge.tbotboot.objects.TelegramFile;
import com.monge.xeye.xeye.Explorer;
import com.monge.xeye.xeye.utils.Utils;
import com.monge.xsqlite.xsqlite.BaseDao;
import java.util.HashMap;
import lombok.Data;

/**
 *
 * @author DeliveryExpress Los archivos de telegram generan un id y diferente
 * cuando se envia a otro usuario o Bot es necesario que bots y que id esta
 * relacionado con el archivo.
 */
@Data
public class Xfile extends BaseDao {

    @DatabaseField(generatedId = false)
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
        this.fileMirrors = new Gson().toJson(new HashMap<>());
        this.previewsMirrors = new Gson().toJson(new HashMap<>());
    }

    public Xfile(String ruta) {
        // Eliminar el último '/' para evitar elementos vacíos
        if (ruta.endsWith("/")) {
            ruta = ruta.substring(0, ruta.length() - 1);
        }

        // Dividir la ruta por "/"
        String[] partes = ruta.split("/");
        this.fileName = partes[partes.length - 1]; // Última parte
        this.path = String.join("/", partes).replace("/" + this.fileName, "") + "/";
        this.type = FileType.FOLDER;

        this.fileMirrors = new Gson().toJson(new HashMap<>());
        this.previewsMirrors = new Gson().toJson(new HashMap<>());

    }

    /**
     * *
     *
     * @return folder/filename
     */
    public String getAbsolutePath() {

        return this.path + this.fileName;

    }

    /**
     * *
     *
     * @param document
     * @param mirror
     */
    public Xfile(TelegramFile file) {
        this.id = Utils.generateXeyeUIDD();
        this.fileName = file.getFileName();
        this.type = file.getType();
        addFileMirror(file.getFileId(), file.getBot());
        this.path = Explorer.ROOT_PATH;

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
        setFileMirrors(prevMirrors1);
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
        return getPreview(botUserName) != null;
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

    public TelegramFile getAsTelegramFile(String botUserName) {
        TelegramFile tf = new TelegramFile();
        tf.setBot(botUserName);
        tf.setType(this.type);
        tf.setDetails(this.details);
        tf.setFileId(this.getFileId(botUserName));

        return tf;

    }

}
