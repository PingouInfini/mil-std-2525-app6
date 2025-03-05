package fr.pingouinfini.symbology;

public enum StandardEntityOnes implements Amplifier
{
    Reality("0"),
    Exercise("1"),
    Simulation("2");
	

    private final String description;
    private final String sidcPart;
    
    StandardEntityOnes(String sidcPart){
    	this(null, sidcPart);
    }

    StandardEntityOnes(String value, String sidcPart) {
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

    public static StandardEntityOnes getEnum(String value) {
        for(StandardEntityOnes v : values())
            if(v.getSidcPart().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }
}