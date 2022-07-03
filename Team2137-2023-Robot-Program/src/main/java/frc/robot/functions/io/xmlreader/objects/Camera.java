package frc.robot.functions.io.xmlreader.objects;

import frc.robot.functions.io.xmlreader.Entity;
import org.w3c.dom.Element;

public class Camera extends Entity {
    private int deviceID;

    public Camera(Element element) {
        super(element);

        deviceID = Integer.parseInt(getOrDefault(element, "ID", "0"));
    }

    @Override
    public void constructTreeItemPrintout(StringBuilder builder, int depth) {
        super.constructTreeItemPrintout(builder, depth);
        buildStringTabbedData(builder, depth, "ID", String.valueOf(deviceID));
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    @Override
    public Element updateElement() {
        super.updateElement();

        getSavedElement().getElementsByTagName("ID").item(0).setTextContent(String.valueOf(deviceID));

        return getSavedElement();
    }
}
