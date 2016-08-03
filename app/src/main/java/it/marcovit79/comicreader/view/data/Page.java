package it.marcovit79.comicreader.view.data;

/**
 * Created by mvit on 30-7-16.
 */
public class Page extends Zone {

    private String image;

    public Page() {
        setFromX(0);
        setFromY(0);
    }

    public Page(int width, int height, String image) {
        super(0,0, width, height);
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Page{" +
                "image='" + image + '\'' + " zone: " + super.toString() +
                "}\n";
    }
}
