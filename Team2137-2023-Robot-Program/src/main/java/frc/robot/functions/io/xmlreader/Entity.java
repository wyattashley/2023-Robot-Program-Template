package frc.robot.functions.io.xmlreader;

import edu.wpi.first.networktables.NetworkTable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Entity {

    private final String strName;
    private Element savedElement;
    private boolean boolIsHardwareDevice = true;

    public Entity(String name) {
        strName = name;
//        Robot.deviceCallList.put(strName, this);
    }

    public void setHardwareDevice(boolean value) {
        boolIsHardwareDevice = value;
    }

    public boolean isHardwareDevice() {
        return boolIsHardwareDevice;
    }

    public Entity(Element element) {
        this(getNodeOrAttribute(element, "name", null));

        savedElement = element;
    }

    protected static String getOrDefault(Element element, String name, String defaultReturn) {
        NodeList childNodes = element.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++) {
            Node a = childNodes.item(i);

            if (Node.ELEMENT_NODE != a.getNodeType()) {
                continue;
            }

            if(((Element) a).getTagName().equalsIgnoreCase(name)) {
                return childNodes.item(i).getTextContent();
            }
        }

        return defaultReturn;
//        NodeList list = element.getElementsByTagName(name);
//        if(list.getLength() > 0)
//            return list.item(0).getTextContent();
//        else
//            return defaultReturn;
    }

    protected static String getAttributeOrDefault(Element element, String name, String defaultReturn) {
        String value = element.getAttribute(name);
        if(value.equals(""))
            return defaultReturn;
        else
            return value;
    }

    protected static String getNodeOrAttribute(Element element, String name, String defaultReturn) {
        String capitalizedFirstLetter = String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1).toLowerCase();

        String nodeResult = getOrDefault(element, capitalizedFirstLetter, null);
        String attributeResult = getAttributeOrDefault(element, name.toLowerCase(), null);

        if(nodeResult != null)
            return nodeResult;
        else if (attributeResult != null)
            return attributeResult;
        else
            return defaultReturn;
    }

    public String getName() {
        if(strName == null)
            return "Default";
        return strName;
    }

    public void constructTreeItemPrintout(StringBuilder builder, int depth) {
        buildStringTabbedData(builder, depth, "Name", getName());
    }

    public final void buildStringTabbedData(StringBuilder builder, int number, String title, String message) {
        builder.append("\t".repeat(number));
        builder.append(title);
        builder.append(": ");
        builder.append(message);
        builder.append("\n");
    }

    public NetworkTable addToNetworkTable(NetworkTable instance, boolean mutable) {
        return instance.getSubTable(getName());
    }

    public void removeFromNetworkTable(NetworkTable instance) {

    }

    protected NetworkTable addToNetworkTable(String name, NetworkTable instance, boolean mutable) {
        return instance.getSubTable(name);
    }

    protected Element getSavedElement() {
        return savedElement;
    }

    public Element updateElement() {
        if (savedElement == null || this.strName == null)
            return getSavedElement();

        if(savedElement.hasAttribute(getName().toLowerCase()))
            savedElement.setAttribute("name", getName());
        else if (savedElement.getElementsByTagName("Name").getLength() > 0)
            savedElement.getElementsByTagName("Name").item(0).setTextContent(getName());

        return savedElement;
    }

//    public void periodic() {
//        Robot.deviceCallList.remove(this);
//    }
}