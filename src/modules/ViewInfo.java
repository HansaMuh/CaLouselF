package modules;

public class ViewInfo {

    // Constructor

    public ViewInfo(String name, double width, double height, boolean isBottomLevel) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.isBottomLevel = isBottomLevel;
    }

    // Properties

    private String name;
    private double width;
    private double height;
    private boolean isBottomLevel;

    // Getters

    public String getName() {
        return name;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isBottomLevel() {
        return isBottomLevel;
    }

}
