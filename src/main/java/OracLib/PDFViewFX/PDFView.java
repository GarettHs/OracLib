package OracLib.PDFViewFX;

import OracLib.PDFViewFX.skins.PDFViewSkin;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.awt.print.Pageable;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class PDFView extends Control {

    public PDFView() {
        super();

        getStyleClass().add("pdf-view");
        setFocusTraversable(false);

        zoomFactorProperty().addListener(it -> {
            if (getZoomFactor() < 1) {
                throw new IllegalArgumentException("zoom factor can not be smaller than 1");
            } else if (getZoomFactor() > getMaxZoomFactor()) {
                throw new IllegalArgumentException("zoom factor can not be larger than max zoom factor, but " + getZoomFactor() + " > " + getMaxZoomFactor());
            }
        });

        showAllProperty().addListener(it -> {
            if (isShowAll()) {
                setZoomFactor(1);
            }
        });

        selectedSearchResultProperty().addListener(it -> {
            final SearchResult result = getSelectedSearchResult();
            if (result != null) {
                setPage(result.getPageNumber());
            }
        });

        documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (oldDoc != null) {
                oldDoc.close();
            }

            setSearchText(null);
        });

        getStylesheets().add(getUserAgentStylesheet());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new PDFViewSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return "OracLib/PDFViewFX/pdf-view.css";
    }

    private final BooleanProperty showThumbnails = new SimpleBooleanProperty(this, "showThumbnails", true);

    public final boolean isShowThumbnails() {
        return showThumbnails.get();
    }

    public final BooleanProperty showThumbnailsProperty() {
        return showThumbnails;
    }

    public final void setShowThumbnails(boolean showThumbnails) {
        this.showThumbnails.set(showThumbnails);
    }

    private final BooleanProperty showToolBar = new SimpleBooleanProperty(this, "showToolBar", true);

    public final boolean isShowToolBar() {
        return showToolBar.get();
    }

    public final BooleanProperty showToolBarProperty() {
        return showToolBar;
    }

    public final void setShowToolBar(boolean showToolBar) {
        this.showToolBar.set(showToolBar);
    }

    private final BooleanProperty showSearchResults = new SimpleBooleanProperty(this, "showSearchResults", true);

    public final boolean isShowSearchResults() {
        return showSearchResults.get();
    }

    public final BooleanProperty showSearchResultsProperty() {
        return showSearchResults;
    }

    public final void setShowSearchResults(boolean showSearchResults) {
        this.showSearchResults.set(showSearchResults);
    }

    private final BooleanProperty cacheThumbnails = new SimpleBooleanProperty(this, "cacheThumbnails", true);

    public final boolean isCacheThumbnails() {
        return cacheThumbnails.get();
    }

    public final BooleanProperty cacheThumbnailsProperty() {
        return cacheThumbnails;
    }

    public final void setCacheThumbnails(boolean cacheThumbnails) {
        this.cacheThumbnails.set(cacheThumbnails);
    }

    private final DoubleProperty maxZoomFactor = new SimpleDoubleProperty(this, "maxZoomFactor", 4);

    public final double getMaxZoomFactor() {
        return maxZoomFactor.get();
    }

    public final DoubleProperty maxZoomFactorProperty() {
        return maxZoomFactor;
    }

    public final void setMaxZoomFactor(double maxZoomFactor) {
        this.maxZoomFactor.set(maxZoomFactor);
    }

    private final DoubleProperty zoomFactor = new SimpleDoubleProperty(this, "zoomFactor", 1);

    public final double getZoomFactor() {
        return zoomFactor.get();
    }

    public final DoubleProperty zoomFactorProperty() {
        return zoomFactor;
    }

    public final void setZoomFactor(double zoomFactor) {
        this.zoomFactor.set(zoomFactor);
    }

    private final DoubleProperty pageRotation = new SimpleDoubleProperty(this, "pageRotation", 0) {
        @Override
        public void set(double newValue) {
            super.set(newValue % 360d);
        }
    };

    public final double getPageRotation() {
        return pageRotation.get();
    }

    public final DoubleProperty pageRotationProperty() {
        return pageRotation;
    }

    public final void setPageRotation(double pageRotation) {
        this.pageRotation.set(pageRotation);
    }

    public final void rotateLeft() {
        setPageRotation(getPageRotation() - 90);
    }

    public final void rotateRight() {
        setPageRotation(getPageRotation() + 90);
    }

    private final IntegerProperty page = new SimpleIntegerProperty(this, "page");

    public final int getPage() {
        return page.get();
    }

    public final IntegerProperty pageProperty() {
        return page;
    }

    public final void setPage(int page) {
        this.page.set(page);
    }

    public final boolean gotoNextPage() {
        int currentPage = getPage();
        setPage(Math.min(getDocument().getNumberOfPages() - 1, getPage() + 1));
        return currentPage != getPage();
    }

    public final boolean gotoPreviousPage() {
        int currentPage = getPage();
        setPage(Math.max(0, getPage() - 1));
        return currentPage != getPage();
    }

    public final boolean gotoLastPage() {
        int currentPage = getPage();
        setPage(getDocument().getNumberOfPages() - 1);
        return currentPage != getPage();
    }

    private final BooleanProperty showAll = new SimpleBooleanProperty(this, "showAll", false);

    public final boolean isShowAll() {
        return showAll.get();
    }

    public final BooleanProperty showAllProperty() {
        return showAll;
    }

    public final void setShowAll(boolean showAll) {
        this.showAll.set(showAll);
    }

    private final FloatProperty thumbnailPageScale = new SimpleFloatProperty(this, "thumbnailScale", 1f);

    public final float getThumbnailPageScale() {
        return thumbnailPageScale.get();
    }

    public final FloatProperty thumbnailPageScaleProperty() {
        return thumbnailPageScale;
    }

    public final void setThumbnailPageScale(float thumbnailPageScale) {
        this.thumbnailPageScale.set(thumbnailPageScale);
    }

    private final FloatProperty pageScale = new SimpleFloatProperty(this, "pageScale", 4f);

    public final float getPageScale() {
        return pageScale.get();
    }

    public final FloatProperty pageScaleProperty() {
        return pageScale;
    }

    public final void setPageScale(float pageScale) {
        this.pageScale.set(pageScale);
    }

    private final DoubleProperty thumbnailSize = new SimpleDoubleProperty(this, "thumbnailSize", 200d);

    public final double getThumbnailSize() {
        return thumbnailSize.get();
    }

    public final DoubleProperty thumbnailSizeProperty() {
        return thumbnailSize;
    }

    public final void setThumbnailSize(double thumbnailSize) {
        this.thumbnailSize.set(thumbnailSize);
    }

    private final ObjectProperty<Document> document = new SimpleObjectProperty<>(this, "document");

    public final ObjectProperty<Document> documentProperty() {
        return document;
    }

    public final Document getDocument() {
        return document.get();
    }

    public final void setDocument(Document document) {
        this.document.set(document);
    }

    private final StringProperty searchText = new SimpleStringProperty(this, "searchText");

    public final String getSearchText() {
        return searchText.get();
    }

    public final StringProperty searchTextProperty() {
        return searchText;
    }

    public final void setSearchText(String searchText) {
        this.searchText.set(searchText);
    }

    private final ListProperty<SearchResult> searchResults = new SimpleListProperty<>(this, "searchResults", FXCollections.observableArrayList());

    public final ListProperty<SearchResult> searchResultsProperty() {
        return searchResults;
    }

    public final ObservableList<SearchResult> getSearchResults() {
        return searchResults.get();
    }

    public final void setSearchResults(ObservableList<SearchResult> searchResults) {
        this.searchResults.set(searchResults);
    }

    private final ObjectProperty<SearchResult> selectedSearchResult = new SimpleObjectProperty<>(this, "selectedSearchResult");

    public final ObjectProperty<SearchResult> selectedSearchResultProperty() {
        return selectedSearchResult;
    }

    public final SearchResult getSelectedSearchResult() {
        return selectedSearchResult.get();
    }

    public final void setSelectedSearchResult(SearchResult selectedSearchResult) {
        this.selectedSearchResult.set(selectedSearchResult);
    }

    private final ObjectProperty<Color> searchResultColor = new SimpleObjectProperty<>(this, "searchResultColor", Color.RED);

    public final ObjectProperty<Color> searchResultColorProperty() {
        return searchResultColor;
    }

    public final Color getSearchResultColor() {
        return searchResultColor.get();
    }

    public final void setSearchResultColor(Color searchResultColor) {
        this.searchResultColor.set(searchResultColor);
    }

    public final void load(File file) {
        Objects.requireNonNull(file, "file can not be null");
        load(() -> new PDFBoxDocument(file));
    }

    public final void load(InputStream stream) {
        Objects.requireNonNull(stream, "stream can not be null");
        load(() -> new PDFBoxDocument(stream));
    }

    public final void load(Supplier<Document> supplier) {
        Objects.requireNonNull(supplier, "supplier can not be null");
        setDocument(supplier.get());
    }

    public final void unload() {
        setDocument(null);
        setSearchText(null);
        setZoomFactor(1);
        setRotate(0);
    }

    public interface Document {

        BufferedImage renderPage(int pageNumber, float scale);

        int getNumberOfPages();

        boolean isLandscape(int pageNumber);

        Pageable getPageable();

        void close();

        class DocumentProcessingException extends RuntimeException {
            public DocumentProcessingException(Throwable cause) {
                super(cause);
            }
        }
    }

    public interface SearchableDocument extends Document {

        List<SearchResult> getSearchResults(String searchText);
    }

    public static class SearchResult implements Comparable<SearchResult> {

        private final String searchText;
        private final String textSnippet;
        private final int pageNumber;
        private final Rectangle2D marker;

        public SearchResult(String searchText, String textSnippet, int pageNumber, Rectangle2D marker) {
            this.searchText = searchText;
            this.textSnippet = textSnippet;
            this.pageNumber = pageNumber;
            this.marker = marker;
        }

        public Rectangle2D getMarker() {
            return marker;
        }

        public Rectangle2D getScaledMarker(double scale) {
            return new Rectangle2D(marker.getMinX() * scale, marker.getMinY() * scale, marker.getWidth() * scale, marker.getHeight() * scale);
        }

        public String getSearchText() {
            return searchText;
        }

        public String getTextSnippet() {
            return textSnippet;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        @Override
        public int compareTo(SearchResult other) {
            int result = Integer.compare(pageNumber, other.pageNumber);

            if (result == 0) {
                result = Double.compare(getMarker().getMinY(), other.getMarker().getMinY());
            }

            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            SearchResult that = (SearchResult) o;
            return pageNumber == that.pageNumber &&
                    Objects.equals(searchText, that.searchText) &&
                    Objects.equals(textSnippet, that.textSnippet) &&
                    Objects.equals(marker, that.marker);
        }

        @Override
        public int hashCode() {
            return Objects.hash(searchText, textSnippet, pageNumber, marker);
        }
    }
}
