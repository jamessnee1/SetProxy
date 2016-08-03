
import java.awt.Dimension;
import java.awt.Toolkit;

//Set Proxy
//Java utility to change Firefox and Chrome proxy settings programatically
//Useful for JMeter
//By James Snee


public class Main {
	
	public static void main(String[] args) {
            
            SetProxyGUI gui = new SetProxyGUI();
            gui.setTitle("Set Proxy");
            //centre JFrame
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            gui.setLocation(dim.width/2-gui.getSize().width/2, dim.height/2-gui.getSize().height/2);
            gui.setVisible(true);

            boolean proxyCheck = AppData.getInstance().checkFirefoxProxy();
            //Set AppData with proxy check so we can access it later
            AppData.getInstance().setProxyCheck(proxyCheck);
            
            //if proxy is enabled, then set button to Turn proxy off. If not, set to turn on
		if(proxyCheck){
                  
			gui.getHostnameTextField().setEditable(false);
                        gui.getHostnameTextField().setText(AppData.getInstance().getFoundHostName());
			gui.getPortTextField().setEditable(false);
                        gui.getPortTextField().setText(AppData.getInstance().getFoundPort());
			gui.getProxyToggleButton().setText("Turn proxy off");	
		}
		else {
			gui.getHostnameTextField().setEditable(true);
                        gui.getHostnameTextField().setText(AppData.getInstance().getFoundHostName());
			gui.getPortTextField().setEditable(true);
                        gui.getPortTextField().setText(AppData.getInstance().getFoundPort());
			gui.getProxyToggleButton().setText("Turn proxy on");
		}

	}

}
