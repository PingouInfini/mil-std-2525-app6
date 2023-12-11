import model.ExtractedData;
import model.NodeAPP6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App6Parser
{
    private static final Logger logger = Logger.getLogger("component.app6Parser");

    public static ExtractedData readColumns(String filePath)
    {
        final NodeAPP6 primaryNode = new NodeAPP6();
        final Map<String, String> mapDescriptionHierarchy = new TreeMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                final String[] columns = line.split(";");

                if (columns.length > 0)
                {
                    final String currentHierarchy = columns[0].trim();
                    final String name = columns[3].trim();
                    final String nameFr = columns[4].trim();
                    if (!currentHierarchy.startsWith("1.X") && !currentHierarchy.startsWith("2.X"))
                        continue;

                    final String parentHierarchy = currentHierarchy.contains(".")
                            ? currentHierarchy.substring(0, currentHierarchy.lastIndexOf(".")) : currentHierarchy;
                    final String currentSymbolCode = columns[1].trim();
                    final String currentFullPath = columns[2].trim();
                    final String currentName = columns[3].trim();
                    final String currentNameFR = columns[4].trim();
                    final NodeAPP6 currentNode = new NodeAPP6(currentHierarchy, currentSymbolCode, currentName, currentNameFR,
                            currentFullPath);

                    mapDescriptionHierarchy.put(name + " / " + nameFr, currentHierarchy);

                    // Le parent n'est pas trouvé, on rajoute le NodeAPP6 au primaryNodeAPP6 s'il est de niveau 1 (1.X.1, 1.X.2...)
                    if (currentHierarchy.length() == 5 && primaryNode.findNodeByHierarchy(parentHierarchy) == null)
                    {
                        primaryNode.addChild(currentNode);
                    }
                    // Sinon on va créer les NodeAPP6s manquants pour le rattacher au primaryNodeAPP6
                    else if (primaryNode.findNodeByHierarchy(parentHierarchy) == null)
                    {
                        createMissingNodeAPP6s(primaryNode, currentNode.getHierarchy(), currentSymbolCode, currentFullPath);
                    }
                    // Le parent a été trouvé, on lui rattache son enfant
                    else
                    {
                        primaryNode.findNodeByHierarchy(parentHierarchy).addChild(currentNode);
                    }
                }
            }
        }
        catch (final IOException e)
        {
            logger.log(Level.SEVERE, "erreur lors de la recuperation des noeuds de l'app6 ", e);
        }
        return new ExtractedData(primaryNode, mapDescriptionHierarchy);
    }

    private static NodeAPP6 createMissingNodeAPP6s(NodeAPP6 primaryNodeAPP6, String hierarchy, String symbolCode, String path)
    {
        final String name = path.substring(path.lastIndexOf("|") + 1);
        final NodeAPP6 currentNodeAPP6 = new NodeAPP6(hierarchy, symbolCode, name, name, path);
        if (hierarchy.length() == 5)
        {
            primaryNodeAPP6.addChild(currentNodeAPP6);
            return currentNodeAPP6;
        }

        final String parentHierarchy = hierarchy.contains(".") ? hierarchy.substring(0, hierarchy.lastIndexOf(".")) : hierarchy;

        if (primaryNodeAPP6.findNodeByHierarchy(parentHierarchy) == null)
        {
            final NodeAPP6 parentNodeAPP6 = createMissingNodeAPP6s(primaryNodeAPP6, parentHierarchy, null,
                    path.substring(0, path.lastIndexOf("|")));
            parentNodeAPP6.addChild(currentNodeAPP6);
        }
        else
        {
            primaryNodeAPP6.findNodeByHierarchy(parentHierarchy).addChild(currentNodeAPP6);
        }
        return currentNodeAPP6;
    }

}
