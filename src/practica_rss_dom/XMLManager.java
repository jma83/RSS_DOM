/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_rss_dom;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author Javier Martinez Arias
 */
public class XMLManager {
    private static final String ELEMENT_ITEM = "item";
    private static final String ELEMENT_TITULO = "title";
    private static final String ELEMENT_URL = "link";
    private static final String ELEMENT_DESCRIPCION = "description";
    private static final String ELEMENT_FECHA_PUB = "pubDate";
    private static final String ELEMENT_CATEGORIA = "category";
    private static final String ELEMENT_NOTICIAS = "noticias";
    private static final String ELEMENT_NOTICIA = "noticia";
    private static final String ELEMENT_CANAL = "canal";
    
    DocumentBuilder docBuilder;
    Document doc = null;
    String ficheroOut = "";
    
    public XMLManager(String url){
        leerFichero(url);
    }
    
    public void leerFichero(String urlString) {
        DocumentBuilderFactory dbf;
        Element title = null;
        String salida;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            docBuilder = dbf.newDocumentBuilder();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream input = conn.getInputStream();
            doc = docBuilder.parse(input);

            title = (Element) doc.getElementsByTagName(ELEMENT_TITULO).item(0);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.getStackTrace();
        }
        salida = this.cleanString(this.getTextNode(title));
        ficheroOut = ELEMENT_NOTICIAS + "_" + salida;
    }
    
    public void mostrarInfoPrincipal() {
        if (doc != null) {
            Element title = (Element) doc.getElementsByTagName(ELEMENT_TITULO).item(0);
            Element url = (Element) doc.getElementsByTagName(ELEMENT_URL).item(0);
            Element desc = (Element) doc.getElementsByTagName(ELEMENT_DESCRIPCION).item(0);
            System.out.println("**INFORMACIÓN GENERAL**");
            System.out.println("Titulo: " + this.getTextNode(title));
            System.out.println("URL: " + this.getTextNode(url));
            System.out.println("Descripción: " + this.getTextNode(desc));
            System.out.println();
        }
    }

    public void mostrarNoticias() {
        //TITULO, URL, DESCRIPCION, FECHA DE PUBLICACION Y CATEGORIA
        if (doc != null) {
            System.out.println("**NOTICIAS**");

            NodeList noticias = doc.getElementsByTagName(ELEMENT_ITEM);
            for (int i = 0; i < noticias.getLength(); i++) {
                Element elem_noticia = (Element) noticias.item(i);
                Element title = (Element) elem_noticia.getElementsByTagName(ELEMENT_TITULO).item(0);
                Element url = (Element) elem_noticia.getElementsByTagName(ELEMENT_URL).item(0);
                Element desc = (Element) elem_noticia.getElementsByTagName(ELEMENT_DESCRIPCION).item(0);
                Element fecha = (Element) elem_noticia.getElementsByTagName(ELEMENT_FECHA_PUB).item(0);

                System.out.println("Noticia nº: " + (i + 1));
                System.out.println("Titulo: " + this.getTextNode(title));
                System.out.println("URL: " + this.getTextNode(url));
                System.out.println("Descripción: " + this.getTextNode(desc));
                System.out.println("Fecha: " + this.getTextNode(fecha));
                System.out.println("Categorias: " + this.getCategorias(elem_noticia));
                System.out.println();
            }
        }
    }

    private String getCategorias(Element elem) {
        NodeList categorias = elem.getElementsByTagName(ELEMENT_CATEGORIA);
        String categoriasStr = "";

        for (int i = 0; i < categorias.getLength(); i++) {
            String categoriaStr = this.getTextNode((Element) categorias.item(i));
            categoriasStr = categoriasStr + categoriaStr;
            if (categorias.getLength() - 1 != i) {
                categoriasStr = categoriasStr + ", ";
            }
        }

        return categoriasStr;
    }

    public void crearDocXML() {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer(); //si es una hoja de estilos aquí se pasa la hoja de estilos
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            //transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"fichero.dtd");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            Document doc_out = this.generarDocNoticias();

            transformer.transform(new DOMSource(doc_out), new StreamResult(new File(ficheroOut + ".xml")));

        } catch (TransformerConfigurationException ex) {
            System.err.println("ERROR! " + ex.getMessage());
            ex.getStackTrace();
        } catch (TransformerException ex) {
            System.err.println("ERROR! " + ex.getMessage());
            ex.getStackTrace();
        }
    }

    private Document generarDocNoticias() {
        NodeList noticias = doc.getElementsByTagName(ELEMENT_ITEM);
        Document docResult = docBuilder.newDocument();

        Element lista_noticiasSalida = docResult.createElement(ELEMENT_NOTICIAS);
        Element titleEntrada = (Element) doc.getElementsByTagName(ELEMENT_TITULO).item(0);
        lista_noticiasSalida.setAttribute(ELEMENT_CANAL, this.getTextNode(titleEntrada));
        docResult.appendChild(lista_noticiasSalida);

        for (int i = 0; i < noticias.getLength(); i++) {
            Element noticia = (Element) noticias.item(i);
            Element titulo = (Element) noticia.getElementsByTagName(ELEMENT_TITULO).item(0);

            Element elem_noticiaSalida = docResult.createElement(ELEMENT_NOTICIA);
            Text tituloStr = docResult.createTextNode(getTextNode(titulo));
            elem_noticiaSalida.appendChild(tituloStr);
            lista_noticiasSalida.appendChild(elem_noticiaSalida);

        }
        return docResult;
    }
    
    private String cleanString(String str) {
        String result = str.replaceAll("[^a-zA-Z0-9]", "");
        result = result.replaceAll("\\s", "");
        return result;
    }
    
    private String getTextNode(Element e) {
        if (e != null && e.getChildNodes()!=null && e.getChildNodes().item(0)!=null)
            return e.getChildNodes().item(0).getNodeValue();
        else
            return "";
    }
    
    public Document getDocument(){
        return doc;
    }
    public String getFicheroOut(){
        return ficheroOut;
    }
    
}
