
package Models;
import java.time.LocalDate;
import Infraestructure.Queue;
/**
 *
 * @author BaalG
 */
public class CreditRequest {
    private String Code;
    private String Type;
    private String Owner;
    private LocalDate RequestDate;
    private Double Value;
    private int Fee;
    private Queue Monthly = null;

    public CreditRequest(String Code, String Type, String Owner, LocalDate RequestDate, Double Value, int Fee) {
        this.Code = Code;
        this.Type = Type;
        this.Owner = Owner;
        this.RequestDate = RequestDate;
        this.Value = Value;
        this.Fee = Fee;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String Owner) {
        this.Owner = Owner;
    }

    public LocalDate getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(LocalDate RequestDate) {
        this.RequestDate = RequestDate;
    }

    public Double getValue() {
        return Value;
    }

    public void setValue(Double Value) {
        this.Value = Value;
    }

    public int getFee() {
        return Fee;
    }

    public void setFee(int Fee) {
        this.Fee = Fee;
    }

    public Queue getMonthly() {
        return Monthly;
    }

    public void setMonthly(Queue Monthly) {
        this.Monthly = Monthly;
    }
    

    @Override
    public String toString() {
        return "CreditRequest{" + "Code=" + Code + ", Type=" + Type + ", Owner=" + Owner + ", RequestDate=" + RequestDate + ", Value=" + Value + ", Fee=" + Fee + '}';
    }
    
}
