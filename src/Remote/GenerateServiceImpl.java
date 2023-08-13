package Remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GenerateServiceImpl extends UnicastRemoteObject implements GenerateService {


    /**
     *
     */
    //private static final long serialVersionUID = 123321L;

    public GenerateServiceImpl() throws RemoteException {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public Integer[] gameFields(int size) throws RemoteException {
        Random random = new Random();
        Set<Integer> set = new HashSet();
        int limit = (size * size) / 2;
        while (set.size() != limit) {
            set.add(random.nextInt(99) + 1);
        }
        List<Integer> inList = new ArrayList<>();
        inList.addAll(set);
        inList.addAll(set);
        Integer[] res = new Integer[inList.size()];
        Collections.shuffle(inList);
        inList.toArray(res);
        return res;
    }

}
