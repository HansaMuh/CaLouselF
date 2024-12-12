module CalouselF {
	opens main;
    opens models;
    opens views;
    opens view_controllers;
    opens singleton;
    opens modules;
    opens views.guest;
    opens views.buyer;
    opens views.seller;

    requires javafx.graphics;
    requires javafx.controls;
    requires java.sql;
}