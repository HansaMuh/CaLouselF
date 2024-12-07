module Project_JavaFX {
	
	opens main;
    opens models;
    opens views;
    opens view_controllers;
    
    requires javafx.graphics;
    requires javafx.controls;
    requires java.sql;
    
}