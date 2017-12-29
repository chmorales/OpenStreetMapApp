package data;
import java.util.HashMap;

public abstract class Element {

    private String id;
    private HashMap<String, String> tagMap;

    /**
     * Constructor for class type "Element"
     * @param id The ID of the element
     */
    public Element(String id){
        this.id = id;
        tagMap = new HashMap<String, String>();
    }

    /**
     * Gets the id of the element
     * @return
     */
    public String getID(){
        return id;
    }

    /**
     * Adds the specified pair of strings into the collection
     * of tags. 
     * @param key The key of the tag
     * @param value The value of the tag
     */
    public void addTag(String key, String value){
        tagMap.put(key, value);
    }

    /**
     * Gets the tag associated with this specific key.
     * @param key
     * @return
     */
    public String getTag(String key){
        return tagMap.get(key);
    }
}
