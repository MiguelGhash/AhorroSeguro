package Services;
import Infraestructure.*;
import Models.*;
import java.time.LocalDate;

public class GeneralServices {
    private List CustomerList = new List();
    private Queue RequestQueue = new Queue();
    private List ApprovedCredits = new List();
    private Stack RejectedCredits = new Stack();
    private BinaryTree RejectedTree = new BinaryTree();
    
    // Métodos numeral 1: Registrar cliente
    public String CustomerSingUp(Customer Customer) {
        CustomerList.AddLast(Customer);
        return "Cliente registrado en el sistema";
    }

    public Customer CustomerData(String Id, String Type, String Name, String Address, String Tel, String Contract, Double Salary) {
        return new Customer(Id, Type, Name, Address, Tel, Contract, Salary);
    }

    // Métodos numeral 2: Registrar solicitud de crédito
    public CreditRequest CreditRequest(String Code, String Type, String Owner, LocalDate RequestDate, Double Value, int Fee) {
        return new CreditRequest(Code, Type, Owner, RequestDate, Value, Fee);
    }

    public String EnqueueRequest(CreditRequest CreditRequest) {
        RequestQueue.EnQueue(CreditRequest);
        return "Solicitud de crédito agregada";
    }

    // Métodos numeral 3: Aprobar o negar una solicitud de crédito
    public String ApproveOrRejectRequest(CreditRequest CreditRequest, double CustomerSalary) {
        double AnnualRate = GetEffectiveAnnualRate(CreditRequest.getType());
        double MonthlyRate = Math.pow(1 + AnnualRate, 1.0 / 12) - 1;
        double MonthlyInstallment = CalculateMonthlyInstallment(CreditRequest.getValue(), MonthlyRate, CreditRequest.getFee());

        if (MonthlyInstallment <= CustomerSalary * 0.5) {
            RegisterApprovedRequest(CreditRequest, MonthlyInstallment, MonthlyRate);
            return "Solicitud aprobada. Código: " + CreditRequest.getCode();
        } else {
            RegisterRejectedRequest(CreditRequest);
            return "Solicitud rechazada. Cuota mensual excede el 50% del salario.";
        }
    }

    private double GetEffectiveAnnualRate(String RequestType) {
        switch (RequestType.toLowerCase()) {
            case "tarjeta":
                return 0.30;
            case "libre inversión":
                return 0.225;
            case "nómina":
                return 0.175;
            case "hipotecario":
                return 0.11;
            default:
                return 0.0;
        }
    }

    private double CalculateMonthlyInstallment(double Amount, double MonthlyRate, int Months) {
        return Amount * (MonthlyRate * Math.pow(1 + MonthlyRate, Months)) / (Math.pow(1 + MonthlyRate, Months) - 1);
    }

    private void RegisterApprovedRequest(CreditRequest CreditRequest, double MonthlyInstallment, double MonthlyRate) {
        Queue MonthlyPayments = new Queue();
        LocalDate StartDate = CreditRequest.getRequestDate();
        double RemainingCapital = CreditRequest.getValue();

        for (int i = 1; i <= CreditRequest.getFee(); i++) {
            LocalDate PaymentDate = StartDate.plusMonths(i);
            
            double Interest = RemainingCapital * MonthlyRate;
            double Capital = MonthlyInstallment - Interest;
            RemainingCapital -= Capital;
            
            Cuotas Installment = new Cuotas(i, MonthlyInstallment, PaymentDate, "Pendiente", Interest, RemainingCapital);
            MonthlyPayments.EnQueue(Installment);
        }
        
        CreditRequest.setMonthly(MonthlyPayments);
        ApprovedCredits.AddLast(CreditRequest);
    }

    private void RegisterRejectedRequest(CreditRequest CreditRequest) {
        RejectedCredits.Push(CreditRequest);
    }

    public String AprobarORechazarSolicitud(String clienteId) {
        Customer cliente = (Customer) CustomerList.SearchById(clienteId);
        if (cliente != null) {
            CreditRequest solicitud = (CreditRequest) RequestQueue.DeQueue();
            if (solicitud != null) {
                String resultado = ApproveOrRejectRequest(solicitud, cliente.getSalary());
                if (resultado.contains("aprobada")) {
                    GenerarFactura(cliente);
                    return resultado + "\nFactura generada para la solicitud aprobada.";
                }
                return resultado;
            } else {
                return "No hay solicitudes pendientes.";
            }
        } else {
            return "Cliente no encontrado.";
        }
    }

    // Métodos numeral 4: Pagar
    public String RealizarPago(String CodigoCredito, double MontoPago) 
    {
        CreditRequest Credito = (CreditRequest) GetApprovedCredits().SearchByCode(CodigoCredito);
    
    
        if (Credito == null)
        {
        return "No se encontró un crédito con el código especificado.";
        }
        Queue CuotasOriginales = Credito.getMonthly();
        Queue CuotasActualizadas = new Queue();
        boolean PagoRealizado = false;
        double Excedente = MontoPago;
        String Mensaje = "";
    
    // Procesar cada cuota
    
       while (!CuotasOriginales.IsEmpty())
       {
           Cuotas cuota = (Cuotas) CuotasOriginales.DeQueue();
        // Si la cuota está pendiente y aún hay monto por pagar 
           if (cuota.getStatus().equals("Pendiente") && Excedente > 0 && !PagoRealizado) 
           {
               double valorCuota = cuota.getValue(); 
               if (Excedente >= valorCuota) 
               {
                // Pago completo de la cuota
                cuota.setStatus("Pagada");
                Excedente -= valorCuota;
                Mensaje += String.format("Cuota %d pagada completamente.\n" +"Fecha de pago: %s\n" + "Valor cuota: $%.2f\n" + "Intereses: $%.2f\n" 
                        +  "Capital amortizado: $%.2f\n",cuota.getCode(), cuota.getPayDay(), cuota.getValue(),cuota.getIntereses(),cuota.getCapital());
               } else 
               {
                
                   // Si el pago es menor que la cuota
                   Mensaje += String.format("Pago parcial para la cuota %d\n" +"Valor de la cuota: $%.2f\n" +"Monto pagado: $%.2f\n" +"Valor pendiente: $%.2f\n",
                   cuota.getCode(),valorCuota,Excedente,(valorCuota - Excedente) );
                   Excedente = 0;
               }
               PagoRealizado = true;
           }
           CuotasActualizadas.EnQueue(cuota);
    
       }
       // Actualizar las cuotas del crédito 
       Credito.setMonthly(CuotasActualizadas);
    
    
       if (Excedente > 0) 
       {
           Mensaje += String.format("Excedente de $%.2f será aplicado a la siguiente cuota.\n", Excedente);
       }
    
    
// Retornar el resultado
      if (PagoRealizado) 
      {
        return "Pago procesado exitosamente.\n" + Mensaje;
      } else 
      {
        return "No se encontraron cuotas pendientes para pagar.";
      }
}

    // Métodos numeral 5: Cancelar crédito
    public String CancelCredit(String creditCode) 
    {
    
        CreditRequest CreditRequest = (CreditRequest) GetApprovedCredits().SearchByCode(creditCode);
        if (CreditRequest != null) 
        {
            double RemainingBalance = CalculateRemainingBalance(CreditRequest);
            double DiscountedAmount = RemainingBalance * 0.9; // 10% de descuento
            String Message = "Crédito con código " + creditCode + " cancelado.\n";
            Message += "Valor a pagar: " + DiscountedAmount;     
            UpdateCreditStatus(CreditRequest);
            return Message;
        } else 
        {
            return "No se encontró un crédito con el código " + creditCode;
        }
}
    private double CalculateRemainingBalance(CreditRequest CreditRequest)
    {
        double RemainingBalance = 0.0;
        Queue Cuotas = CreditRequest.getMonthly();
        while (!Cuotas.IsEmpty()) 
        {
            Cuotas Cuota = (Cuotas) Cuotas.DeQueue();
            if (Cuota.getStatus().equals("Pendiente")) 
            {
                RemainingBalance += Cuota.getValue();
            }
        }
        return RemainingBalance;
    }

    private void UpdateCreditStatus(CreditRequest CreditRequest) 
    {
        Queue Cuotas = CreditRequest.getMonthly();
        while (!Cuotas.IsEmpty()) 
        {
            Cuotas Cuota = (Cuotas) Cuotas.DeQueue();
            Cuota.setStatus("Pagada");
        }
    }

    // Métodos numeral 6: Revaluar la solicitud
   public String RevalidateRejectedRequests() 
    {
        if (RejectedCredits.IsEmpty()) {
            return "No hay solicitudes rechazadas para revaluar.";
    }
        // Construir árbol con solicitudes rechazadas
        while (!RejectedCredits.IsEmpty()) 
        {
            CreditRequest Request = (CreditRequest) RejectedCredits.Pop();
            RejectedTree.AddCreditRequest(Request);
        }
        // Obtener solicitudes con un solo hijo
        List ApprovedRequests = RejectedTree.GetSingleChildRequests();
        if (ApprovedRequests.IsEmpty()) 
        {
            return "No hay solicitudes que cumplan con los criterios de aprobación.";
        }

        // Mover las solicitudes aprobadas a la lista de créditos vigentes
        String Result = "Solicitudes aprobadas:\n";
        Node Aux = ApprovedRequests.GetFirst();
        
        while (Aux != null) 
        {
            CreditRequest Request = (CreditRequest) Aux.getData();
            
            // Calcular tasa mensual
            double AnnualRate = GetEffectiveAnnualRate(Request.getType());
            double MonthlyRate = Math.pow(1 + AnnualRate, 1.0/12) - 1;
            
            // Calcular cuota mensual
            double MonthlyInstallment = CalculateMonthlyInstallment(Request.getValue(),MonthlyRate,Request.getFee());
            
            // Registrar como crédito aprobado
            RegisterApprovedRequest(Request, MonthlyInstallment, MonthlyRate);
            
            // Eliminar del árbol
            RejectedTree.Delete(Request);
            
            // Agregar a la cadena de resultado
            Result = Result + Request.toString() + "\n";
            Aux = Aux.getLink();
        }

        return Result;
    }

    public String PrintRejectedTree() {
        if (RejectedTree.IsTreeEmpty()) {
            return "El árbol está vacío";
        }
        return RejectedTree.InOrder();
    }

    // Métodos numeral 7: Enviar factura
    public void GenerarFactura(Customer Cliente) 
    {
        Archivo Archivo = new Archivo();
        List CreditosAprobados = GetApprovedCredits();
        Node CreditAux = CreditosAprobados.GetFirst(); // Recorremos los créditos aprobados
        String Factura = "Señor " + Cliente.getName() + ":\n";
        Factura += "Dentro de los primeros 5 días del mes " + LocalDate.now().plusMonths(1).getMonth() + " debe realizar el pago de las siguientes cuotas:\n\n";
        Factura += "CódigoCrédito\tValor\tPagosPendientes\tTotal\n";
        double TotalDeuda = 0.0;
        while (CreditAux != null) 
        {
            CreditRequest Credito = (CreditRequest) CreditAux.getData();
            if (Credito.getOwner().equals(Cliente.getId())) 
            {
                Queue CuotasPendientes = Credito.getMonthly();
                int PagosPendientes = 0;
                double TotalCredito = 0.0;
                Queue CuotasAux = new Queue();
                while (!CuotasPendientes.IsEmpty())
                {
                    Cuotas cuota = (Cuotas) CuotasPendientes.DeQueue();
                    CuotasAux.EnQueue(cuota); 
                    if (cuota.getStatus().equals("Pendiente")) 
                    {
                        PagosPendientes++;
                        TotalCredito += cuota.getValue();
                    }
                }
                while (!CuotasAux.IsEmpty()) 
                {
                    CuotasPendientes.EnQueue(CuotasAux.DeQueue());
                }

                TotalDeuda += TotalCredito;
                Factura += Credito.getCode() + "\t" + Credito.getValue() + "\t" + PagosPendientes + "\t" + TotalCredito + "\n";
            }

            CreditAux  = CreditAux.getLink();
        }
        
        Factura += "\nEl total a pagar de sus créditos es: $ " + TotalDeuda + " pesos.";
        Archivo.EscribirFactura(Cliente.getId(), Factura);
    }

    public double CalcularTotalCreditosAprobados(String ClienteId) 
    {
        double TotalCreditos = 0.0;
        Node CreditAux = ApprovedCredits.GetFirst(); // Recorremos la lista de créditos aprobados
        // Recorrer los créditos aprobados
        while (CreditAux  != null) 
        {
            CreditRequest credito = (CreditRequest) CreditAux .getData();
            // Verificar si el crédito pertenece al cliente actual
            if (credito.getOwner().equals(ClienteId))
            {
                TotalCreditos += credito.getValue();
            }     
            CreditAux = CreditAux.getLink();
        }
        return TotalCreditos;
    }
    
    //Métodos numeral 9: Mostrar los créditos de una persona, dada su identificación 
        public double CalcularSaldoPendiente(String ClienteId, String CodigoCredito) 
        {
            double SaldoPendiente = 0.0;
            Node CreditAux = ApprovedCredits.GetFirst(); // Recorremos los créditos aprobados
            // Buscar el crédito con el código y cliente correspondientes
            while (CreditAux  != null) 
            {   
                CreditRequest Credito = (CreditRequest) CreditAux.getData();
                if (Credito.getOwner().equals(ClienteId) && Credito.getCode().equals(CodigoCredito)) 
                {
                    Queue cuotasPendientes = Credito.getMonthly();
                    // Revisamos todas las cuotas pendientes         
                    while (!cuotasPendientes.IsEmpty()) 
                    {
                        Cuotas Cuota = (Cuotas) cuotasPendientes.DeQueue();
                        if (Cuota.getStatus().equals("Pendiente")) 
                        {
                            SaldoPendiente += Cuota.getValue();
                        }
                    }

                    break; // Si ya encontramos el crédito, salimos del ciclo
                }
        
                CreditAux  = CreditAux.getLink();
            }
            return SaldoPendiente;

        }
    // Métodos numeral 9: Eliminar solicitud de crédito
    public String EliminarSolicitudCredito(String ClienteId, String CodigoCredito) 
    {
        Queue AuxQueue = new Queue();
        boolean Encontrado = false;
        // Recorremos la cola de solicitudes   
        while (!RequestQueue.IsEmpty()) 
        {
            CreditRequest Solicitud = (CreditRequest) RequestQueue.DeQueue();
            // Si coincide con el cliente y el código del crédito, no la volvemos a encolar
            if (Solicitud.getOwner().equals(ClienteId) && Solicitud.getCode().equals(CodigoCredito)) 
            {
                Encontrado = true;
            } 
            else 
            {
                AuxQueue.EnQueue(Solicitud); // Guardamos las solicitudes no eliminadas en la cola temporal      
            }
        }
        // Restauramos la cola original
        while (!AuxQueue.IsEmpty())
        {
            RequestQueue.EnQueue(AuxQueue.DeQueue());
        }
        if (Encontrado)
        {
            return "Solicitud eliminada con éxito.";
        } 
        else 
        {
            return "No se encontró la solicitud para eliminar.";
        }
    }

    // Métodos numeral 10: Ver historial de créditos aprobados  
    public String VerHistorialCreditosAprobados(String ClienteId)    
    {
            String Historial = "";
            Node CreditAux = ApprovedCredits.GetFirst(); // Recorremos los créditos aprobados
            // Buscar los créditos correspondientes al cliente
            while (CreditAux != null) 
            {
                CreditRequest Credito = (CreditRequest) CreditAux.getData(); 
                if (Credito.getOwner().equals(ClienteId)) 
                {
                    Historial += Credito.toString() + "\n"; // Agregamos la información del crédito al historial
                }
                CreditAux  = CreditAux.getLink();
    
            } 
            if (Historial.isEmpty()) 
            {
                return "No se encontraron créditos aprobados para este cliente.";
            } 
            else 
            {
                return Historial;
            }
    }

    // Métodos numeral 11: Ver solicitudes pendientes
    public String VerSolicitudesPendientes() 
    {
        String SolicitudesPendientes = "";
        Queue AuxQueue = new Queue();
        // Recorremos la cola de solicitudes pendientes
        while (!RequestQueue.IsEmpty()) 
        {
            CreditRequest Solicitud = (CreditRequest) RequestQueue.DeQueue();
            SolicitudesPendientes += Solicitud.toString() + "\n"; // Guardamos la solicitud
            AuxQueue .EnQueue(Solicitud); // Guardamos en la cola temporal para restaurar la cola original
        }
        // Restauramos la cola original
        while (!AuxQueue.IsEmpty()) 
        {
            RequestQueue.EnQueue(AuxQueue.DeQueue());
        }
        if (SolicitudesPendientes.isEmpty())
        {
            return "No hay solicitudes pendientes de aprobación.";
        } 
        else 
        {
            return SolicitudesPendientes;
        }
    }


    // Métodos numeral 12: Otras funciones de visualización
    public String PrintStack(Stack Stack) 
    {
        Stack Aux = new Stack();
        String text = "";
        while (!Stack.IsEmpty()) 
        {
            CreditRequest CreditRequest = (CreditRequest) Stack.Pop(); 
            text += CreditRequest.toString() + "\n";
            Aux.Push(CreditRequest);
        }
        while (!Aux.IsEmpty()) Stack.Push(Aux.Pop());
        return text;
    }

    public String PrintRejected() 
    {
        return PrintStack(RejectedCredits);
    }

    public boolean RejectedEmpty() {
        return RejectedCredits.IsEmpty();
    }
    
    public List CustomerListLocal() {
        return CustomerList;
    }

    public List GetApprovedCredits() {
        return ApprovedCredits;
    }

    public Stack GetRejectedCredits() {
        return RejectedCredits;
    }

    public Queue RequestQueue() {
        return RequestQueue;
    }
}