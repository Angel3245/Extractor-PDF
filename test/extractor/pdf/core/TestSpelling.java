/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdf.core;

import java.io.IOException;

/**
 *
 * @author Angel
 */
public class TestSpelling {
    public static void main(String[] args) {
        try{
            Spelling corrector = new Spelling("src/extractor/pdf/core/diccionario.txt");
            
            System.out.println(corrector.correct("Ficha"));
            
        }catch(IOException exc){
            System.out.println(exc);
        }
    }
}
