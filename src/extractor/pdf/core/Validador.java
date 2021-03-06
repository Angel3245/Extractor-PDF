/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdf.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Datos a validar:
 *
 * - Nombre proveedor - Cif proveedor
 *
 * - Nombre cliente - nif cliente
 *
 * - numero factura - fecha factura
 *
 * - total factura
 *
 * @author Angel
 */
public abstract class Validador {

    public enum TipoUltCaracter { //para la función esCif
        LETRA, NUMERO, AMBOS
    };

    public static boolean esTotal(String entrada) {
        if (entrada.contains(",") || entrada.contains(".")) { //Si la parte decimal está expresada con coma la transformamos en punto
            entrada = entrada.replace(",", ".");
            try {
                Double.parseDouble(entrada); //Intentamos convertir el dato en un double, si salta una excepción sabemos que no es un posible total
                return true;
            } catch (Exception exc) {
                return false;
            }
        }
        return false;
    }

    public static boolean esDni(String entrada) {
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

    public static boolean esNif(String entrada) { //Similar a la funciÃ³n anterior
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

    public static boolean esCif(String entrada) {

        if (entrada != null && !entrada.equals("") ) {
            final String cifUP = entrada.toUpperCase();

            if ("ABCDEFGHJKLMNPQRSUVW".indexOf(cifUP.charAt(0)) == -1) {
                return false;
            }

            final Pattern mask = Pattern
                    .compile("[ABCDEFGHJKLMNPQRSUVW][0-9]{7}[A-Z[0-9]]{1}");
            final Matcher matcher = mask.matcher(cifUP);

            if (!matcher.matches()) {
                return false;
            }

            final char primerCar = cifUP.charAt(0);
            final char ultimoCar = cifUP.charAt(cifUP.length() - 1);

            TipoUltCaracter tipUltCar;

            if (primerCar == 'P' || primerCar == 'Q' || primerCar == 'S' || primerCar == 'K' || primerCar == 'W') {
                tipUltCar = TipoUltCaracter.LETRA;
                if (!(ultimoCar >= 'A' && ultimoCar <= 'Z')) {
                    return false;
                }

            } else if (primerCar == 'A' || primerCar == 'B' || primerCar == 'E'
                    || primerCar == 'H') {
                tipUltCar = TipoUltCaracter.NUMERO;
                if (!(ultimoCar >= '0' && ultimoCar <= '9')) {
                    return false;
                }

            } else {
                tipUltCar = TipoUltCaracter.AMBOS;
            }

            final String digitos = cifUP.substring(1, cifUP.length() - 1);

            Integer sumaPares = 0;
            for (int i = 1; i <= digitos.length() - 1; i = i + 2) {
                sumaPares += Integer.parseInt(digitos.substring(i, i + 1));
            }

            Integer sumaImpares = 0;
            for (int i = 0; i <= digitos.length() - 1; i = i + 2) {
                Integer cal = Integer.parseInt(digitos.substring(i, i + 1)) * 2;
                if (cal.toString().length() > 1) {
                    cal = Integer.parseInt(cal.toString().substring(0, 1))
                            + Integer.parseInt(cal.toString().substring(1, 2));
                }
                sumaImpares += cal;
            }

            final Integer total = sumaPares + sumaImpares;

            Integer numControl = 10 - (total % 10);

            /*if (numControl == 10){
            numControl = 0;
            }*/
            int pos = numControl == 10 ? 0 : numControl;

            final char carControl = "JABCDEFGHI".charAt(pos);

            if (tipUltCar == TipoUltCaracter.NUMERO) {

                final Integer ultCar = Integer.parseInt(Character
                        .toString(ultimoCar));
                if (pos != ultCar.intValue()) {

                    return false;
                }

            } else if (tipUltCar == TipoUltCaracter.LETRA) {
                if (carControl != ultimoCar) {
                    return false;
                }

            } else {
                // find all occurrences forward
                Integer ultCar = -1;

                ultCar = "JABCDEFGHI".indexOf(ultimoCar);

                if (ultCar < 0) {
                    ultCar = Integer.parseInt(Character.toString(ultimoCar));
                    if (pos != ultCar.intValue()) {
                        return false;
                    }
                }
                if ((pos != ultCar.intValue()) && (carControl != ultimoCar)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean esFecha(String entrada) {
        boolean toret = false;
        String[] fecha;

        try {
            fecha = entrada.split("/");

            if (fecha.length != 3) {
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

    
    public static boolean esNombre(String palabra) {
        if (esMayuscula(palabra.charAt(0))||esMinuscula(palabra.charAt(0))) {
        
            File nombresPosibles = new File("src/extractor/pdf/core/nombres.txt");
            String[] nomPos = extraeTextoPalabrasTxt(nombresPosibles, "\n");

//        for(int i=0; i<nomPos.length; i++){
//            System.out.println(nomPos[i]);
//        }
            int inicio = 0;
            int fin = nomPos.length - 1;
            int medio = 0;
            System.out.println(inicio+"-"+medio+"-"+fin);
            System.out.println(palabra);
            while (nomPos[inicio].compareTo(nomPos[fin]) <= 0) {
                medio = (inicio + fin) / 2;
                if (nomPos[medio].compareTo(palabra) < 0) {
                    inicio = medio + 1;
                } else {
                    if (nomPos[medio].compareTo(palabra) > 0) {
                        fin = medio - 1;
                    } else {
                        return true;//Está en la pos medio
                    }
                }
            }

        }
        return false; //No está
    }
    
    
    public static boolean esApell(String palabra) {

        if (esMayuscula(palabra.charAt(0))||esMinuscula(palabra.charAt(0))) {

            File apellidosPosibles = new File("src/extractor/pdf/core/apellidos.txt");
            String[] apellPos = extraeTextoPalabrasTxt(apellidosPosibles, " ");

//        for (int i = 0; i < apellPos.length; i++) {
//            System.out.println(apellPos[i]);
//        }
            int inicio = 0;
            int fin = apellPos.length - 1;
            int medio;
            while (apellPos[inicio].compareTo(apellPos[fin]) <= 0) {
                medio = (inicio + fin) / 2;
                if (apellPos[medio].compareTo(palabra) < 0) {
                    inicio = medio + 1;
                } else {
                    if (apellPos[medio].compareTo(palabra) > 0) {
                        fin = medio - 1;
                    } else {
                        return true;//Está en la pos medio
                    }
                }
            }
        }
        return false; //No está

    }

    private static boolean esMayuscula(String palabra, int pos) {
        char letr = palabra.charAt(pos);
        return esMayuscula(letr);

    }
    
    public static boolean esMayuscula(char letr) {
        return ((letr >= 'A' && letr <= 'Z') || letr == 'Á'
                || letr == 'É' || letr == 'Í' || letr == 'Ó' || letr == 'Ú');

    }

    private static boolean esMinuscula(String palabra, int pos) {
        char letr = palabra.charAt(pos);
        return esMinuscula(letr);

    }
    
    public static boolean esMinuscula(char letr) {
        return ((letr >= 'a' && letr <= 'z') || letr == 'á'
                || letr == 'é' || letr == 'í' || letr == 'ó' || letr == 'ú');

    }
    
    /**
     * Función que devuelve un array de Strings formado por las palabras del
     * archivo txt recibido por parámetro.
     *
     * @param archivo el txt de donde se extraen las palabras, como File
     * @return un array de Strings con las palabras extraídas
     */
    private static String[] extraeTextoPalabrasTxt(File archivo, String splt) {
        Archivo actual = new Archivo(archivo);

        return actual.txtExtraeTextoPalabras(splt);
    }

}
