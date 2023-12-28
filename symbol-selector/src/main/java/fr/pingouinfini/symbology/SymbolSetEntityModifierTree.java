package fr.pingouinfini.symbology;

import java.util.*;

public class SymbolSetEntityModifierTree {
    private Set<Entity> entities;
    private final SymbolSets set;
    private List<Modifier> sectorModifierOnes;
    private List<Modifier> sectorModifierTwos;


    public SymbolSetEntityModifierTree(SymbolSets set) {
        entities = new HashSet<>();
        sectorModifierOnes = new ArrayList<>();
        sectorModifierTwos = new ArrayList<>();
        this.set = set;
    }

    public List<Modifier> getSectorModifierOnes() {
        return sectorModifierOnes;
    }

    public void setSectorModifierOnes(List<Modifier> modifierOnes) {
        this.sectorModifierOnes = modifierOnes;
    }

    public List<Modifier> getSectorModifierTwos() {
        return sectorModifierTwos;
    }

    public void setSectorModifierTwos(List<Modifier> modifierTwos) {
        this.sectorModifierTwos = modifierTwos;
    }

    public SymbolSets getSet() {
        return set;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public Set<Entity> getEntitiesFromId(String identifier) {
        Set<Entity> filtredEntities = new HashSet<>();
        for(Entity entity : entities){
            if(entity.getIdentifier().startsWith(identifier))
                filtredEntities.add(entity);
        }
        return filtredEntities;
    }

    public List<EntityType> getEntityTypesFromEntityId(String entityId) {
        List<EntityType> filtredEntityTypes = new ArrayList<>();
        for(Entity entity : entities){
            if(entity.getIdentifier().substring(0,2).equals(entityId))
                filtredEntityTypes.addAll(entity.getTypes());
        }
        return filtredEntityTypes;
    }

    public List<EntitySubType> getEntitySubTypesFromEntityAndEntityTypeId(String entityId, String entityTypeId) {
        List<EntitySubType> filtredEntitySubType = new ArrayList<>();
        for(Entity entity : entities){
            if(entity.getIdentifier().startsWith(entityId)) {
                for(EntityType entityType : entity.getTypes()) {
                    if(entityType.getIdentifier().substring(2,4).equals(entityTypeId)) {
                        filtredEntitySubType.addAll(entityType.getSubTypes());
                    }
                }
            }
        }
        return filtredEntitySubType;
    }

    public void setEntities(Set<Entity> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("->").append(set.getDescription()).append("\n");
        builder.append("# Entities -> Types -> Subtypes: \n");
        for (Entity e : entities) {
            builder.append(e.toString());
        }

        builder.append("\n# Modifier One: \n");
        for (Modifier e : sectorModifierOnes) {
            builder.append(e.toString());
        }

        builder.append("# Modifier Two: \n");
        for (Modifier e : sectorModifierTwos) {
            builder.append(e.toString());
        }
        return builder.toString();
    }
}
