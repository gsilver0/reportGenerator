package model;

import java.util.List;

public class LineOffsets {
    private int numberOfPages;
    private int firstDetailsPage;
    private int firstDetailsLine;
    private List<Integer> headerAccountOffsetPerPage;
    private List<Integer> accountOffsetPerPage;

    public LineOffsets(int numberOfPages, int firstDetailsPage, int firstDetailsLine, List<Integer> headerAccountOffsetPerPage, List<Integer> accountOffsetPerPage) {
        this.numberOfPages = numberOfPages;
        this.firstDetailsPage = firstDetailsPage;
        this.firstDetailsLine = firstDetailsLine;
        this.headerAccountOffsetPerPage = headerAccountOffsetPerPage;
        this.accountOffsetPerPage = accountOffsetPerPage;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getFirstDetailsPage() {
        return firstDetailsPage;
    }

    public int getFirstDetailsLine() {
        return firstDetailsLine;
    }

    public List<Integer> getHeaderAccountOffsetPerPage() {
        return headerAccountOffsetPerPage;
    }

    public List<Integer> getAccountOffsetPerPage() {
        return accountOffsetPerPage;
    }
}
