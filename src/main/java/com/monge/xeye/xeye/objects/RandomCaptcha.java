/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.monge.xeye.xeye.objects;

import com.monge.xeye.xeye.telegram.MessageMenu;
import com.monge.xeye.xeye.telegram.MessageMenu.Button;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lombok.Data;

/**
 *
 * @author DeliveryExpress
 */
@Data
public class RandomCaptcha {

    int num1, num2;
    String fileHash;

    public RandomCaptcha(String fileHash) {
        Random random = new Random();
        this.num1 = random.nextInt(11); // 11 porque el límite superior no está incluido
        this.num2 = random.nextInt(11); // 11 porque el límite superior no está incluido
        this.fileHash = fileHash;

    }

    public String getCaptchaMessage() {
        return "Seleccione el resultado correcto de " + this.num1 + "+" + this.num2;
    }

    private String getTotal() {
        return String.valueOf(this.num1 + this.num2);
    }

    public String getUnsuccess() {
        Random random = new Random();
        int incorrectResult;

        do {
            // Generar un resultado aleatorio entre 0 y 20 (asegúrate de ajustar este rango según tus necesidades)
            incorrectResult = random.nextInt(21); // 0 a 20
        } while (incorrectResult == (this.num1 + this.num2)); // Asegurarse de que sea diferente al resultado correcto

        return String.valueOf(incorrectResult);
    }

    public void setMenu(MessageMenu menu) {

     // Generar las operaciones correctas e incorrectas
    String correctResult = "" + getTotal();
    String incorrectResult1 = getUnsuccess();
    String incorrectResult2 = getUnsuccess();

    // Crear botones con sus respectivas acciones
    Button successOperation = new Button(correctResult, "/go&" + fileHash);
    Button unSuccessOperation1 = new Button(incorrectResult1, "/fail&");
    Button unSuccessOperation2 = new Button(incorrectResult2, "/fail&");

    // Crear una lista de botones
    Button[] operations = {successOperation, unSuccessOperation1, unSuccessOperation2};

    // Mezclar las operaciones de manera aleatoria
    List<Button> shuffledButtons = Arrays.asList(operations);
    Collections.shuffle(shuffledButtons);

    // Agregar los botones al menú
    for (Button button : shuffledButtons) {
        menu.addButton(button, false); // Agregar los botones en orden aleatorio
    }

    }

}
