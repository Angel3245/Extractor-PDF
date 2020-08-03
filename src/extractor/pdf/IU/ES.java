/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor.pdf.IU;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Angel
 */
public abstract class ES {
    
    public static File pideDirectorio(){
        File directorioElegido = new File("");
        JFileChooser fc = new JFileChooser();
        //Mostrar la ventana para abrir directorio y recoger la respuesta
        //En el parámetro del showOpenDialog se indica la ventana
        //  al que estará asociado. Con el valor this se asocia a la
        //  ventana que la abre.
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); //configuramos como única respuesta posible un directorio
        int respuesta = fc.showOpenDialog(fc);
        //Comprobar si se ha pulsado Aceptar
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            //Crear un objeto File con el archivo elegido
            directorioElegido = fc.getSelectedFile();
            
        }
        
        return directorioElegido;
    }
}
