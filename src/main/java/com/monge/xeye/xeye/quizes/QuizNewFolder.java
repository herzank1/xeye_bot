/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.quizes;

import com.monge.tbotboot.messenger.Response;
import com.monge.tbotboot.messenger.Xupdate;
import com.monge.tbotboot.quizes.Quiz;
import com.monge.xeye.xeye.Explorer;

/**
 *
 * @author DeliveryExpress
 */
public class QuizNewFolder extends Quiz {

    public QuizNewFolder(String userId) {
        super(userId);
    }

    @Override
    public void execute(Xupdate xupdate) {
    
        
        Response response = new Response(xupdate.getTelegramUser());

        switch (super.getStep()) {

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
