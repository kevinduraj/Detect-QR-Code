package b_components;

/*------------------------------------------------------------------------------------------------*/
class ListNode {

    Object data;
    ListNode nextNode;

    ListNode(Object _object) {
        this(_object, null);
    }

    ListNode(Object _object, ListNode _nextnode) {
        data = _object;
        nextNode = _nextnode;
    }

    Object getObject() {
        return data;
    }

    ListNode getNext() {
        return nextNode;
    }

}
/*------------------------------------------------------------------------------------------------*/

public class List {

    public ListNode firstNode;
    public ListNode lastNode;
    public String name;

    public List() {
        this("list");
    }

    public List(String _listname) {
        name = _listname;
        firstNode = lastNode = null;
    }

    public synchronized void insertAtFront(Object _insertItem) {
        if (isEmpty()) {
            firstNode = lastNode = new ListNode(_insertItem);
        } else {
            firstNode = new ListNode(_insertItem, firstNode);
        }
    }

    public synchronized void insertAtBack(Object _insertItem) {
        if (isEmpty()) {
            firstNode = lastNode = new ListNode(_insertItem);
        } else {
            lastNode = lastNode.nextNode = new ListNode(_insertItem);
        }
    }

    public synchronized Object removeFromFront() throws EmptyListException {
        if (isEmpty()) {
            throw new EmptyListException(name);
        }

        Object removedItem = firstNode.data;

        if (firstNode == lastNode) {
            firstNode = lastNode = null;
        } else {
            firstNode = firstNode.nextNode;
        }

        return removedItem;
    }

    public synchronized void clearList() {
        while (!isEmpty()) {
            Object removedObject = removeFromFront();
        }
    }

    public synchronized Object removeFromBack() throws EmptyListException {
        if (isEmpty()) {
            throw new EmptyListException(name);
        }

        Object removedItem = lastNode.data;

        if (firstNode == lastNode) {
            firstNode = lastNode = null;
        } else {
            ListNode current = firstNode;
            while (current.nextNode != lastNode) {
                current = current.nextNode;
            }

            lastNode = current;
            current.nextNode = null;
        }

        return removedItem;
    }

    public synchronized boolean isEmpty() {
        return (firstNode == null);
    }

    public synchronized void print() {
        if (isEmpty()) {
            System.out.println("Empty " + name);
            return;
        }

        System.out.print("The " + name + " is: ");

        ListNode current = firstNode;
        while (current != null) {
            System.out.print(current.data.toString() + " ");
            current = current.nextNode;
        }

        System.out.println("\n");
    }
}
/*------------------------------------------------------------------------------------------------*/
