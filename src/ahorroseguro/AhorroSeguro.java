
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
       "Aprovar o rechazar solicitud", "Ver créditos aprobados", 
       "Ver créditos rechazados", "Cancelar crédito", 
       "Revaluar solicitudes", "Salir"};
       String CustomerType[]={"Jurídico","Natural"};
       String RequestType[]={"tarjeta", "libre inversión", "nómina", "hipotecario"};
       String Id, Type, Name, Adress, Tel, Contract; 
       String Code,Owner;
       LocalDate RequestDate;
       Double Value; 
       int Fee;
       Double Salary;
       String Opt;
       int CodeNumber = 1;
       
       
       do
       {
         Opt=(String)JOptionPane.showInputDialog(null, "Selected option",
                      "Main Menu", 1, null, Menu, Menu[0]);
              switch(Opt)
              {
                 case "Registrar cliente":
                     Id =  JOptionPane.showInputDialog("Identificación del clinete");
                     Type =  (String)JOptionPane.showInputDialog(null, "Tipo de cliente",
                      "Seleccionar tipo de cliente", 1, null, CustomerType, CustomerType[0]);
                     Name =  JOptionPane.showInputDialog("Nombre del cliente");
                     Adress =  JOptionPane.showInputDialog("Dirección del cliente");
                     Tel =  JOptionPane.showInputDialog("Teléfono de contacto del cliente");
                     Contract =  JOptionPane.showInputDialog("Tipo de contrato");
                     Salary = Double.parseDouble(JOptionPane.showInputDialog("Salario del cliente"));
                     Service.CustomerSingUp(Service.CustomerData(Id, Type, Name, Adress, Tel, Contract, Salary));
                     JOptionPane.showMessageDialog(null,Service.CustomerListLocal().ToString());
                     break;
                 case "Registrar solicitud":
                     Code = Integer.toString(CodeNumber);
                     CodeNumber = CodeNumber+1;
                     Type =  (String)JOptionPane.showInputDialog(null, "Tipo de solicitud",
                      "Seleccionar tipo de solicitud", 1, null, RequestType, RequestType[0]);
                     Owner = JOptionPane.showInputDialog("Nombre del titular del trámite");
                     RequestDate = LocalDate.now();
                     Value =  Double.parseDouble(JOptionPane.showInputDialog("Valor de la solicitud"));
                     Fee = Integer.parseInt(JOptionPane.showInputDialog(null, "Número de cuotas"));
                     JOptionPane.showMessageDialog(null,Service.EnqueueRequest(Service.CreditRequest(Code, Type, Owner, RequestDate, Value, Fee)));
                     break; 
                 case "Aprovar o rechazar solicitud":
                     Id = JOptionPane.showInputDialog("Ingrese la identificación del cliente para la solicitud");
                     Customer Customer = (Customer)Service.CustomerListLocal().SearchById(Id); 
                      if (Customer != null) {
                        CreditRequest Request = (CreditRequest) Service.RequestQueue().DeQueue();
                        if (Request != null) {
                            String Result = Service.ApproveOrRejectRequest(Request, Customer.getSalary());
                            JOptionPane.showMessageDialog(null, Result);
                        } else {
                            JOptionPane.showMessageDialog(null, "No hay solicitudes pendientes.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
                    }
                     break;
                     case "Ver créditos aprobados":
                         if( Service.GetApprovedCredits().IsEmpty())
                         {
                            JOptionPane.showMessageDialog(null, "No hay solicitudes aprobadas");  
                         }
                         else
                         {
                            JOptionPane.showMessageDialog(null, Service.GetApprovedCredits().ToString());
                         }
                    
                    break;
                     case "Ver créditos rechazados":
                         if(Service.RejectedEmpty())
                         {
                             JOptionPane.showMessageDialog(null, "No hay solicitudes rechazadas");
                         }
                         else
                         {
                            JOptionPane.showMessageDialog(null, Service.PrintRejected()); 
                         }
                         break;
                     case "Cancelar crédito":
                        Code = JOptionPane.showInputDialog("Ingrese el código del crédito a cancelar");
                        JOptionPane.showMessageDialog(null, Service.CancelCredit(Code)); 
                         break;
                     case "Revaluar solicitudes":
                         
                         String result = Service.RevalidateRejectedRequests();
                         JOptionPane.showMessageDialog(null, result);
                         String TreeStatus = Service.PrintRejectedTree();
                         JOptionPane.showMessageDialog(null, "Estado actual del árbol:\n" + TreeStatus);
                         break;
              }
       } while(!Opt.equals("Salir"));
    }
    
}
