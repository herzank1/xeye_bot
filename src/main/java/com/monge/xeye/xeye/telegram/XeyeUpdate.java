/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.telegram;

import com.monge.tbotboot.messenger.Xupdate;
import com.monge.xeye.xeye.database.DataBase;
import com.monge.xeye.xeye.objects.Xuser;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 *
 * @author DeliveryExpress
 */
public class XeyeUpdate extends Xupdate{

    public XeyeUpdate(Update update, String botUserName) {
        super(update, botUserName);
    }
    
    public Xuser getXuser(){
        return DataBase.getTelegramUser(super.getSenderId(), super.getBotUserName());

    }

    
}
