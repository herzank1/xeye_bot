/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.telegram;

import com.monge.tbotboot.commands.Command;
import com.monge.tbotboot.messenger.Response;
import com.monge.tbotboot.quizes.QuizesControl;
import com.monge.xeye.xeye.objects.Xuser;
import com.monge.xeye.xeye.quizes.UserGUI;


/**
 *
 * @author DeliveryExpress
 */
class ModeradorController {

    static void execute(XeyeUpdate xeyeUpdate) {
        Xuser xuser = xeyeUpdate.getXuser();

        Command command = xeyeUpdate.getCommand();
        switch (command.command()) {

            case "/start":
            case "/menu":

                QuizesControl.add(new UserGUI(xeyeUpdate));
                QuizesControl.execute(xeyeUpdate);

                break;
            
            case "/delete_msg":
                Response.deleteMessage(xeyeUpdate);
                
                break;
        }

    }

}
