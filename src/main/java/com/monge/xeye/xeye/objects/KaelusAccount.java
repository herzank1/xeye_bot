/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.objects;

import com.j256.ormlite.field.DatabaseField;
import com.monge.xsqlite.utils.BaseDao;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author DeliveryExpress
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class KaelusAccount extends BaseDao {

    @DatabaseField(id = true)
    String telegramId;
    @DatabaseField
    String user;
    @DatabaseField
    String password;
    @DatabaseField
    String expirationDate;

    public KaelusAccount() {
    }

    public KaelusAccount(String telegramId) {
        this.telegramId = telegramId;

    }

   public String toTelegramString() {
    // Usa emojis representativos para cada campo
    String telegramEmoji = "📲";
    String userEmoji = "👤";
    String passwordEmoji = "🔑";
    String expirationEmoji = "⏳";

    // Usa valores predeterminados si los campos son nulos
    String telegramIdValue = (telegramId != null) ? telegramId : "No disponible";
    String userValue = (user != null) ? user : "No disponible";
    String passwordValue = (password != null) ? password : "No disponible";
    String expirationDateValue = (expirationDate != null) ? expirationDate : "No disponible";

    // Formatea y devuelve el String con emojis
    return String.format(
        "%s Telegram ID: %s\n%s Usuario: %s\n%s Contraseña: %s\n%s Fecha de expiración: %s",
        telegramEmoji, telegramIdValue,
        userEmoji, userValue,
        passwordEmoji, passwordValue,
        expirationEmoji, expirationDateValue
    );
}

}
