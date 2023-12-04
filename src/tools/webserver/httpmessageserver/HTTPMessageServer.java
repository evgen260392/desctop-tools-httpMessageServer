/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.webserver.httpmessageserver;

import com.sun.net.httpserver.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
       
/**
 *
 * @author  Evgeny Martynov
 * @version 1.0
 */
public class HTTPMessageServer {
    
    private static final String CONFIG_FILE_PATH = "config.xml";
    
    private static Config Config; 
    
    /**
     * Класс реализующий авторизацию пользователя
     */
    private static class Authenticator extends BasicAuthenticator {

        public Authenticator(String string) {
            super(string);
        }

        @Override
        public boolean checkCredentials(String user, String pswd) {
            return user.equals(Config.getLogin()) && pswd.equals(Config.getPassword());
        }
        
    }
            
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws Exception {
        
        try {
            
            Config = Config.getInstance (CONFIG_FILE_PATH);
            
            ArrayList<String> contextList = Config.getContextList();
            
            if (!contextList.isEmpty()) {
            
                System.out.println("Init server...");
                
                HttpServer server = HttpServer.create(new InetSocketAddress(Config.getPort()), 5);
            
                for (String context : contextList) {
                    
                    server.createContext("/" + context, new QueueHandler())
                          .setAuthenticator(new Authenticator(context));                    
                
                    System.out.println("Context: " + context + " added!");
                }
                
                ExecutorService executor = Executors.newFixedThreadPool(5);
                
                server.setExecutor(executor);
                server.start();
                
                System.out.println("Server started on port " + Config.getPort());
                System.out.println("------------------------------------------");
                
            } else System.out.println("Contexts is Empty!");
            
        } catch (Exception ex) {
            
            System.out.println("Application is stopped!");
            System.out.println("An exception occurred: " + ex.toString());
            
            ex.printStackTrace(System.out);
            
            System.out.println("Press \"Enter\" to exit");
            System.in.read();
        }
    }
}