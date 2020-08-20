/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdf.core;

import java.io.File;
import java.nio.file.*;
import com.qoppa.pdfText.PDFText;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    
    
    public String[] txtExtraeTextoPalabras(String splt) {

        try {

            String[] texto = null;

            File archivo = new File(this.getRuta());
            FileReader fr = null;
            BufferedReader br = null;

            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            
            // Lectura del fichero
            if (splt.equals(" ")) {
                String linea = br.readLine();
                texto = linea.split(splt);

            } else if (splt.equals("\n")) {
                String linea;
                String text="";
                while ((linea = br.readLine()) != null) {
                    text=text+" "+linea;    
                }
                text=text.trim();
                texto=text.split(" ");
            }
            return texto;

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            return null;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }
    
    public String extraeTextoEnArea(){
        String texto = "";
        try{
            PDFText pdfText = new PDFText(this.getRuta(), null);
            
            Rectangle2D area = new Rectangle2D.Float();
            area.setFrame(100, 150, 200,100);
            
            texto = pdfText.getTextInArea(0, area);
        }
        catch(com.qoppa.pdf.PDFException exc){
            System.out.println(exc);
        }
        return texto;
    }
}