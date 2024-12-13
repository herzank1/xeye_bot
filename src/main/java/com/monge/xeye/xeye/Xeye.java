/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.monge.xeye.xeye;

import com.monge.tbotboot.messenger.Bot;
import com.monge.tbotboot.messenger.BotsHandler;
import com.monge.tbotboot.messenger.SystemSecurity;
import com.monge.xeye.xeye.BackUpper;
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


    public static void main(String[] args) {
        
       
        
       Settings.cargar();
        /*Cargamos variables*/
     
        /*Iniciamos la base de datos*/
        DataBase.init();
        
        DriveExplorer.init();
        
        SystemSecurity.setMAX_REQUEST_COUNTER(1000);
        
        
      
        /*Cargamos los bots e iniciamos el mensajero executor*/
        
      
         BotsHandler.init(new UsersHandlers(), DBot.readAll(DBot.class)
                    .stream()
                    .map(DBot::getBot)
                    .toArray(Bot[]::new));
       
        /*activamos el respaldador*/
        // BackUpper.init();
        
        
    }
}
