/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdf.IU;

import java.io.File;
import extractor.pdf.core.Archivo;
import extractor.pdf.core.Validador;
import java.util.*;


/** Clase base del programa
 *
 * @author Angel
 */
public class ExtractorPDF {
    
    //datos a extraer
    private static List<String> nombreProveedor; 
    private static List<String> cifProveedor; 
    private static List<String> nombreCliente;  
    private static List<String> nifCliente; 
    private static List<String> numeroFactura;  
    private static List<String> fechaFactura;  
    private static List<String> totalFactura; 
    
    public static void inicio(){
        //pedir ruta
        File ruta = ES.pideDirectorio();
        //iterar entre los archivos del directorio
        File [] archivos = ruta.listFiles();
        //creamos los arrayList de los datos
        nombreProveedor = new ArrayList<String>(archivos.length); 
        cifProveedor = new ArrayList<String>(archivos.length); 
        nombreCliente = new ArrayList<String>(archivos.length);  
        nifCliente = new ArrayList<String>(archivos.length); 
        numeroFactura = new ArrayList<String>(archivos.length);  
        fechaFactura = new ArrayList<String>(archivos.length);  
        totalFactura = new ArrayList<String>(archivos.length); 
        //numero de archivos analizados
        int numArchivo = 0;

        Vector<String> palabrasArchivo;
        for (int i = 0; i < archivos.length; i++){
            if (archivos[i].isFile() && archivos[i].getName().endsWith(".pdf")){ //esta línea se salta otros directorios / carpetas o archivos que no sean pdf
                //extraer texto del pdf
                palabrasArchivo = extraeTextoPalabras(archivos[i]);
                
                //iterar entre las palabras del texto
                for(String palabra: palabrasArchivo){
                    //método para analizar palabra a palabra
                    analizaPalabra(palabra,numArchivo);   
                }
                
                //comprobamos si algún dato no se ha encontrado y marcamos el error
                compruebaFaltas(numArchivo);
                //pasamos al siguiente archivo
                numArchivo++;
            }
        }
        //crear un archivo nuevo con los datos organizados (proximamente), de momento muestra los datos por pantalla
        System.out.println("\nnombreProveedor: " + nombreProveedor.toString() + "\ncifProveedor" + 
                cifProveedor.toString() + "\nnombreCliente" + nombreCliente.toString() + "\nnifCliente" + 
                nifCliente.toString() + "\nnumeroFactura" + numeroFactura.toString() + "\nfechaFactura" + 
                fechaFactura.toString() + "\ntotalFactura" + totalFactura.toString());
    }
    
    /** Función que devuelve un vector de Strings formado por las palabras del archivo
    pdf recibido por parámetro.
    * @param archivo el pdf de donde se extraen las palabras, como File
    * @return un Vector<String> con las palabras extraídas
    */
    private static Vector<String> extraeTextoPalabras(File archivo){
        Archivo actual = new Archivo(archivo);
        return actual.pdfExtraeTextoPalabras();
    }
    
    /**Función que analiza la palabra que recibe por parámetro y la almacena en el String[]
     * dato correspondiente.
     * @param palabra el dato a analizar, como String
     * @param numArchivo el archivo que estamos analizando, como int
     */
    private static void analizaPalabra(String palabra, int numArchivo){

        if(fechaFactura.size() == numArchivo && Validador.esFecha(palabra)){ //comprobamos que no haya otra fechaañadido procedente de este archivo y comprobamos que la palabra sea una fecha
            fechaFactura.add(palabra);
        }
        
        palabra = soloCaracteresAlfanumericos(palabra);
        if(nombreProveedor.size() == numArchivo && Validador.esNombre(palabra)){ //comprobamos que no haya otro nombre añadido procedente de este archivo y comprobamos que la palabra sea un nombre
            nombreProveedor.add(palabra);
        }
        
        if(cifProveedor.size() == numArchivo && Validador.esCif(palabra)){ //comprobamos que no haya otro cif añadido procedente de este archivo y comprobamos que la palabra sea un cif
            cifProveedor.add(palabra);
        }
        
        if(nifCliente.size() == numArchivo && Validador.esNif(palabra)){ //comprobamos que no haya otro nif añadido procedente de este archivo y comprobamos que la palabra sea un nif
            nifCliente.add(palabra);
        }
        
        
    }
    
    /**Función que comprueba si no se ha encontrado algún dato del archivo y marca
     * el error.
     * @param numArchivo el archivo que se ha analizado, como int
     */
    private static void compruebaFaltas(int numArchivo){
        if(nombreProveedor.size() == numArchivo){
            nombreProveedor.add("*ERROR");
        }
        
        if(cifProveedor.size() == numArchivo){
            cifProveedor.add("*ERROR");
        }
        
        if(nifCliente.size() == numArchivo){
            nifCliente.add("*ERROR");
        }
        
        if(fechaFactura.size() == numArchivo){
            fechaFactura.add("*ERROR");
        }
      
    }
    
    /**Función que elimina los caracteres especiales de un String para facilitar su análisis.
     * @param entrada la palabra a trabajar, como String
     * @return la entrada sin caracteres especiales, como String
     */
    private static String eliminaCaracteresEspeciales(String entrada){
        return entrada.replaceAll("\\W+","");
    }
    
    /**Función que deja solo los caracteres alfanuméricos de un String para facilitar su análisis.
     * @param entrada la palabra a trabajar, como String
     * @return la entrada con solo caracteres alfanuméricos, como String
     */
    private static String soloCaracteresAlfanumericos(String entrada){
        return entrada.replaceAll("[^A-Za-z0-9]","");
    }
    
}
