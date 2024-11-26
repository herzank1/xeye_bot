/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.database;


import com.monge.xeye.xeye.contability.BalanceAccount;
import com.monge.xeye.xeye.contability.Transacction;
import com.monge.xeye.xeye.objects.AccountType;
import com.monge.xeye.xeye.objects.BackUpChannel;
import com.monge.xeye.xeye.objects.TelegramFile;
import com.monge.xeye.xeye.objects.TelegramUser;
import com.monge.xeye.xeye.telegram.Bot;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DeliveryExpress a SQLITE balancer avoid frezzen
 */
public class DbBalancer {

    static DbConection accounts;
    static DbConection contability;
    static DbConection files;
   

    public static void init() {

        accounts = new DbConection("db_accounts.sqlite");
        accounts.addDao(TelegramUser.class);
        accounts.addDao(Bot.class);

        contability = new DbConection("db_contability.sqlite");
        contability.addDao(Transacction.class);
        contability.addDao(BalanceAccount.class);
        
        files = new DbConection("db_files.sqlite");
        files.addDao(TelegramFile.class);
        files.addDao(BackUpChannel.class);



    }

    public static class Accounts {
        
      public static TelegramUser getTelegramUser(String id, String node) {
        TelegramUser read = null;

  
            read = TelegramUsers().read(id);
  

        /*si no existe creamos uno*/
        if (read == null) {
            read = new TelegramUser(id,node,false,AccountType.NOT_REGISTRED,null);
            DataBase.Accounts.TelegramUsers().create(read);

        }else{
            /*si si existe pero el node es diferente, actualizamos el node*/
            if (!read.getLastNodeBot().equals(node)) {
                read.setLastNodeBot(node);
                DataBase.Accounts.TelegramUsers().update(read);

            }
        
        }
        return read;
    }

        public static GenericDao<TelegramUser, String> TelegramUsers() {

            GenericDao<TelegramUser, String> dao = accounts.getDao(TelegramUser.class);
            return dao;

        }



    
        public static class Bots {

            public static GenericDao<Bot, String> Bots() {

                GenericDao<Bot, String> dao = accounts.getDao(Bot.class);
                return dao;

            }
        }
        


    }

    public static class Contability {

        public static class BalancesAccounts {

            public interface MainBalancesAccountsIDs {

                String CARGO = "@CARGO";
                String DELIVERY_EXPRESS = "@DELIVERY_EXPRESS";
                String SERVICIOS_REPARTOS = "@SERVICIOS_REPARTOS";
                String SERGUROS_ORDENES = "@SERGUROS_ORDENES";
                String SEGUROS_REPARTIDORES = "@SEGUROS_REPARTIDORES";
                String CAJA_CHICA = "@CAJA_CHICA";
                String EMERGENCIAS = "@EMERGENCIAS";
                String CUOTAS_REPARTIDORES = "@CUOTAS_REPARTIDORES";
                String CUOTAS_NEGOCIOS = "@CUOTAS_NEGOCIOS";

            }

      
            public static GenericDao<BalanceAccount, String> BalancesAccounts() {

                GenericDao<BalanceAccount, String> dao = contability.getDao(BalanceAccount.class);
                return dao;

            }

        }

        public static class Transacctions {

            public static GenericDao<Transacction, String> Transacctions() {

                GenericDao<Transacction, String> dao = contability.getDao(Transacction.class);
                return dao;

            }

            public static boolean execute(Transacction transacction) {
                try {

                    BalanceAccount bfrom = Contability.BalancesAccounts.BalancesAccounts().read(transacction.getFrom());
                    BalanceAccount bto = Contability.BalancesAccounts.BalancesAccounts().read(transacction.getTo());

                    bfrom.setBalance((int) (bfrom.getBalance() - transacction.getMount()));
                    bto.setBalance((int) (bto.getBalance() + transacction.getMount()));

                    Contability.BalancesAccounts.BalancesAccounts().update(bfrom);
                    Contability.BalancesAccounts.BalancesAccounts().update(bto);

                    Contability.Transacctions.Transacctions().create(transacction);
                    
                    return true;

                } catch (Exception ex) {
                    Logger.getLogger(Transacction.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }

            }

        }

    }

    
    public static class Files{
        
          public static GenericDao<TelegramFile, String> Files() {

                GenericDao<TelegramFile, String> dao = files.getDao(TelegramFile.class);
                return dao;

            }
          
                public static GenericDao<BackUpChannel, String> BackUpChannels() {

                GenericDao<BackUpChannel, String> dao = files.getDao(BackUpChannel.class);
                return dao;

            } 
    
    
    }

}
