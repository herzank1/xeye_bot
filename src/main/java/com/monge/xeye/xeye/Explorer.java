/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye;

import com.google.gson.GsonBuilder;
import com.monge.tbotboot.messenger.MessageMenu;
import com.monge.tbotboot.messenger.Response;
import com.monge.tbotboot.messenger.Xupdate;
import com.monge.tbotboot.objects.TelegramFile;
import com.monge.tbotboot.utils.RandomCaptcha;
import com.monge.xeye.xeye.contability.BalanceAccount;
import com.monge.xeye.xeye.objects.Xfile;
import com.monge.xeye.xeye.objects.Xuser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DeliveryExpress
 */
public class Explorer {

    public static final String ROOT_PATH = "XEYE:/";
    public static final String ROOT_NAME = "XEYE:";
    public static final String FOLDER_SEPARATOR = "/";

    public static Map<String, String> usersCurrentsPath = new HashMap<>();

    public static Map<String, Xfile> files = new HashMap<>();
    public static Map<String, String> hashedPath = new HashMap<>();

    private static void loadFile(Xfile f) {

        /*verificacion y creacion de directorios*/
        extractFolders(f);

        if (f.isDirectory()) {
            /*archivos son cargados con su path absoluto*/
            files.put(f.getAbsolutePath() + "/", f);
            hashedPath.put(stringToMD5(f.getAbsolutePath() + "/"), f.getAbsolutePath() + "/");
        } else {
            /*archivos son cargados con su path absoluto*/
            files.put(f.getAbsolutePath(), f);
            hashedPath.put(stringToMD5(f.getAbsolutePath()), f.getAbsolutePath());
        }

    }

    private static void extractFolders(Xfile f) {

        // Dividir la ruta en partes
        String[] partes = f.getPath().split("/");
        // Reconstruir las subrutas iterativamente
        String acumulador = "";

        for (String parte : partes) {
            if (!parte.isEmpty()) { // Ignorar elementos vacíos
                if (acumulador.isEmpty()) {
                    acumulador = parte;
                } else {
                    acumulador = acumulador + "/" + parte;
                }

                if (!files.containsKey(acumulador + "/")) {

                    /*creamos la carpeta o subcarpeta*/
                    Xfile telegramFile = new Xfile(acumulador + "/");

                    if (!telegramFile.getFileName().equals(ROOT_NAME)) {
                        files.put(acumulador + "/", telegramFile);
                        hashedPath.put(stringToMD5(acumulador + "/"), acumulador + "/");
                    }

                }

            }
        }

    }

    /*cargamos archivos de la base de datos*/
    public static void init() {
        List<Xfile> readAll = Xfile.readAll(Xfile.class);
        for (Xfile f : readAll) {

            loadFile(f);

        }

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(hashedPath));

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(files));

    }

    /**
     * *
     *
     * @param ruta
     * @return la ruta padre /directorio/casa -> /directorio
     */
    private static String getUpLevel(String ruta) {

        try {// Eliminar el último '/' si existe
            if (ruta.endsWith("/")) {
                ruta = ruta.substring(0, ruta.length() - 1);
            }

            // Obtener la parte antes del último '/'
            int lastSlashIndex = ruta.lastIndexOf('/');
            String rutaSuperior = ruta.substring(0, lastSlashIndex + 1); // +1 para incluir el '/'

            return rutaSuperior;
        } catch (Exception e) {
            return ROOT_PATH;
        }

    }

    /**
     * *
     *
     * @param path
     * @return la lista de archivos y carpetas del directorio
     */
    public static ArrayList<Xfile> readDirectory(String path) {

        System.out.println("Readind directory -> " + path);
        ArrayList<Xfile> result = new ArrayList<>();
        for (Map.Entry<String, Xfile> entry : files.entrySet()) {

            if (entry.getValue().getPath().equals(path)) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    /**
     * *
     *
     * @param path
     * @return hashed path of this path
     */
    private static String getHash(String path) {

        String hash = null;
        // Itera sobre el HashMap y encuentra los archivos que pertenecen al directorio actual
        for (Map.Entry<String, String> entry : hashedPath.entrySet()) {
            if (entry.getValue().equals(path)) {
                hash = entry.getKey();
            }

        }
        return hash;
    }

    /**
     * *
     *
     * @param input
     * @return MD5 de un String
     */
    public static String stringToMD5(String input) {
        try {
            // Obtiene una instancia del algoritmo MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Convierte el String de entrada en un arreglo de bytes
            byte[] messageDigest = md.digest(input.getBytes());

            // Convierte el arreglo de bytes a formato hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular el hash MD5", e);
        }
    }

    /**
     * *
     *
     * @param userId
     * @return la ruta actual de un usuario
     */
    public static String getUserCurrentPath(String userId) {

        if (!usersCurrentsPath.containsKey(userId)) {
            usersCurrentsPath.put(userId, ROOT_PATH);
        }

        return usersCurrentsPath.get(userId);

    }

    public static void getMenu(Xupdate xupdate) {

        try {
            String userCurrentPath = getUserCurrentPath(xupdate.getSenderId());

            System.out.println("userCurrentPath: " + userCurrentPath);

            MessageMenu menu = new MessageMenu();
            ArrayList<Xfile> readDirectory = readDirectory(userCurrentPath);

            menu.addButton("📂✴", "/newfolder", false);
            menu.addButton("♻ ", "/menu", true);

            if (userCurrentPath != ROOT_PATH) {
                /*Boton de subir nivel*/
                String upLevel = getUpLevel(userCurrentPath);
                System.out.println("upLevel:" + upLevel);
                String hash = getHash(upLevel);
                menu.addButton("⤴ Arriba", "/dir&" + hash, true);
            }

            if (readDirectory.isEmpty()) {

                menu.addButton("📂 " + "Vacio", "/dir&", true);

            } else {

                for (Xfile f : readDirectory) {

                    String hash = null;
                    if (f.isDirectory()) {
                        hash = getHash(f.getAbsolutePath() + "/");
                        menu.addButton("📂 " + f.getFileName(), "/dir&" + hash, true);
                    } else {
                        hash = getHash(f.getAbsolutePath());
                        menu.addButton("💾 " + f.getFileName(), "/file&" + hash, true);
                    }

                }
            }
            
                Xuser xuser = new Xuser(xupdate.getTelegramUser());


            BalanceAccount balance = xuser.getBalance();

            Response.editMessage(xupdate.getTelegramUser(), xupdate.getMessageId(),
                    "Creditos:" + balance.getBalance() + "\n" + userCurrentPath,
                    menu);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getPreviewFile(Xupdate xupdate) {

        String hashedFile = xupdate.getCommand().getParam(1);
        String filepath = hashedPath.get(hashedFile);
        Xfile file = files.get(filepath);

        MessageMenu menu = new MessageMenu();
        menu.addButton("⬅", "/menu", true);
        RandomCaptcha rc = new RandomCaptcha(hashedFile);
        rc.setMenu(menu);

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(file));
        
        String text =  file.getFileName()
                + "\n" + file.getDetails()
                + "\n -1 creditos."
                + "\n" + rc.getCaptchaMessage();
        
        if (file.hasPreview(xupdate.getBotUserName())) {

            Response.editMediaMessage(xupdate.getTelegramUser(), xupdate.getMessageId(),file.getAsTelegramFile(xupdate.getBotUserName()) ,text, menu);
        } else {
            Response.editMessage(xupdate.getTelegramUser(), xupdate.getMessageId(), text, menu);
        }

        

    }

    public static void getFile(Xupdate xupdate) {
        String hashedFile = xupdate.getCommand().getParam(1);
        String filepath = hashedPath.get(hashedFile);
        Xfile file = files.get(filepath);
        
        Xuser xuser = new Xuser(xupdate.getTelegramUser());

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(file));

        /*charge credits*/
        BalanceAccount balance = xuser.getBalance();
        
        

        if (balance.getBalance() > 0) {
            Response.deleteMessage(xupdate);
            
            TelegramFile asTelegramFile = file.getAsTelegramFile(xupdate.getBotUserName());
            Response.sendFile(xupdate.getTelegramUser(), asTelegramFile, asTelegramFile.getFileName()
                    + "\n" + file.getId(), null);
            balance.setBalance(balance.getBalance() - 1);
            balance.update();
        } else {

            Response.sendMessage(xupdate.getTelegramUser(), "Se agotaron tus creditos!", null);
        }

    }

    /**
     * *
     *
     * @param xupdate
     * @param name
     */
    public static void createFolder(Xupdate xupdate, String name) {

        String userCurrentPath = getUserCurrentPath(xupdate.getSenderId());
        Xfile folder = new Xfile(userCurrentPath + name);
        folder.create();

        loadFile(folder);

    }

}
