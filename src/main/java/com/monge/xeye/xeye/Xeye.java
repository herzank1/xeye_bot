/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.monge.xeye.xeye;

import com.google.gson.GsonBuilder;
import com.monge.xeye.xeye.database.DataBase;
import com.monge.xeye.xeye.objects.BackUpChannel;
import com.monge.xeye.xeye.telegram.BotsHandler;
import com.monge.xeye.xeye.telegram.Methods;
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
        
        Explorer.init();
        /*Cargamos los bots e iniciamos el mensajero executor*/
        BotsHandler.init();
        /*activamos el sistema de seguridad contra ataques de fuerza bruta*/
        SystemSecurity.init();
        
        /*activamos el respaldador*/
         BackUpper.init();
        
        
    }
}
