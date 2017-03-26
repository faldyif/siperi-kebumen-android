package id.go.kebumenkab.perizinan.siperikebumen;

/**
 * Created by Faldy on 25/03/2017.
 */

public class Kegiatan {
    private Integer icon;
    private String title;
    private String description;

    public Kegiatan(Integer icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer gambar) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
