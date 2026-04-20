/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package visao;

import javax.sound.sampled.*;
import java.io.InputStream;

public class SomAlerta {

    public static void tocar() {
        new Thread(() -> {
            try {

                InputStream audioSrc = SomAlerta.class
                        .getResourceAsStream("/resources/alerta.wav");

                if (audioSrc == null) {
                    System.out.println("Som não encontrado!");
                    return;
                }

                AudioInputStream audioStream =
                        AudioSystem.getAudioInputStream(audioSrc);

                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

//import javax.sound.sampled.*;
//import java.io.File;
//
//public class SomAlerta {
//
//    public static void tocar() {
//        new Thread(() -> {
//            try {
//                AudioInputStream audioStream =
//                        AudioSystem.getAudioInputStream(
//                                new File("alerta.wav"));
//
//                Clip clip = AudioSystem.getClip();
//                clip.open(audioStream);
//                clip.start();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//}