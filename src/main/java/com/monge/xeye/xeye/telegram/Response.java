/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.telegram;

import com.monge.xeye.xeye.database.DataBase;
import com.monge.xeye.xeye.objects.AccountType;
import com.monge.xeye.xeye.objects.TelegramFile;
import com.monge.xeye.xeye.objects.TelegramUser;
import lombok.Data;

/**
 *
 * @author DeliveryExpress Representa la accion o el mensaje que se enviara a
 * TelegramUser contiene el cuerpo y lo elementos internos del mensaje o accion
 */
@Data
public class Response {

    /*Usuario de destino*/
    TelegramUser tu;
    
    String action;

    /*basic*/
    String text;
    MessageMenu menu;
    /*for updating messages*/
    String editMessageId;
    String reference;

    /*extra*/
    String threadId;
    boolean isHtml;
    
    TelegramFile file;

    /*returned messageId*/
    String messageId;
    boolean gotoGroup;

    /**
     * *
     * Default constructor de response, un mensage simple
     */
    public Response() {
        this.setAction(ResponseAction.SEND_MESSAGE);
        
    }

    /**
     * *
     * Default constructor de response, un mensage simple
     */
    public Response(TelegramUser tu) {
        this.tu = tu;
        this.setAction(ResponseAction.SEND_MESSAGE);
    }

    /**
     * *
     *
     * @param senderId destinantary
     * @param text
     * @param menu
     */
    public static void sendMessage(String senderId, String text, MessageMenu menu) {
        
        TelegramUser read = DataBase.Accounts.TelegramUsers().read(senderId);
        sendMessage(read, text, menu);
    }
    
    public static void sendMessage(TelegramUser telegramUser, String text, MessageMenu menu) {
        Response response = new Response(telegramUser);
        response.setText(text);
        response.setMenu(menu);
        response.execute();
        
    }
    
    public static void editMediaMessage(TelegramUser telegramUser, String messageId, TelegramFile file, String text, MessageMenu menu) {
        
        Response response = new Response(telegramUser);
        response.setAction(ResponseAction.EDIT_MEDIA_MESSAGE);
        response.setEditMessageId(messageId);
        response.setFile(file);
        response.setText(text);
        response.setMenu(menu);
        response.execute();
        
    }
    
    public static void editMessage(TelegramUser telegramUser, String messageId, String text, MessageMenu menu) {
        
        Response response = new Response(telegramUser);
        response.setAction(ResponseAction.EDIT_MESSAGE);
        response.setEditMessageId(messageId);
        response.setText(text);
        response.setMenu(menu);
        response.execute();
    }
    
    public static void deleteMessage(Xupdate xupdate) {
        
        Response response = new Response(xupdate.getTelegramUser());
        response.setAction(ResponseAction.DELETE_MESSAGE);
        response.setMessageId(xupdate.getMessageId());
        response.execute();
        
    }
    
    public static void sendFile(TelegramUser telegramUser, TelegramFile file, String text, MessageMenu menu) {
        Response response = new Response(telegramUser);
        response.setAction(ResponseAction.SEND_FILE);
        response.setFile(file);
        response.setText(text);
        response.setMenu(menu);
        response.execute();
    }

    /**
     * *
     *
     * @param senderBot el bot que envial el mensaje este sera el bot bout
     * @param chatId grupo o usuario que recibira el mensake
     * @param file
     * @param text
     * @param menu
     */
    public static void sendFile(String senderBot, String chatId, TelegramFile file, String text, MessageMenu menu) {
        
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setId(chatId);
        telegramUser.setLastNodeBot(senderBot);
        telegramUser.setAccountType(AccountType.IS_GROUP);
        telegramUser.setBlackList(false);
        
        Response response = new Response(telegramUser);
        response.setAction(ResponseAction.SEND_FILE);
        response.setFile(file);
        response.setText(text);
        response.setMenu(menu);
        response.execute();
    }    
    
    public Response execute() {
        return Executor.execute(this);
    }
    
    public boolean actionSuccess() {
        return this.messageId != null;
    }
    
}
