/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_rss_dom;

/**
 *
 * @author Javier Martinez Arias
 */
public class Practica_RSS_DOM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        String[] urlEntrada = {"https://e00-elmundo.uecdn.es/elmundo/rss/portada.xml",
            "https://rss.nytimes.com/services/xml/rss/nyt/World.xml"};
        
        run(urlEntrada[1]);
        //practicaDOM.run(urlEntrada[1]);
        
    }
    
    public static void run(String s){
        XMLManager xManager = new XMLManager(s);
        JSONManager jManager = new JSONManager(xManager.getDocument());

        xManager.mostrarInfoPrincipal();
        xManager.mostrarNoticias();
        xManager.crearDocXML();
        jManager.crearDocJSON(false, xManager.getFicheroOut());
    }

}
