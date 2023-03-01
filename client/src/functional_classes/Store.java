package functional_classes;

import java.util.ArrayList;
import java.util.List;


public class Store {
    List<Integer> usedPorts;

    public Store(){
        this.usedPorts = new ArrayList<>();
    }

    public boolean addPort(int port){
        System.out.println(usedPorts);
        if (usedPorts.contains(port)){
            return false;
        } else {
            usedPorts.add(port);
            return true;
        }

    }

}
