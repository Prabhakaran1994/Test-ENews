package Enews_Reporter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Date;

import Enews_WDMethods.ProjectMethods;

public class consoleoutput extends ProjectMethods {
	
	public void createreport(String testcasename) throws IOException
	{
	
		String tname=testcasename;
		Date d= new Date();
		long date=System.currentTimeMillis();
		
		
		/*String fileName = "/home/tringapps-admin/Desktop/Reports/"+ tname + d +".txt";
    	final boolean append = true, autoflush = true;
    	PrintStream printStream = new PrintStream(new FileOutputStream(fileName, append),  autoflush);
    	System.setOut(printStream);
    	System.setErr(printStream);*/
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			PrintStream myconsole=new PrintStream(new File("/home/tringapps-admin/Desktop/Reports/"+ tname + d +".txt"));
			System.setOut(myconsole);
		}
		catch(Exception E){
			 System.out.println("Error during reading/writing");
		}
		
		
		
		
		
	/*BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    BufferedWriter out = new BufferedWriter(new FileWriter("/home/tringapps-admin/Desktop/Reports/"+ tname + d +".txt"));
    
    try {
        String inputLine = null;
        do {
            inputLine=in.readLine();
            out.write(inputLine);
            out.newLine();
        } while (!inputLine.equalsIgnoreCase("eof"));
        System.out.print("Write Successfsl");
    } catch(IOException e1) {
        System.out.println("Error during reading/writing");
    } finally {
        out.close();
        in.close();
    }*/
  
	}
}
	

