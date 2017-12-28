package data;
import java.util.ArrayList;
public class Relation extends Element {
    
    private ArrayList<Element> members;
    
    /**
     * Creates a new relation. A relation consists of "members" of type Element
     * @param id The ID of the relation
     */
    public Relation(String id){
	super(id);
	members = new ArrayList<Element>();
    }
    
    /**
     * Creates a new Relation with the specified ID and containing the List of 
     * members passed. The members are added to the relation in the order the List
     * is iterated over naturally.
     * @param id The ID of the Relation
     * @param members The members of the Relation
     */
    public Relation(String id, ArrayList<Element> members){
	this(id);
	this.members.addAll(members);
    }
    
    /**
     * If the Element is not null, it adds it to the relation.
     * @param element
     */
    public void addMember(Element element){
	members.add(element);
    }
    
    /**
     * Gets the member from the Relation at the specified index
     * @param index Zero based in order of being added.
     * @return The specified member
     */
    public Element getMember(int index){
	return members.get(index);
    }
    
    /**
     * Returns a list of all contained members, in order of
     * being added.
     * @return
     */
    public ArrayList<Element> getMembers(){
	return new ArrayList<Element>(members);
    }
}
