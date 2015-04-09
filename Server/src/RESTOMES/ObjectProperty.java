package RESTOMES;

public class ObjectProperty{

    private OntologyClass domain;
    private OntologyClass range;
    private String property;

    public ObjectProperty(){
	this.domain = new OntologyClass();
	this.range = new OntologyClass();
	this.property = "";
    }

    public ObjectProperty(OntologyClass domain, OntologyClass range, String property){
	this.domain = domain;
	this.range = range;
	this.property = property;
    }

    public OntologyClass getDomain(){
	return this.domain;
    }

    public void setDomain(OntologyClass domain){
	this.domain = domain;
    }

    public OntologyClass getRange(){
	return this.range;
    }

    public void setRange(OntologyClass range){
	this.range = range;
    }

    public String getProperty(){
	return this.property;
    }

    public void setProperty(String property){
	this.property = property;
    }
}
