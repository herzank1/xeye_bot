/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye;


import com.monge.xeye.xeye.database.DataBase;
import com.monge.xeye.xeye.objects.TelegramUser;
import com.monge.xeye.xeye.telegram.Response;
import com.monge.xeye.xeye.telegram.Xupdate;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author DeliveryExpress
 */
public class SystemSecurity {

    static Map<String, Integer> requestCounter = new HashMap<>();
    static int MAX_REQUEST_COUNTER = 3;

    public static void init() {

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                requestCounter.clear();

            }
        };

        timer.schedule(timerTask, 1000, 1000);

        System.out.println("System security Activate!");

    }

    public static boolean allowUpdate(Xupdate xupdate) {
        TelegramUser telegramUser = xupdate.getTelegramUser();
        
        /*verificamos is esta en la lista negra*/
        if(xupdate.getTelegramUser().isBlackList()){
        return false;
        }
        
        /*si proviene de un grupo*/
        if (xupdate.isGroupMessage()) {
            return true;
        }
        
        /*verificamos is expiro*/
        if(xupdate.isExpired()){
            System.out.println("update expired!");
        return false;
        }
        
        /*contador de mensajes*/

        increaseCounter(xupdate.getFromId(), 1);

        if (requestCounter.get(xupdate.getSenderId()) > MAX_REQUEST_COUNTER ) {

            telegramUser.setBlackList(true);
            DataBase.Accounts.TelegramUsers().update(telegramUser);
            Response response = new Response(telegramUser);
            response.setText("Has sido bloqueado!\n demasiados mensajes consecutivos.");
            response.execute();
            return false;
        }else{
            
            System.out.println("messages count "+requestCounter.get(xupdate.getSenderId()));
        
            return true;
        
        }

        

    }

    private static void increaseCounter(String telegramId, int qty) {

        try {
            requestCounter.put(telegramId, requestCounter.get(telegramId) + qty);
        } catch (Exception e) {
            requestCounter.put(telegramId, 0);
        }

    }

}
