/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.quizes;

import com.monge.tbotboot.commands.Command;
import com.monge.tbotboot.gui.GuiBase;
import com.monge.tbotboot.gui.GuiElement;
import com.monge.tbotboot.gui.GuiItem;
import com.monge.tbotboot.messenger.MessageMenu;
import com.monge.tbotboot.messenger.Response;
import com.monge.tbotboot.messenger.Xupdate;
import com.monge.tbotboot.objects.FileType;
import com.monge.tbotboot.objects.Receptor;
import com.monge.tbotboot.objects.TelegramFile;
import com.monge.tbotboot.objects.TelegramUser;
import com.monge.xeye.xeye.contability.General;
import com.monge.xeye.xeye.database.DataBase;
import com.monge.xeye.xeye.objects.KaelusAccount;
import com.monge.xeye.xeye.telegram.XeyeUpdate;

/**
 *
 * @author DeliveryExpress
 */
public class GUIKaelus extends GuiItem {

    KaelusAccount kAccount;

    public GUIKaelus(String userid, GuiElement parent, String text) {
        super(parent, text);
        kAccount = DataBase.getKaelusAccount(userid);
    }

    @Override
    public void execute(Xupdate update) {
        XeyeUpdate XeyeUpdate = new XeyeUpdate(update.getUpdate(), update.getBotUserName());
        TelegramUser receptor = XeyeUpdate.getTelegramUser();
        
        
        if(XeyeUpdate.getFile()!=null&&XeyeUpdate.getFile().getType().equals(FileType.IMAGE)){
        processImage(update,XeyeUpdate.getFile());
        return;
        }

        Command command = XeyeUpdate.getCommand();
        switch (command.command()) {

            case "--info":

                Response.sendMessage(receptor, kaelusInfo(), MessageMenu.okAndDeleteMessage("--deletemsg"));

                break;

      

            case "--contract":

                Response.sendMessage(receptor, contractKaelus(), MessageMenu.okAndDeleteMessage("--deletemsg"));


                break;
                
              case "--gettrial":

                Response.sendMessage(receptor, getTrialMsg(update), MessageMenu.okAndDeleteMessage("--deletemsg"));


                break;  
                
                
              case "--deletemsg":

                Response.deleteMessage(update);

                break;   

        }

    }

    @Override
    public MessageMenu getMenu() {

        MessageMenu menu = new MessageMenu();
        menu.addButton("🎬 Que es Kaelus TV? ", "--info", true);
        if (kAccount.getUser() == null) {
            menu.addButton("⚡ Contratar", "--contract", true);
            menu.addButton("⚡ Prueba Gratis", "--gettrial", true);
        }
        menu.addUrlButton("⬇ Descargar App (Android)", "http://go.aftvnews.com/381800");
        menu.addNewLine();
        menu.addUrlButton("👉 Tutorial Google Tv", "https://www.youtube.com/watch?v=aHe-xrJtz9I");
        menu.addNewLine();
        menu.addUrlButton("👉 Tutorial Roku TV", "https://www.youtube.com/watch?v=BX7AEOe1MSs");
        menu.addNewLine();
        menu.addUrlButton("❓ Soporte", "https://t.me/Soporte_01");
        menu.addNewLine();

        return menu;

    }

    @Override
    public String draw() {

        return "Tu cuenta ⚡ Kaelus TV ⚡\n"
                + kAccount.toTelegramString();

    }

    private String kaelusInfo() {

        return "🎬 ¡Bienvenido a Kaelus TV! 📱\n"
                + "\n"
                + "🚀 Miles de películas y series 📺 para disfrutar sin parar. 🎥\n"
                + "\n"
                + "💥 Accede a contenido exclusivo 🎞️ y encuentra lo que más te gusta, ¡todo en un solo lugar! 🌟\n"
                + "\n"
                + "🌍 Disponible en todos tus dispositivos 🖥️📱 para que disfrutes donde quieras y cuando quieras.\n"
                + "\n"
                + "🎉 ¡No esperes más! Únete a Kaelus TV y comienza a ver ahora mismo! 🔥"
                + "\n"
                + "Descargar la App 👉 http://go.aftvnews.com/381800"
                + "\n";

    }

    private String contractKaelus() {
    
    
        return "El costo del servicio es 250 pesos"
                + " por un mes, para contratar realiza un pago"
                + " desde tu App de banco o en oxxo a..."
                + "\n👉 Targeta: 4152 3138 1329 7715"
                + "\n👉 Banco BBVA"
                + "\n👉 Beneficiario Diego Villarreal"
                + "\n👉 Ref: id de telegram."
                + "\n"
                + "Una vez echo tu pago envia captura o foto del"
                + " comprobante a este mismo chat. y en unos momentos "
                + "recibiras tus datos de acceso (usuario y contraseña)"
                + " si ya eres cliente se renovara tu acceso"
                + " para tu cuenta Kaelus TV."
                + " cualquier duda o soporte aqui 👉 @Soporte_01"
                + "\n\n"
                + "";
    
    }

    private void processImage(Xupdate update, TelegramFile file) {
   
        Receptor receptor = General.getPaymentsGroupReceptor();
        Response.sendFile(receptor, file, "Nuevo pago recibido!"
                + "\n Usuario ID:"+update.getSenderId()
                + "\n Servicio: Kaelus TV", null);
        
        Response.sendMessage(update.getTelegramUser(), "Imagen recibida!"
                + "\n⚠ En este chat solo debes enviar evidencias de pago de "
                + "Servicio Kaelus, su foto(Screen Shot) sera revisado por algun "
                + "moderador, una vez confirmado (puede durar hasta 4hrs), "
                + "recibiras tu usuario y contraseña"
                + " para iniciar sesion en los servicios de Kaelus TV, ❗si se "
                + "detecta un intento de engaño, seras bloqueado.❗"
                + "\n 👉 @Soporte_01", MessageMenu.okAndDeleteMessage("--deletemsg"));
                
    
    
    }

    private String getTrialMsg(Xupdate update) {
        
        Receptor receptor = General.getPaymentsGroupReceptor();
        Response.sendMessage(receptor,  "Solicitu de pueba Kaelus TV!"
                + "\n Usuario ID:"+update.getSenderId()
                + "\n Servicio: Kaelus TV", null);
    
        return "⚠ Al ser usuario nuevo recibiras un usuario y contraseña"
                + " para acceder a los servicios de Kaelus TV (puede tarder hasta 4 horas)"
                + ", solo puedes"
                + " solicitar una vez y esta debe ser entre semana Lunes a viernes."
                + ", una vez que recibas los datos tendras acceso por ❗2 horas❗"
                + " para que pruebes la plataforma. Disfruta tu prueba.⚠";
    
    }
}
