package MoviePickRESTfulService;

public class Theater{
  private String theaterName;
  private String theaterAddress;
  
  public Theater(){
    this.theaterName = "";
    this.theaterAddress = "";
  }
  
  public Theater(String name, String address){
    this.theaterName = name;
    this.theaterAddress = address;
  }
  
  public String getTheaterName(){
    return this.theaterName;
  }
  
  public void setTheaterName(String name){
    this.theaterName = name;
  }
  
  public String getTheaterAddress(){
    return this.theaterAddress;
  }
  
  public void setTheaterAddress(String address){
    this.theaterAddress = address;
  }
}
