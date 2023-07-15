import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class App6Parser {
    public static Node readFirstColumn(String filePath) {
        Node primaryNode = new Node();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(";");
                if (columns.length > 0) {
                    String currentHierarchy = columns[0].trim();
                    if(!currentHierarchy.startsWith("1.X"))
                        continue;

                    String parentHierarchy =
                        currentHierarchy.contains(".") ? currentHierarchy.substring(0, currentHierarchy.lastIndexOf(".")) : currentHierarchy;
                    String currentSymbolCode = columns[1].trim();
                    String currentName = columns[3].trim();
                    Node currentNode = new Node(currentHierarchy, currentSymbolCode, currentName, currentName);

                    // Le parent n'est pas trouvé, on rajoute le node au primaryNode
                    if (primaryNode.findNodeByHierarchy(parentHierarchy) == null) {
                        primaryNode.addChild(currentNode);
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

        return primaryNode;
    }
}
