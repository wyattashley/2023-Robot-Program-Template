package frc.robot.functions.io.xmlreader;

import frc.robot.functions.io.FileLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class XMLSettingReader {

    private Document document;
    private final File settingFile;
    private EntityGroup robotEntityGroup;
    private FileLogger log;

    public XMLSettingReader(String dir, FileLogger logger) {
        this(dir, false, logger);
    }

    public XMLSettingReader(String dir, boolean printTreeMap, FileLogger logger) {
        log = logger;
        this.settingFile = new File(dir);

        if(!this.settingFile.exists()) {
            logger.writeEvent(0, FileLogger.EventType.Warning, "XML File Specified Does Not Exist, Attempting to Create a Basic One");
            try {
                this.settingFile.createNewFile();

                FileWriter tmpFileWriter = new FileWriter(this.settingFile);
                tmpFileWriter.write("<?xml version=\"1.0\"?>\n<Robot>\n\t<Settings>\n\t</Settings>\n</Robot>");
                tmpFileWriter.flush();
                tmpFileWriter.close();
            } catch (IOException e) {
                logger.writeEvent(0, FileLogger.EventType.Error, "Failed to create basic xml file because...");
                logger.writeEvent(0, FileLogger.EventType.Error, e.getMessage());
            }
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        log.writeEvent(8, FileLogger.EventType.Status, "Starting to Parse XML file...");
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            document = db.parse(this.settingFile);
            document.getDocumentElement().normalize();
        } catch(Exception e) {
            e.printStackTrace();
            log.writeEvent(0, FileLogger.EventType.Error, "FAILED TO OPEN AND PARSE XML FILE");
            return;
        }

        NodeList entities = document.getChildNodes();
        log.writeEvent(6, FileLogger.EventType.Status, "Starting recursive search in XML File...");
        robotEntityGroup = new EntityGroup((Element) entities.item(0), 0, printTreeMap, log);
    }

    public void write() {
        log.writeEvent(0, FileLogger.EventType.Status, "Writing Settings from Element (Recursive)");

        robotEntityGroup.updateElement();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            DOMSource source = new DOMSource(document);

            StreamResult file = new StreamResult(settingFile);
            transformer.transform(source, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EntityGroup getRobot() {
        return robotEntityGroup;
    }
}