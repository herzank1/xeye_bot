/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.telegram;

import com.google.gson.GsonBuilder;
import com.monge.tbotboot.commands.Command;
import com.monge.tbotboot.messenger.Response;
import com.monge.tbotboot.messenger.Xupdate;
import com.monge.tbotboot.quizes.QuizesControl;
import com.monge.xeye.xeye.objects.BackUpChannel;
import com.monge.xeye.xeye.objects.Xuser;
import com.monge.xeye.xeye.quizes.UserGUI;

/**
 *
 * @author DeliveryExpress
 */
class GroupController {

    public static void execute(Xupdate xupdate) {

        Command command = xupdate.getCommand();
        switch (command.command()) {

            case "/start":
            case "/menu":

                String groupId = xupdate.getFromId();
                Response.sendMessage(xupdate.getTelegramGroup(), "Group ID: " + groupId, null);

                break;

            case "/delete_msg":
                Response.deleteGroupMessage(xupdate);

                break;

            /*use /setrespaldo&nombredel canal*/
            case "/setrespaldo":
            case "/setnewbk":
            case "/respaldo":

                BackUpChannel newBk = new BackUpChannel();
                newBk.setId(xupdate.getFromId());
                newBk.setName(command.getParam(1));
                newBk.setDescription("Backup");
                newBk.create();

                break;
        }

    }

}
