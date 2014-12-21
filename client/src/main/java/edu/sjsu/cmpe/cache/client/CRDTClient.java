package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

public class CRDTClient {
	
	private ArrayList<String> servers;
	int code;
	public AtomicInteger successVar;
	public AtomicInteger getSuccessVar;
	Map<String, Integer> m;
	
	public CRDTClient() {

        servers = new ArrayList(3);
        servers.add("http://localhost:3000");
        servers.add("http://localhost:3001");
        servers.add("http://localhost:3002");
    }
	
	public int put(long key, String value) {
		
		successVar = new AtomicInteger();
		
		for (final String serverUrl : servers) {
			
			try {
	             Thread.sleep(100);                 //1000 milliseconds is one second.
	         	Future<HttpResponse<JsonNode>> future = Unirest.put(serverUrl + "/cache/{key}/{value}")
	      			  .header("accept", "application/json")
	      			  .routeParam("key", Long.toString(key))
	                    .routeParam("value", value)
	      			  .asJsonAsync(new Callback<JsonNode>() {

	      			    public void failed(UnirestException e) {
	      			        System.out.println("The request has failed for Post"+serverUrl);
	      			    }

	      			    public void completed(HttpResponse<JsonNode> response) {
	      			         code = response.getStatus();
	      			        // Map<String, String> headers = response.getHeaders();
	      			        // JsonNode body = response.getBody();
	      			        // InputStream rawBody = response.getRawBody();
	      			         System.out.println("In completion for Post"+code);
	      			         if(code == 200)
	      			         {
	      			        	// successVar++;
	      			        	 successVar.incrementAndGet();
	      			        	 System.out.println("In code for Post"+successVar);
	      			         }
	      			         
	      			    }

	      			    public void cancelled() {
	      			        System.out.println("The request has been cancelled");
	      			    }

	      			});	
	         } catch(InterruptedException ex) {
	             Thread.currentThread().interrupt();
	         }


			
		}
		//return successVar;
		try {
            Thread.sleep(50);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
		return (int)successVar.floatValue();
	
	}
	
	
	public Map<String, Integer> get(long key) {
		
		getSuccessVar = new AtomicInteger();
		m = new HashMap<String, Integer>();
		
	           
	    		for (final String serverUrl : servers) {
	    			try {
	    	            Thread.sleep(150); 
	    			Future<HttpResponse<JsonNode>> future  = Unirest.get(serverUrl + "/cache/{key}")
	                        .header("accept", "application/json")
	                        .routeParam("key", Long.toString(key))
	                        .asJsonAsync(new Callback<JsonNode>(){

	    			    public void failed(UnirestException e) {
	    			        System.out.println("The request has failed");
	    			    }

	    			    public void completed(HttpResponse<JsonNode> response) {
	    			         code = response.getStatus();
	    			        // Map<String, String> headers = response.getHeaders();
	    			        // JsonNode body = response.getBody();
	    			        // InputStream rawBody = response.getRawBody();
	    			         String value = response.getBody().getObject().getString("value");
	    			         System.out.println("In completion for GET"+serverUrl+" "+code);
	    			         if(code == 200)
	    			         {
	    			        	// successVar++;
	    			        	int tempVar = getSuccessVar.incrementAndGet();
	    			        	 m.put(response.getBody().getObject().getString("value"), tempVar);
	    			        	 System.out.println("In code for GET"+successVar+" Map thing"+m.values());
	    			         }
	    			         
	    			    }

	    			    public void cancelled() {
	    			        System.out.println("The request has been cancelled");
	    			    }

	    			});	
	    			
	    		}//1000 milliseconds is one second.
	    			catch(InterruptedException ex) {
	    	            Thread.currentThread().interrupt();
	        } 
	        }
		

		//return successVar;
		try {
            Thread.sleep(50);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
		return m;
	
	}
	
	public int delete(long key) {
		
		for (final String serverUrl : servers) {
			Future<HttpResponse<JsonNode>> future  = Unirest.delete(serverUrl + "/cache/{key}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key))
                    .asJsonAsync(new Callback<JsonNode>(){

			    public void failed(UnirestException e) {
			        System.out.println("The request has failed for DELETE"+serverUrl);
			    }

			    public void completed(HttpResponse<JsonNode> response) {
			         code = response.getStatus();
			        // Map<String, String> headers = response.getHeaders();
			        // JsonNode body = response.getBody();
			        // InputStream rawBody = response.getRawBody();
			         String value = response.getBody().getObject().getString("value");
			         System.out.println("In completion for DELETE"+code);
			         if(code == 201)
			         {
			        	// successVar++;
			        	 successVar.incrementAndGet();
			        	 System.out.println("In code"+successVar);
			         }
			         
			    }

			    public void cancelled() {
			        System.out.println("The request has been cancelled");
			    }

			});	
			
		}
		//return successVar;

		return (int)successVar.floatValue();
	
	}

}
