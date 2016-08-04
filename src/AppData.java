
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

//Singleton class
public class AppData {
    
    private static AppData instance;
    //Global variable for prefs.js
    private static String firefoxUserPrefsPath;
    private static File firefoxUserPrefsFile;
    //If proxy is on, populate text fields with values
    private static String foundHostName;
    private static String foundPort;
    //Global variable for chrome preferences
    private static String chromeUserPrefsPath;
    private static File chromeUserPrefsFile;
    
    //when turn proxy button on/off is pressed, store host name and port here
    private static String hostName;
    private static String port;
    
    //bufferedreader for file
    private static BufferedReader br;
    
    //ArrayList for file
    private static ArrayList<String> prefsFile = new ArrayList<String>();
    
    //boolean to check if proxy is on or off
    private static boolean proxyCheck;
    

    public static boolean isProxyCheck() {
        return proxyCheck;
    }

    public static void setProxyCheck(boolean proxyCheck) {
        AppData.proxyCheck = proxyCheck;
    }

    public static String getHostName() {
        return hostName;
    }

    public static void setHostName(String hostName) {
        AppData.hostName = hostName;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        AppData.port = port;
    }

    public static String getFirefoxUserPrefsPath() {
        return firefoxUserPrefsPath;
    }

    public static void setFirefoxUserPrefsPath(String firefoxUserPrefsPath) {
        AppData.firefoxUserPrefsPath = firefoxUserPrefsPath;
    }

    public static File getFirefoxUserPrefsFile() {
        return firefoxUserPrefsFile;
    }

    public static void setFirefoxUserPrefsFile(File firefoxUserPrefsFile) {
        AppData.firefoxUserPrefsFile = firefoxUserPrefsFile;
    }

    public static String getFoundHostName() {
        return foundHostName;
    }

    public static void setFoundHostName(String foundHostName) {
        AppData.foundHostName = foundHostName;
    }

    public static String getFoundPort() {
        return foundPort;
    }

    public static void setFoundPort(String foundPort) {
        AppData.foundPort = foundPort;
    }

    public static String getChromeUserPrefsPath() {
        return chromeUserPrefsPath;
    }

    public static void setChromeUserPrefsPath(String chromeUserPrefsPath) {
        AppData.chromeUserPrefsPath = chromeUserPrefsPath;
    }

    public static File getChromeUserPrefsFile() {
        return chromeUserPrefsFile;
    }

    public static void setChromeUserPrefsFile(File chromeUserPrefsFile) {
        AppData.chromeUserPrefsFile = chromeUserPrefsFile;
    }
    
    //Method to check for alphaNumeric characters
    public boolean isAlpha(String input){
        char[] chars = input.toCharArray();
        
        for(char c : chars){
            if(!Character.isLetter(c)){
                return false;
            }
        }  
        return true;
    }
    
    //Open File
    public static void openFile(String location) throws FileNotFoundException, IOException{
        
        BufferedReader buffer = null;
       
        try {
            buffer = new BufferedReader(new FileReader(location));
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        
        //set br to this br 
        AppData.br = buffer;
    }
    
    //Close File
    public static void closeFile(BufferedReader br) throws IOException{
        br.close();
    }
    
    //read each file line into arraylist
    public static void readFile(BufferedReader br) throws IOException{
        
        String currentLine = "null";
        //iterate through file
        while(((currentLine = br.readLine()) != null)){
            //add to arraylist
            AppData.prefsFile.add(currentLine);

        }

    }
    
    //write arraylist out to file
    public static void writeFile(ArrayList<String> list) throws IOException{
        
        FileWriter writer = new FileWriter(AppData.getInstance().getFirefoxUserPrefsPath());
        
        //iterate through file
        for(int i = 0; i < list.size(); i++){
            writer.write(list.get(i));
        }
        
        //close both writer and the BufferedReader
        writer.close();
        br.close();

    }
    
    //method to check firefox proxy settings
    public static boolean checkFirefoxProxy(){
		
	boolean isProxyEnabled = false;

	//Get current user directory
	String user = System.getProperty("user.home");
	//Get Path of Firefox's Profiles directory
	String profilePath = "\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\";
	String partialPath = user + profilePath;
	File f = new File(partialPath);
	AppData.getInstance().setFirefoxUserPrefsFile(f);
		
	//if partial path does not exist, it means that Firefox is not installed
	//TODO: validate if Firefox is installed or not. For now, we will assume the user
	//has installed Firefox in the default location.
		
	ArrayList<String> name = new ArrayList<String>(Arrays.asList(f.list()));
	String match = "null";
	
        for(String i : name){
            if(name.isEmpty()){
		System.out.println("Error: no items!");
            }
            else {
                match = name.get(0).toString();
            }
	}
		
	String filePath = partialPath + match + "\\prefs.js";
	//Open prefs.js and find the proxy property
	AppData.getInstance().setFirefoxUserPrefsPath(filePath);
		
	try {
			
            String currentLine = "null";
            //Regular Expressions for parsing vars in the file
            String re = "network.proxy.type";
            String re2 = "network.proxy.http";
            String hostNameRe = "http\\\"\\,\\s\\\"(.+?)\\\"\\)\\;";
            String portRe = "port\",\\s(\\d{0,5})";
            String re3 = "network.proxy.http_port";

            openFile(AppData.getInstance().getFirefoxUserPrefsPath());
            
            //read file to array
            readFile(AppData.br);
            
            //iterate through file
            for(int i = 0; i < AppData.prefsFile.size(); i++){
                
                String curr = AppData.prefsFile.get(i);
                
                if(curr.contains(re2)){
                    //if not empty
                    if(!curr.isEmpty()){
                        Pattern pattern = Pattern.compile(hostNameRe);
                        Matcher matcher = pattern.matcher(curr);
                        if(matcher.find()){
                            System.out.println(matcher.group(1));
                            //set matched characters to string
                            AppData.getInstance().setFoundHostName(matcher.group(1));
                        }
                    }
                    
                }
                
                //use Regular Expression to get the proxy port
                if(curr.contains(re3)){
                //if not empty
                    if(!curr.isEmpty()){
                        Pattern pattern = Pattern.compile(portRe);
                        Matcher matcher = pattern.matcher(curr);
                        if(matcher.find()){
                            System.out.println(matcher.group(1));
                            //set matched characters to string
                            AppData.getInstance().setFoundPort(matcher.group(1));
                        }
                    }     
                }
                
                //use Regular Expression to get if proxy turned on
		if(curr.contains(re)){
                    //check if Proxy is enabled in Firefox
                    if(curr.contains("0")){
                    System.out.println("Firefox Proxy Off! " + curr);
                    isProxyEnabled = false;
                    }
                    else {
                        System.out.println("Firefox Proxy On! " + curr);
			isProxyEnabled = true;
                    }
		}
                
            }
		
	}
	catch(IOException e){
            e.printStackTrace();
	}
            return isProxyEnabled;
		
	}
        
        //method to check chrome proxy settings
        public static boolean checkChromeProxy(){
            
                boolean isProxyEnabled = false;
            
                //BufferedReader
		BufferedReader br = null;
		
		//Get current user directory
		String user = System.getProperty("user.home");
		//Get Path of Firefox's Profiles directory
		String profilePath = "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\";
		String partialPath = user + profilePath;
		File f = new File(partialPath);
		AppData.getInstance().setChromeUserPrefsFile(f);
		
		//if partial path does not exist, it means that Chrome is not installed
		//TODO: validate if Firefox is installed or not. For now, we will assume the user
		//has installed Firefox in the default location.
		
		ArrayList<String> name = new ArrayList<String>(Arrays.asList(f.list()));
		String match = "null";
		for(String i : name){
			if(name.isEmpty()){
				System.out.println("Error: no items!");
			}
			else {
				match = name.get(0).toString();
			}
		}
		
		String filePath = partialPath + match + "\\prefs.js";
		//Open prefs.js and find the proxy property
		AppData.getInstance().setChromeUserPrefsPath(filePath);
		
		try {
			
			String currentLine;
			//Regular Expressions for parsing vars in the file
			String re = "network.proxy.type";
			String re2 = "network.proxy.http";
			String re3 = "network.proxy.http_port";
			
			br = new BufferedReader(new FileReader(filePath));
			
			//iterate through file
			while((currentLine = br.readLine()) != null){
				//use Regular Expression
				if(currentLine.contains(re)){
					//check if Proxy is enabled in Firefox
					if(currentLine.contains("0")){
						System.out.println("Off! " + currentLine);
						isProxyEnabled = false;
					}
					else {
						System.out.println("On! " + currentLine);
						isProxyEnabled = true;
					}
				}
			}
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally {
			try {
				
				if(br != null){
					br.close();
				}
				
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
            
            return isProxyEnabled;
        }
    
    //Method to modify prefs.js file with new data
	public static void turnOnProxy(String inputHost, String inputPort) throws IOException{
		
		String turnOn = "user_pref(\"network.proxy.type\", 1)";
                String re = "network.proxy.type";
		String hostname = "user_pref(\"network.proxy.http\", " + "\"" + inputHost + "\"" + ")";
		String port = "user_pref(\"network.proxy.http_port\", " + inputPort +")";
                
                String currentLine = null;
                
                try {
                    openFile(AppData.getInstance().getFirefoxUserPrefsPath());
                    //iterate through file.
                    while((currentLine = br.readLine()) != null){
                        if(currentLine.contains(re)){
                            currentLine.replaceAll(currentLine, turnOn);
                            System.out.println("currentLine: " + currentLine);
                        }
                    }	
		}
		catch(IOException e){
			e.printStackTrace();
		}

                
                System.out.println("Turning on...");
		System.out.println("Hostname: " + hostname);
		System.out.println("Port: " + port);

	}
	
	//Method to turn off proxy (modify prefs.js to turn off flag)
	public static void turnOffProxy(){

		BufferedReader br = null;
		String re = "network.proxy.type";
		String turnOff = "user_pref(\"network.proxy.type\", 0)";
		String currentLine = null;
		
		try {
                    br = new BufferedReader(new FileReader(getFirefoxUserPrefsFile()));
                    //iterate through file.
                    while((currentLine = br.readLine()) != null){
                        if(currentLine.contains("1")){
                            //replace line with 0
                            System.out.println("Replace " + currentLine + "with " + turnOff);
					
					
			}
                    }	
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		
		
	}
    
    private AppData(){
        this.instance = instance;
    }
    
    public static AppData getInstance(){
        
        if(instance == null){
            instance = new AppData();
        }
        
        return instance;
    }
    
}
