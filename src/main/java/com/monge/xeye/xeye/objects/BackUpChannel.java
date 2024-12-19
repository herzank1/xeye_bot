/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.objects;

import com.j256.ormlite.field.DatabaseField;
import com.monge.xsqlite.utils.BaseDao;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;

/**
 *
 * @author DeliveryExpress
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class BackUpChannel extends BaseDao{
      @DatabaseField(id = true)
    String id;
    @DatabaseField
    String name;
    @DatabaseField
    String description;

}
