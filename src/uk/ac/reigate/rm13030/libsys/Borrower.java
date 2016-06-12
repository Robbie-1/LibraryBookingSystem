package uk.ac.reigate.rm13030.libsys;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Robbie McLeod <rm13030@reigate.ac.uk>
 * @date 09/11/15
 */

public class Borrower {

	private String name;
	private FileWriter fw;
	private File f;
	private ArrayList < Book > books;

	public Borrower(String name, ArrayList < Book > books) {
		this.name = name;
		this.books = books;
	}

	public void addBooks(ArrayList < Book > bookSet) {
		books.addAll(bookSet);
	}

	public void addBook(Book book) {
		books.add(book);
		boolean check = new File(Launch.BOOK_PATH, book.getBookTitle() + ".txt").exists();
		if (!check) {
			writeNewBookFile(book);
		}
		if (!this.getBooks().contains(book)) {
			writeToFile(book);
		}
	}

	private void writeNewBookFile(Book book) {
		//f = new File("data" + File.separator + "books" + File.separator, book.getBookTitle() + ".txt");
		f = new File(Launch.BOOK_PATH, book.getBookTitle() + ".txt");
		if (!f.exists()) {
			try {
				f.createNewFile();
				writeData(book);
				System.out.println("(DEBUG): New book file successfully written to directory!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return;
		}
	}

	private void writeData(Book b) {
		f = new File(Launch.BOOK_PATH, b.getBookTitle() + ".txt");
		if (f.exists()) {
			try {
				FileWriter fw = new FileWriter(f);
				fw.write(b.getBookTitle() + System.lineSeparator());
				fw.write(b.getAuthor() + System.lineSeparator());
				fw.write(b.getGenre().toString());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Error locating book file at path: (" + f.getAbsolutePath() + ")");
			return;
		}
	}

	public void writeToFile(Book bk) {
		f = new File(Launch.BORROWER_PATH, this.getName() + ".txt");
		try {
			fw = new FileWriter(f, true);
			fw.write(bk.getBookTitle() + System.lineSeparator());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("(DEBUG): Book appended to file.\n");
	}

	public String getName() {
		return name;
	}

	public ArrayList < Book > getBooks() {
		return books;
	}

}