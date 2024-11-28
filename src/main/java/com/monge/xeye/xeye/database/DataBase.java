/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.database;

import com.monge.xeye.xeye.objects.AccountType;
import com.monge.xeye.xeye.contability.BalanceAccount;

import com.monge.xeye.xeye.objects.Xfile;
import com.monge.xeye.xeye.objects.Xuser;
import com.monge.xsqlite.xsqlite.ConnectionPoolManager;

/**
 *
 * @author DeliveryExpress
 */
public class DataBase{
    
    public static void init(){
    ConnectionPoolManager.addConnection("", Xfile.class);
 
    }

    
    /*Modo seguro de obtener el TelegramUser actualizando el bot o nodo del mismo*/
    public static Xuser getTelegramUser(String senderId, String botUserName) {
        Xuser read = Xuser.read(Xuser.class, senderId);

        /*verificamos si existe*/
        if (read == null) {
            BalanceAccount ba=new BalanceAccount();
            ba.create();
            read = new Xuser(senderId, botUserName,false, AccountType.USER, ba.getAccountNumber());
            read.create();

        } else {

            if (!read.getLastNodeBot().equals(botUserName)) {
                read.setLastNodeBot(botUserName);
                read.update();
            }

        }

        return read;

    }
    
    
   
}
