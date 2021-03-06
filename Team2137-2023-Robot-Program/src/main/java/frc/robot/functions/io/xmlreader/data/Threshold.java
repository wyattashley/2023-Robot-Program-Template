package frc.robot.functions.io.xmlreader.data;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.robot.functions.io.xmlreader.Entity;
import org.w3c.dom.Element;

public class Threshold extends Entity {
    private double upperValue;
    private double lowerValue;

    public Threshold(String name, int _upperValue, int _lowerValue) {
        super(name);
        this.upperValue = _upperValue;
        this.lowerValue = _lowerValue;
    }

    public Threshold(Element element) {
        super(element);
        this.upperValue = Double.parseDouble(getNodeOrAttribute(element, "High", "0.0"));
        this.lowerValue = Double.parseDouble(getNodeOrAttribute(element, "Low", "0.0"));
    }

    public synchronized double getUpperValue() {
        return upperValue;
    }

    public synchronized void setUpperValue(double upperValue) {
        this.upperValue = upperValue;
    }

    public synchronized double getLowerValue() {
        return lowerValue;
    }

    public synchronized void setLowerValue(double lowerValue) {
        this.lowerValue = lowerValue;
    }

    public boolean valueWithin(double val) {
        return val >= lowerValue && val <= upperValue;
    }

    @Override
    public void constructTreeItemPrintout(StringBuilder builder, int depth) {
        super.constructTreeItemPrintout(builder, depth);
        buildStringTabbedData(builder, depth, "Upper", String.valueOf(upperValue));
        buildStringTabbedData(builder, depth, "Lower", String.valueOf(lowerValue));
    }


    @Override
    public NetworkTable addToNetworkTable(NetworkTable dashboard, boolean mutable) {
        NetworkTable table = super.addToNetworkTable(dashboard, mutable);

        NetworkTableEntry highEntry = table.getEntry("High");
        highEntry.setNumber(upperValue);

        NetworkTableEntry lowEntry = table.getEntry("Low");
        lowEntry.setNumber(lowerValue);

        if(mutable) {
            highEntry.addListener((entryNotification) -> {
                setUpperValue(entryNotification.getEntry().getDouble(getUpperValue()));
            }, EntryListenerFlags.kUpdate);

            lowEntry.addListener((entryNotification) -> {
                setLowerValue(entryNotification.getEntry().getDouble(getLowerValue()));
            }, EntryListenerFlags.kUpdate);
        }

        return table;
    }

    @Override
    public void removeFromNetworkTable(NetworkTable instance) {
        NetworkTable table = instance.getSubTable(getName());

        table.getEntry("High").removeListener(0);
        table.getEntry("Low").removeListener(0);
    }

    @Override
    public Element updateElement() {
        super.updateElement();

        getSavedElement().getElementsByTagName("High").item(0).setTextContent(String.valueOf(upperValue));
        getSavedElement().getElementsByTagName("Low").item(0).setTextContent(String.valueOf(lowerValue));

        return getSavedElement();
    }
}
