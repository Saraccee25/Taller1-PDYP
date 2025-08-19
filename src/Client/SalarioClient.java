package Client;

import Interface.SalarioInterface;
import java.rmi.Naming;
import java.util.Locale;
import java.text.NumberFormat;
import java.util.Scanner;

public class SalarioClient {
    public static void main(String[] args) {
        try {
            SalarioInterface servicio = (SalarioInterface) Naming.lookup("rmi://localhost/Salario");
            Scanner sc = new Scanner(System.in);

            boolean autenticado = false;
            int intentos = 0;
            int maxIntentos = 3;


            while (!autenticado && intentos < maxIntentos) {
                System.out.print("Usuario: ");
                String usuario = sc.nextLine();
                System.out.print("Clave: ");
                String clave = sc.nextLine();

                autenticado = servicio.autenticar(usuario, clave);

                if (!autenticado) {
                    intentos++;
                    System.out.println("❌ Credenciales incorrectas. Intento " + intentos + " de " + maxIntentos);
                    if (intentos == maxIntentos) {
                        System.out.println("🚫 Demasiados intentos fallidos. Saliendo...");
                        sc.close();
                        return;
                    }
                }
            }

            System.out.println("✅ Bienvenido, autenticación correcta.");


            System.out.print("Ingrese número de empleados: ");
            int empleados = sc.nextInt();
            System.out.print("Ingrese número de meses: ");
            int meses = sc.nextInt();

            servicio.llenarMatriz(empleados, meses);
            double[][] matriz = servicio.verMatriz();

            NumberFormat formatoCOP = NumberFormat.getCurrencyInstance(Locale.of("es", "CO"));


            System.out.print("\t\t");
            for (int j = 0; j < meses; j++) {
                System.out.printf("Mes %d\t\t", j + 1);
            }
            System.out.println();


            for (int i = 0; i < matriz.length; i++) {
                System.out.print("Empleado " + (i + 1) + ":\t");
                for (int j = 0; j < matriz[i].length; j++) {
                    System.out.print(formatoCOP.format(matriz[i][j]) + "\t");
                }
                System.out.println();
            }


            double[] totales = servicio.totalPorEmpleado();
            double[] promedios = servicio.promedioPorMes();
            double totalGeneral = servicio.totalGeneral();

            System.out.println("\n--- Total pagado por empleado ---");
            for (int i = 0; i < totales.length; i++) {
                System.out.println("Empleado " + (i + 1) + ": " + formatoCOP.format(totales[i]));
            }

            System.out.println("\n--- Promedio pagado por mes ---");
            for (int i = 0; i < promedios.length; i++) {
                System.out.println("Mes " + (i + 1) + ": " + formatoCOP.format(promedios[i]));
            }

            System.out.println("\n💰 Total general pagado: " + formatoCOP.format(totalGeneral));

            servicio.cerrarSesion();
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
