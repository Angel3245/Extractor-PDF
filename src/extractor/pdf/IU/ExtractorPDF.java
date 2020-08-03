/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdf.IU;

import java.io.File;
import extractor.pdf.core.Archivo;
import java.util.Vector;


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
        File ruta = ES.pideDirectorio();
        //iterar entre los archivos del directorio
        File [] archivos = ruta.listFiles();

        Vector<String> palabrasArchivo;
        for (int i = 0; i < archivos.length; i++){
            if (archivos[i].isFile() && archivos[i].getName().endsWith(".pdf")){ //esta línea se salta otros directorios / carpetas o archivos que no sean pdf
                //extraer texto del pdf
                palabrasArchivo = extraeTextoPalabras(archivos[i]);
                
                //iterar entre las palabras del texto
                for(String palabra: palabrasArchivo){
                    //falta implementar método para analizar palabra a palabra
                }
            }
        }
        //crear un archivo nuevo con los datos organizados
    }
    
    public static Vector<String> extraeTextoPalabras(File archivo){
        Archivo actual = new Archivo(archivo);
        return actual.pdfExtraeTextoPalabras();
    }
    
}
