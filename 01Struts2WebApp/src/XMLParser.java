


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stax.StAXSource;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class XMLParser {

    private int numberOfTestCases;

    public TestCaseDetails testCaseDetails;

    @SuppressWarnings("finally")
    private TestCaseDetails extractXML(String InputTestCaseName, String CurrentPath)
            throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();

        FileInputStream source;
        try {
            source = new FileInputStream(CurrentPath + "\\Test_Input.xml");
            System.out.println(source);
            // source = new FileInputStream(System.getProperty("user.dir") + "\\Test_Input.xml");
            XMLStreamReader reader = factory.createXMLStreamReader(source);

            DOMResult result = new DOMResult();
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.transform(new StAXSource(reader), result);

            Document document = (Document)result.getNode();

            // To Extract Test Case details

            NodeList testCaseNode = document.getElementsByTagName("testsuite");

            numberOfTestCases = testCaseNode.getLength();
            testCaseDetails = new TestCaseDetails();
            int testCaseNo = 0;
            int eachTest = 0;
            TestCaseForLoop : for (; eachTest < numberOfTestCases; eachTest++) {

                // Get the test case Name and enable value
                NamedNodeMap attributes = testCaseNode.item(eachTest).getAttributes();
                int length = attributes.getLength();

                for (int attributesIterator = 0; attributesIterator < length; attributesIterator++) {

                    String attribKey = attributes.item(attributesIterator).getLocalName();
                    String attribvalue = attributes.item(attributesIterator).getNodeValue();

                    if (attribKey.equalsIgnoreCase("testCaseName")) {
                        testCaseDetails.setTestCaseName(attribvalue);

                        if (attribvalue.equalsIgnoreCase(InputTestCaseName)) {
                            break TestCaseForLoop;
                        }

                    }

                }

                /* if (attribKey.equalsIgnoreCase("enable")) {
                     eachTestCaseDetails[eachTest].setEnableValue(attribvalue);
                 }*/

            }
            testCaseNo = eachTest;

            // Get the Child Node values of each test Case
            // Here trying to get actions for these tests in order and put parameters for each
            // of the action

            NodeList childNodesOfTest = testCaseNode.item(testCaseNo).getChildNodes();
            int numberOfChildrenForTestCase = childNodesOfTest.getLength();

            int noOfActions = 0;

            for (int eachChildOfTest = 0; eachChildOfTest < numberOfChildrenForTestCase; eachChildOfTest++) {

                if (childNodesOfTest.item(eachChildOfTest).getLocalName() != null
                        && childNodesOfTest.item(eachChildOfTest).getLocalName()
                                .equalsIgnoreCase("actions")) {
                    NodeList childNodesOfActions = childNodesOfTest.item(eachChildOfTest)
                            .getChildNodes();

                    // Calculating number of actions in each test case
                    for (int eachActionChildNodes = 0; eachActionChildNodes < childNodesOfActions
                            .getLength(); eachActionChildNodes++) {

                        if (childNodesOfActions.item(eachActionChildNodes).getLocalName() != null
                                && childNodesOfActions.item(eachActionChildNodes).getLocalName()
                                        .equalsIgnoreCase("action")) {
                            noOfActions++;
                        }
                    }

                    /*Get each of the actions in Order that it has to be performed 
                     * Order according to Step attribute in the XML 
                     * 
                     */

                    String actionsInOrder[] = new String[noOfActions];
                    @SuppressWarnings("unchecked")
                    Hashtable<String, String> actionInputs[] = new Hashtable[noOfActions];

                    for (int eachActionChildNodes = 0; eachActionChildNodes < childNodesOfActions
                            .getLength(); eachActionChildNodes++) {

                        if (childNodesOfActions.item(eachActionChildNodes).getLocalName() != null
                                && childNodesOfActions.item(eachActionChildNodes).getLocalName()
                                        .equalsIgnoreCase("action")) {

                            NamedNodeMap actionAttributes = childNodesOfActions.item(
                                    eachActionChildNodes).getAttributes();

                            int stepNo = 0;
                            String actionName = "";

                            // Get the actions in Order of Execution
                            for (int actionAttributesIterator = 0; actionAttributesIterator < actionAttributes
                                    .getLength(); actionAttributesIterator++) {

                                String attribKey = actionAttributes.item(actionAttributesIterator)
                                        .getLocalName();
                                String attribvalue = actionAttributes
                                        .item(actionAttributesIterator).getNodeValue();

                                if (attribKey.equalsIgnoreCase("step")) {

                                    stepNo = Integer.parseInt(attribvalue);

                                }
                                if (attribKey.equalsIgnoreCase("actionName")) {

                                    actionName = attribvalue;

                                }

                            }

                            actionsInOrder[stepNo - 1] = actionName;

                            /*
                             * Extracting the input parameters for each action
                             */

                            actionInputs[stepNo - 1] = new Hashtable<String, String>();
                            NodeList inputsToEachAction = childNodesOfActions.item(
                                    eachActionChildNodes).getChildNodes();

                            for (int eachActionInput = 0; eachActionInput < inputsToEachAction
                                    .getLength(); eachActionInput++) {

                                if (inputsToEachAction.item(eachActionInput).getLocalName() != null
                                        && inputsToEachAction.item(eachActionInput).getLocalName()
                                                .equalsIgnoreCase("input")) {

                                    NamedNodeMap attributesInput = inputsToEachAction.item(
                                            eachActionInput).getAttributes();
                                    int attributesInputLength = attributesInput.getLength();

                                    for (int attributesIterator = 0; attributesIterator < attributesInputLength; attributesIterator++) {

                                        String attribKey = attributesInput.item(attributesIterator)
                                                .getLocalName();
                                        String attribvalue = attributesInput.item(
                                                attributesIterator).getNodeValue();

                                        actionInputs[stepNo - 1].put(attribKey, attribvalue);
                                    }
                                }

                            }

                        }

                    }
                    testCaseDetails.setActionNames(actionsInOrder);
                    testCaseDetails.setActionInputs(actionInputs);
                }

            }

        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (Exception e) {
            System.err.println("Something went wrong, this might help :\n" + e.getMessage());
        }
        finally {
            return testCaseDetails;
        }

    }

    public HashMap<String, Object> ReadXMLTestData(String TestCaseID, String CurrentPath) {

        XMLParser parseTestCaseXML = new XMLParser();
        TestCaseDetails testCaseDetails = new TestCaseDetails();
        try {
            testCaseDetails = parseTestCaseXML.extractXML(TestCaseID, CurrentPath);
        }
        catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Running TestCase : " + testCaseDetails.getTestCaseName());
        // System.out.println("TestCase Description: " + testCaseDetails.getDescription());

        String actionName[] = testCaseDetails.getActionNames();
        Hashtable<String, String> actionInputs[] = testCaseDetails.getActionInputs();

        HashMap<String, Object> ReadData = new HashMap<String, Object>();

        for (int actionIputsIterator = 0; actionIputsIterator < actionInputs.length; actionIputsIterator++) {
            for (int eachActionInput = 0; eachActionInput < actionInputs[actionIputsIterator]
                    .size(); eachActionInput++) {

                Set<String> Keys = actionInputs[actionIputsIterator].keySet();
                String keysArray[] = new String[Keys.size()];
                Keys.toArray(keysArray);

                for (int eachKey = 0; eachKey < keysArray.length; eachKey++) {
                    String KeyValue = "$" + actionName[actionIputsIterator] + "_"
                            + keysArray[eachKey];

                    ReadData.put(KeyValue,
                            actionInputs[actionIputsIterator].get(keysArray[eachKey]));

                }

            }
        }
        return ReadData;

    }

}
