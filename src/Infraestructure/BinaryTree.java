package Infraestructure;
import Models.*;
import javax.swing.JOptionPane;
public class BinaryTree {
    BinaryNode Root;

    public BinaryTree()
    {
        Root = null;
    }
    public boolean IsTreeEmpty()
    {
        return Root == null;
    }
    public BinaryNode GetRoot()
    {
        return Root;
    }
    private void Add(Object Data, BinaryNode Aux)
    {
        if(IsTreeEmpty())
        {
            Root = new BinaryNode(Data);
        }
        else
        {
            String SideMenu[] = {"Right","Left"};
            String Side =(String)JOptionPane.showInputDialog(null, "Select", "Main menu", 1, null, SideMenu, SideMenu[0]);
            if(Side.equals("Left"))
            {
                if (Aux.getLeft() == null) 
                {
                Aux.setLeft(new BinaryNode(Data));
                }
                else
                {
                Add(Data, Aux.getLeft());
                }
            }
            else
            {
                if(Side.equals("Right"))
                {
                    if (Aux.getRight() == null) 
                    {
                    Aux.setRight(new BinaryNode(Data));
                    }
                    else
                    {
                    Add(Data, Aux.getRight());
                    }
                }   
            }

        }          
    }
    

    public void Add(Object Data)
    {
 
        Add(Data, GetRoot());
    }
    public String PreOrder()
    {
       return PreOrder(GetRoot());
    }
    private String PreOrder(BinaryNode Aux)
    {
        if(Aux!= null)
        {
            return  Aux.getData()+ " " + PreOrder(Aux.getLeft())+" "+PreOrder(Aux.getRight());
        }
        return "";
    } 
    public String InOrder()
    {
       return PostOrder(GetRoot());
    }
    private String InOrder(BinaryNode Aux)
    {
        if(Aux!= null)
        {
            return  PreOrder(Aux.getLeft())+" "+Aux.getData() + " "+PreOrder(Aux.getRight()) ;
        }
        return "";
    } 
    public String PostOrder()
    {
       return PostOrder(GetRoot());
    }
    private String PostOrder(BinaryNode Aux)
    {
        if(Aux!= null)
        {
            return  PostOrder(Aux.getLeft())+" "+PostOrder(Aux.getRight())+" "+ Aux.getData();
        }
        return "";
    }
    public int Size()
    {
        return Size(Root);
    }
    private int Size(BinaryNode Aux)
    {
        int Cont = 0;
       
        if(Aux!= null) 
        {
            return + 1 + (Size(Aux.getLeft())) + (Size(Aux.getRight())); 
        }
         return 0;         
    }
    private int Height(BinaryNode Aux)
    {
        int Cont = 0;
        if(Aux!=null)
            return  1 + Math.max(Height(Aux.getLeft()), Height(Aux.getRight()));
        return 0;
         
    }
    public int Height()
    {
        return Height(Root);
    }
    
    private boolean Search(BinaryNode Aux, Object Data) 
    {
        if (Aux == null) 
        {
            return false;
        }
        if (Aux.getData().equals(Data))
        {
            return true;
        }
        return Search(Aux.getLeft(), Data) || Search(Aux.getRight(), Data);
    }
    public boolean Search(Object Data)
    {
        return Search(Root,Data);
    }
    private BinaryNode SearchNode(BinaryNode Aux, Object Data)
    {
        BinaryNode Result = null;
        if(Aux!=null)
        {
            if(Aux.getData().equals(Data))
            {
                Result = Aux;
            }
            else
            {
               Result = SearchNode(Aux.getLeft(),Data);
               if(Result == null)
               {
                   Result = SearchNode(Aux.getRight(),Data);
               }
            }
        } 
        return Result;
    }
    
    public BinaryNode SearchNode(Object Data)
    {
        return SearchNode(Root, Data);
    }
    private BinaryNode GetFather(BinaryNode Aux, Object Data)
    {
        BinaryNode Father = null;
        if(Aux!= null)
        {
            if(Aux.getLeft()!=null && (Aux.getLeft()).getData().equals(Data) ||Aux.getRight()!=null && (Aux.getRight()).getData().equals(Data)  )
            {
                Father = Aux;
            }
            else
            {
              Father = GetFather(Aux.getLeft(),Data);
              if(Father == null)
              {
                  Father = GetFather(Aux.getRight(),Data);
              }
            }
        }
     return Father;
    }
    
    public BinaryNode GetFather(Object Data)
    {
        if(!IsTreeEmpty())
        {
            if((GetRoot()).getData().equals(Data))
            {
                return null;
            }
            else
            {
                return GetFather(GetRoot(), Data);
            }
        }
     return null;
    }
    private List Sucessor(Object Data, BinaryNode Aux, List List)
    {
        if(Aux!=null)
        {
         if(Aux.getData().equals(Data))
         {
             List.AddLast(Aux.getData());
             Sucessor(Data,Aux.getLeft(),List);
             Sucessor(Data,Aux.getRight(),List);
         }
        }
        return List;
    }
    public List Sucessor(Object Data)
    {
        BinaryNode Init = SearchNode(Data);
        if(Init!=null)
        {
            return Sucessor(Data, Init, new List());
        }else
        {
            return null;
        }
            
    }
    public boolean Delete(Object Data)
    {
        BinaryNode Delete = SearchNode(Data);
        if(Delete!=null)
        {
            if(Delete.getLeft()==null && Delete.getRight()==null)
            {
                DeleteZeroChild(Delete);
            }
            else
            {
                if(Delete.getLeft()==null || Delete.getRight()==null)
                {
                    DeleteOne(Delete);
                }
                else
                {
                    DeleteTwo(Delete);
                }
               
            }
            return true;
        }
        return false;
    }
    private void DeleteZeroChild(BinaryNode Delete) 
    {
            BinaryNode Father = GetFather(Delete.getData());
            if (Father == null)
            {
                Root = null; 
            } 
            else 
            {
                if (Father.getLeft() == Delete) 
                {
                    Father.setLeft(null);
                }
                else 
                {
                    Father.setRight(null); 
                }
        }
    }
    

    private void DeleteOne(BinaryNode Delete)
    {
        BinaryNode Father = GetFather(Delete.getData());
        if(Father == null)
        {
            if(Delete.getLeft()!=null)
            {
                Root = Delete.getLeft();
            }
            else
            {
               Root = Delete.getRight();
            }
        }
        else
        {
            if(Father.getLeft()==Delete)
            {
                if(Delete.getLeft()!=null)
                {
                    Father.setLeft(Delete.getLeft());
                }
                else
                {
                    Father.setLeft(Delete.getRight());
                }
            }
            else
            {
                if(Delete.getRight()!=null)
                {
                    Father.setRight(Delete.getLeft());
                }
                else
                {
                    Father.setRight(Delete.getLeft());
                } 
            }
        }
    }

    private void DeleteTwo(BinaryNode Delete)
    { 
           String Value = (String)(MostLeft(Delete)).getData();
           Delete(Value);
           Delete.setData(Value);
    }
    private BinaryNode MostLeft(BinaryNode Aux)
    {
        if(Aux.getLeft()!=null)
        {
            return MostLeft(Aux.getLeft());
        }
        return Aux;
    }
    
    // Nuevo método para insertar según el valor del préstamo
    public void AddCreditRequest(CreditRequest Request)
    {
        Root = AddCreditRequestRecursive(Root, Request);
    }

    private BinaryNode AddCreditRequestRecursive(BinaryNode Aux, CreditRequest Request) {
        if (Aux == null) {
            return new BinaryNode(Request);
        }

        CreditRequest currentRequest = (CreditRequest) Aux.getData();
        
        // Si el valor es mayor, va a la izquierda
        if (Request.getValue() > currentRequest.getValue()) {
            Aux.setLeft(AddCreditRequestRecursive(Aux.getLeft(), Request));
        }
        // Si el valor es menor o igual, va a la derecha
        else {
            Aux.setRight(AddCreditRequestRecursive(Aux.getRight(), Request));
        }

        return Aux;
    }
    
    
    ///
    // Método para verificar si un nodo tiene un solo hijo
    public boolean HasSingleChild(BinaryNode Node) 
    {
        if (Node == null) 
        {
          return false;  
        }    
        return (Node.getLeft() == null && Node.getRight() != null) ||
               (Node.getLeft() != null && Node.getRight() == null);
    }

    // Método para obtener todas las solicitudes con un solo hijo
    public List GetSingleChildRequests() 
    {
        List SingleChildRequests = new List();
        CollectSingleChildRequests(Root, SingleChildRequests);
        return SingleChildRequests;
    }

    private void CollectSingleChildRequests(BinaryNode Node, List List) 
    {
        if (Node == null) return;
        
        if (HasSingleChild(Node)) {
            List.AddLast(Node.getData());
        }
        
        CollectSingleChildRequests(Node.getLeft(), List);
        CollectSingleChildRequests(Node.getRight(), List);
    }
}
