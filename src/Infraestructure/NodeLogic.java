
package Infraestructure;
import Models.*;

public class NodeLogic {

    public NodeLogic() {}

    // Buscar el último nodo
    public Node Last(Node first) {
        Node aux = first;
        while (aux != null && aux.getLink() != null) {
            aux = aux.getLink();
        }
        return aux;
    }

    // Nodo previo
    public Node Previous(Node First, Node Search) {
        Node Aux = First;
        Node Before = null;
        while (Aux != null && Aux != Search) {
            Before = Aux;
            Aux = Aux.getLink();
        }
        return Before;
    }
}

