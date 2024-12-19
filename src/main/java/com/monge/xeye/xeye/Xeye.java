/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.monge.xeye.xeye;

import com.monge.tbotboot.messenger.Bot;
import com.monge.tbotboot.messenger.BotsHandler;
import com.monge.tbotboot.messenger.SystemSecurity;
import com.monge.xeye.xeye.objects.DBot;
import com.monge.xeye.explorer.DriveExplorer;
import com.monge.xeye.xeye.database.DataBase;
import com.monge.xeye.xeye.telegram.UsersHandlers;
import com.monge.xeye.xeye.utils.Settings;

/**
 *
 * @author DeliveryExpress
 */
public class Xeye {

    public static  String botName;
    public static void main(String[] args) {

        Settings.cargar();
        botName = Settings.getString("bot", "@xeye_1_bot");
       
        /*Cargamos variables*/

 /*Iniciamos la base de datos*/
        DataBase.init();

        DriveExplorer.init();
        //DriveExplorer.initGui();

        SystemSecurity.setMAX_REQUEST_COUNTER(1000);

        /*Cargamos los bots e iniciamos el mensajero executor*/
        DBot bot = DBot.read(DBot.class, botName);

//        BotsHandler.init(new UsersHandlers(), DBot.readAll(DBot.class)
//                .stream()
//                .map(DBot::getBot)
//                .toArray(Bot[]::new));
        BotsHandler.init(new UsersHandlers(), bot.getBot());

        /*activamos el respaldador*/
        // BackUpper.init();
        /*
        String path = "XEYE:/Anime/";
        String moveToDirectorie = "XEYE:/Prime/";
        List<VirtualFile> readAllDirectory = DriveExplorer.readAllDirectory(path);
        for (VirtualFile v : readAllDirectory) {
            System.out.println(v.getPath());

            System.out.println(moveTo(path,v.getPath(),moveToDirectorie));

        }
         */
    }

}
