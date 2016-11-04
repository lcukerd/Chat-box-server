import java.io.*;
import java.net.*;
import java.util.*;

class DailyAdviceS
{
	ArrayList<PrintWriter> Clients=new ArrayList<PrintWriter>();
	Socket sock;
	public static void main(String args[])
	{
		DailyAdviceS ad= new DailyAdviceS();
		ad.go();
	}
	private void go()
	{
		try
		{
			ServerSocket server= new ServerSocket(4242);
			InetAddress ip;
			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip.getHostAddress());
			while(true)
			{
				sock=server.accept();
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				Clients.add(writer);
				Thread foundR= new Thread(new nodeR(sock));
				foundR.start();
				System.out.println("got a connection");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private void SendAll(String Chat,String name)
	{
		Iterator it=Clients.iterator();
		while(it.hasNext())
		{
			try
			{
				PrintWriter writer= (PrintWriter)it.next();
				writer.println(name+" /t "+Chat);
				writer.flush();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	class nodeR implements Runnable						//recieve message from clients
	{
		private String hostName; 
		private BufferedReader reader;
		public nodeR(Socket s)
		{
			try
			{
				String hostName = s.getInetAddress().getHostName();
				System.out.println(hostName);
				InputStreamReader in= new InputStreamReader(s.getInputStream());
				reader = new BufferedReader(in);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		public void run()
		{
			String Message;
			try
			{
				String Name=InetAddress.getLocalHost().getCanonicalHostName();	
				SendAll(Name+" Ready","");
				while(true)
				{
					while ((Message=reader.readLine())!=null) 
					{
						SendAll(Message,Name);
					}
				}

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}