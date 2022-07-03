package frc.robot.functions.io.xmlreader.objects;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.robot.functions.io.xmlreader.Entity;
import org.w3c.dom.Element;

public class Gyro extends Entity {

    public enum GyroTypes {
        PIGEON ("PIGEON");

        final String name;

        GyroTypes (String value) {
            name = value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    GyroTypes type;
    boolean inverted;
    double offset;
    int id;

    public Gyro(String _name, int _id, GyroTypes _type, boolean _invert, double offset) {
        super(_name);
        this.type = _type;
        this.inverted = _invert;
        this.offset = offset;
        this.id = _id;
    }

    public Gyro(Element element) {
        super(element);

        this.type = GyroTypes.valueOf(getOrDefault(element, "Type", "Pigeon"));
        this.inverted = Boolean.parseBoolean(getOrDefault(element, "Inverted", "false"));
        this.offset = Double.parseDouble(getOrDefault(element, "Offset", "0"));
        this.id = Integer.parseInt(getOrDefault(element, "ID", "0"));
    }

    public GyroTypes getEncoderType() {
        return this.type;
    }

    public void setEncoderType(GyroTypes _type) { this.type = _type; }

    public boolean inverted() {
        return this.inverted;
    }

    public void setInverted(boolean _inverted) { this.inverted = _inverted; }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double _offset) { this.offset = _offset; }

    public int getID() { return id; }

    public void setID(int _id) { id = _id; }


    @Override
    public NetworkTable addToNetworkTable(NetworkTable dashboard, boolean mutable) {
        NetworkTable table = super.addToNetworkTable(dashboard, mutable);

        NetworkTableEntry entryInverted = table.getEntry("Inverted");
        entryInverted.setBoolean(inverted);

        if(mutable) {
            entryInverted.addListener((entryNotification) -> {
                setInverted(entryNotification.getEntry().getBoolean(inverted()));
            }, EntryListenerFlags.kUpdate);
        }

        //None mutable
        NetworkTableEntry entryType = table.getEntry("Type");
        entryType.setString(type.toString());

        NetworkTableEntry entryID = table.getEntry("ID");
        entryID.setDouble(id);

        NetworkTableEntry entryOffset = table.getEntry("Offset");
        entryType.setString(String.valueOf(offset));

        if(mutable) {
            entryOffset.addListener((entryNotification) -> {
                setOffset(entryNotification.getEntry().getDouble(getOffset()));
            }, EntryListenerFlags.kUpdate);
        }

        return table;
    }

    @Override
    public void removeFromNetworkTable(NetworkTable instance) {
        NetworkTable table = instance.getSubTable(getName());

        table.getEntry("Inverted").removeListener(0);
        table.getEntry("Offset").removeListener(0);
    }

    @Override
    public void constructTreeItemPrintout(StringBuilder builder, int depth) {
        super.constructTreeItemPrintout(builder, depth);
        buildStringTabbedData(builder, depth, "ID", String.valueOf(id));
        buildStringTabbedData(builder, depth, "Type", type.toString());
        buildStringTabbedData(builder, depth, "Inverted", String.valueOf(inverted));
        buildStringTabbedData(builder, depth, "Offset", String.valueOf(offset));
    }

    @Override
    public Element updateElement() {
        super.updateElement();

        getSavedElement().getElementsByTagName("ID").item(0).setTextContent(String.valueOf(id));
        getSavedElement().getElementsByTagName("Inverted").item(0).setTextContent(String.valueOf(inverted));
        getSavedElement().getElementsByTagName("Type").item(0).setTextContent(type.name);
        getSavedElement().getElementsByTagName("Offset").item(0).setTextContent(String.valueOf(offset));

        return getSavedElement();
    }
}
