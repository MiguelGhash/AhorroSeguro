package Models;

/**
 *
 * @author BaalG
 */
public class Customer {
    private String Id;
    private String Type;
    private String Name;
    private String Adress;
    private String Tel;
    private String Contract;
    private Double Salary;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String Adress) {
        this.Adress = Adress;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public String getContract() {
        return Contract;
    }

    public void setContract(String Contract) {
        this.Contract = Contract;
    }

    public Double getSalary() {
        return Salary;
    }

    public void setSalary(Double Salary) {
        this.Salary = Salary;
    }

    public Customer(String Id, String Type, String Name, String Adress, String Tel, String Contract, Double Salary) {
        this.Id = Id;
        this.Type = Type;
        this.Name = Name;
        this.Adress = Adress;
        this.Tel = Tel;
        this.Contract = Contract;
        this.Salary = Salary;
    }


    @Override
    public String toString() {
        return "Cliente{" + "Id: " + Id + ", Tipo: " + Type + ", Nombre: " + Name + ", Dirección: =" + Adress + ", Teléfono: " + Tel + ", Contrato: " + Contract + ", Salario: " + Salary + '}';
    }
    
    
    
}
