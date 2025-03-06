package fr.pingouinfini.symbology;

public enum Reinforcer {
    NotApplicable("Not Applicable"),
    Reinforced("Reinforced"),
    Reduced("Reduced"),
    ReinforcedAndReduced("Reinforced And Reduced");

    private final String description;

    Reinforcer(String value){
        this.description = value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        if (this.description!=null)
            return this.getDescription();
        return super.toString();
    }

}
