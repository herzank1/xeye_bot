/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.contability;

import com.monge.tbotboot.objects.Receptor;
import com.monge.xeye.xeye.database.DataBase;
import com.monge.xeye.xeye.objects.Xuser;

/**
 *
 * @author DeliveryExpress
 */
public class General {
    
    public static Receptor getPaymentsGroupReceptor(){
    
        Xuser group = Xuser.read(Xuser.class, "-1002159002314");
        
        return new Receptor(group.getId(),group.getLastNodeBot());
    
    }
    
}
