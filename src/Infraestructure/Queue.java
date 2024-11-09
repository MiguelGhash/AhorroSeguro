package Infraestructure;
import Models.*;


public class Queue {
    private Node First;
    private Node Last;

    public Queue() {
    }
    public boolean IsEmpty()
    {
        return First == null && Last == null;
    }
public void EnQueue(Object Data)
{
    Node New = new Node(Data);
    if(IsEmpty())
    {
        First = New; // Inicializa First con el nuevo nodo
        Last = First; // Inicializa Last con el nodo que es ahora First
    }
    else
    {
        Last.setLink(New);
        Last = New;
    }
}

    public Object DeQueue()
    {
         Object Data = null;
        if(!IsEmpty())
        { 
            Data = First.getData();
            First = First.getLink();
            if(First==null)
            {
                Last = null;
            }
        }
        return Data;
    }
}