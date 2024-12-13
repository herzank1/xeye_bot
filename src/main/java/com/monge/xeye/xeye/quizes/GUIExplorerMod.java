/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.quizes;

import com.google.gson.GsonBuilder;
import com.monge.tbotboot.commands.Command;
import com.monge.tbotboot.gui.GuiElement;
import com.monge.tbotboot.gui.GuiItem;
import com.monge.tbotboot.messenger.MessageMenu;
import com.monge.tbotboot.messenger.Response;
import com.monge.tbotboot.messenger.Xupdate;
import com.monge.tbotboot.objects.FileType;
import com.monge.tbotboot.objects.TelegramFile;
import com.monge.tbotboot.utils.PageListViewer;
import com.monge.tbotboot.utils.RandomCaptcha;
import com.monge.xeye.explorer.DriveExplorer;
import static com.monge.xeye.explorer.DriveExplorer.ROOT_PATH;
import com.monge.xeye.explorer.VirtualFile;
import com.monge.xeye.xeye.objects.Xfile;
import com.monge.xeye.xeye.objects.Xuser;
import com.monge.xeye.xeye.utils.Utils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author DeliveryExpress
 */
public class GUIExplorerMod extends GuiItem {

    ArrayList<VirtualFile> currentDirectory;
    PageListViewer list;
    int currentPage = 1;
    VirtualFile current;
    // Xfile current;
    String userId;
    String botUserNname;
    //  Xuser xuser;
    String msgId;

    public GUIExplorerMod(String userId, GuiElement parent, String text) {
        super(parent, text);
        currentDirectory = (ArrayList<VirtualFile>) DriveExplorer.readDirectory(ROOT_PATH);
        list = new PageListViewer(currentDirectory, 10);
        this.userId = userId;
        current = DriveExplorer.getRoot();

    }

    @Override
    public void execute(Xupdate xupdate) {

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(xupdate.getUpdate()));

        if (this.msgId == null) {
            this.msgId = xupdate.getMessageId();
        }

        if (this.botUserNname == null) {
            this.botUserNname = xupdate.getBotUserName();
        }

        /*procesamos si recibimos un archivo*/
        if (xupdate.getFile() != null) {
            processFile(xupdate.getFile());
            return;
        }else{
        super.setDisableParentResponse(false);
        }

        Command command = new Command(xupdate.getText(), "=");
        // System.out.println("-------------> " + command);

        switch (command.command()) {

            case "--page":
                currentPage = list.getValueOf(command.getParam(1));
                break;

            case "--dir":

                VirtualFile dir = DriveExplorer.getVirtualDirectoryByHash(command.getParam(1));
                // setCurrentUserPath(dir.getPath());

                //  System.out.println("--dir -------------> " + dir.getPath());
                /*actualizamos el page viewer*/
                currentDirectory = (ArrayList<VirtualFile>) DriveExplorer.readDirectory(dir.getPath());
                list.setList(currentDirectory);
                current = dir;
                break;

            case "--file":

                VirtualFile file = DriveExplorer.getVirtualFileByHash(command.getParam(1));
                current = file;
                //   System.out.println("-------------> " + file.getPath());

                break;
            case "--portrait":
                Response.sendFile(xupdate.getTelegramUser(),
                        current.getXfile().getPreviewAsTelegramFile(botUserNname),
                        current.getXfile().getDetails(),
                        null);

                break;

            case "--getfile":
                if (current != null) {
                    MessageMenu menu = new MessageMenu();
                    // Configurar captcha
                    RandomCaptcha rc = new RandomCaptcha(current.getHash());
                    rc.setMenu(menu, "--success=", "--fail");
                    /*el hijo(this) responde*/
                    super.setDisableParentResponse(true);
                    if (current.getXfile().hasPreview(xupdate.getBotUserName())) {
                        String fileId = current.getXfile().getPreview(xupdate.getBotUserName());
                        TelegramFile telegramFile = new TelegramFile();
                        telegramFile.setFileId(fileId);
                        telegramFile.setBot(xupdate.getBotUserName());
                        telegramFile.setType(FileType.IMAGE);

                        Response.sendFile(xupdate.getTelegramUser(), telegramFile, rc.getCaptchaMessage(), menu);
                    } else {
                        Response.sendMessage(xupdate.getTelegramUser(), rc.getCaptchaMessage(), menu);
                    }

                }

                break;

            case "--success":

                if (current != null) {

                    super.setDisableParentResponse(false);
                    Response.deleteMessage(xupdate);
                    Response.sendFile(xupdate.getTelegramUser(),
                            current.getXfile().getAsTelegramFile(xupdate.getBotUserName()),
                            "[🔒Protected]",
                            new MessageMenu("❌ Cerrar ", "--close"));

                    super.setDisableParentResponse(true);
                }

                break;

            case "--close":
                Response.deleteMessage(xupdate);
                break;

            case "--fail":
                Response.sendMessage(xupdate.getTelegramUser(), "Demuestra que eres humano!", null);

                break;

            case "mkdir":

                makeDir(command.getParam(1));

                break;

            case "rmpreview":

                rmPreview();

                break;

            case "rmthis":

                removeThis();

                break;
                
            case "movethisto":

                moveThisTo(command.getParam(1));

                break;   
                
            case "reload":
                reloadDrive();
                
                break;
        }

    }

    @Override
    public MessageMenu getMenu() {

        MessageMenu menu = new MessageMenu();

        if (current != null && !current.isRootFolder()) {
            menu.addButton("⬆ ", "--dir=" + current.getParentVf().getHash(), false);
        }
        MessageMenu navMenu = list.getNavMenu("--page=", this.currentPage);
        menu.merge(navMenu);
        menu.addNewLine();

        // Mostrar contenido del directorio
        /*
        if (currentDirectory.isEmpty()) {
            menu.addButton("📂 ... ", "/empty", true);//vacio

            return menu;
        }
*/
        if (current != null && current.isDirectory()) {
            
            ArrayList<VirtualFile> page = list.getPage(currentPage);

            for (VirtualFile f : page) {

                String buttonLabel = "";
                if (f.isDirectory()) {
                    buttonLabel = "📂 " + f.getName();
                } else {

                    switch (f.getXfile().getType()) {

                        case FileType.IMAGE:
                            buttonLabel = "🌅 " + f.getName();

                            break;

                        case FileType.VIDEO:

                            if (f.getXfile().hasPreview(botUserNname)) {
                                buttonLabel = "🎬🌅 " + f.getName();
                            } else {
                                buttonLabel = "🎬 " + f.getName();
                            }

                            break;

                    }
                }

                String command = f.isDirectory() ? "--dir=" + f.getHash() : "--file=" + f.getHash();
                menu.addButton(buttonLabel, command, true);
            }

            return menu;

        }

        if (current != null && !current.isDirectory()) {

            switch (current.getXfile().getType()) {

                case FileType.VIDEO:
                    menu.addButton("🎬 Ver ", "--success", true);

                    if (current.getXfile().hasPreview(botUserNname)) {
                        menu.addButton("🌅 Ver Portada", "--portrait", true);
                    }

                    break;

            }

        }

        return menu;

    }

    @Override
    public String draw() {
        if (current != null) {

            if (current.isDirectory()) {

                return "💽 " + current.getPath() + "\n📂 "+current.getName() + commands();
            } else {

                return "💽 " + current.getPath() + "\n\n" + current.getXfile().getData() + commands();
            }

        }

        return "null";

    }

    /*
    private String getCurrentUserPath() {
        return DriveExplorer.getUserCurrentPath(userId);
    }

    private void setCurrentUserPath(String path) {
        DriveExplorer.setCurrentUserPath(userId, path);
    }
     */
    private String getCurrentDirectory() {

        if (current.isDirectory()) {
            return current.getPath();
        } else {

            return current.getPath();
        }

    }

    private void reloadDrive() {
        DriveExplorer.reload();
        currentDirectory = (ArrayList<VirtualFile>) DriveExplorer.readDirectory(ROOT_PATH);
        list.setList(currentDirectory);
    }

    private void makeDir(String folderName) {

        VirtualFile createFolder = DriveExplorer.createFolder(getCurrentDirectory(), folderName);
        current = createFolder;
        reloadDrive();

    }

    private void processFile(TelegramFile file) {

        System.out.println("Verificando archivo  -> " + file.getFileName());

        try {
            switch (file.getType()) {

                /*para videos verificar duplicados y guardar*/
                case FileType.VIDEO:

                    String xeyeUUID = Utils.getXeyeUUID(file.getFileName());

                    /*si el video no contiene un xeyeid */
                    if (xeyeUUID == null) {
                        /*creamos el archivo video*/
                        Xfile xfile = null;
                        if (current.isDirectory()) {
                            xfile = new Xfile(file, current.getPath());
                        } else {
                            xfile = new Xfile(file, current.getParent());
                        }

                        xfile.create();

                        /*asiganmos el archivo recibido como el actual*/
                        System.out.println("Video recibido! -> " + xfile.getFileName());

                        /*recargamos el disco*/
                     //   reloadDrive();

                        current = DriveExplorer.getVirtualFileByXid(xfile.getId());
                        super.setDisableParentResponse(true);
                    }

                    break;

                /*para imagenes solo agregar mirror al current y no almacenar en db*/
                case FileType.IMAGE:
                    if (current != null) {
                        /*solo se puede asginar portada si mo la tiene, asi evitar portadas equivocadas*/
                        if (current.getXfile().getPreview(botUserNname) == null) {
                            Xfile xfile = current.getXfile();
                            HashMap<String, String> previewsMirrors = xfile.getPrevMirrors();
                            previewsMirrors.put(file.getBot(), file.getFileId());
                            xfile.setPrevMirrors(previewsMirrors);
                            xfile.update();

                            System.out.println("Portada recibida para -> " + xfile.getFileName());

                            /*recargamos el disco*/
                           // reloadDrive();
                        }

                    }

                    break;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private String commands() {

        return "\n"
                + "\n⚡Comandos:"
                + "\n⚡mkdir=folderName -> crea nueva carpeta."
                + "\n⚡rmpreview -> elimina una vista previa o portada."
                + "\n⚡rmthis -> elimina permanentemente el archivo actual."
                + "\n⚡movethisto=path -> move el archivo actual a path indicado."
                + "\n⚡reload -> recarga todo";
    }

    private void rmPreview() {

        current.getXfile().removePreview(this.botUserNname);
        reloadDrive();

    }

    private void removeThis() {

        if (!current.isDirectory()) {
            current.getXfile().delete();
            reloadDrive();
        }

    }

    private void moveThisTo(String path) {
    
          if (!current.isDirectory()) {
            current.getXfile().updatePath(path);
            current.getXfile().update();
            reloadDrive();
        }
        
    }

}
