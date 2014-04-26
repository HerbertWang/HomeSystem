import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import Demo.MonitorPrx;
import Demo.MonitorPrxHelper;
import Demo._MonitorDisp;
import Ice.Current;
import Ice.ObjectPrx;
import IceStorm.BadQoS;



 public class HomeManager extends Ice.Application
{
	 String day = "";
	 String location="";
      public class MonitorI extends _MonitorDisp {
           public void report(String s , Current __current) {
		      System.out.println(s);
        	   String[] result=s.split(":");
        	   String type=result[0];
        	   if(type.equalsIgnoreCase("clock"))
        	   {
        		   day=result[1];
        	   }
        	   else if(type.equalsIgnoreCase("BP"))
        	   {
        		   String[] bps=result[1].split("_");
        		   String heart=bps[0];
        		   String blood=bps[1];
        		   if(Integer.parseInt(heart)>100||Integer.parseInt(blood)>140)
        		   {
        			Ice.ObjectPrx obj=communicator().stringToProxy("Warning:tcp -h 127.0.0.1 -p 10003");
        			MonitorPrx warn=MonitorPrxHelper.uncheckedCast(obj);
        			warn.report(day+":"+heart+":"+blood);
        			 try {
             		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("healthwarning.txt", true)));
            			    out.println(day+":"+heart+":"+blood);
            			    out.close();
            			} catch (IOException e) {
            			    System.out.println("Cannot open file");
            			} 
        		   }
        	   }
        	   else if(type.equalsIgnoreCase("water"))
        	   {
        		   try {
        		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("waterlog.txt", true)));
       			    out.println("<"+day+"> Water usage reading");
       			    out.println("Usage:"+result[1]);
       			    out.close();
       			} catch (IOException e) {
       			    System.out.println("Cannot open file");
       			} 
              }
        	   else if(type.equals("temperature"))
        	   {
        		  if(location.equalsIgnoreCase("home")&&result[1].equals("22")!=true)
        		{
        		try
        		{
        		Thread.currentThread();
        	    Thread.sleep(5000); 
        		try {
       			    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("tempadjustlog.txt", true)));
       			    out.println("<"+day+"> Air-Conditioning adjusted");
       			    out.println("Temperature: At"+result[1]);
       			    out.close();
       			} catch (IOException e) {
       			    System.out.println("Cannot open file");
       			}
        		
		        }
		    	catch(java.lang.InterruptedException e)
		    	{
		    		System.out.println("error happenes");
		    	}
        		}
        		  
        	   }
        	   else if(type.equalsIgnoreCase("location"))
        	   {
        		  location=result[1];
        	   }
        	   
        	   }
           }
   /*   public class MonitorII extends _MonitorDisp 
      {

		public void report(String s, Current __current) {
		
			 if(s.equals("1"))
			 {
				 try {
					    BufferedReader in = new BufferedReader(new FileReader("tempadjustlog.txt"));
					    String str;
					    while ((str = in.readLine()) != null)
					    {
					    	Ice.ObjectPrx obj=communicator().stringToProxy("Warning:tcp -h 127.0.0.1 -p 9999");
		        			MonitorPrx warn=MonitorPrxHelper.uncheckedCast(obj);
		        			warn.report("twlog :"+str);
					    	
				 }
					    in.close();
					    BufferedReader in2 = new BufferedReader(new FileReader("waterlog.txt"));
					    String str2;
					    while ((str2 = in2.readLine()) != null)
					    {
					    	Ice.ObjectPrx obj=communicator().stringToProxy("Warning:tcp -h 127.0.0.1 -p 9999");
		        			MonitorPrx warn=MonitorPrxHelper.uncheckedCast(obj);
		        			warn.report("twlog :"+str);
					    	
				 }
					    in2.close();
					}
				 catch(IOException e)
				 {
					 System.out.println("Cannot open the file");
				 }
			 }
			 else if(s.equals("2"))
			 {
				 try {
					    BufferedReader in = new BufferedReader(new FileReader("healthwarning.txt.txt"));
					    String str;
					    while ((str = in.readLine()) != null)
					    {
					    	Ice.ObjectPrx obj=communicator().stringToProxy("Warning:tcp -h 127.0.0.1 -p 9999");
		        			MonitorPrx warn=MonitorPrxHelper.uncheckedCast(obj);
		        			warn.report("warnlog :"+str);
					    	
				 }
					    in.close();
					}
				 catch(IOException e)
				 {
					 System.out.println("Cannot open the file");
				 }
			 }
		       
			
		}
    	  
      }*/
      
        	  
	

	@Override
	public int run(String[] args) {
	
		
		String topicname="Figures";
		String id=null;
		String retryCount = null;
        IceStorm.TopicManagerPrx topicManager =
		        IceStorm.TopicManagerPrxHelper.checkedCast(communicator().propertyToProxy("TopicManager.Proxy"));
        if(topicManager==null)
        {
        	System.out.println("Invalid Proxy");
        	return 1;
        }
       
	    IceStorm.TopicPrx topic = null;
		    try {
		        topic = topicManager.retrieve("Figures");
		        }
		    catch (IceStorm.NoSuchTopic ex) {
		       try
		       {
		    	   topic=topicManager.create("Figures");
		       }
		       catch(IceStorm.TopicExists e)
		       {
		    	   System.err.println(appName()+":temporary failure,try again");
		    	   return 1;
		       }
		       
		    }
		    
		    Ice.ObjectAdapter adapter = communicator().createObjectAdapter("Monitor.Subscriber");
		    Ice.Identity subId=new Ice.Identity(id,"");
		    if(subId.name==null)
		    {
		    	subId.name=java.util.UUID.randomUUID().toString();
		    }
		    Ice.ObjectPrx proxy = adapter.add(new MonitorI(), subId);
		    adapter.activate();
		    java.util.Map<String, String> qos = new java.util.HashMap<String, String>();
	        if(retryCount != null)
	        {
	            qos.put("retryCount", retryCount);
	        }
		    proxy=proxy.ice_oneway();
		    try
	        {
	            topic.subscribeAndGetPublisher(qos, proxy);
	        }
	        catch(IceStorm.AlreadySubscribed e)
	        {
	            // If we're manually setting the subscriber id ignore.
	            if(id == null)
	            {
	                e.printStackTrace();
	                return 1;
	            }
	            else
	            {
	                System.out.println("reactivating persistent subscriber");
	            }
	        }
	        catch(IceStorm.BadQoS e)
	        {
	            e.printStackTrace();
	            return 1;
	        }
		    
		    communicator().waitForShutdown();
            topic.unsubscribe(proxy);
            
            //Ice.ObjectAdapter adapter2=communicator().createObjectAdapterWithEndpoints("Warning", "tcp -h 127.0.0.1 -p 9999");
    		//adapter2.add(new MonitorII(), communicator().stringToIdentity("Warning"));
    		//adapter2.activate();
    		//communicator().waitForShutdown();
			return 0;
	}
	
	
	public static void main(String[] args) throws Exception, BadQoS
	{
		HomeManager hm = new HomeManager();
        int status = hm.main("HomeManager", args, "config.sub");
        System.exit(status);
	    
	}
}

