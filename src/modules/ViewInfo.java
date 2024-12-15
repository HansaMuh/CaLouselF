package modules;

/*
    ViewInfo class is used to store details about a view in the application, such as:
    - name: the display name of the view
    - width: the actual width of the view
    - height: the actual height of the view
    - isBottomLevel: a boolean value indicating whether the view is a bottom-level view or not
    (in other words, indicating whether it needs to have a Back button for navigation)
 */
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
