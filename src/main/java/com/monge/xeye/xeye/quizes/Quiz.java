/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.quizes;

import com.monge.xeye.xeye.telegram.Xupdate;
import java.util.List;
import lombok.Data;

/**
 *
 * @author DeliveryExpress
 * Clase padre de todos los cuestionarios
 */

@Data
public abstract class Quiz {

    String userId;
    int step;
    boolean finalized;

    public Quiz(String userId) {
        this.userId = userId;
        this.step = 0;
    }

    abstract void execute(Xupdate xupdate);

    void next() {
        this.step += 1;
    }

   
    void goTo(int step) {
        this.step = step;
    }
   
    void back() {
        this.step -= 1;
    }

    void destroy(){
    QuizesControl.destroy(this);
    }
    
  

}
