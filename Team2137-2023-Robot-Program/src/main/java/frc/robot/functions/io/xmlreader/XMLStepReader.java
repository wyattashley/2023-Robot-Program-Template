package frc.robot.functions.io.xmlreader;

import edu.wpi.first.math.Pair;
import frc.robot.functions.io.FileLogger;
import frc.robot.functions.io.xmlreader.data.Step;
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
import java.util.*;

public class XMLStepReader {
    private final File stepFile;
    private final FileLogger logger;
    private final List<Step> steps = new ArrayList<>();

    public XMLStepReader(String dir, FileLogger _logger) {
        this.stepFile = new File(dir);
        this.logger = _logger;

        if(!this.stepFile.exists()) {
            logger.writeEvent(0, FileLogger.EventType.Warning, "XML File Specified Does Not Exist, Attempting to Create a Basic One");
            try {
                this.stepFile.createNewFile();

                FileWriter tmpFileWriter = new FileWriter(this.stepFile);
                tmpFileWriter.write("<?xml version=\"1.0\"?>\n<Steps>\n\t<Step>\n\t</Step>\n</Steps>");
                tmpFileWriter.flush();
                tmpFileWriter.close();
            } catch (IOException e) {
                logger.writeEvent(0, FileLogger.EventType.Error, "Failed to create basic xml file because...");
                logger.writeEvent(0, FileLogger.EventType.Error, e.getMessage());
            }
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document doc = null;

        try { 
            DocumentBuilder db = dbf.newDocumentBuilder();  
            doc = db.parse(this.stepFile);  
            doc.getDocumentElement().normalize();  
        } catch(Exception e) {
            e.printStackTrace();
        }

        NodeList tmpStep = doc.getElementsByTagName("Step");

        for (int i = 0; i < tmpStep.getLength(); i++) {
            this.steps.add(parseSteps(tmpStep.item(i)));
        }
    }

    public List<Step> getSteps() {
        return this.steps;
    }

    public void addStep(Step a) {
        this.steps.add(a);
    }

    public Step parseSteps(Node stepNode) {
        Element element = (Element) stepNode;
//        NodeList parmNodeList = element.getElementsByTagName("parm");
//        String[] tmpParms = new String[parmNodeList.getLength()];
//
//        for (int i = 0; i < parmNodeList.getLength(); i++) {
//            Element id = (Element) parmNodeList.item(0);
//            tmpParms[Integer.parseInt(id.getAttribute("id"))] = parmNodeList.item(i).getTextContent();
//        }

        Step returner = new Step();

        for (Step.StepValues a : Step.StepValues.values()) {
            NodeList node = element.getElementsByTagName(a.toString().toLowerCase());
            if(node.getLength() > 0)
                returner.setValue(new Pair<>(a, node.item(0).getTextContent()));
        }
        return returner;
    }

    public void writeSteps() {
        logger.writeEvent(0, FileLogger.EventType.Status, "Writing Steps from List");

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root elements
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("company");
            doc.appendChild(rootElement);

            for(Step a : getSteps()) {
                rootElement.appendChild(a.getXMLFileElement(doc));
            }

            //write the content into xml file
            TransformerFactory transformerFactory =  TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result =  new StreamResult(stepFile);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}