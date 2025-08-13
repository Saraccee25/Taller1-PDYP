package Client;

import Interface.SalarioInterface;
import java.rmi.Naming;
import java.util.Scanner;

public class SalarioClient {
    public static void main(String[] args) {
        try {
            SalarioInterface servicio = (SalarioInterface) Naming.lookup("rmi://localhost/Salario");
            Scanner sc = new Scanner(System.in);

            System.out.print("Ingrese número de empleados: ");
            int empleados = sc.nextInt();
            System.out.print("Ingrese número de meses: ");
            int meses = sc.nextInt();

            servicio.llenarMatriz(empleados, meses);

            double[][] matriz = servicio.verMatriz();

            // Imprimir encabezado
            System.out.print("\t\t"); 
            for (int j = 0; j < meses; j++) {
                System.out.printf("Mes %d\t", j + 1);
            }
            System.out.println();

            // Imprimir filas de empleados
            for (int i = 0; i < matriz.length; i++) {
                System.out.print("Empleado " + (i + 1) + ":\t");
                for (int j = 0; j < matriz[i].length; j++) {
                    System.out.printf("%.2f\t", matriz[i][j]);
                }
                System.out.println();
            }

            double[] totales = servicio.totalPorEmpleado();
            double[] promedios = servicio.promedioPorMes();
            double totalGeneral = servicio.totalGeneral();

            System.out.println("\n--- Total pagado por empleado ---");
            for (int i = 0; i < totales.length; i++) {
                System.out.printf("Empleado %d: %.2f\n", i + 1, totales[i]);
            }

            System.out.println("\n--- Promedio pagado por mes ---");
            for (int i = 0; i < promedios.length; i++) {
                System.out.printf("Mes %d: %.2f\n", i + 1, promedios[i]);
            }

            System.out.printf("\nTotal general pagado: %.2f\n", totalGeneral);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
