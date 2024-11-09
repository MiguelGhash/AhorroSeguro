package Models;
import java.time.LocalDate;
/**
 *
 * @author BaalG
 */
public class Cuotas {
   private int Code; 
   private Double Value; 
   private LocalDate PayDay;
   private String Status;
   private Double  Intereses; 
   private Double Capital;

    public Cuotas(int Code, Double Value, LocalDate PayDay, String Status, Double Intereses, Double Capital) {
        this.Code = Code;
        this.Value = Value;
        this.PayDay = PayDay;
        this.Status = Status;
        this.Intereses = Intereses;
        this.Capital = Capital;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public Double getValue() {
        return Value;
    }

    public void setValue(Double Value) {
        this.Value = Value;
    }

    public LocalDate getPayDay() {
        return PayDay;
    }

    public void setPayDay(LocalDate PayDay) {
        this.PayDay = PayDay;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public Double getIntereses() {
        return Intereses;
    }

    public void setIntereses(Double Intereses) {
        this.Intereses = Intereses;
    }

    public Double getCapital() {
        return Capital;
    }

    public void setCapital(Double Capital) {
        this.Capital = Capital;
    }

    @Override
    public String toString() {
        return "Cuotas{" + "Code=" + Code + ", Value=" + Value + ", PayDay=" + PayDay + ", Status=" + Status + ", Intereses=" + Intereses + ", Capital=" + Capital + '}';
    }
    
    
    
}
