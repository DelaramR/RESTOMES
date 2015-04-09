package RESTOMES;

public class OntologyClass{
    private String className;
    
    public OntologyClass(){
	this.className = "";
    }

    public OntologyClass(String name){
	this.className = name;
    }

    public String getClassName(){
	return this.className;
    }

    public void setClassName(String name){
	this.className = name;
    }
}
