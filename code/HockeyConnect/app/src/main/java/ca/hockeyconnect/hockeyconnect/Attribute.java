package ca.hockeyconnect.hockeyconnect;

import java.io.Serializable;

public class Attribute implements Serializable {
    private String attributeName;
    private String attributeDescription;
    private int ID;

    public Attribute(String attributeName, String attributeDescription, int ID) {
        this.attributeName = attributeName;
        this.attributeDescription = attributeDescription;
        this.ID = ID;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getAttributeDescription() {
        return attributeDescription;
    }

    public int getID() {
        return ID;
    }

    /*@Override
    public String toString() {
        return getFullName();
    }*/
}
