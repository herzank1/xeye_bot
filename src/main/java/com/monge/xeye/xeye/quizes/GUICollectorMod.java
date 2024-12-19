/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.quizes;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.monge.tbotboot.commands.Command;
import com.monge.tbotboot.gui.GuiElement;
import com.monge.tbotboot.gui.GuiItem;
import com.monge.tbotboot.messenger.MessageMenu;
import com.monge.tbotboot.messenger.Xupdate;
import com.monge.tbotboot.objects.FileType;
import com.monge.tbotboot.objects.TelegramFile;
import com.monge.virtualexplorer.utils.SharedUtilities;
import com.monge.xeye.explorer.DriveExplorer;

import com.monge.xeye.xeye.objects.Xfile;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DeliveryExpress
 */
public class GUICollectorMod extends GuiItem {

    String prevCollected = "";
    String path;
    Xfile video;
    Xfile img;

    boolean getVideosOnly = false;

    public GUICollectorMod(GuiElement parent, String text) {
        super(parent, text);
        this.path = DriveExplorer.ROOT_PATH;
    }

    @Override
    public void execute(Xupdate xupdate) {

        if (xupdate.getFile() != null) {

            TelegramFile file = xupdate.getFile();

            switch (file.getType()) {

                case FileType.IMAGE:
                    img = new Xfile(file);

                    break;

                case FileType.VIDEO:
                    video = new Xfile(file);
                    break;

            }

            if (this.getVideosOnly) {

                /*guardamos si se recibio video y portada*/
                if ((video != null) && (!existOnDb(video))) {
                    video.updatePath(path);
                    video.create();
                    video = null;
                    this.prevCollected = "🎬" + video.getFileName();
                }

            } else {
                /*guardamos si se recibio video y portada*/
                if ((video != null) && (img != null) && (!existOnDb(video))) {
                    video.addPrewMirror(xupdate.getBotUserName(), img.getId());
                    video.updatePath(path);
                    video.create();
                    video = null;
                    img = null;
                    this.prevCollected = "🎬🌅 " + video.getFileName();
                }
            }

        }

        Command command = new Command(xupdate.getText(), "=");
        // System.out.println("-------------> " + command);

        switch (command.command()) {

            case "setdirectory":
              
                    this.path = command.getParam(1);
     

                break;

            case "switchcollector":
                this.getVideosOnly = !this.getVideosOnly;

                break;

        }

    }

    @Override
    public MessageMenu getMenu() {

        return new MessageMenu();
    }

    @Override
    public String draw() {
        String v = "🎬 Esperando";
        if (video != null) {

            v = "🎬" + video.getFileName();
        }

        String i = "🌅 Esperando";
        if (video != null) {

            i = "🌅 " + img.getFileName();
        }

        return "Collector de Peliculas con portada"
                + "\nDirectorio -> " + this.path
                + "\nRecibir solo videos:" + this.getVideosOnly
                + "\n" + v
                + "\n" + i
                + "\nUltimo:" + this.prevCollected
                + getCommands();

    }

    private String getCommands() {

        return "\n"
                + "\nsetdirectory=path"
                + "\nswitchcollector";

    }

    public boolean existOnDb(Xfile xfile) {
        Dao finalDao = Xfile.getDao(Xfile.class).getFinalDao();
        QueryBuilder queryBuilder = finalDao.queryBuilder();

        try {
            queryBuilder.where().eq("fileName", xfile.getFileName())
                    .and().eq("type", xfile.getType());

            return queryBuilder.countOf() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(GUICollectorMod.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

}
