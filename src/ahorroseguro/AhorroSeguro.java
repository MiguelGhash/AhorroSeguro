package ahorroseguro;

import java.time.LocalDate;
import javax.swing.JOptionPane;
import Services.*;
import Models.*;

/**
 *
 * @author BaalG
 */
public class AhorroSeguro {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       GeneralServices Service  = new GeneralServices();
       String Menu[] = {"Registrar cliente", "Registrar solicitud", 
       "Aprobar o rechazar solicitud", "Ver créditos aprobados", 
       "Ver créditos rechazados", "Cancelar crédito", 
       "Revaluar solicitudes", "Calcular total de créditos aprobados", 
       "Calcular saldo pendiente de crédito", 
       "Ver historial de créditos aprobados", 
       "Ver solicitudes pendientes",
       "Realizar pago",  
       "Salir"};
       
       String CustomerType[] = {"Jurídico", "Natural"};
       String RequestType[] = {"tarjeta", "libre inversión", "nómina", "hipotecario"};
       String Id, Type, Name, Address, Tel, Contract; 
       String Code, Owner;
       LocalDate RequestDate;
       Double Value; 
       int Fee;
       Double Salary;
       String Opt;
       int CodeNumber = 1;
       
       do {
         Opt = (String) JOptionPane.showInputDialog(null, "Selecciona una opción",
                      "Menú Principal", 1, null, Menu, Menu[0]);
         
         switch(Opt) {
             case "Registrar cliente":
                 Id =  JOptionPane.showInputDialog("Identificación del cliente");
                 Type =  (String) JOptionPane.showInputDialog(null, "Tipo de cliente",
                  "Seleccionar tipo de cliente", 1, null, CustomerType, CustomerType[0]);
                 Name =  JOptionPane.showInputDialog("Nombre del cliente");
                 Address =  JOptionPane.showInputDialog("Dirección del cliente");
                 Tel =  JOptionPane.showInputDialog("Teléfono de contacto del cliente");
                 Contract =  JOptionPane.showInputDialog("Tipo de contrato");
                 Salary = Double.parseDouble(JOptionPane.showInputDialog("Salario del cliente"));
                 Service.CustomerSingUp(Service.CustomerData(Id, Type, Name, Address, Tel, Contract, Salary));
                 JOptionPane.showMessageDialog(null, Service.CustomerListLocal().ToString());
                 break;

             case "Registrar solicitud":
                 Code = Integer.toString(CodeNumber);
                 CodeNumber++;
                 Type = (String) JOptionPane.showInputDialog(null, "Tipo de solicitud",
                  "Seleccionar tipo de solicitud", 1, null, RequestType, RequestType[0]);
                 Owner = JOptionPane.showInputDialog("Ingrese el ID del titular del trámite");
                 RequestDate = LocalDate.now();
                 Value = Double.parseDouble(JOptionPane.showInputDialog("Valor de la solicitud"));
                 Fee = Integer.parseInt(JOptionPane.showInputDialog(null, "Número de cuotas"));
                 JOptionPane.showMessageDialog(null, Service.EnqueueRequest(Service.CreditRequest(Code, Type, Owner, RequestDate, Value, Fee)));
                 break;

             case "Aprobar o rechazar solicitud":
                 Id = JOptionPane.showInputDialog("Ingrese la identificación del cliente para la solicitud");
                 String Resultado = Service.AprobarORechazarSolicitud(Id);
                 JOptionPane.showMessageDialog(null, Resultado);
                 break;

             case "Ver créditos aprobados":
                 if (Service.GetApprovedCredits().IsEmpty()) {
                    JOptionPane.showMessageDialog(null, "No hay solicitudes aprobadas");  
                 } else {
                    JOptionPane.showMessageDialog(null, Service.GetApprovedCredits().ToString());
                 }
                 break;

             case "Ver créditos rechazados":
                 if (Service.RejectedEmpty()) {
                     JOptionPane.showMessageDialog(null, "No hay solicitudes rechazadas");
                 } else {
                     JOptionPane.showMessageDialog(null, Service.PrintRejected()); 
                 }
                 break;

             case "Cancelar crédito":
                 Code = JOptionPane.showInputDialog("Ingrese el código del crédito a cancelar");
                 JOptionPane.showMessageDialog(null, Service.CancelCredit(Code)); 
                 break;

             case "Revaluar solicitudes":
                 String revalResult = Service.RevalidateRejectedRequests();
                 JOptionPane.showMessageDialog(null, revalResult);
                 String treeStatus = Service.PrintRejectedTree();
                 JOptionPane.showMessageDialog(null, "Estado actual del árbol:\n" + treeStatus);
                 break;

             // Nuevas opciones implementadas
             case "Calcular total de créditos aprobados":
                 Id = JOptionPane.showInputDialog("Ingrese la identificación del cliente");
                 double totalCreditos = Service.CalcularTotalCreditosAprobados(Id);
                 JOptionPane.showMessageDialog(null, "Total de créditos aprobados: $" + totalCreditos);
                 break;

             case "Eliminar solicitud de crédito":
                 Id = JOptionPane.showInputDialog("Ingrese la identificación del cliente");
                 Code = JOptionPane.showInputDialog("Ingrese el código de la solicitud a eliminar");
                 String eliminarMensaje = Service.EliminarSolicitudCredito(Id, Code);
                 JOptionPane.showMessageDialog(null, eliminarMensaje);
                 break;

             case "Calcular saldo pendiente de crédito":
                 Id = JOptionPane.showInputDialog("Ingrese la identificación del cliente");
                 Code = JOptionPane.showInputDialog("Ingrese el código del crédito");
                 double saldoPendiente = Service.CalcularSaldoPendiente(Id, Code);
                 JOptionPane.showMessageDialog(null, "Saldo pendiente del crédito: $" + saldoPendiente);
                 break;

             case "Ver historial de créditos aprobados":
                 Id = JOptionPane.showInputDialog("Ingrese la identificación del cliente");
                 String historial = Service.VerHistorialCreditosAprobados(Id);
                 JOptionPane.showMessageDialog(null, historial);
                 break;

             case "Ver solicitudes pendientes":
                 String solicitudesPendientes = Service.VerSolicitudesPendientes();
                 JOptionPane.showMessageDialog(null, solicitudesPendientes);
                 break;
             case "Realizar pago":
                 //
                 Code = JOptionPane.showInputDialog("Ingrese el código del crédito");
                 double montoPago = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el monto a pagar"));
                 if (montoPago <= 0) 
                 {
                     //
                     JOptionPane.showMessageDialog(null, "El monto a pagar debe ser mayor que cero.");
                 } else 
                 {
            String resultadoPago = Service.RealizarPago(Code, montoPago);
            JOptionPane.showMessageDialog(null, resultadoPago);
                 }

    break;
                 
         }
       } while (!Opt.equals("Salir"));
    }
}
