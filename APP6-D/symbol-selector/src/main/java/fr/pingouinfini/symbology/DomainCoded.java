package fr.pingouinfini.symbology;

import java.util.Objects;

public abstract class DomainCoded implements Comparable<DomainCoded>{
    private String name;
    private String identifier;

    public DomainCoded(String name, String identifier){
        this.setName(name);
        this.setIdentifier(identifier);
    }

    public int compareTo(DomainCoded obj){
        return (this.identifier.equalsIgnoreCase(obj.getIdentifier())?1:0);
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public boolean equals(Object o) {
 
		// null check
		if (o == null) {
			return false;
		}
 
		// this instance check
		if (this == o) {
			return true;
		}
 
		// instanceof Check and actual value check
        return (o instanceof DomainCoded) && (Objects.equals(((DomainCoded) o).identifier, this.identifier));
	}
 
	@Override
	public int hashCode() {
		int result = 0;
		int id = Integer.parseInt(identifier);
		result = (int) (id * 10);
		return result;
	}
}