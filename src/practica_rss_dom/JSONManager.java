/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica_rss_dom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONML;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Javier Martinez Arias
 */
public class JSONManager {

    private static final String ELEMENT_ITEM = "item";
    private static final String ELEMENT_TITULO = "title";
    private static final String ELEMENT_NOTICIAS = "noticias";
    private static final String ELEMENT_NOTICIA = "noticia";

    private Document doc;

    public JSONManager(Document doc) {
        this.doc = doc;
    }

    public void crearDocJSON(boolean leerFicheroSalidaXML, String ficheroOut) {
        String s;

        try {
            if (leerFicheroSalidaXML) {
                s = leerFicheroSalidaXML(ficheroOut);
            } else {
                s = generarDocNoticiasJSON();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroOut + ".json"));
            bw.write(s);
            bw.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ex.getStackTrace();
        }

    }

    public String leerFicheroSalidaXML(String ficheroOut) {
        FileReader fr;
        String textXML = new String();
        JSONML jsonXML = new JSONML();
        JSONObject json_out;

        try {
            fr = new FileReader(ficheroOut + ".xml");
            BufferedReader bf = new BufferedReader(fr);

            String cadena = bf.readLine();
            while (cadena != null) {
                textXML = textXML + cadena;
                cadena = bf.readLine();
            }
            fr.close();

            json_out = jsonXML.toJSONObject(textXML);
            return json_out.toString();

        } catch (FileNotFoundException ex) {
            System.err.println("ERROR! " + ex.getMessage());
            ex.getStackTrace();
        } catch (JSONException | IOException ex) {
            System.err.println("ERROR! " + ex.getMessage());
            ex.getStackTrace();
        }
        return "";
    }

    public String generarDocNoticiasJSON() {
        NodeList noticias = doc.getElementsByTagName(ELEMENT_ITEM);

        String jsonString = "{\"" + ELEMENT_NOTICIAS + "\":[";

        for (int i = 0; i < noticias.getLength(); i++) {
            Element noticia = (Element) noticias.item(i);
            Element titulo = (Element) noticia.getElementsByTagName(ELEMENT_TITULO).item(0);
            String s = getTextNode(titulo);
            jsonString += "{\"" + ELEMENT_NOTICIA + "\":\"" + cleanJSONFormat(s) + "\"}";
            if (noticias.getLength() - 1 != i) {
                jsonString = jsonString + ",\n";
            }
        }
        jsonString += "]}";
        System.out.println("JSON: " + jsonString);

        try {
            JSONObject json = new JSONObject(jsonString);
            return json.toString();

        } catch (JSONException ex) {
            System.err.println(ex.getMessage());
            ex.getStackTrace();
        }
        return "";
    }

    public String getTextNode(Element e) {
        return e.getChildNodes().item(0).getNodeValue();
    }

    public String cleanJSONFormat(String str) {
        String result = str.replaceAll("\"", "'");
        return result;
    }
}
