/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye;

import com.monge.xeye.xeye.database.DataBase;
import com.monge.xeye.xeye.database.DbBalancer;
import com.monge.xeye.xeye.objects.BackUpChannel;
import com.monge.xeye.xeye.objects.TelegramFile;
import com.monge.xeye.xeye.telegram.Bot;
import com.monge.xeye.xeye.telegram.BotsHandler;
import com.monge.xeye.xeye.telegram.Executor;
import com.monge.xeye.xeye.telegram.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
            ArrayList<TelegramFile> files = new ArrayList<>(Explorer.files.values());
            files = files.stream().filter(c-> !c.isDirectory()).collect(Collectors.toCollection(ArrayList::new));
            
            System.out.println("Archivos cargados: "+files.size());
            
            if(files.isEmpty()){
            System.out.println("No se puede continuar sin archivos.");
            return;
            }
            
            /*Obtenemos los canales de respaldo*/
            ArrayList<BackUpChannel> channels = (ArrayList<BackUpChannel>) DataBase.Files.BackUpChannels().readAll();

            System.out.println("Canales de respaldo: "+channels.size());
            if (channels.isEmpty()) {
                System.out.println("No se puede continuar sin canales de respaldo.");
                return;
            }
            
            /**
             * Por cada archivo
             */
            for (TelegramFile file : files) {
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
                                    Response.sendFile(senderBot.getBotUsername(), bk.getId(), file, file.getFileName()
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
}

