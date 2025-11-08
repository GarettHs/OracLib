package OracLib;

public class BookProfiles {
    private int size = 0;
    private Books[] book;

    public BookProfiles() {
        book = new Books[0];
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        book = new Books[size];
        this.size = size;
    }

    public Books getBook(int index) {
        return book[index];
    }

    public void setBook(Books book, int index) {
        this.book[index] = book;
    }

    public int searchBook(String bookCode) {
        int index = -1;
        for(int i=0; i<book.length; i++) {
            if(bookCode.equals(book[i].getBookCode())) {
                index = i;
            }
        }
        return index;
    }
}

