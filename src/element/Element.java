package element;

abstract public class Element {
    protected String name;

    abstract public void loadFromXML();
    abstract public void saveToXML();
}
