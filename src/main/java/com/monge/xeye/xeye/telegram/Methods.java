/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.telegram;

import java.util.ArrayList;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;



/**
 *
 * @author DeliveryExpress
 */
public class Methods {
    /***
     * 
     * @param botUserName caller bot
     * @return 
     */
       public static ArrayList<ChatMember> getChatAdmins(String botUserName){
        //org.telegram.telegrambots.meta.api.methods
        
        GetChatAdministrators chatAdmins=new GetChatAdministrators();
        chatAdmins.setChatId("-1002260899409");
        
        return Executor.execute(botUserName,chatAdmins);
       
    
    }
    
 
    
}
