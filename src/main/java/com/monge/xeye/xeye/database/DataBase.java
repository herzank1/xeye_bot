/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.database;

import com.monge.tbotboot.messenger.Response;
import com.monge.tbotboot.objects.Receptor;
import com.monge.xeye.xeye.objects.DBot;
import com.monge.xeye.xeye.objects.AccountType;
import com.monge.xeye.xeye.contability.BalanceAccount;
import com.monge.xeye.xeye.contability.Transacction;
import com.monge.xeye.xeye.objects.BackUpChannel;

import com.monge.xeye.xeye.objects.Xfile;
import com.monge.xeye.xeye.objects.Xuser;
import com.monge.xsqlite.xsqlite.ConnectionPoolManager;

/**
 *
 * @author DeliveryExpress
 */
public class DataBase {

    public static void init() {
        ConnectionPoolManager.addConnection("db_accounts.sqlite", Xuser.class,BackUpChannel.class,DBot.class);
        ConnectionPoolManager.addConnection("db_xdrive.sqlite", Xfile.class);
        ConnectionPoolManager.addConnection("db_contability.sqlite", BalanceAccount.class,Transacction.class);

    }

    /*Modo seguro de obtener el TelegramUser actualizando el bot o nodo del mismo*/
    public static Xuser getTelegramUser(String senderId, String botUserName) {
        Xuser read = Xuser.read(Xuser.class, senderId);

        /*verificamos si existe*/
        if (read == null) {
            BalanceAccount ba = new BalanceAccount();
            ba.create();
            read = new Xuser(senderId, botUserName, false, AccountType.USER, ba.getAccountNumber());
            read.create();
            sendWelcomeMsg(senderId,botUserName);

        } else {

            if (!read.getLastNodeBot().equals(botUserName)) {
                read.setLastNodeBot(botUserName);
                read.update();
            }

        }

        return read;

    }
    
    private static void sendWelcomeMsg(String senderId, String botUserName){
        
        Receptor receptor = new Receptor(senderId,botUserName);
        Response.sendMessage(receptor, "⭐Bienvenido al disco duro de Xeye!"
                + " eres usuario nuevo y en este momento tienes dos horas de acceso"
                + " gratis para poder acceder a todas las puliculas y series "
                + "alojadas!.", null);
    
    }

}
