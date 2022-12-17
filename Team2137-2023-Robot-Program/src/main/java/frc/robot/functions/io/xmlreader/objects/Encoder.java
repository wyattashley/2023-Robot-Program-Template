//              Copyright 2022 Wyatt Ashley
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package frc.robot.functions.io.xmlreader.objects;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.robot.Robot;
import frc.robot.functions.io.xmlreader.Entity;
import org.w3c.dom.Element;

public class Encoder extends Entity {

    public enum EncoderTypes {
        CTRE_CAN_ABS ("CTRE_CAN_ABS");

        final String name;

        EncoderTypes (String value) {
            name = value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    EncoderTypes type;
    boolean inverted;
    double offset;
    int id;

    public Encoder(String _name, int _id, EncoderTypes _type, boolean _invert, double offset) {
        super(_name);
        this.id = _id;
        this.type = _type;
        this.inverted = _invert;
        this.offset = offset;
    }

    public Encoder(Element element) {
        super(element);

        this.id = Integer.parseInt(getOrDefault(element, "ID", "0"));
        this.type = EncoderTypes.valueOf(getOrDefault(element, "Type", "CTRE_CAN_ABS").toUpperCase());
        this.inverted = Boolean.parseBoolean(getOrDefault(element, "Inverted", "false").toLowerCase());
        this.offset = Double.parseDouble(getOrDefault(element, "Offset", "0"));
    }

    public EncoderTypes getEncoderType() {
        return this.type;
    }

    public void setEncoderTypes(EncoderTypes _type) { type = _type; }

    public boolean inverted() {
        return this.inverted;
    }

    public void setInverted(boolean val) { inverted = val; }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double _offset) { offset = _offset; }

    public int getID() { return id; }

    public void setID(int _id) { id = _id;}

    @Override
    public Element updateElement() {
        super.updateElement();

        getSavedElement().getElementsByTagName("Type").item(0).setTextContent(String.valueOf(type.name));
        getSavedElement().getElementsByTagName("Inverted").item(0).setTextContent(String.valueOf(inverted));
        getSavedElement().getElementsByTagName("Offset").item(0).setTextContent(String.valueOf(offset));
        getSavedElement().getElementsByTagName("ID").item(0).setTextContent(String.valueOf(id));

        return getSavedElement();
    }

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
}
