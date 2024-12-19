/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye;

import com.monge.xeye.xeye.objects.BackUpChannel;
import java.util.ArrayList;

/**
 *
 * @author DeliveryExpress Respaldo y solicitu de archivos para otros bots Esta
 * clase envia los archivos que no contengan los bots en los mirrors los envia a
 * los canales de respaldo
 */
public class BackUpper {

    public static void init() {
        /*obtenemos los canales de respaldo*/
        ArrayList<BackUpChannel> bkChannels = BackUpChannel.readAll(BackUpChannel.class);
        if (bkChannels == null || bkChannels.isEmpty()) {
            return;
        }

    }

}
