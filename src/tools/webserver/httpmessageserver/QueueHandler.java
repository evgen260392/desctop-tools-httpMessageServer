/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.webserver.httpmessageserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Evgeny Martynov
 * @version 1.0
 * 
 * Класс реализует буффер html сообщений.
 * Запроса HTTP GET возвращает последние сообщение из буфера и удаляет его.
 * СЗапрос HTTP POST записывает в буфер новое сообщение.
 */
public class QueueHandler implements HttpHandler{
     
    Queue<String>   queue;
    
    public QueueHandler () {
        
        queue = new LinkedList<>();               
    }
    
    @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            
            String responce;
            
            switch (httpExchange.getRequestMethod()) {
            
                case "GET":
                    
                    // Read command from queue
                    responce = queue.poll();
                    
                    // Is empty queue
                    if (responce == null){
                        
                        responce = "Queue is empty!";
                        
                    }
                    
                    // Send responce
                    httpExchange.sendResponseHeaders(200, responce.length());
                    httpExchange.getResponseBody().write(responce.getBytes());                    
                    
                    System.out.println("Method <GET>");
                    System.out.println("Context <" + httpExchange.getRequestURI().getPath() + ">");
                    System.out.println("Response:");                   
                    System.out.println(responce);
                    System.out.println("------------------------------------------");
                    System.out.println("");
                    
                    break;
                
                case "POST":
                    
                    // Read request body
                    StringBuilder sb = new StringBuilder();
                    
                    for (int ch; (ch = httpExchange.getRequestBody().read()) != -1; ) {
                        sb.append((char) ch);
                    }
                    
                    // Add command in queue
                    queue.offer(sb.toString());
                    
                    responce = "Request accepted!";
                    
                    // Send responce
                    httpExchange.sendResponseHeaders(200, responce.length());
                    httpExchange.getResponseBody().write(responce.getBytes());
                    
                    System.out.println("Method <POST>");
                    System.out.println("Context <" + httpExchange.getRequestURI().getPath() + ">");
                    System.out.println("Message received:" );
                    System.out.println(sb.toString());
                    System.out.println("------------------------------------------");
                    System.out.println("");
                    
                    break;                                                
                
                default:
                    
                    System.out.println("Recive the unknown request!");
                    break;
            }  
             
        }     
}
