/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdf.core;

/**
 *
 * @author Angel
 */
public class TestValidador {
    
    public static void main(String[] args) {
        String dato;
        
        System.out.println("Probando esDni:\n"
                + "45123142A: " + Validador.esDni("45123142A") + "\n"
                + "12345678Z: " + Validador.esDni("12345678Z") + "\n");
        
        System.out.println("Probando esCif:\n"
                + "45123142A: " + Validador.esCif("45123142A") + "\n"
                + "A72033425: " + Validador.esCif("A32033425") + "\n");
        
        System.out.println("Probando esFecha:\n"
                + "1/1/2: " + Validador.esFecha("1/1/2") + "\n"
                + "7/90/2: " + Validador.esFecha("7/90/2") + "\n");
        
        System.out.println("Probando esNombre:\n"
                + "Paco: " + Validador.esNombre("Paco") + "\n"
                + "estevan: " + Validador.esNombre("estevan") + "\n"
                + "E: " + Validador.esNombre("E") + "\n");
    }
}
