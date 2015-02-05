package MoviePickRESTfulService;

import java.util.
public class IntStringArray{
  private Integer integer;
  private ArrayList<String> stringArray;
  
  public IntStringArray(){
    integer = 0;
    stringArray = new ArrayList<String>();
  }
  
  public Integer getInteger(){
    return integer;
  }
  
  public void setInteger(Integer i){
    integer = i;
  }
  
  public ArrayList<String> getStringArray(){
    return stringArray;
  }
  
  public void setStringArray(ArrayList<String> stringArr){
    stringArray = stringArr;
  }
}
