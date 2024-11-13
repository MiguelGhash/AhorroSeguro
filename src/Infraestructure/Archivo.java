package Infraestructure;
import java.io.*;
import Models.*;

public class Archivo {
    private File F;
    private FileWriter Fw;
    private BufferedWriter Bw;

    // Método para leer clientes desde un archivo (sin cambios)
    public BinaryTree Leer(String Ruta) {
        BinaryTree BinaryTree = new BinaryTree();
        String Register, Campos[];
        try {
            F = new File(Ruta);
            FileReader Fr = new FileReader(F);
            BufferedReader Br = new BufferedReader(Fr);
            while((Register = Br.readLine()) != null) {
                Campos = Register.split("\t");

                Customer cliente = new Customer(Campos[0], Campos[1], Campos[2], Campos[3], Campos[4], Campos[5], Double.parseDouble(Campos[6]));
                BinaryTree.Add(cliente);
            }
            Br.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return BinaryTree;
    }

    // Método para escribir la factura para el cliente
    public void EscribirFactura(String Id, String FacturaContenido) {
        String rutaArchivo = Id + ".txt";  // Archivo con el ID del cliente
        try {
            F = new File(rutaArchivo);
            Fw = new FileWriter(F);
            Bw = new BufferedWriter(Fw);
            Bw.write(FacturaContenido);  // Escribe el contenido de la factura
            Bw.close();
            System.out.println("Factura creada con éxito: " + F.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
