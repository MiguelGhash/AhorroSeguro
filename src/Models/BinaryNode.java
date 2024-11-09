
package Models;

public class BinaryNode {
     private BinaryNode left;
     private Object Data;
     private BinaryNode Right;

    public BinaryNode(Object Data) {
        this.Data = Data;
    }

    public BinaryNode getLeft() {
        return left;
    }

    public void setLeft(BinaryNode left) {
        this.left = left;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object Data) {
        this.Data = Data;
    }

    public BinaryNode getRight() {
        return Right;
    }

    public void setRight(BinaryNode Right) {
        this.Right = Right;
    }
     
     
}
