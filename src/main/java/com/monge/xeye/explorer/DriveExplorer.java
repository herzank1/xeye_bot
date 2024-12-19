/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.explorer;

import com.monge.virtualexplorer.Drive;
import com.monge.virtualexplorer.VirtualExplorer;
import com.monge.virtualexplorer.objects.TemporalFolder;
import com.monge.virtualexplorer.utils.SharedUtilities;
import com.monge.xeye.xeye.objects.Xfile;
import java.util.ArrayList;
import java.util.List;
import com.monge.virtualexplorer.objects.FileVirtual;

/**
 *
 * @author DeliveryExpress
 */
public class DriveExplorer {

    static Drive xeye;
    public static String ROOT_PATH = "XEYE:/";

    public static void init() {

        xeye = new Drive(ROOT_PATH) {
            @Override
            public FileVirtual createFolder(String path, String name) {

                if (!name.endsWith("/")) {
                    name += "/";
                }
                String createPath = SharedUtilities.createPath(path, name);
                Xfile folder = new Xfile(createPath);
                folder.create();
                this.processFile(folder);
                reload();
                return folder;

            }

            @Override
            public ArrayList<FileVirtual> load() {

                ArrayList<Xfile> readAll = Xfile.readAll(Xfile.class);
                return new ArrayList<>(readAll);

            }
        };

    }

    public static Drive getXeye() {
        return xeye;
    }

    public static String getROOT_PATH() {
        return ROOT_PATH;
    }

    public static Xfile getRoot() {
        FileVirtual root = xeye.getRoot();
        return new Xfile(root.getPath());
    }

    public static Xfile getVirtualFileByHash(String param) {

        return (Xfile) xeye.getVirtualFileByHash(param);

    }

    public static void reload() {
        xeye.reload();
    }

    public static Xfile createFolder(String currentDirectory, String folderName) {

        return (Xfile) xeye.createFolder(currentDirectory, folderName);
    }

    public static Xfile getVirtualFileByXid(String id) {

        return (Xfile) xeye.getVirtualFileByXid(id);

    }

    public static Xfile getVirtualDirectoryByHash(String param) {

        FileVirtual vdir = xeye.getVirtualDirectoryByHash(param);

        if (vdir instanceof Xfile) {
            return (Xfile) vdir; // Cast explícito de Virtuable a Xfile
        } else if (vdir instanceof TemporalFolder) {
            return new Xfile(vdir.getPath());
        }

        return null;

    }

    public static ArrayList<Xfile> readDirectory(String path) {
        // Lee todos los directorios como Virtuable
        List<FileVirtual> readAllDirectory = xeye.readDirectory(path);

        System.out.println("reading path -> " + path + " :files " + readAllDirectory.size());

        // Convertir la lista de Virtuable a una lista de Xfile
        ArrayList<Xfile> xfileList = new ArrayList<>();

        for (FileVirtual virt : readAllDirectory) {
            if (virt instanceof Xfile) {
                xfileList.add((Xfile) virt); // Cast explícito de Virtuable a Xfile
            } else if (virt instanceof TemporalFolder) {
                xfileList.add(new Xfile(virt.getPath()));
            }
        }

        return xfileList;
    }

    public static void initGui() {
        VirtualExplorer.initGui(xeye);
    }

}
