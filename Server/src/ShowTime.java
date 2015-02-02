package MoviePickRESTfulService;

public class ShowTime{
  private Movie movie;
  private Theater theater;
  private String date;
  private String time;
  
  public ShowTime(){
    this.movie = new Movie();
    this.theater = new Theater();
    this.date = "";
    this.time = "";
  }
  
  public ShowTime(Movie movie, Theater theater, String date, String time){
    this.movie = new Movie(movie.getMovieId(), movie.getMovieTitle(), movie.getRate());
    this.theater = new Theater(theater.getTheaterId(), theater.getTheaterName(), theater.getTheaterAddress());
    this.date = date;
    this.time = time;
  }
  
  public Movie getMovie(){
    return this.movie;
  }
  
  public void setMovie(Movie movie){
    this.movie = new Movie(movie.getMovieId(), movie.getMovieTitle(), movie.getRate());
  }
  
  public Theater getTheater(){
    return this.theater;
  }
  
  public void setTheater(Theater theater){
    this.theater = new Theater(theater.getTheaterId(), theater.getTheaterName(), theater.getTheaterAddress());
  }
  
  public String getDate(){
    return this.date;
  }
  
  public void setDate(String date){
    this.date = date;
  }
  
  public String getTime(){
    return this.time;
  }
  
  public void setTime(String time){
    this.time = time;
  }
}
