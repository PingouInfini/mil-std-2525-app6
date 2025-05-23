package fr.pingouinfini.symbology;

public enum Status
{
    Present("0"),
    Planned("Planned/Anticipated/Suspect","1"),
    PresentFullyCapable("Present/Fully Capable","2"),
    PresentDamaged("Present/Damaged","3"),
    PresentDestroyed("Present/Destroyed","4"),
    PresentFullToCapacity("Present/Full to Capacity","5");
    
    private final String description;
    private final String sidcPart;
    
    Status(String sidcPart){
    	this(null, sidcPart);
    }

    Status(String value, String sidcPart) {
        this.description = value;
        this.sidcPart = sidcPart;
    }

    public String getDescription() {
        return description;
    }
    
    public String getSidcPart() {
    	return sidcPart;
    }
    
    @Override
    public String toString() {
    	if (this.description!=null)
    		return this.getDescription();
    	return super.toString();
    }

    public static Status getEnum(String value) {
        for(Status v : values())
            if(v.getSidcPart().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }
}
