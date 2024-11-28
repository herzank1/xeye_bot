package com.monge.xeye.xeye.objects;

import com.j256.ormlite.field.DatabaseField;
import com.monge.tbotboot.objects.TelegramUser;
import com.monge.xeye.xeye.contability.BalanceAccount;

import com.monge.xsqlite.xsqlite.BaseDao;
import lombok.Data;

/**
 *
 * @author DeliveryExpress
 */
@Data
public class Xuser extends BaseDao {

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

    public Xuser() {
    }

    public Xuser(TelegramUser tu) {
        this.id = tu.getId();
        this.lastNodeBot = tu.getBot();
    }

    public Xuser(TelegramUser tu, boolean blackList, String accountType, String balanceAccountId) {
        this.id = tu.getId();
        this.lastNodeBot = tu.getBot();
        this.blackList = blackList;
        this.accountType = accountType;
        this.balanceAccountId = balanceAccountId;
    }

    public Xuser(String id, String lastNodeBot, boolean blackList, String accountType, String balanceAccountId) {
        this.id = id;
        this.lastNodeBot = lastNodeBot;
        this.blackList = blackList;
        this.accountType = accountType;
        this.balanceAccountId = balanceAccountId;
    }

    public BalanceAccount getBalance() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
