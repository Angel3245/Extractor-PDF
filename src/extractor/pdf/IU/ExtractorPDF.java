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
                
                //buscarNombre(palabrasArchivo, numArchivo); //Al llegar aquí palabrasArchivo está vacío, no podemos buscar nada
                
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
        String nom = "";
        char letra;

        int i = 0;
        letra = texto.get(i).charAt(0);
        while (i < texto.size() && !Validador.esMayuscula(letra)) {
            letra = texto.get(i).charAt(0);
            i++;
        }
        //Al salir ha localizado una mayúscula en la posic i-1
        int comienzoNombre=-1;
        if (i == 0) {
            comienzoNombre = 0;
        } else {
            comienzoNombre = i - 1;
        }

        int finNombre = -1;

        while (i < texto.size() && finNombre == -1) {
            if (Validador.esMinuscula(texto.get(i).charAt(0))) {
                if (texto.get(i).equals("de") || texto.get(i).equals("del") || texto.get(i).equals("la")
                        || texto.get(i).equals("los") || texto.get(i).equals("las") || texto.get(i).equals("y")
                        || texto.get(i).equals("do") || texto.get(i).equals("da") || texto.get(i).equals("dos")
                        || texto.get(i).equals("das") || texto.get(i).equals("e")) {
                    if (!Validador.esMinuscula(texto.get(i + 1).charAt(0))) {
                        i++;
                    } else {
                        //El nombre se ha acabado
                        finNombre = i - 1;
                    }
                } else {
                    //El nombre se ha acabado
                    finNombre = i - 1;
                }
            } else {
                i++;
            }

        }

        if (i >= texto.size()) {
            finNombre = texto.size() - 1;
        }

        if (comienzoNombre != -1) {
            int n = comienzoNombre;
            while (n <= finNombre) {
                nom += texto.get(n) + " ";
                n++;
            }
            nom = nom.trim();

        } else {
            return false;
        }

        if (nombreProveedor.size() == numArchivo && Validador.esNombre(nom)) {
            nombreProveedor.add(nom);
            return true;
        }
        return false;
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
