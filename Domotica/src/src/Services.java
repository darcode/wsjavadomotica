package src;

import java.util.ArrayList;

public class Services {
	
	public Services(){
		
	}
	
	public void execService(String refVal, String countVal, String repeatVal){		
		ModBus connessione = new ModBus();
		connessione.openConnection(refVal, countVal, repeatVal);
		connessione.execRequest();
		connessione.closeConnection();
	}

}
