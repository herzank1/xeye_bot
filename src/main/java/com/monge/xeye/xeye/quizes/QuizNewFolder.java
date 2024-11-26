/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.quizes;

import com.monge.xeye.xeye.Explorer;
import com.monge.xeye.xeye.objects.TelegramUser;
import com.monge.xeye.xeye.telegram.Response;
import com.monge.xeye.xeye.telegram.Xupdate;
import com.monge.xeye.xeye.utils.Utils;

/**
 *
 * @author DeliveryExpress
 */
public class QuizNewFolder extends Quiz {

    public QuizNewFolder(String userId) {
        super(userId);
    }

    @Override
    void execute(Xupdate xupdate) {

        Response response = new Response(xupdate.getSenderTelegramUser());

        switch (super.step) {

            case 0:
                response.setText("Ingrese Nombre de la carpeta");
                response.execute();
                next();

                break;

            case 1:

                Explorer.createFolder(xupdate, xupdate.getText());
                response.setText("Carpeta "+xupdate.getText()+" creada!");
                response.execute();
                destroy();

                break;

        }

    }

}
