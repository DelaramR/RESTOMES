package MoviePickRESTfulService;

public class Movie{
  private String movieTitle;
  private String genre;
  private int rate;
  
  public Movie(){
    this.movieTitle = "";
    this.rate = 0;
    this.genre = "";
  }
  
  public Movie(String movieTitle, String genre, int rate){
    this.movieTitle = movieTitle;
    this.rate = rate;
    this.genre = genre;
  }
  
  public String getMovieTitle(){
    return this.movieTitle;
  }
  
  public void setMovieTitle(String title){
    this.movieTitle = title;
  }
  
  public int getRate(){
    return this.rate;
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
  
  public boolean equals(Movie movie){
    if(this.movieTitle.compareTo(movie.getMovieTitle()) == 0 && this.genre.compareTo(movie.getGenre()) == 0)
      return true;
    return false;
  }
}
