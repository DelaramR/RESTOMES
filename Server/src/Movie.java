package MoviePickRESTfulService;

public class Movie{
  private int movieId;
  private String movieTitle;
  private int rate;
  private String genre;
  
  public Movie(){
    this.movieId = "";
    this.movieTitle = "";
    this.rate = 0;
    this.genre = "";
  }
  
  public Movie(String movieId, String movieTitle, int rate, String genre){
    this.movieId = movieId;
    this.movieTitle = movieTitle;
    this.rate = rate;
    this.genre = "";
  }
  
  public String getMovieId(){
    return this.movieId;
  }
  
  public void setMovieId(String id){
    this.movieId = id;
  }
  
  public String getMovieTitle(){
    return movieTitle;
  }
  
  public void setMovieTitle(String title){
    this.movieTitle = title;
  }
  
  public int getRate(){
    return rate;
  }
  
  public void setRate(int rate){
    this.rate = rate;
  }
  
  public String getGenre(){
    return this.genre;
  }
  
  public void setGenre(String genre){
    this.genre = genre;
  }
}
