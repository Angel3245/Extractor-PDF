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
public class TestArchivo {
    public static void main(String[] args) {
        Archivo archivo = new Archivo("C:/Users/Angel/Desktop/f.pdf");
        System.out.println(archivo.pdfExtraeTexto());
    }
    
}
