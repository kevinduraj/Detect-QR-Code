package b_components;

public class EmptyListException extends RuntimeException {

    public EmptyListException() {
        this("List");
    }

    public EmptyListException(String _name) {
        super(_name + " is empty");
    }
}
