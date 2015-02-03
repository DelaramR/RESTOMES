package MoviePickRESTfulService;

public class Theater{
  private int theaterId;
  private String theaterName;
  private String theaterAddress;
  
  public Theater(){
    this.theaterId = 0;
    this.theaterName = "";
    this.theaterAddress = "";
  }
  
  public Theater(int id, String name, String address){
    this.theaterId = id;
    this.theaterName = name;
    this.theaterAddress = address;
  }
  
  public int getTheaterId(){
    return this.theaterId;
  }
  
  public void setTheaterId(int id){
    this.theaterId = id;
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
