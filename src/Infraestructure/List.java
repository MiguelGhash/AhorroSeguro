package Infraestructure;

import Models.*;

public class List {

    private Node first;
    NodeLogic Estructura = new NodeLogic();

    public List() {}

    public boolean IsEmpty() {
        return first == null;
    }
    public Node GetFirst()
    {
        return first;
    }
    public void AddFirst(Object Obj) {
        if (IsEmpty()) {
            first = new Node(Obj);
        } else {
            Node n = new Node(Obj);
            n.setLink(first);
            first = n;
        }
    }

    public void AddLast(Object Obj) {
        if (IsEmpty()) {
            AddFirst(Obj);
        } else {
            Node Nuevo = new Node(Obj);
            Node Final = Estructura.Last(first);
            Final.setLink(Nuevo);
        }
    }

    public boolean DeleteFirst() {
        if (!IsEmpty()) {
            Node aux = first;
            first = first.getLink();
            aux = null;
            return true;
        }
        return false;
    }

    public boolean Delete(Object Obj) {
        Node aux = first;
        if (!IsEmpty()) {
            while (aux != null && !aux.getData().equals(Obj)) {
                aux = aux.getLink();
            }
            if (aux == null) {
                return false;
            } else {
                if (aux == first) {
                    DeleteFirst();
                } else {
                    Node previous = Estructura.Previous(first, aux);
                    previous.setLink(aux.getLink());
                    aux = null;
                }
                return true;
            }
        }
        return false;
    }

    public void DeleteLast() {
        if (!IsEmpty()) {
            if (first.getLink() == null) {
                // Si solo hay un nodo en la lista
                first = null;
            } else {
                Node previous = Estructura.Previous(first, Estructura.Last(first));
                previous.setLink(null);
            }
        }
    }

    public String ToString() {
        String text = "";
        Node aux = first;
        while (aux != null) {
            text = text + aux.getData() + "\n";
            aux = aux.getLink();
        }
        return text;
    }
    public Object SearchById(String Id) {
    Node Aux = first;
    while (Aux != null) {
        if (((Customer) Aux.getData()).getId().equals(Id)) {
            return Aux.getData();
        }
        Aux = Aux.getLink();
    }
    return null;
}
    public Object SearchByCode(String code) {
    Node aux = first;
    while (aux != null) {
        if (((CreditRequest) aux.getData()).getCode().equals(code)) {
            return aux.getData();
        }
        aux = aux.getLink();
    }
    return null;
}

}
