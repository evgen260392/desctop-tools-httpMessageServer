/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.webserver.httpmessageserver;

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
/**
 * Класс конфигурации Веб-сервера
 * @author Evgeny Martynov
 */
public class Config {
    
    private static Config Instance;
    
    private String Login;           // Логин пользователя
    private String Password;        // Пароль пользователя
    private int    Port;            // Порт
    
    private ArrayList<String> ContextList; // Список контекстов
    
    /**
     * Создание конфигурации
     * @throws Exception 
     */
    private Config (String path) throws Exception {
    
        Login       = "";
        Password    = "";
        Port        = 80;
        ContextList = new ArrayList();
        
        DocumentBuilder bulder = DocumentBuilderFactory.newInstance()
                                                       .newDocumentBuilder();
    
        Document document   = bulder.parse(path);
        Element  element    = document.getDocumentElement();
    
        Login       = element.getAttribute("login");
        Password    = element.getAttribute("password");
        Port        = Integer.parseInt(element.getAttribute("port"));
        
        NodeList contextList   = document.getElementsByTagName("Context");
        
        for (int i = 0; i <  contextList.getLength(); i++) {
            
            ContextList.add(contextList.item(i).getTextContent());        
        } 
    }

    public String getLogin() {
        return Login;
    }

    public String getPassword() {
        return Password;
    }

    public int getPort() {
        return Port;
    }

    public ArrayList<String> getContextList() {
        return ContextList;
    }
    
    /**
     * Возвращает экземпляр конфигурации
     * @param path - путь к файлу конфигурации
     * @return     - конфигурация 
     * @throws java.lang.Exception 
     */
    public static Config getInstance (String path) throws Exception {
        
        if (Instance == null)     
            Instance = new Config(path);
       
        return Instance;
    }
}
