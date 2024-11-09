/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Infraestructure;

import Models.*;

/**
 *
 * @author salas
 */
public class Stack {
    private Node Top;
    
    public Stack(){}
    
    public boolean IsEmpty()
    {
        return Top==null;
    }
    //Meter un dato en la pila
    public void Push(Object Data)
    {
        if(IsEmpty())
        {
            Top = new Node(Data);
        }
        else
        {
         Node Nuevo = new Node(Data);
         Nuevo.setLink(Top);
         Top = Nuevo;
        }
    }
    //Sacar de la pila
    public Object Pop()
    {
        Object Data = null;
        if(!IsEmpty())
        {
            Data = Top.getData();
            Top = Top.getLink();
            return Data;       
        }
        return null;
    }
    
}

