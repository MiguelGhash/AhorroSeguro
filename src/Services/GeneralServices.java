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

    public String CustomerSingUp(Customer Customer) {
        CustomerList.AddLast(Customer);
        return "Cliente registrado en el sistema";
    }

    public Customer CustomerData(String Id, String Type, String Name, String Address, String Tel, String Contract, Double Salary) {
        return new Customer(Id, Type, Name, Address, Tel, Contract, Salary);
    }

    public CreditRequest CreditRequest(String Code, String Type, String Owner, LocalDate RequestDate, Double Value, int Fee) {
        return new CreditRequest(Code, Type, Owner, RequestDate, Value, Fee);
    }

    public List CustomerListLocal() {
        return CustomerList;
    }

    public String EnqueueRequest(CreditRequest CreditRequest) {
        RequestQueue.EnQueue(CreditRequest);
        return "Solicitud de crédito agregada";
    }

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
    private void RegisterRejectedRequest(CreditRequest CreditRequest) 
    {
        RejectedCredits.Push(CreditRequest);
    }
    public List GetApprovedCredits()
    {
        return ApprovedCredits;
    }

    public Stack GetRejectedCredits() 
    {
        return RejectedCredits;
    }
    public Queue RequestQueue()
    {
        return RequestQueue;
    }
    
public String PrintStack(Stack Stack)
{   
    Stack Aux = new Stack();
    String text = "";
    while (!Stack.IsEmpty())
    {
        CreditRequest CreditRequest = (CreditRequest) Stack.Pop(); 
        text = text + CreditRequest.toString() + "\n";
        Aux.Push(CreditRequest);
    }
    while (!Aux.IsEmpty())
        Stack.Push(Aux.Pop());
    
    return text;
}

public String PrintRejected()
{
    return PrintStack(RejectedCredits);
}

public boolean RejectedEmpty()
{
    return RejectedCredits.IsEmpty();
}

public String CancelCredit(String creditCode) {
    CreditRequest CreditRequest = (CreditRequest) GetApprovedCredits().SearchByCode(creditCode);
    if (CreditRequest != null) {
        double RemainingBalance = CalculateRemainingBalance(CreditRequest);
        double DiscountedAmount = RemainingBalance * 0.9; // 10% de descuento
        String Message = "Crédito con código " + creditCode + " cancelado.\n";
        Message += "Valor a pagar: " + DiscountedAmount;
        UpdateCreditStatus(CreditRequest);
        return Message;
    } else {
        return "No se encontró un crédito con el código " + creditCode;
    }
}

private double CalculateRemainingBalance(CreditRequest CreditRequest) {
    double RemainingBalance = 0.0;
    Queue instalments = CreditRequest.getMonthly();
    while (!instalments.IsEmpty()) {
        Cuotas installment = (Cuotas) instalments.DeQueue();
        if (installment.getStatus().equals("Pendiente")) {
            RemainingBalance += installment.getValue();
        }
    }
    return RemainingBalance;
}

private void UpdateCreditStatus(CreditRequest CreditRequest) {
    Queue instalments = CreditRequest.getMonthly();
    while (!instalments.IsEmpty()) {
        Cuotas installment = (Cuotas) instalments.DeQueue();
        installment.setStatus("Pagada");
    }
}

//
    // Método para revaluar las solicitudes rechazadas
    public String RevalidateRejectedRequests() {
        if (RejectedCredits.IsEmpty()) {
            return "No hay solicitudes rechazadas para revaluar.";
        }

        // Construir árbol con solicitudes rechazadas
        while (!RejectedCredits.IsEmpty()) {
            CreditRequest request = (CreditRequest) RejectedCredits.Pop();
            RejectedTree.AddCreditRequest(request);
        }

        // Obtener solicitudes con un solo hijo
        List ApprovedRequests = RejectedTree.GetSingleChildRequests();
        
        if (ApprovedRequests.IsEmpty()) {
            return "No hay solicitudes que cumplan con los criterios de aprobación.";
        }

        // Mover las solicitudes aprobadas a la lista de créditos vigentes
        String Result = "Solicitudes aprobadas:\n";
        Node Aux = ApprovedRequests.GetFirst();
        
        while (Aux != null) {
            CreditRequest Request = (CreditRequest) Aux.getData();
            
            // Calcular tasa mensual
            double annualRate = GetEffectiveAnnualRate(Request.getType());
            double monthlyRate = Math.pow(1 + annualRate, 1.0/12) - 1;
            
            // Calcular cuota mensual
            double monthlyInstallment = CalculateMonthlyInstallment(
                Request.getValue(), 
                monthlyRate,
                Request.getFee()
            );
            
            // Registrar como crédito aprobado
            RegisterApprovedRequest(Request, monthlyInstallment, monthlyRate);
            
            // Eliminar del árbol
            RejectedTree.Delete(Request);
            
            // Agregar a la cadena de resultado
            Result = Result + Request.toString() + "\n";
            Aux = Aux.getLink();
        }

        return Result;
    }

    // Método auxiliar para convertir el contenido del árbol a String
    public String PrintRejectedTree() {
        if (RejectedTree.IsTreeEmpty()) {
            return "El árbol está vacío";
        }
        return RejectedTree.InOrder();
    }
}
