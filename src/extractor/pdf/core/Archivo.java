/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdf.core;

import java.io.File;
import java.nio.file.*;
import com.qoppa.pdfText.PDFText;
import java.util.Vector;

/**
 *
 * @author Angel
 */
public class Archivo {
    
    File archivo;
    
    public Archivo(String ruta){
        if(Files.exists(Paths.get(ruta))) { 
            this.archivo = new File(ruta);
        }
    }
    
    public Archivo(File archivo){
        this.archivo = archivo;
    }

    public String getRuta() {
        return archivo.getPath();
    }
    
    public String getName(){
        return archivo.getName();
    }
    
    public String pdfExtraeTexto(){
        String texto = "";
        
        try{
            PDFText pdfText = new PDFText(this.getRuta(), null);
            
            texto = pdfText.getText();
        }
        catch(com.qoppa.pdf.PDFException exc){
            System.out.println(exc);
        }
        
        return texto;
    }
    
    public Vector<String> pdfExtraeTextoPalabras(){
        Vector<String> texto = new Vector<>();
        try{
            PDFText pdfText = new PDFText(this.getRuta(), null);
            
            texto = pdfText.getWords();
        }
        catch(com.qoppa.pdf.PDFException exc){
            System.out.println(exc);
        }
        return texto;
    }
}
