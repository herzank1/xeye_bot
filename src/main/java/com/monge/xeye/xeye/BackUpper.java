/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye;


import com.monge.xeye.explorer.DriveExplorer;
import com.monge.tbotboot.messenger.Bot;
import com.monge.tbotboot.messenger.BotsHandler;
import com.monge.tbotboot.messenger.Response;
import com.monge.tbotboot.objects.TelegramGroup;
import static com.monge.xeye.explorer.DriveExplorer.ROOT_PATH;
import com.monge.xeye.explorer.VirtualFile;
import com.monge.xeye.xeye.objects.BackUpChannel;
import com.monge.xeye.xeye.objects.Xfile;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author DeliveryExpress Respaldo y solicitu de archivos para otros bots Esta
 * clase envia los archivos que no contengan los bots en los mirrors los envia a
 * los canales de respaldo
 */

public class BackUpper {

    public static void init() {
        // Ejecutar el método completo en un hilo separado
        new Thread(() -> {
            
            System.out.println("Iniciando el respaldador de archivos");
            /*Obtenemos los bots*/
            ArrayList<Bot> bots = BotsHandler.getBots();
            
            System.out.println("Bots Cargados: "+bots.size());
            
            if(bots.isEmpty()){
            System.out.println("No se encontro ningun bot");
            return;
            }
            
            /*Obtenemos los archivos ya cargados en el explorador y filtramos las carpetas*/
            ArrayList<Xfile> files = (ArrayList<Xfile>) readDirectory(ROOT_PATH);
            files = files.stream().filter(c-> !c.isDirectory()).collect(Collectors.toCollection(ArrayList::new));
            
            System.out.println("Archivos cargados: "+files.size());
            
            if(files.isEmpty()){
            System.out.println("No se puede continuar sin archivos.");
            return;
            }
            
            /*Obtenemos los canales de respaldo*/
            ArrayList<BackUpChannel> channels = (ArrayList<BackUpChannel>) BackUpChannel.readAll(BackUpChannel.class);

            System.out.println("Canales de respaldo: "+channels.size());
            if (channels.isEmpty()) {
                System.out.println("No se puede continuar sin canales de respaldo.");
                return;
            }
            
            /**
             * Por cada archivo
             */
            for (Xfile file : files) {
                /*Obtenemos el primer bot que sí lo tiene*/
                HashMap<String, String> fileMirrors = file.getFileMirrors();
                if (!fileMirrors.isEmpty()) {
                    Map.Entry<String, String> mirror = fileMirrors.entrySet().iterator().next();
                    Bot senderBot = BotsHandler.getBotByUserName(mirror.getKey());
                    System.out.println("sender bot del archivo "+file.getId()+" es "+senderBot.getBotUsername());
                    /*Iteramos cada bot registrado. Con solo un bot que no lo tenga, se envía una vez*/
                    requester:
                    for (Bot bot : bots) {
                        if (!file.existFileId(bot.getBotUsername())) {
                            for (BackUpChannel bk : channels) {
                                // Aquí introducimos la espera de 30 segundos
                                try {
                                    System.out.println("Respaldando archivo en "+bk.getName());
                                    TelegramGroup tg = new TelegramGroup(bk.getId(),senderBot.getBotUsername());
                                    Response.sendFile(tg,
                                            file.getAsTelegramFile(mirror.getKey()), 
                                            file.getFileName()
                                            + "\n" + file.getId(), null);
                                    
                                    
                                    Thread.sleep(30000); // Espera de 30 segundos antes de la siguiente iteración
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    System.err.println("Hilo interrumpido: " + e.getMessage());
                                    return; // Salir del método si el hilo es interrumpido
                                }
                            }
                            break requester;
                        }
                    }
                }
            }
            
              System.out.println("Proceso finalizado.");
        }).start(); // Inicia el hilo separado
    }
    
    
    
        private static ArrayList<Xfile> readDirectory(String path) {
        List<VirtualFile> all = DriveExplorer.readDirectory(path);
        List<Xfile> result = new ArrayList<>();

        for (VirtualFile file : all) {
                result.add(file.getXfile());

        }
        // Ordenar alfabéticamente por el nombre (getName)
        // Ordenar alfabéticamente por el nombre (getName)
        result.sort(Comparator.comparing(Xfile::getFileName));

        return (ArrayList<Xfile>) result;
  
    }
}

