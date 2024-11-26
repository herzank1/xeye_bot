/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.objects;

import com.j256.ormlite.field.DatabaseField;
import com.monge.xeye.xeye.contability.BalanceAccount;
import com.monge.xeye.xeye.database.DataBase;
import com.monge.xeye.xeye.telegram.Xupdate;
import lombok.Data;

/**
 *
 * @author DeliveryExpress
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author DeliveryExpress
 */
@Data
public class TelegramUser {

    @DatabaseField(id = true)
    String id;
    @DatabaseField
    String lastNodeBot;
    @DatabaseField
    boolean blackList;
    @DatabaseField
    String accountType;
    @DatabaseField
    String balanceAccountId;

    public TelegramUser() {
    }

    /**
     * *
     *
     * @param id
     * @param lastNodeBot
     * @param blackList
     * @param accountType
     * @param accountId
     */
    public TelegramUser(String id, String lastNodeBot, boolean blackList, String accountType, String balanceAccountId) {
        this.id = id;
        this.lastNodeBot = lastNodeBot;
        this.blackList = blackList;
        this.accountType = accountType;
        this.balanceAccountId = balanceAccountId;
    }

    public TelegramUser(Xupdate aThis) {
        if (aThis.isGroupMessage()) {
            this.id = aThis.getFromId();
            this.accountType = AccountType.IS_GROUP;
        } else {
            this.id = aThis.getSenderId();
            this.accountType = AccountType.NOT_REGISTRED;
        }
        this.lastNodeBot = aThis.getBotUserName();
        this.blackList = false;
        this.balanceAccountId = null;
    }

    public BalanceAccount getBalance() {

        BalanceAccount read = DataBase.Contability.BalancesAccounts.BalancesAccounts().read(this.balanceAccountId);
        
        /*Si no tiene una cuenta de balance creamos una con 10 creditos*/
        if (read == null) {
            read = new BalanceAccount();
            read.setBalance(10);
            this.balanceAccountId = read.getAccountNumber();
            DataBase.Accounts.TelegramUsers().update(this);
            DataBase.Contability.BalancesAccounts.BalancesAccounts().create(read);
        }

        return read;
    }
    
}
