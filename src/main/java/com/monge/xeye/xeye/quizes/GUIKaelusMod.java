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
import com.monge.tbotboot.utils.PageListViewer;
import com.monge.xeye.xeye.objects.KaelusAccount;
import java.util.ArrayList;

/**
 *
 * @author DeliveryExpress
 */
public class GUIKaelusMod extends GuiItem {

    ArrayList<KaelusAccount> kaccounts;
    KaelusAccount current;
    PageListViewer list;
    int currentPage = 1;

    public GUIKaelusMod(GuiElement parent, String text) {
        super(parent, text);
        ArrayList<KaelusAccount> readAll = KaelusAccount.readAll(KaelusAccount.class);

// Ordenar por id (tratándolo como número en String)
        readAll.sort((u1, u2) -> {
            try {
                return Long.compare(Long.parseLong(u1.getTelegramId()), Long.parseLong(u2.getTelegramId()));
            } catch (NumberFormatException e) {
                throw new RuntimeException("ID no es un número válido: " + u1.getTelegramId() + " o " + u2.getTelegramId(), e);
            }
        });

// Asignar el resultado ordenado a `users`
        kaccounts = readAll;

        list = new PageListViewer(kaccounts, 10);
    }

    @Override
    public void execute(Xupdate update) {

        Command command = new Command(update.getText(), "=");

        switch (command.command()) {

            case "--page":
                currentPage = list.getValueOf(command.getParam(1));
                break;

            case "--view":
                KaelusAccount read = KaelusAccount.read(KaelusAccount.class, command.getParam(1));
                if (read != null) {
                    current = read;

                }
                break;

            case "--set":
                try {
                    String[] split = command.getParam(1).split(",");
                    current.setUser(split[0]);
                    current.setPassword(split[1]);
                    current.setExpirationDate(split[3]);
                    current.update();

                } catch (Exception e) {
                    Response.sendMessage(update.getTelegramUser(), e.getMessage(), MessageMenu.okAndDeleteMessage("--deletemsg"));
                }

                break;
                
              case "--deletemsg":

                Response.deleteMessage(update);

                break;   

        }

    }

    @Override
    public MessageMenu getMenu() {

        MessageMenu navMenu = list.getNavMenu("--page=", this.currentPage);

        /*agregamos la lista*/
        for (KaelusAccount user : kaccounts) {

            navMenu.addButton("👤 " + user.getTelegramId(), "--view=" + user.getTelegramId(), true);

        }

        return navMenu;
    }

    @Override
    public String draw() {
        if (current != null) {
            return current.toTelegramString() + commands();
        } else {
            return "Seleccione una cuenta o escriba view=id de telegram." + commands();
        }
    }

    private String commands() {

        return "\n"
                + "\n--set=user,pass,date -> asigna los datos de una cuenta"
                + "\ndate[dia/mes/año - 12/15/24]";
        // + "\n--setuser=user -> asigna usuario"
        // + "\n--setpass=pass -> asignar password"
        //  + "\n--setexp=fecha [dia/mes/año - 12/15/24] asignar expiracion.";
    }

}
