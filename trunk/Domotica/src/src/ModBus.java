package src;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.msg.WriteCoilResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

public class ModBus {
	TCPMasterConnection con = null; //the connection
	ModbusTCPTransaction trans = null; //the transaction
	ReadInputDiscretesRequest req = null; //the request
	ReadInputDiscretesResponse res = null; //the response
	WriteCoilRequest coilRequest = new WriteCoilRequest();
	WriteCoilResponse resCoil = new WriteCoilResponse();

	/* Variables for storing the parameters */
	private InetAddress addr = null; //the slave's address
	private int port = Modbus.DEFAULT_PORT;
	private int ref = 0; //the reference; offset where to start reading from
	private int count = 0; //the number of DI's to read
	private int repeat = 1; //a loop for repeating the transaction
	
	public ModBus(){
		
	}	
	
	public void openConnection(String refVal, String countVal, String repeatVal) {
		try {
//			int idx = astr.indexOf(':');
//			if (idx > 0) {
//				port = Integer.parseInt(astr.substring(idx + 1)); //porta di ascolto
//				astr = astr.substring(0, idx);
//			}
			addr = InetAddress.getByAddress(new byte[] {(byte)192,(byte)168,(byte)1,(byte)20} );//InetAddress.getByName(astr); //indirizzo del plc da cui leggere i dati
			ref = Integer.decode(refVal).intValue(); 
			count = Integer.decode(countVal).intValue();
			if (!repeatVal.equals("")) {
				repeat = Integer.parseInt(repeatVal);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		// 2. Open the connection
		con = new TCPMasterConnection(addr);
		con.setPort(port);
		try {
			con.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void execRequest(){
		
		coilRequest.setCoil(true);
		coilRequest.setReference(24);
		
		
		//3. Prepare the request
		req = new ReadInputDiscretesRequest(ref, count);

		//4. Prepare the transaction
		trans = new ModbusTCPTransaction(con);
//		trans.setRequest(req);
		trans.setRequest(coilRequest);

		//No we are ready for action. The last part is executing the prepared transaction the given (repeat) 
		//number of times and then for cleanup

		//5. Execute the transaction repeat times
		int k = 0;
		do {
			try {
				trans.execute();
			} catch (ModbusIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ModbusSlaveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ModbusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			res = (ReadInputDiscretesResponse) trans.getResponse();
			resCoil = (WriteCoilResponse) trans.getResponse();
			System.out.println("Digital Inputs Status="+ resCoil.getCoil());
			k++;
		} while (k < repeat);
	}
	
	public void closeConnection(){
		con.close();
	}
}
