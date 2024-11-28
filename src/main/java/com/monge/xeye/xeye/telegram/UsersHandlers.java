/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.telegram;

import com.monge.tbotboot.commands.Command;
import com.monge.tbotboot.messenger.Response;
import com.monge.tbotboot.messenger.Xupdate;
import com.monge.tbotboot.objects.TelegramUser;
import com.monge.tbotboot.quizes.QuizesControl;
import com.monge.xeye.xeye.Explorer;
import static com.monge.xeye.xeye.Explorer.ROOT_PATH;
import com.monge.xeye.xeye.objects.AccountType;
import com.monge.xeye.xeye.objects.Xfile;
import com.monge.xeye.xeye.objects.Xuser;
import com.monge.xeye.xeye.quizes.QuizNewFolder;

/**
 *
 * @author DeliveryExpress
 */
public class UsersHandlers {
    

    static void execute(Xupdate xupdate) {
        TelegramUser telegramUser = xupdate.getTelegramUser();
        Xuser xuser = new Xuser(telegramUser);
               

        System.out.println("execute up´date");

        try {

            if (xupdate.getFile() != null) {
                System.out.println("Se recibio un archivo");
                new Xfile(xupdate.getFile()).create();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("update no contiene arvhivos");

        if (xupdate.isGroupMessage()) {
            Response.sendMessage(xupdate.getTelegramUser(), xupdate.getText(), null);
        } else {

            if (QuizesControl.hasQuiz(xupdate.getSenderId())) {
                QuizesControl.execute(xupdate);
            } else {

                Command command = xupdate.getCommand();

                switch (xuser.getAccountType()) {

                    case AccountType.NOT_REGISTRED:

                        switch (command.command()) {

                            case "/start":
                            case "/menu":
                                Explorer.getMenu(xupdate);

                                break;

                            case "/newfolder":
                                QuizesControl.add(new QuizNewFolder(xupdate.getSenderId()));
                                QuizesControl.execute(xupdate);

                                break;

                            case "/file":

                                Explorer.getPreviewFile(xupdate);

                                break;
                                
                             case "/go":

                                Explorer.getFile(xupdate);

                                break;    

                            case "/dir":
                                String get = Explorer.hashedPath.get(command.getParam(1));
                                if (get != null) {
                                    Explorer.usersCurrentsPath.put(xupdate.getSenderId(), get);
                                } else {
                                    Explorer.usersCurrentsPath.put(xupdate.getSenderId(), ROOT_PATH);
                                }

                                Explorer.getMenu(xupdate);
                                break;

                        }

                        break;

                    case AccountType.USER:

                        break;

                    case AccountType.MODERATOR:

                        break;

                }
            }

        }

    }


    
}
