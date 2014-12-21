package edu.sjsu.cmpe.cache.client;

import java.util.Map;


public class Client {

    public static void main(String[] args) throws Exception {
    	
    
    	CRDTClient cdrdClient = new CRDTClient();
    	int successCount = cdrdClient.put(1, "a");
    	
    	 try {
             Thread.sleep(500);                 //1000 milliseconds is one second.
         } catch(InterruptedException ex) {
             Thread.currentThread().interrupt();
         }
         
    	 System.out.println("successCount value "+successCount);
    	if(successCount<=2)
    	{
          
    		 int deletValue = cdrdClient.delete(1);
    	     System.out.println("delete(1) => " + deletValue);
    		
    	}
        System.out.println("Starting Cache Client...");
        CacheServiceInterface cache = new DistributedCacheService(
                "http://localhost:3000");

      //  cache.put(1, "foo");
      //  System.out.println("put(1 => foo)");

        //String value = cache.get(12);
 
        try {
            Thread.sleep(2000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        
        Map<String, Integer> getMap = cdrdClient.get(1);
        if(getMap.containsValue(2))
        {
        	cdrdClient.put(1, "a");
        }

        System.out.println("Existing Cache Client...");
    }

}
