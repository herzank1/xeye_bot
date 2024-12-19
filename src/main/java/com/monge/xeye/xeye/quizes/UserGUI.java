/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.quizes;

import com.monge.tbotboot.gui.GuiMenu;
import com.monge.tbotboot.messenger.Xupdate;
import com.monge.tbotboot.objects.Receptor;
import com.monge.tbotboot.quizes.QuizGui;
import com.monge.xeye.xeye.objects.AccountType;
import com.monge.xeye.xeye.objects.Xuser;
import com.monge.xeye.xeye.telegram.XeyeUpdate;

/**
 *
 * @author DeliveryExpress
 */
public class UserGUI extends QuizGui {

    public UserGUI(XeyeUpdate xeyeUpdate) {
        super(xeyeUpdate.getTelegramUser(), true);

        Xuser user = Xuser.read(Xuser.class, xeyeUpdate.getSenderId());

        GuiMenu mainMenu = new GuiMenu(null, "Menu Principal");

        switch (user.getAccountType()) {

            case AccountType.USER:
                mainMenu.addItem(new GUIExplorerUser(xeyeUpdate.getSenderId(), null, "💽 Xeye Explorer"));
                mainMenu.addItem(new GUIKaelus(xeyeUpdate.getSenderId(), null, "⚡ Kaelus TV ⚡"));

                break;

            case AccountType.MODERATOR:

                mainMenu.addItem(new GUIExplorerMod(xeyeUpdate.getSenderId(), null, "✴ 💽 XEYE:/ 💽"));
                mainMenu.addItem(new GUIXeyeAccountsMod(null, "✴ Explorer Mng 👥"));
                mainMenu.addItem(new GUICollectorMod(null, "✴ Collector 🎞"));
                mainMenu.addItem(new GUIKaelusMod( null, "✴ ⚡ Kaelus TV 👥"));

                break;

        }

        super.setMainMenu(mainMenu);

    }

}
