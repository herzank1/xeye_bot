/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.telegram;

import com.monge.xeye.xeye.objects.FileType;
import com.monge.xeye.xeye.utils.Utils;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author DeliveryExpress Esta clase executa el Telegram action o envia el
 * mensaje
 */
public class Executor {

    /**
     * *
     * Executa el Response conforme a la configuracion establecida
     *
     * @param response
     * @return el mismo telegramAction pero con el messageID generado al
     * entregar
     */
    public static Response execute(Response response) {
        System.out.println(Utils.toJsonString(response));

        Bot botOut = BotsHandler.botsList.get(response.getTu().getLastNodeBot());

        if (botOut == null) {
            throw new NullPointerException("No se encontro bot");
        }

        System.out.println("TelegramAction - bot " + botOut.getBotUsername());

        switch (response.getAction()) {

            case ResponseAction.SEND_MESSAGE:

                SendMessage sm = new SendMessage();
                sm.setChatId(response.getTu().getId());
                sm.setText(response.getText());
                if (response.isHtml()) {
                    sm.enableHtml(true);
                }
                if (response.getThreadId() != null) {
                    sm.setMessageThreadId(Integer.valueOf(response.getThreadId()));

                }

                if (response.getMenu() != null) {
                    sm.setReplyMarkup(response.getMenu().getReplyKeyboard());

                }

                 {
                    try {
                        Message execute = botOut.execute(sm);

                        response.setMessageId(String.valueOf(execute.getMessageId()));

                    } catch (TelegramApiException ex) {
                        Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("Error al ejecutar el TelegramAction:\n" + Utils.toJsonString(response));
                    }
                }

                break;

            case ResponseAction.EDIT_MESSAGE:
                try {
                    EditMessageText emt = new EditMessageText();
                    emt.setMessageId(Integer.valueOf(response.getEditMessageId()));
                    emt.setChatId(response.getTu().getId());
                    emt.setText(response.getText());
                    if (response.isHtml()) {
                        emt.enableHtml(true);
                    }

                    if (response.getMenu() != null) {
                        emt.setReplyMarkup((InlineKeyboardMarkup) response.getMenu().getReplyKeyboard());

                    }

                    Serializable execute = botOut.execute(emt);

                    response.setMessageId(String.valueOf(execute.toString()));

                } catch (TelegramApiException ex) {
                    String message = ex.getMessage();
                    Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error al ejecutar el TelegramAction:\n" + Utils.toJsonString(response));
                    System.out.println("Message Id:" + response.getEditMessageId());

                    if (message.equals(TelegramApiExecptionsMessage.CANT_EDIT_MSG)) {
                        response.setAction(ResponseAction.SEND_MESSAGE);
                        response.execute();

                    }
                }

                break;
                
                

            case ResponseAction.DELETE_MESSAGE:

                DeleteMessage dm = new DeleteMessage();
                dm.setMessageId(Integer.parseInt(response.getMessageId()));
                dm.setChatId(response.getTu().getId());

                 {
                    try {
                        Boolean execute = botOut.execute(dm);

                        response.setMessageId(String.valueOf(execute.toString()));

                    } catch (TelegramApiException ex) {
                        Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("Error al ejecutar el TelegramAction:\n" + Utils.toJsonString(response));
                    }
                }

                break;

            case ResponseAction.SEND_FILE:
                
                /*obtenemos el file id relacionado con el bot que solicita*/
            String fileId = response.getFile().getFileId(botOut.getBotUsername());

                switch (response.getFile().getType()) {

                    case FileType.VIDEO:

                        SendVideo sendVideo = new SendVideo();
                        sendVideo.setChatId(response.getTu().getId());
                        sendVideo.setCaption(response.getText());
                        InputFile video = new InputFile(fileId);
                        sendVideo.setVideo(video);

                        if (response.getMenu() != null) {
                            sendVideo.setReplyMarkup((InlineKeyboardMarkup) response.getMenu().getReplyKeyboard());

                        }

                         {
                            try {
                                botOut.execute(sendVideo);
                            } catch (TelegramApiException ex) {
                                Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        break;

                }

                break;


        }

        return response;
    }

    public static ArrayList<ChatMember> execute(String botUserName, GetChatAdministrators chatAdmins) {
    
        try {
            Bot botOut = BotsHandler.botsList.get(botUserName);
            ArrayList<ChatMember> execute = botOut.execute(chatAdmins);
            return execute;
        } catch (TelegramApiException ex) {
            Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public static interface TelegramApiExecptionsMessage {

        String CANT_EDIT_MSG = "Error executing org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText query: [400] Bad Request: message can't be edited";

    }

}
