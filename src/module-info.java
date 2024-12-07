module CalouselF {
	opens main;
    opens models;
    opens views;
    opens view_controllers;
    opens singleton;
    opens modules;

    requires javafx.graphics;
    requires javafx.controls;
    requires java.sql;
}