import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Demo.MonitorPrx;
import Demo.MonitorPrxHelper;



public class Sensor extends Ice.Application
{
	@Override
	public int run(String[] args) {
		  
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
	        try {
				topic = topicManager.create("Figures");
			}
	        catch (IceStorm.TopicExists e) {
				 System.err.println(appName() + ": temporary failure, try again.");
	             return 1;
			}
	    }

	    Ice.ObjectPrx pub = topic.getPublisher().ice_oneway();
	    MonitorPrx monitor = MonitorPrxHelper.uncheckedCast(pub);
	    System.out.println("Puslishing data....Press^c to terminate the application");
	    try
        {
        String type=args[0];
	    String filename=args[1];
	    int value=1;
		while (true) 
		{
			
			try {
			    BufferedReader in = new BufferedReader(new FileReader(filename));
			    String str;
			    while ((str = in.readLine()) != null)
			    {
			    	
			    	String[] result=str.split(",");
			    	monitor.report(type+":"+result[0]);
			    	value=Integer.parseInt(result[1].trim());
			    	try
			    	{
			    	Thread.currentThread();
					Thread.sleep(value*1000);
			        }
			    	catch(java.lang.InterruptedException e)
			    	{
			    		
			    	}
			    }
			    in.close();
			}
			
			catch (IOException e)
			{
				System.out.println("Cannot open the file");
			}
			}
        }
		 catch(Ice.CommunicatorDestroyedException ex)
	        {
	            System.out.println("Communication destroyed");
	        }
                  return 0;
    	    }
   
	public static void main(String[] args) throws Exception
	{
	   Sensor s =new Sensor();
	   int status=s.main("Sensor", args,"config.pub");
	   System.exit(status);
	    
	}
	}

