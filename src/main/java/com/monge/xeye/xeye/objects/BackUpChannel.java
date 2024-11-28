/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.objects;

import com.j256.ormlite.field.DatabaseField;
import com.monge.xsqlite.xsqlite.BaseDao;
import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;

/**
 *
 * @author DeliveryExpress
 */
@Data
public class BackUpChannel extends BaseDao{
    @DatabaseField(generatedId = false)
    String id;
    @DatabaseField
    String name;
    @DatabaseField
    String description;

}
