package Implement;

import Interface.SalarioInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class SalarioImplement extends UnicastRemoteObject implements SalarioInterface {

    private double[][] matriz;
    private int empleados;
    private int meses;

    public SalarioImplement() throws RemoteException {
        super();
    }

    @Override
    public void llenarMatriz(int empleados, int meses) throws RemoteException {
        this.empleados = empleados;
        this.meses = meses;
        matriz = new double[empleados][meses];

        for (int i = 0; i < empleados; i++) {
            for (int j = 0; j < meses; j++) {
                matriz[i][j] = 1000 + Math.random() * 9000;
            }
        }
    }

    @Override
    public double[] totalPorEmpleado() throws RemoteException {
        double[] totales = new double[empleados];
        for (int i = 0; i < empleados; i++) {
            double suma = 0;
            for (int j = 0; j < meses; j++) {
                suma += matriz[i][j];
            }
            totales[i] = suma;
        }
        return totales;
    }

    @Override
    public double[] promedioPorMes() throws RemoteException {
        double[] promedios = new double[meses];
        for (int j = 0; j < meses; j++) {
            double suma = 0;
            for (int i = 0; i < empleados; i++) {
                suma += matriz[i][j];
            }
            promedios[j] = suma / empleados;
        }
        return promedios;
    }

    @Override
    public double totalGeneral() throws RemoteException {
        double total = 0;
        for (int i = 0; i < empleados; i++) {
            for (int j = 0; j < meses; j++) {
                total += matriz[i][j];
            }
        }
        return total;
    }

    @Override
    public double[][] verMatriz() throws RemoteException {
        return matriz;
    }
}
