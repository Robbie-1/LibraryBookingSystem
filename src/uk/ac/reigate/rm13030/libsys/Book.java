package uk.ac.reigate.rm13030.libsys;

/**
 * @author Robbie McLeod <rm13030@reigate.ac.uk>
 * @date 09/11/15
 */

public class Book {

	private String bookTitle, author;
	private Genre genre;

	/**
	 * Book encoding:
	 * file name = book title
	 * Line 1: Book Title [String]
	 * Line 2: Author [String]
	 * Line 3: Genre [String]
	 */

	public Book(String bookTitle, String author, String genre) {
		this.bookTitle = bookTitle;
		this.author = author;
		this.genre = decodeGenre(genre);
	}

	private Genre decodeGenre(String genre) {
		Genre g;
		try {
			g = Genre.valueOf(genre.toUpperCase());
		} catch (Exception e) {
			g = Genre.HORROR;
			System.out.println("\n(DEBUG): Error decoding genre! Setting as default (Horror)");
		}
		return g;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public String getTitle() {
		return bookTitle;
	}

	public String getAuthor() {
		return author;
	}

	public Genre getGenre() {
		return genre;
	}

	/*public static enum Books {
    	
    	SomeBookTitle(new Book("SomeBookTitle", "SomeBookAuthor", "Horror")),
    	AnotherBookTitle(new Book("AnotherBookTitle", "AnotherBookAuthor", "Thriller"));
    	
    	private Book b;
    	
    	Books(Book b) {
    		this.b = b;
    	}

		public Book getBook() {
			return b;
		}
    }*/

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setGenre(String genre) {
		this.genre = decodeGenre(genre);
	}

	public enum Genre {
		HORROR,
		THRILLER,
		ROMANCE,
	}

}