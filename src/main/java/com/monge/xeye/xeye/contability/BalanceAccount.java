/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.contability;

import com.j256.ormlite.field.DatabaseField;
import com.monge.xeye.xeye.utils.Utils;
import com.monge.xsqlite.utils.BaseDao;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalanceAccount extends BaseDao {

    private static final long SECONDS_IN_TWO_HOURS = 60L * 60 * 2;
    private static final long SECONDS_IN_30_DAYS = 30L * 24 * 60 * 60;

    @DatabaseField(id = true)
    String accountNumber;
    @DatabaseField
    int balance;
    @DatabaseField
    /*Unix timeStamp*/
    long expiration;

    public BalanceAccount() {
        this.accountNumber = UUID.randomUUID().toString();
        this.balance = 0;
        this.expiration = Utils.DateUtils.getUnixTimeStamp() + SECONDS_IN_TWO_HOURS;
    }

    public BalanceAccount(String accountNumber, int balance) {
        this.accountNumber = UUID.randomUUID().toString();
        this.balance = balance;
        this.expiration = Utils.DateUtils.getUnixTimeStamp() + SECONDS_IN_TWO_HOURS;
    }

    public void add30Days() {
        this.expiration += SECONDS_IN_30_DAYS;
        this.update();
    }

    public void add2hours() {
        this.expiration += SECONDS_IN_TWO_HOURS;
        this.update();
    }

    public void setExired() {
        this.expiration = Utils.DateUtils.getUnixTimeStamp() - 10000;
        this.update();
    }

    /**
     * Verifica si el campo expiration ya está vencido.
     *
     * @param expiration El Unix timestamp de expiración.
     * @return true si está vencido, false si no.
     */
    public boolean isExpired() {
        // Obtener el timestamp actual
        long currentTimestamp = Instant.now().getEpochSecond();
        // Comparar con el campo expiration
        return expiration < currentTimestamp;
    }

    public String toStringForTelegram() {
        String expirationStatus = isExpired() ? "❌ Vencido" : "✅ Activo";
        return "Acceso a Xeye\n" // "💳 Número de Cuenta: " + accountNumber + "\n"
                // + "💰 Balance: " + balance + "\n"
                + "⏳ Expiración: " + (expiration > 0 ? Utils.DateUtils.unixToDate(expiration) : "No especificada") + " " + expirationStatus;
    }

}
