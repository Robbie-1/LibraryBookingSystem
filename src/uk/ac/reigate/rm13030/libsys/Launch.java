package uk.ac.reigate.rm13030.libsys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import uk.ac.reigate.rm13030.util.SimpleLogger;

/**
 * @author Robbie McLeod <rm13030@reigate.ac.uk>
 * @date 09/11/15
 */

public class Launch extends SimpleLogger {

	/**
	 * TODO:
	 * 	- Fix log() and sys.out() consistencies
	 */

	private Scanner sc = new Scanner(System.in);
	private String UI;
	private BufferedReader reader;
	private Borrower b;
	private File f;
	public static final String BORROWER_PATH = "data" + File.separator + "borrowers" + File.separator;
	public static final String BOOK_PATH = "data" + File.separator + "books" + File.separator;

	private Launch() {
		System.out.println("Welcome to my Library Booking Program!");
		requestLogin();
		mainMenu(false);
	}

	private void requestLogin() {
		System.out.println("Please enter your name");
		UI = sc.nextLine();
		f = new File(BORROWER_PATH + UI + ".txt"); //name + ".txt");
		if (!f.exists()) {
			//new user
			System.out.println("\nWelcome, " + UI + ", to my Library Booking System program!");
			try {
				f.createNewFile(); //create new user file
			} catch (IOException e) {

			} finally {
				mainMenu(true);
			}
		} else {
			//user file already exists in directory
			//load and print user's currently borrowed books

			b = new Borrower(UI, new ArrayList < Book > ());
			//loadBooksFromFile(name);
			loadBooks(b);
			printBooks(b);
		}
	}

	private void loadBooks(Borrower b) {
		/**
		 * Psuedo:
		 * - get borrower user file
		 * - read book titles in user file
		 * - load book data from "books" directory (use borrower.addBook(book));
		 */
		loadBookData(getBookTitles(b.getName()));
		//loadBookData(getBookTitles(b.getName()));
		//b.addBook(new Book());
	}

	private void loadBookData(ArrayList < String > bookTitles) {
		for (int i = 0; i < bookTitles.size(); i++) {
			/**
			 * For each bookTitle parsed as an argument, use the bookTitle to find the book file and then read all of its data
			 */
			f = new File(BOOK_PATH, bookTitles.get(i) + ".txt");

			if (f.exists()) {
				ArrayList < String > bookData = readBookData(f);
				Book bk = new Book(bookData.get(0), bookData.get(1), bookData.get(2));
				b.addBook(bk);
			} else {
				log(this.getClass(), MessageType.ERROR, "Could not locate book file for name: " + bookTitles.get(i));
				continue;
			}
		}
		log(this.getClass(), MessageType.DEBUG, "Loaded " + bookTitles.size() + " books");
	}

	private ArrayList < String > readBookData(File file) {
		ArrayList < String > bookData = new ArrayList < String > ();

		try {
			String line;
			reader = new BufferedReader(new FileReader(file));

			while ((line = reader.readLine()) != null) {
				bookData.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return bookData;
	}

	private ArrayList < String > getBookTitles(String user) {
		ArrayList < String > bookTitles = new ArrayList < String > ();

		f = new File(BORROWER_PATH + user + ".txt");

		try {
			String line;
			reader = new BufferedReader(new FileReader(f));

			while ((line = reader.readLine()) != null) {
				if (isBook(line)) {
					bookTitles.add(line);
				} else {
					//when book with given title does not exist in books directory
					removeBookFromUserFile(b, line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return bookTitles;
	}

	/*private Book getBookByName(String name) {
    	return Books.valueOf(name).getBook();
    }*/

	private boolean isBook(String title) {
		boolean check = new File(BOOK_PATH, title + ".txt").exists();
		if (check) {
			//valid book
			return true;
		} else {
			return false;
		}
	}

	private void printBooks(Borrower b) {
		if (b.getBooks() == null) {
			System.out.println("You aren't borrowing any books.");
		} else {
			System.out.println("Your borrowed books:\n");
			for (int i = 0; i < b.getBooks().size(); i++) {
				Book bk = b.getBooks().get(i);
				System.out.println("Title: " + bk.getBookTitle() + "; \nAuthor: " + bk.getAuthor() + "; \nGenre: " + bk.getGenre() + ";\n");
			}
			System.out.println("You are currently borrowing a total of " + b.getBooks().size() + " books.\n");
		}
	}

	private void mainMenu(boolean newLine) {
		System.out.println((newLine == true ? "\n" : "") + "Please select an option");
		System.out.println("Add new books (A) : Delete books (D) : Edit books (E)");
		getUI(sc);
	}

	private void getUI(Scanner sc) {
		UI = sc.next();

		if (UI.equalsIgnoreCase("a")) {
			addBooks(sc);
		} else if (UI.equalsIgnoreCase("d")) {
			deleteBooks(sc);
		} else if (UI.equalsIgnoreCase("e")) {
			editBooks(sc);
		} else {
			//System.out.println("I didn't understand your command...");
			log(this.getClass(), MessageType.ERROR, "I didn't understand your command...");
			mainMenu(true);
		}
	}
	
	private void editBooks(Scanner sc) {
		System.out.println("\nPlease enter the name of the book you wish to edit:");
		UI = sc.nextLine();
		Book bk = null;
		for (Book bk1 : b.getBooks()) {
			if (bk1.getBookTitle().equalsIgnoreCase(UI)) {
				bk = bk1;
			} else {
				continue;
			}
		} if (bk != null) {
			System.out.println("What would you like to edit? ([N]=Name/[A]=Author/[G]=Genre)");
			UI = sc.nextLine();
			edit(bk, UI.toLowerCase());
		} else {
			System.out.println("Couldn't find the book with name '"+UI+"'");
		}
	}
	
	private void edit(Book bk, String key) {
		System.out.println("Please enter the new value");
		UI = sc.nextLine();
		switch (key) {
			case "n":
				bk.setBookTitle(UI);
			break;
			
			case "a":
				bk.setAuthor(UI);
			break;
			
			case "g":
				bk.setGenre(UI);
			break;
			
			default:
				System.out.println("Key unrecognized, restarting edit process...");
				editBooks(sc);
			break;
		}
		System.out.println(bk.getBookTitle()+" has been updated!");
	}

	private Book getBookByTitle(String bookTitle) {
		ArrayList < String > data = readBookData(new File(BOOK_PATH, bookTitle + ".txt"));
		Book bk = new Book(data.get(0), data.get(1), data.get(2));
		return bk;
	}

	private void addBooks(Scanner sc) {

		System.out.println("Would you like to add an existing book (press 1), or create a new one? (press 2)");
		int val = sc.nextInt();
		if (val == 1) {
			//print existing books then allow user to pick book via id
			String[] bookNames = new File(BOOK_PATH).list();
			for (int i = 0; i < bookNames.length; i++) {
				if (!b.getBooks().contains(bookNames[i])) {
					System.out.println((i + 1) + ") " + bookNames[i]);
				}
			}
			System.out.println("Please select a book from the list above by entering the book ID");
			int id = sc.nextInt();
			if (isBook(bookNames[id])) {
				Book bk = getBookByTitle(bookNames[id]);
				b.writeToFile(bk);
			}
		} else {

			boolean run = true;
			while (run) {
				Object[] bookInfo = new Object[3];

				String[] questions = {
					"Please enter the name of the book", "Please enter the author of the book", "Please enter the genre of the book"
				};

				boolean run2 = true;

				while (run2) {
					System.out.println(questions[0]);
					bookInfo[0] = sc.next();
					if (isBook(bookInfo[0].toString())) {
						log(this.getClass(), MessageType.ERROR, "Book with the same title already exists in directory!", "restarting process...");
					} else {
						run2 = false;
						System.out.println(questions[1]);
						bookInfo[1] = sc.next();
						System.out.println(questions[2]);
						bookInfo[2] = sc.next();
					}
				}

				Book bk = new Book(bookInfo[0].toString(), bookInfo[1].toString(), bookInfo[2].toString());

				b.addBook(bk);
				b.writeToFile(bk);
				//printDebug(b.getBooks().get(b.getBooks().size() - 1));
				//printDebug(bookArray.get(bookArray.size() - 1));

				System.out.println("Would you like to enter another book? (Y/N)");
				if (sc.next().equalsIgnoreCase("Y")) {
					//loop again
				} else {
					//return to main menu
					mainMenu(true);
					run = false;
				}
			}
		}
	}

	private void deleteBooks(Scanner sc) {
		boolean run = true;
		while (run) {
			System.out.println("[DeleteBook]: Please enter the name of the book");
			System.out.println("You can type 'N' to return to the main menu");
			UI = sc.next();

			if (UI.equalsIgnoreCase("n")) {
				run = false;
				mainMenu(true);
			}

			if (isBook(UI)) {
				f = new File(BOOK_PATH, UI + ".txt"); //the book to be deleted
				f.delete();
				System.out.println("\nBook with title: '" + UI + "' has been deleted successfully.\n");
			} else {
				System.out.println("\nThe book title you have entered does not exist in our records, please try again.\n");
			}

			removeBookFromUserFile(b, UI);

		}
	}

	public void removeBookFromUserFile(Borrower user, String bookTitle) {
		Path path = Paths.get(BORROWER_PATH + user.getName() + ".txt");
		Charset charset = StandardCharsets.UTF_8;

		String content;
		try {
			content = new String(Files.readAllBytes(path), charset);
			content = content.replaceAll(bookTitle, "");
			Files.write(path, content.getBytes(charset));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("\n[debug]: book title has been removed from user file! (title: " + bookTitle + ")\n");

	}

	/*private void printDebug(Book b) {
		System.out.println("\n[DEBUG INFO]:" + "\nBook name: " + b.getBookTitle() + "\nBook author: " + b.getAuthor() + "\nBook genre: " + b.getGenre() + "\n");
		//end of lesson (6-11-15)
	}*/

	public static void main(String[] args) {
		new Launch();
	}

}