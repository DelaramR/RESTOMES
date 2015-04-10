package RESTOMES;

public class JsonFile{

	private String name;
	private String content;
	
	public JsonFile(){
		this.name = "";
		this.content = "";
	}
	
	public JsonFile(String name, String content){
		this.name = name;
		this.content = content;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getContent(){
		return this.content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
}
