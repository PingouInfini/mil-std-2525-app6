package fr.pingouinfini.symbology;

public enum HQTFDummy implements Amplifier
{
    NotApplicable("Not Applicable","0"),
    FeintDummy("Feint/Dummy","1"),
    Headquarters("2"),
    FeintDummyHeadquarters("Feint/ Dummy Headquarters","3"),
    TaskForce("Task Force","4"),
    FeintDummyTaskforce("Feint/Dummy Task Force","5"),
    TaskForceHeadquarters("Task Force Headquarters","6"),
    FeintDummyTaskforceHeadquarters("Feint/ Dummy Task Force Headquarters","7");
	
	private final String description;
    private final String sidcPart;
    
    HQTFDummy(String sidcPart){
    	this(null, sidcPart);
    }

    HQTFDummy(String value, String sidcPart) {
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

    public static HQTFDummy getEnum(String value) {
        for(HQTFDummy v : values())
            if(v.getSidcPart().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }
}
