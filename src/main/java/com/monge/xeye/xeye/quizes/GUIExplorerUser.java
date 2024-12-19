
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
import com.monge.tbotboot.objects.Receptor;
import com.monge.tbotboot.objects.TelegramFile;
import com.monge.tbotboot.utils.PageListViewer;
import com.monge.tbotboot.utils.RandomCaptcha;
import com.monge.xeye.explorer.DriveExplorer;
import static com.monge.xeye.explorer.DriveExplorer.ROOT_PATH;
import com.monge.xeye.xeye.contability.BalanceAccount;
import com.monge.xeye.xeye.contability.General;
import com.monge.xeye.xeye.database.DataBase;
import com.monge.xeye.xeye.objects.Xfile;
import com.monge.xeye.xeye.objects.Xuser;
import java.util.ArrayList;

/**
 *
 * @author DeliveryExpress
 */
public class GUIExplorerUser extends GuiItem {

    ArrayList<Xfile> currentDirectory;
    PageListViewer list;
    int currentPage = 1;
    Xfile current;
    // Xfile current;
    Xuser user;
    BalanceAccount balance;
    String botUserNname;
    //  Xuser xuser;
    String msgId;

    public GUIExplorerUser(String userId, GuiElement parent, String text) {
        super(parent, text);
        currentDirectory = DriveExplorer.readDirectory(ROOT_PATH);
        list = new PageListViewer(currentDirectory, 10);
        user = Xuser.read(Xuser.class, userId);
        balance = user.getBalance();
        current = DriveExplorer.getRoot();

        System.out.println("currentDirectory " + currentDirectory.size());

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

        if (xupdate.getFile() != null && xupdate.getFile().getType().equals(FileType.IMAGE)) {
            processImage(xupdate, xupdate.getFile());
            return;
        }

        Command command = new Command(xupdate.getText(), "=");

        switch (command.command()) {

            case "--page":
                currentPage = list.getValueOf(command.getParam(1));
                break;

            case "--dir":

                super.setDisableParentResponse(false);
                    Xfile dir = DriveExplorer.getVirtualDirectoryByHash(command.getParam(1));
                    System.out.print("--dir "+command.getParam(1));
                    currentDirectory = DriveExplorer.readDirectory(dir.getPath());
                    list.setList(currentDirectory);
                    current = dir;
              

                break;

            case "--file":

                Xfile file = DriveExplorer.getVirtualFileByHash(command.getParam(1));
                current = file;

                break;
            case "--portrait":
                Response.sendFile(xupdate.getTelegramUser(),
                        current.getPreviewAsTelegramFile(botUserNname),
                        current.getDetails(),
                        null);

                break;

            case "--getfile":

                Xuser telegramUser = DataBase.getTelegramUser(xupdate.getSenderId(), xupdate.getBotUserName());
                boolean expired = telegramUser.getBalance().isExpired();
                if (expired) {

                    super.setDisableParentResponse(true);
                    Response.sendMessage(xupdate.getTelegramUser(), "Tu acceso ha expirado", MessageMenu.okAndDeleteMessage("--deletemsg"));

                    return;
                }

                if (current != null) {
                    MessageMenu menu = new MessageMenu();
                    // Configurar captcha
                    RandomCaptcha rc = new RandomCaptcha(current.getHash());
                    rc.setMenu(menu, "--success=", "--fail");
                    /*el hijo(this) responde*/
                    super.setDisableParentResponse(true);
                    if (current.hasPreview(xupdate.getBotUserName())) {
                        String fileId = current.getPreview(xupdate.getBotUserName());
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
                            current.getAsTelegramFile(xupdate.getBotUserName()),
                            "[🔒Protected]",
                            new MessageMenu("❌ Cerrar ", "--deletemsg"));

                    super.setDisableParentResponse(true);
                }

                break;

            case "--close":

                Response.deleteMessage(xupdate);
                break;

            case "--fail":
                Response.sendMessage(xupdate.getTelegramUser(), "Demuestra que eres humano!", null);

                break;
            case "--unlock":
                Response.sendMessage(xupdate.getTelegramUser(), unlockMessage(), MessageMenu.okAndDeleteMessage("--deletemsg"));

                break;

            case "reload":
                reloadDrive();

                break;

            case "--deletemsg":

                Response.deleteMessage(xupdate);

                break;
        }

    }

    @Override
    public MessageMenu getMenu() {

        MessageMenu menu = new MessageMenu();

        if (current != null && !current.isRootFolder()) {
         
            menu.addButton("⬆ ", "--dir=" + current.getParentVf().getHash(), false);
          
            
           
        }

        if (balance.isExpired()) {
            menu.addButton("🔑 Desbloquear", "--unlock", true);
        }

        if (current != null && current.isDirectory()) {

            MessageMenu navMenu = list.getNavMenu("--page=", this.currentPage);
            menu.merge(navMenu);
            menu.addNewLine();

            ArrayList<Xfile> page = list.getPage(currentPage);

            for (Xfile f : page) {

                String buttonLabel = "";
                if (f.isDirectory()) {
                    buttonLabel = "📂 " + f.getName();
                } else {

                    switch (f.getType()) {

                        case FileType.IMAGE:
                            buttonLabel = "🌅 " + f.getName();

                            break;

                        case FileType.VIDEO:

                            if (f.hasPreview(botUserNname)) {
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

        /*si current es archivo*/
        if (current != null && !current.isDirectory()) {

            switch (current.getType()) {

                case FileType.VIDEO:
                    menu.addButton("🎬 Ver ", "--getfile", true);

                    if (current.hasPreview(botUserNname)) {
                        menu.addButton("🌅 Ver Portada", "--portrait", true);
                    }

                    break;

            }

        }

        return menu;

    }

    @Override
    public String draw() {

        String accessStatus = balance.isExpired() ? "🔒❌ " : "🔓✅ ";

        if (current != null) {

            if (current.isDirectory()) {

                return "🆔 " + this.user.getId() + "\n" + accessStatus + "💽 " + current.getPath() + "\n📂 " + current.getName();
            } else {

                return "🆔 " + this.user.getId() + "\n" + accessStatus + "💽 " + current.getPath() + "\n\n" + current.getData();
            }

        }

        return "null";

    }

    private void reloadDrive() {
        DriveExplorer.reload();
        currentDirectory = (ArrayList<Xfile>) DriveExplorer.readDirectory(ROOT_PATH);
        list.setList(currentDirectory);
    }

    private String unlockMessage() {
        return "Para desbloquear la unidad XEYE/:"
                + " realiza un pago de 20 pesos MXN (1 Mes de Accesso)"
                + " desde tu App de banco o en oxxo a..."
                + "\n👉 Targeta: 4152 3138 1329 7715"
                + "\n👉 Banco BBVA"
                + "\n👉 Beneficiario Diego Villarreal"
                + "\n👉 Ref: id de telegram."
                + "\n"
                + "Una vez echo tu pago envia captura o foto del"
                + " comprobante a este mismo chat. y en unos momentos "
                + " se debloqueara la unidad y podras acceder a miles de peliculas y series!"
                + " ademas de cientos de archivos!"
                + " cualquier duda o soporte aqui 👉 @Soporte_01"
                + "\n\n"
                + "";
    }

    private void processImage(Xupdate update, TelegramFile file) {

        Receptor receptor = General.getPaymentsGroupReceptor();
        Response.sendFile(receptor, file, "Nuevo pago recibido!"
                + "\n Usuario ID:" + update.getSenderId()
                + "\n Servicio: Xeye Explorer", null);

        Response.sendMessage(update.getTelegramUser(), "Imagen recibida!"
                + "\n⚠ En este chat solo debes enviar evidencias de pago de "
                + "Servicio Xeye Explorer, su foto(Screen Shot) sera revisado por algun "
                + "moderador, una vez confirmado (puede durar hasta 4hrs), "
                + "se habilitara acceso por un mes, que te permitira acceder a todo el contenido de la unidad."
                + " ❗si se "
                + "detecta un intento de engaño, seras bloqueado.❗"
                + "\n 👉 @Soporte_01", MessageMenu.okAndDeleteMessage("--deletemsg"));

    }

}
