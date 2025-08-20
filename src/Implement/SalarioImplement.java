package Implement;

import Interface.SalarioInterface;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.ServerNotActiveException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SalarioImplement extends UnicastRemoteObject implements SalarioInterface {

    private double[][] matriz;
    private int empleados;
    private int meses;


    private final Map<String, String> credenciales = Map.of(
            "admin", "1234",
            "user", "pass",
            "isa", "1234",
            "sara", "1234"
    );


    private final Set<String> hostsAutenticados = ConcurrentHashMap.newKeySet();

    private static final Logger LOGGER = Logger.getLogger(SalarioImplement.class.getName());

    public SalarioImplement() throws RemoteException {
        super();
        configurarLogger();
    }


    private void configurarLogger() {
        try {
            FileHandler fh = new FileHandler("server_rmi.log", true); // modo append
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);
            LOGGER.setUseParentHandlers(false);
            LOGGER.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String hostCliente() {
        try {
            return RemoteServer.getClientHost();
        } catch (ServerNotActiveException e) {
            return "desconocido";
        }
    }

    private void log(String mensaje) {
        String host = hostCliente();
        LOGGER.info("[" + host + "] " + mensaje);
    }


    private void exigirAutenticado() throws RemoteException {
        String host = hostCliente();
        if (!hostsAutenticados.contains(host)) {
            log("ACCESO DENEGADO (no autenticado)");
            throw new RemoteException("Cliente no autenticado. Llame primero a autenticar(usuario, clave).");
        }
    }

    @Override
    public boolean autenticar(String usuario, String clave) throws RemoteException {
        String host = hostCliente();
        int intentos = 0;
        final int MAX_INTENTOS = 3;

        while (intentos < MAX_INTENTOS) {
            boolean ok = credenciales.containsKey(usuario) && credenciales.get(usuario).equals(clave);

            if (ok) {
                hostsAutenticados.add(host);
                log("AUTENTICACIÓN OK para usuario=" + usuario);
                return true;
            } else {
                intentos++;
                log("AUTENTICACIÓN FALLIDA intento=" + intentos + " para usuario=" + usuario);
                if (intentos >= MAX_INTENTOS) {
                    log("USUARIO BLOQUEADO por demasiados intentos fallidos (" + usuario + ")");
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void cerrarSesion() throws RemoteException {
        String host = hostCliente();
        hostsAutenticados.remove(host);
        log("CERRAR SESIÓN");
    }


    @Override
    public void llenarMatriz(int empleados, int meses) throws RemoteException {
        exigirAutenticado();
        this.empleados = empleados;
        this.meses = meses;
        matriz = new double[empleados][meses];

        for (int i = 0; i < empleados; i++) {
            for (int j = 0; j < meses; j++) {
                matriz[i][j] = 1_000_000 + Math.random() * 9_000_000;
            }
        }
        log("llenarMatriz(empleados=" + empleados + ", meses=" + meses + ")");
    }

    @Override
    public double[] totalPorEmpleado() throws RemoteException {
        exigirAutenticado();
        double[] totales = new double[empleados];
        for (int i = 0; i < empleados; i++) {
            double suma = 0;
            for (int j = 0; j < meses; j++) {
                suma += matriz[i][j];
            }
            totales[i] = suma;
        }
        log("totalPorEmpleado()");
        return totales;
    }

    @Override
    public double[] promedioPorMes() throws RemoteException {
        exigirAutenticado();
        double[] promedios = new double[meses];
        for (int j = 0; j < meses; j++) {
            double suma = 0;
            for (int i = 0; i < empleados; i++) {
                suma += matriz[i][j];
            }
            promedios[j] = suma / empleados;
        }
        log("promedioPorMes()");
        return promedios;
    }

    @Override
    public double totalGeneral() throws RemoteException {
        exigirAutenticado();
        double total = 0;
        for (int i = 0; i < empleados; i++) {
            for (int j = 0; j < meses; j++) {
                total += matriz[i][j];
            }
        }
        log("totalGeneral()");
        return total;
    }

    @Override
    public double[][] verMatriz() throws RemoteException {
        exigirAutenticado();
        log("verMatriz()");
        return matriz;
    }
}
