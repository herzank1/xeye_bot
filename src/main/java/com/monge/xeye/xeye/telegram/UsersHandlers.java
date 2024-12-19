/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.telegram;

import com.google.gson.GsonBuilder;
import com.monge.tbotboot.commands.CommandsHandlers;
import com.monge.tbotboot.messenger.Xupdate;
import com.monge.tbotboot.quizes.QuizesControl;
import com.monge.xeye.xeye.database.DataBase;
import com.monge.xeye.xeye.objects.AccountType;
import com.monge.xeye.xeye.objects.Xuser;

/**
 *
 * @author DeliveryExpress
 */
public class UsersHandlers implements CommandsHandlers {

    @Override
    public void execute(Xupdate xupdate) {
        
        XeyeUpdate xeyeUpdate = new XeyeUpdate(xupdate.getUpdate(),xupdate.getBotUserName());

   //    System.out.println("execute update" + xuser.toString());

        //System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(xupdate));
        System.out.println(xupdate.toStringDetails());

     

        if (xupdate.isGroupMessage()) {
            GroupController.execute(xupdate);
        } else {

            if (QuizesControl.hasQuiz(xupdate.getSenderId())) {
                QuizesControl.execute(xupdate);
            } else {
                      Xuser xuser = DataBase.getTelegramUser(xupdate.getSenderId(), xupdate.getBotUserName());
   

                switch (xuser.getAccountType()) {

                    case AccountType.USER:
                        UserController.execute(xeyeUpdate);
                        break;

                    case AccountType.MODERATOR:

                        ModeradorController.execute(xeyeUpdate);

                        break;

                }
            }

        }

    }

}
