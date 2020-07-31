/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdf.core;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**Datos a validar:
 * 
 * - Nombre proveedor
   - Cif proveedor

   - Nombre cliente
   - nif cliente

   - numero factura
   - fecha factura

   - total factura
 *
 * @author Angel
 */

public abstract class Validador {
    
   

    public boolean esDni(String entrada) {
        // Array con las letras posibles del dni en su posiciÃ³n
        char[] letraDni = {
            'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'
        };

        String num = "";
        int ind = 0;

        // boolean que nos indicara si es un dni correcto o no
        boolean valido;

        // compruebo su longitud que sea 9
        if (entrada.length() != 9) {
            return false;
        }

        // compruebo que el 9Âº digito es una letra
        if (!Character.isLetter(entrada.charAt(8))) {
            return false;
        }

        // Compruebo que los 8 primeros digitos sean numeros
        for (int i = 0; i < 8; i++) {

            if (!Character.isDigit(entrada.charAt(i))) {
                return false;
            }
            // si es numero, lo recojo en un String
            num += entrada.charAt(i);
        }

        // paso a int el string donde tengo el numero del dni
        ind = Integer.parseInt(num);

        // calculo la posiciÃ³n de la letra en el array que corresponde a este dni
        ind %= 23;

        // verifico que la letra del dni corresponde con la del array
        if ((Character.toUpperCase(entrada.charAt(8))) != letraDni[ind]) {
            return false;
        }
        // si el flujo de la funcion llega aquÃ­, es que el dni es correcto
        return true;

    }

    public boolean esNif(String entrada) { //Similar a la funciÃ³n anterior
        boolean toret = false;
        Pattern pattern = Pattern.compile("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])");
        Matcher matcher = pattern.matcher(entrada);

        if (matcher.matches()) {
            String letra = matcher.group(2);
            String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
            int index = Integer.parseInt(matcher.group(1));
            index = index % 23;
            String reference = letras.substring(index, index + 1);

            if (reference.equalsIgnoreCase(letra)) {
                toret = true;
            }
        }

        return toret;
    }

    public boolean esFecha(String entrada) {
        boolean toret = false;
        String[] fecha;

        try {
            fecha = entrada.split("/");
            
            if(fecha.length != 3){
                return false;
            }
            
            //Formato de fecha (dÃ­a/mes/aÃ±o)
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFecha.setLenient(false);
            //ComprobaciÃ³n de la fecha
            formatoFecha.parse(Integer.parseInt(fecha[0]) + "/" + Integer.parseInt(fecha[1]) + "/" + Integer.parseInt(fecha[2]));
            toret = true;
        } catch (Exception e) {
            //Si la fecha no es correcta, pasarÃ¡ por aquÃ­
            toret = false;
        }

        return toret;
    }
    
    
   
    //entrada: es un String desde una mayúscula hasta la última letra seguida de espacio minúscula
    //si la palabra después de espacio minúscula es :de los//de//del//de la//de las... y después 
    //hay una mayúscula habría que seguir hasta el siguiente espacio minúscula
    public boolean esNombre(String entrada) {

        String array[] = entrada.split(" ");
        int numPal = array.length;

        //elimino las palabras de, del, la, los, las, y, do, da, dos, das, e del array
        for (int i = 0; i < numPal; i++) {
            if (array[i].equals("de") || array[i].equals("del") || array[i].equals("la")
                    || array[i].equals("los") || array[i].equals("las") || array[i].equals("y")
                    || array[i].equals("do") || array[i].equals("da") || array[i].equals("dos")
                    || array[i].equals("das") || array[i].equals("e")) {
                for (int e = i + 1; e < array.length; e++) {
                    array[e - 1] = array[e];
                }
                numPal--;
                i = i - 1;
            }

        }

        for (int i = 0; i < numPal; i++) {
            //compruebo que las palabras sobrantes empiezan por mayúscula
            if (!esMayuscula(array[i], 0)) {
                return false;
            }
            //compruebo que las demás letras sean minúsculas
            for (int e = 1; e < array[i].length(); e++) {
                if (!esMinuscula(array[i], e)) {
                    //si no son minusculas puede ser -May
                    try {
                        if (array[i].charAt(e) == '-' && esMayuscula(array[i], e + 1)) {
                            e++; //correcto y sigo verificando
                        } else {
                            return false;//Si no es minúscula ni -May
                        }
                    } catch (Exception exc) {
                        return false;
                    }
                }
            }

        }

        return true;
    }

    private boolean esMayuscula(String palabra, int pos) {
        char letr = palabra.charAt(pos);
        return ((letr >= 'A' && letr <= 'Z') || letr == 'Á'
                || letr == 'É' || letr == 'Í' || letr == 'Ó' || letr == 'Ú');

    }

    private boolean esMinuscula(String palabra, int pos) {
        char letr = palabra.charAt(pos);
        return ((letr >= 'a' && letr <= 'z') || letr == 'á'
                || letr == 'é' || letr == 'í' || letr == 'ó' || letr == 'ú');

    }
}
