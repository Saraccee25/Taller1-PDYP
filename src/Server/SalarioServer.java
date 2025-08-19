package Server;

import Implement.SalarioImplement;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SalarioServer {
    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("Salario", new SalarioImplement());
            System.out.println("Servidor RMI de salarios iniciado...");
            System.out.println("Log -> server_rmi.log");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
