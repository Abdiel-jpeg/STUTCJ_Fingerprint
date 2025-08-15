package jsonTemplates;

public class AsistenciaJsonTemplate {
    private String option;
    private String encodedImage;
    private int idEvento;
    private int nreloj;

    public String getOption() {
        return option;
    }
    public String getEncodedImage() {
        return encodedImage;
    }
    public int getIdEvento() {
        return idEvento;
    }
    public int getNreloj() {
        return nreloj;
    }

    @Override
    public String toString() {
        return "AsistenciaJsonTemplate [option=" + option + ", encodedImage=" + encodedImage + ", idEvento=" + idEvento
                + ", nreloj=" + nreloj + "]";
    }
}
