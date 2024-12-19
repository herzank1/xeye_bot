/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.objects;

import com.j256.ormlite.field.DatabaseField;
import com.monge.tbotboot.messenger.Bot;
import com.monge.xsqlite.utils.BaseDao;
import java.util.logging.Logger;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author DeliveryExpress
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class DBot extends BaseDao {
    
     @DatabaseField(id = true)
    private String userName;
    @DatabaseField
    private String apiKey;

    public DBot() {
    }
   

    public Bot getBot() {
        return new Bot(this.userName,this.apiKey);
    }

}
