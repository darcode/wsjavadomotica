package src;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import src.ServicesStub.Somma;
import src.ServicesStub.SommaResponse;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServicesStub srv = null;
		try {
			srv = new ServicesStub();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Somma somma = new Somma();
		somma.setA(3);
		somma.setB(6);
		
		SommaResponse response = null;
		try {
			response = srv.somma(somma);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(response.get_return());

	}

}
