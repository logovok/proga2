import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {


        Smartphone sph = new Smartphone();
        sph.networkingComponent.networks = new ArrayList<>(){{
            add("192.168.100.24"); add("74.56.28.62"); add("123.123.123.123");
        }};
        sph.addFirewallRule("123.123.123.123");

        Phone nokia3310 = new Phone();
        nokia3310.networks.add("Lifecell");
        Computer lenovoWithRtx4090 = new Computer();

        ArrayList<IProcessable> devices= new ArrayList<>(){{
            add(sph); add(nokia3310); add(lenovoWithRtx4090);
        }};

        for(var device : devices){
            System.out.println(device.process());
        }

    }
}

interface IProcessable{
    String process();
}

class Computer implements  IProcessable{
    @Override
    public String process() {
        return "Activating large OS";
    }
}

class Phone implements IProcessable{
    public ArrayList<String> networks;

    public Phone(){
        networks = new ArrayList<>();
    }

    @Override
    public String process() {
        StringBuilder sb = new StringBuilder();
        networks.forEach(item -> sb.append(item).append("\n"));
        return sb.toString();
    }
}

class Smartphone implements IProcessable{

    public Smartphone(){
        firewallRules = new ArrayList<>();
    }
    private ArrayList<String> firewallRules;

    public void addFirewallRule(String banedNetwork){
        if (!firewallRules.contains(banedNetwork)){
            firewallRules.add(banedNetwork);
        }
    }

    public void removeFirewallRule(String banedNetwork){
        firewallRules.remove(banedNetwork);
    }

    public final Phone networkingComponent = new Phone(){
        @Override
        public String process(){
            StringBuilder sb = new StringBuilder();
            Arrays.stream(super.process().split("\n"))
                    .filter(item -> !firewallRules.contains(item))
                    .forEach(item -> sb.append(item).append("\n"));
            return sb.toString();
        }
    };

    final Computer computeComponent = new Computer(){
        @Override
        public String process(){
            return "Activating lite OS\n";
        }
    };

    @Override
    public String process() {
        return computeComponent.process() + networkingComponent.process() ;
    }


}
