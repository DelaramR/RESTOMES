package MoviePickRESTfulService;

public class MovieGenre{
  private Movie movie;
  private Genre genre;
  
  public MovieGenre(){
    this.movie = new Movie();
    this.genre = new Genre();
  }
  
  public MovieGenre(Movie movie, Genre genre){
    this.movie = new Movie(movie.getMovieId(), movie.getMovieTitle(), movie.getRate());
    this.genre = new Genre(genre.getGenreId(), genre.getName());
  }
  
  public Movie getMovie(){
    return this.movie;
  }
  
  public void setMovie(Movie movie){
    this.movie = new Movie(movie.getMovieId(), movie.getMovieTitle(), movie.getRate());
  }
  
  public Genre getGenre(){
    return this.genre;
  }
  
  public void setGenre(Genre genre){
    this.genre = new Genre(genre.getGenreId(), genre.getName());
  }
}
