package com.monge.xeye.xeye.objects;

import com.j256.ormlite.field.DatabaseField;
import com.monge.tbotboot.objects.TelegramUser;
import com.monge.xeye.xeye.Xeye;
import com.monge.xeye.xeye.contability.BalanceAccount;

import com.monge.xsqlite.utils.BaseDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author DeliveryExpress
 */
@Data
@EqualsAndHashCode(callSuper = false)
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

    public Xuser(String fromId, String botUserName, boolean b, String type) {

        this.id = fromId;
        this.lastNodeBot = botUserName;
        this.blackList = false;
        this.accountType = type;
        this.balanceAccountId = null;
    }

    public BalanceAccount getBalance() {
        BalanceAccount read = BalanceAccount.read(BalanceAccount.class, this.balanceAccountId);

        if (read == null) {
            read = new BalanceAccount();
            this.setBalanceAccountId(read.getAccountNumber());
            this.update();
            read.create();
        }

        return read;

    }

    public void swtichBlackList() {
        this.blackList = !this.blackList;
        this.update();
    }

    public String toStringForTelegram() {
        return "👤 ID: " + id + "\n"
                + "🤖 Último Nodo Bot: " + lastNodeBot + "\n"
                + "⛔ Lista Negra: " + (blackList ? "Sí" : "No") + "\n"
                + "💳 Tipo de Cuenta: " + accountType + "\n";
               // + "🏦 Balance Account ID: " + (balanceAccountId != null ? balanceAccountId : "No disponible");
    }

}
