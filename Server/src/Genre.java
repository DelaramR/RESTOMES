package MoviePickRESTfulService;

public class Genre{
  private String name;
  
  public Genre(){
    this.name = "";
  }
  
  public Genre(String name){
    this.name = name;
  }
  
  public String getName(){
    return this.name;
  }
  
  public void setName(String name){
    this.name = name;
  }
}
