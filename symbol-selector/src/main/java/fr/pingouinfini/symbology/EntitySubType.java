package fr.pingouinfini.symbology;

public class EntitySubType extends DomainCoded{
	
    public EntitySubType(String name, String identifier){
    	super(name, identifier);
    }
    
    @Override
    public String toString() {
    	return "-------->"+getName()+" - "+getIdentifier()+"\n";
    }
}
