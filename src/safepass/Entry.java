/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package safepass;

import java.text.SimpleDateFormat;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Pankti
 */
public class Entry {
    public final int id;
    static public byte[] key = null;
    private String website;
    private String login;
    private String pass;
    private String comment;
    private String date;
    
    public Entry(final String website, final String login, final String pass, final String comment) {
        id = 0;
        this.website = website;
        this.login = login;
        this.pass = Crypto.encode(key, pass);
        this.comment = comment;
        updDate();
    }
    public Entry(Node node) {
        this((Element)node);
    }    
    public Entry(Element node) {
        this(node, 0);
    }
    public Entry(Element node, int id) {
        this.id = id;
        website = node.getElementsByTagName("website").item(0).getTextContent();
        website = Crypto.decode(key, website);
        login = node.getElementsByTagName("login").item(0).getTextContent();
        login = Crypto.decode(key, login);
        pass = node.getElementsByTagName("pass").item(0).getTextContent();
        comment = node.getElementsByTagName("comment").item(0).getTextContent();
        comment = Crypto.decode(key, comment);
        date = node.getElementsByTagName("date").item(0).getTextContent();
    }
    
    public String getSite() {return website;}
    public String getLogin() {return login;}
    public String getComment() {return comment;}
    public String getDate() {return date;}
    
    static private void add(Element el, final String tag, final String text) {
        Element tmp = el.getOwnerDocument().createElement(tag);
        tmp.setTextContent(text);
        el.appendChild(tmp);
    }
    
    public Element toElement(Document doc, String name) {
        Element el = doc.createElement(name);
        add(el, "website", Crypto.encode(key, website));
        add(el, "login", Crypto.encode(key, login));
        add(el, "pass", pass);
        add(el, "comment", Crypto.encode(key, comment));
        add(el, "date", date);
        return el;       
    }
    
    public Element toElement(Document doc, String name, byte[] newKey) {
        Element el = doc.createElement(name);
        add(el, "website", Crypto.encode(newKey, website));
        add(el, "login", Crypto.encode(newKey, login));
        add(el, "pass", Crypto.encode(newKey, Crypto.decode(key, pass)));
        add(el, "comment", Crypto.encode(newKey, comment));
        add(el, "date", date);
        return el;       
    }
    
    public Boolean like(final String str) {
        return website.contains(str) || login.contains(str) || comment.contains(str);
    }
    
    public String[] toArray() {
        return new String[] {website, login, comment, date};
    }
    
    public String name() {
        return website + " (" + login + ")";
    }
    
    public String getPassword() {
        return Crypto.decode(key, pass);
    }  
    
    private void updDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        date = fmt.format(System.currentTimeMillis());
    }
}
