/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.telegram;

import com.monge.tbotboot.commands.Command;
import com.monge.tbotboot.quizes.QuizesControl;
import com.monge.xeye.xeye.objects.Xuser;
import com.monge.xeye.xeye.quizes.UserGUI;

/**
 *
 * @author DeliveryExpress
 */
class UserController {

    static void execute(XeyeUpdate xeyeUpdate) {

    Xuser xuser = xeyeUpdate.getXuser();

        Command command = xeyeUpdate.getCommand();
        switch (command.command()) {

            case "/start":
            case "/menu":

                QuizesControl.add(new UserGUI(xeyeUpdate));
                QuizesControl.execute(xeyeUpdate);

                break;
        }


    }
/*
    private static String rechargeMessage() {
        return "\n👉+30 dias  X 20 pesos MXN"
                + "\n👉+60 dias x 30 pesos MXN"
                + "\n👉+1 año x 100 pesos MXN"
                + "\n";

    }
    
 /*   
/*
    private static String kTvMessage() {

        return "🎬 ¡Bienvenido a Kaelus TV! 📱\n"
                + "\n"
                + "🚀 Miles de películas y series 📺 para disfrutar sin parar. 🎥\n"
                + "\n"
                + "💥 Accede a contenido exclusivo 🎞️ y encuentra lo que más te gusta, ¡todo en un solo lugar! 🌟\n"
                + "\n"
                + "🌍 Disponible en todos tus dispositivos 🖥️📱 para que disfrutes donde quieras y cuando quieras.\n"
                + "\n"
                + "🎉 ¡No esperes más! Únete a Kaelus TV y comienza a ver ahora mismo! 🔥"
                + "\n Descarga la App "
                + "Grupo de películas y series\n"
                + "\n"
                + "Descargar la App 👉 http://go.aftvnews.com/381800"
                + "\nSolicita tu prueba aqui 👉 ";

    }
*/
}
