/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_rss_dom;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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

        String urlEntrada = "https://e00-elmundo.uecdn.es/elmundo/rss/portada.xml";
        String ficheroOut = "salida.xml";
        Practica_RSS_DOM practica = new Practica_RSS_DOM();
        practica.leerFichero(urlEntrada);
    }

    public void leerFichero(String urlString) {
        DocumentBuilderFactory dbf;
        DocumentBuilder docBuilder;
        Document doc;
        System.out.println("practica_rss_dom.Practica_RSS_DOM.leerFichero()");
        try {
            dbf = DocumentBuilderFactory.newInstance();
            //dbf.setValidating(true);
            dbf.setIgnoringElementContentWhitespace(true);
            docBuilder = dbf.newDocumentBuilder();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            doc = docBuilder.parse(conn.getInputStream());

            NodeList title = doc.getElementsByTagName("title");
            System.out.println("A ver: " + this.getTextNode((Element) title.item(0)));
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.getStackTrace();
        }
    }

    public String getTextNode(Element e) {
        return e.getChildNodes().item(0).getNodeValue();
    }
}
