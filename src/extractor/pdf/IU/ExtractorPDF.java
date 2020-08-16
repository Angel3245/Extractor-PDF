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
    //creamos una variable temporal para almacenar el total
    private static double totalTemp = 0.00;
    
    public static void inicio(){
        //pedir ruta
        File ruta = ES.pideDirectorio();
        //listar los archivos del directorio
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
        //iterar entre los archivos
        for (int i = 0; i < archivos.length; i++){
            if (archivos[i].isFile() && archivos[i].getName().endsWith(".pdf")){ //esta línea se salta otros directorios / carpetas o archivos que no sean pdf
                
                //extraer texto del pdf
                palabrasArchivo = extraeTextoPalabras(archivos[i]);
                
                //iterar entre las palabras del texto
                int e = 0;
                //for(String palabra: palabrasArchivo){ (Con forEach, no eficiente)
                while(palabrasArchivo.size() > e && !datosExtraidos(numArchivo)){
                    //método para analizar palabra a palabra
                    analizaPalabra(palabrasArchivo.get(e),numArchivo); //Eliminamos el primer elemento del vector de Strings hasta que no quede ningún elemento, usando un contador nos saldríamos del array
                    e++;
                }
                
                buscarNombre(palabrasArchivo, numArchivo);
                
                if(totalTemp > 0.0){ //Si hemos encontrado el total lo añadimos
                    totalFactura.add(String.valueOf(totalTemp));
                }
                
                //comprobamos si algún dato no se ha encontrado y marcamos el error
                arreglaFaltas(numArchivo);
                
                //contamos cuantos archivos llevamos analizados
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
     * @return true o false dependiendo de si la palabra analizada es un dato o no
     */
    private static boolean analizaPalabra(String palabra, int numArchivo){

        if(fechaFactura.size() == numArchivo && Validador.esFecha(palabra)){ //comprobamos que no haya otra fecha añadida procedente de este archivo y comprobamos que la palabra sea una fecha
            fechaFactura.add(palabra);
            return true;
        }
        
        palabra = soloCaracteresAlfanumericos(palabra); 
// Un nombre son varias palabras por lo que se comprueba a parte
//        if(nombreProveedor.size() == numArchivo && Validador.esNombre(palabra)){ //comprobamos que no haya otro nombre añadido procedente de este archivo y comprobamos que la palabra sea un nombre
//            nombreProveedor.add(palabra);
//            return true;
//        }
        
        if(cifProveedor.size() == numArchivo && Validador.esCif(palabra)){ //comprobamos que no haya otro cif añadido procedente de este archivo y comprobamos que la palabra sea un cif
            cifProveedor.add(palabra);
            return true;
        }
        
        if(nifCliente.size() == numArchivo && Validador.esNif(palabra)){ //comprobamos que no haya otro nif añadido procedente de este archivo y comprobamos que la palabra sea un nif
            nifCliente.add(palabra);
            return true;
        }
        
        if(Validador.esTotal(palabra) && Double.parseDouble(palabra) > totalTemp){
            totalTemp = Double.parseDouble(palabra);
            return true;
        }
        
        return false;
        
        
    }
    
    /**Función que busca un nombre en el Vector que recibe por parámetro y almacena en el String[]
     * dato correspondiente.
     * @param texto el Vector de Strings con todas las palabras 
     * @param numArchivo el archivo que estamos analizando, como int
     * @return true o false dependiendo de si se encuentra y almacena un nombre o no
     */
    private static boolean buscarNombre(Vector<String> texto, int numArchivo) {

        //Eliminar las palabras DE, DEL, LA, LOS, LAS, Y, DO, DA, DOS, DAS, E del texto
        int numPal = texto.size();

        for (int i = 0; i < numPal; i++) {

            if (texto.get(i).equals("DE") || texto.get(i).equals("DEL") || texto.get(i).equals("LA")
                    || texto.get(i).equals("LOS") || texto.get(i).equals("LAS") || texto.get(i).equals("Y")
                    || texto.get(i).equals("DO") || texto.get(i).equals("DA") || texto.get(i).equals("DOS")
                    || texto.get(i).equals("DAS") || texto.get(i).equals("E")) {
                for (int e = i + 1; e < texto.size(); e++) {
                    texto.remove(e - 1);
                }
                numPal--;
                i = i - 1;
            }

        }

        //Eliminar los posibles guiones de un nombre   
//        for (int i = 0; i < numPal; i++) {
//            texto.add(i, soloCaracteresAlfanumericos(texto.remove(i)));
//        }

        for(int i=0; i<numPal;i++){
            System.out.println(texto.get(i));
        }
        
        //Buscamos el nombre
        String nombre = "";
        int e = 0;

        while (e < numPal && !nomEncontrado(nombre)) {

            if (Validador.esNombre(texto.get(e)) || Validador.esApell(texto.get(e))) {
                nombre += texto.get(e);
                e++;
            } else {
                nombre = "";
                e++;
            }

        }

        if (nombreProveedor.size() == numArchivo) { //comprobamos que no haya otro nombre añadido procedente de este archivo
            nombreProveedor.add(nombre);
            return true;
        }

        return false;

    }

    /**Función que comprueba si ya se ha encontrado el nombre verificando si las 
     * dos últimas palabras son apellido
     * @param nom el nombre a comprobar, como String
     * @return true si el nombre está completo, false en caso contrario
     */
    private static boolean nomEncontrado(String nom) {
        nom = nom.trim();
        String[] nombre = nom.split(" ");
        if (nombre.length < 3) {
            return false;
        } else if (!Validador.esApell(nombre[nombre.length - 1]) || !Validador.esApell(nombre[nombre.length - 2])) {
            return false;
        }
        return true;
    }
    
    
    /**Función que comprueba si están todos los datos extraídos.
     * @param numArchivo el archivo que se ha analizado, como int
     * @return true o false dependiendo de si están todos los datos extraídos o no
     */
    private static boolean datosExtraidos(int numArchivo){
        if(nombreProveedor.size() == numArchivo || cifProveedor.size() == numArchivo || nifCliente.size() == numArchivo || fechaFactura.size() == numArchivo || totalFactura.size() == numArchivo){
            return false;
        }
        return true;
    }
    
    /**Función que comprueba si no se ha encontrado algún dato del archivo y marca
     * el error.
     * @param numArchivo el archivo que se ha analizado, como int
     */
    private static void arreglaFaltas(int numArchivo){
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
        
        if(totalFactura.size() == numArchivo){
            totalFactura.add("*ERROR");
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
