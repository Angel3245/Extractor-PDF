/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdf.IU;

import java.io.File;


/**
 *
 * @author Angel
 */
public class ExtractorPDF {

    /**
     * @param args the command line arguments
     */
    public static void inicio(){
        //pedir ruta
        File ruta = new File("c:/documents and settings/Zachary/desktop");
        //iterar entre los archivos del directorio
        File [] archivos = ruta.listFiles();
        for (int i = 0; i < archivos.length; i++){
            if (archivos[i].isFile() && archivos[i].getName().endsWith(".pdf")){ //esta línea se salta otros directorios / carpetas o archivos que no sean pdf
                //iterar entre las palabras del archivo
                
            }
        }
        //crear un archivo nuevo con los datos organizados
    }
    
}
