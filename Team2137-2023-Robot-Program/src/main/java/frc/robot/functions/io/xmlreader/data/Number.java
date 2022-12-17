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

package frc.robot.functions.io.xmlreader.data;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.robot.functions.io.xmlreader.Entity;
import org.w3c.dom.Element;

/**
 * This class represents a double value that is mutable and
 * has the ability to be published to the Smartdashboard
 * and to the XML file
 */
public class Number extends Entity {

    private double value;

    public Number(String name, double _value) {
        super(name);

        value = _value;
    }

    public Number(Element element) {
        super(element);

        String val = getNodeOrAttribute(element, "value", null);
        if (val == null)
            value = Double.parseDouble(element.getTextContent());
        else
            value = Double.parseDouble(val);
    }

    public synchronized double getValue() {
        return value;
    }

    public synchronized void setValue(double value) {
        this.value = value;
    }

    @Override
    public NetworkTable addToNetworkTable(NetworkTable dashboard, boolean mutable) {
        NetworkTable table = super.addToNetworkTable(dashboard, mutable);

        NetworkTableEntry entry = table.getEntry("Value");
        entry.setNumber(value);

        if(mutable) {
            entry.addListener((entryNotification) -> {
                setValue(entryNotification.getEntry().getDouble(getValue()));
            }, EntryListenerFlags.kUpdate);
        }

        return table;
    }

    @Override
    public void removeFromNetworkTable(NetworkTable instance) {
        NetworkTable table = instance.getSubTable(getName());

        table.getEntry("Value").removeListener(0);
    }

    @Override
    public void constructTreeItemPrintout(StringBuilder builder, int depth) {
        super.constructTreeItemPrintout(builder, depth);
        buildStringTabbedData(builder, depth, "Value", String.valueOf(value));
    }

    @Override
    public Element updateElement() {
        super.updateElement();

        getSavedElement().setTextContent(String.valueOf(value));

        return getSavedElement();
    }
}
