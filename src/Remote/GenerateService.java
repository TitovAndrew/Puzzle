package Remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GenerateService  extends Remote{

    Integer[] gameFields(int size) throws RemoteException;

}
