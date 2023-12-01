import model.ExtractedData;
import model.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App6Parser {
    public static ExtractedData readColumns(String filePath) {
        Node primaryNode = new Node();
        Map<String, String> mapDescriptionHierarchy = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(";");
                if (columns.length > 0) {
                    String currentHierarchy = columns[0].trim();
                    String name = columns[3].trim();
                    String nameFr = columns[4].trim();
                    if (!currentHierarchy.startsWith("1.X") && !currentHierarchy.startsWith("2.X")) continue;

                    String parentHierarchy = currentHierarchy.contains(".") ? currentHierarchy.substring(0, currentHierarchy.lastIndexOf(".")) : currentHierarchy;
                    String currentSymbolCode = columns[1].trim();
                    String fullPath = columns[2].trim();
                    String currentName = columns[3].trim();
                    String currentNameFR = columns[4].trim();
                    Node currentNode = new Node(currentHierarchy, currentSymbolCode, currentName, currentNameFR);

                    mapDescriptionHierarchy.put(name + " / "+nameFr, currentHierarchy);

                    // Le parent n'est pas trouvé, on rajoute le node au primaryNode s'il est de niveau 1 (1.X.1, 1.X.2...)
                    if (currentHierarchy.length() == 5 && primaryNode.findNodeByHierarchy(parentHierarchy) == null) {
                        primaryNode.addChild(currentNode);
                    }
                    // Sinon on va créer les nodes manquants pour le rattacher au primaryNode
                    else if (primaryNode.findNodeByHierarchy(parentHierarchy) == null) {
                        createMissingNodes(primaryNode, currentNode.getHierarchy(), currentSymbolCode, fullPath);
                    }
                    // Le parent a été trouvé, on lui rattache son enfant
                    else {
                        primaryNode.findNodeByHierarchy(parentHierarchy).addChild(currentNode);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ExtractedData(primaryNode, mapDescriptionHierarchy);
    }

    private static Node createMissingNodes(Node primaryNode, String hierarchy, String symbolCode, String path) {
        String name = path.substring(path.lastIndexOf("|") + 1);
        Node currentNode = new Node(hierarchy, symbolCode, name, name);
        if (hierarchy.length() == 5) {
            primaryNode.addChild(currentNode);
            return currentNode;
        }

        String parentHierarchy = hierarchy.contains(".") ? hierarchy.substring(0, hierarchy.lastIndexOf(".")) : hierarchy;

        if (primaryNode.findNodeByHierarchy(parentHierarchy) == null) {
            Node parentNode = createMissingNodes(primaryNode, parentHierarchy, null, path.substring(0, path.lastIndexOf("|")));
            parentNode.addChild(currentNode);
        } else {
            primaryNode.findNodeByHierarchy(parentHierarchy).addChild(currentNode);
        }
        return currentNode;
    }

}
