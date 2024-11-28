/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.monge.xeye.xeye;

import com.monge.tbotboot.messenger.BotsHandler;
import com.monge.xeye.xeye.database.DataBase;
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
        
        /*activamos el respaldador*/
         BackUpper.init();
        
        
    }
}
