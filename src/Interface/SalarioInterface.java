package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SalarioInterface extends Remote {
    void llenarMatriz(int empleados, int meses) throws RemoteException;
    double[] totalPorEmpleado() throws RemoteException;
    double[] promedioPorMes() throws RemoteException;
    double totalGeneral() throws RemoteException;
    double[][] verMatriz() throws RemoteException;

    boolean autenticar(String usuario, String clave) throws RemoteException;
    void cerrarSesion() throws RemoteException;

}

