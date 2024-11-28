/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.contability;

import com.j256.ormlite.field.DatabaseField;
import com.monge.xsqlite.xsqlite.BaseDao;
import java.util.UUID;
import lombok.Data;

@Data
public class BalanceAccount extends BaseDao{

     @DatabaseField(id = true)
    String accountNumber;
      @DatabaseField
    int balance;

    public BalanceAccount() {
        this.accountNumber=UUID.randomUUID().toString();
        this.balance=0;
    }

    public BalanceAccount(String accountNumber, int balance) {
        this.accountNumber = UUID.randomUUID().toString();
        this.balance = balance;
    }
    
    

}
   

