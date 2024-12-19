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
import com.monge.tbotboot.objects.Receptor;
import com.monge.tbotboot.utils.PageListViewer;
import com.monge.xeye.xeye.contability.BalanceAccount;
import com.monge.xeye.xeye.objects.AccountType;
import com.monge.xeye.xeye.objects.KaelusAccount;
import com.monge.xeye.xeye.objects.Xuser;
import java.util.ArrayList;

/**
 *
 * @author DeliveryExpress
 */
public class GUIXeyeAccountsMod extends GuiItem {

    ArrayList<Xuser> users;
    Xuser current;
    BalanceAccount currentBal;
    PageListViewer list;
    int currentPage = 1;

    public GUIXeyeAccountsMod(GuiElement parent, String text) {
        super(parent, text);
        ArrayList<Xuser> readAll = Xuser.readAll(Xuser.class);

// Ordenar por id (tratándolo como número en String)
        readAll.sort((u1, u2) -> {
            try {
                return Long.compare(Long.parseLong(u1.getId()), Long.parseLong(u2.getId()));
            } catch (NumberFormatException e) {
                throw new RuntimeException("ID no es un número válido: " + u1.getId() + " o " + u2.getId(), e);
            }
        });

// Asignar el resultado ordenado a `users`
        users = readAll;

        list = new PageListViewer(users, 10);
    }

    @Override
    public void execute(Xupdate update) {

        Command command = new Command(update.getText(), "=");

        switch (command.command()) {

            case "--page":
                currentPage = list.getValueOf(command.getParam(1));
                break;

            case "--view":
                Xuser read = Xuser.read(Xuser.class, command.getParam(1));
                if (read != null) {
                    current = read;
                    currentBal = read.getBalance();

                }

                break;

            case "--plus30":
                currentBal.add30Days();
                Response.sendMessage(new Receptor(current.getId(),current.getLastNodeBot())
                        , "➕3⃣0⃣ de Acceso a Xeye Drive❗", MessageMenu.okAndDeleteMessage("--delete_msg"));

                break;
            case "--plus2hours":
                currentBal.add2hours();

                break;
                
             case "--setexpired":
                currentBal.setExired();

                break;  
                
             case "--switchbalcklist":
                 current.swtichBlackList();
                
                break;

        }

    }

    @Override
    public MessageMenu getMenu() {

        MessageMenu navMenu = list.getNavMenu("--page=", this.currentPage);
        navMenu.addNewLine();

        if (currentBal != null) {
            navMenu.addButton("✴ +2 hours ", "--plus2hours", false);
            navMenu.addButton("✴ +30 Days ", "--plus30", true);
             navMenu.addButton("❌ set Expired ", "--setexpired", true);
        }
        
        if(current!=null){
        navMenu.addButton("🔃 Swtich BlackList ", "--switchbalcklist", true);
        }


        /*agregamos la lista*/
        for (Xuser user : users) {
            if (user.getAccountType().equals(AccountType.USER)) {
                navMenu.addButton("👤 " + user.getId(), "--view=" + user.getId(), true);
            }

        }

        return navMenu;
    }

    @Override
    public String draw() {
        if (current != null) {
            return current.toStringForTelegram() + "\n\n" + currentBal.toStringForTelegram() + commands();
        } else {
            return "Seleccione una cuenta o escriba view=id de telegram." + commands();
        }

    }

    private String commands() {

        return "\n";
              //  + "\n--setuser=user -> asigna usuario"
              //  + "\n--setpass=pass -> asignar password"
               // + "\n--setexp=fecha [dia/mes/año - 12/15/24] asignar expiracion.";
    }

}
