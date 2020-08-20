package jovanka_milosevic_a3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class represents UI that a user can use to add course name and a grade, remove them, create file of that list
 * and read the file.
 *
 * @author Jovanka Milosevic
 */
public class Jovanka_Milosevic_A3 extends Application {

    TextField course;
    TextField grade;
    Button btnAdd;
    Button btnRemove;
    Button btnReadFile;
    Button btnCreateFile;
    ListView<Course> lvwDisplay;
    ObservableList<Course> courseList;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * A overridden start method that creates a scene, put it on the stage and load the stage.
     *
     * @param stage - a window of MyCourses app
     */
    @Override
    public void start(Stage stage) {
        Scene scene = createGridPane(stage);

        stage.setTitle("My Courses");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * A method that creates a GridPane, adds nodes to it, and place the pane on the scene.
     *
     * @param stage - a window of MyCourses app
     * @return scene that is created
     */
    private Scene createGridPane(Stage stage) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20));
        pane.setVgap(5);
        pane.setHgap(5);

        Label courseLabel = new Label("Course:");
        pane.add(courseLabel, 0, 0, 1, 1);

        Label gradeLabel = new Label("Grade:");
        pane.add(gradeLabel, 0, 1, 1, 1);

        course = new TextField();
        pane.add(course, 1, 0, 3, 1);

        grade = new TextField();
        pane.add(grade, 1, 1, 3, 1);

        btnAdd = new Button("Add");
        pane.add(btnAdd, 1, 2);
        btnAdd.setOnAction(event -> addCourseInfo());

        btnRemove = new Button("Remove");
        pane.add(btnRemove, 3, 2);
        btnRemove.setOnAction(event -> removeCourseInfo());

        lvwDisplay = new ListView<>();
        pane.add(lvwDisplay, 0, 3, 4, 15);

        btnReadFile = new Button("Read File");
        pane.add(btnReadFile, 0, 19);
        btnReadFile.setOnAction(event -> readFile(stage));

        btnCreateFile = new Button("Create File");
        pane.add(btnCreateFile, 3, 19);
        btnCreateFile.setOnAction(event -> writeFile(stage));

        courseList = FXCollections.observableArrayList();
        lvwDisplay.setItems(courseList);

        return new Scene(pane, 240, 300);
    }

    /**
     * A method that adds the course name and course grade that the user entered to the ObservableList. The result of
     * this action is visible in the ListView (the info about course name and grade will show up in the ListView field).
     */
    public void addCourseInfo() {
        String courseNameString = course.getText();
        String courseGradeString = grade.getText();
        String gradeRegex = "^(100|100\\.0+|[0-9]{1,2}|[0-9]{1,2}\\.[0-9]*)$";
        if ((courseNameString != null) && (courseGradeString != null)) {
            if (!courseNameString.contains(",")) {
                if (courseGradeString.matches(gradeRegex)) {
                    double courseGradeDouble = Double.parseDouble(courseGradeString);
                    Course courseInfo = new Course(courseNameString, courseGradeDouble);
                    courseList.add(courseInfo);
                } else {
                    showAlert("Please, enter a valid course grade (0<=x<=100\n"
                            + "and if a grade is decimal, use a dot, e.g. 75.5)");
                }
            } else {
                showAlert("Please, enter a course name without\n"
                        + "comma (',') in it.");
            }
        } else {
            showAlert("Course name/grade cannot be null.");
        }
    }

    /**
     * A method that removes a course info (a course name and a grade) from the ObservableList. The result of this
     * action is visible in the ListView (the info about course name and grade will disappear from the ListView field).
     */
    public void removeCourseInfo() {
        int selectedIndex = lvwDisplay.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && !courseList.isEmpty()) {
            courseList.remove(selectedIndex);
        }
    }

    /**
     * A method used to write the user's input into the selected file.
     *
     * @param stage - a window of MyCourses app
     */
    public void writeFile(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select File To Save");
        chooser.setInitialDirectory(new File("."));
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File selectedFile = chooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try (PrintWriter writer = new PrintWriter(selectedFile)) {
                courseList.forEach(element -> {
                    writer.println(element.toFile());
                });
            } catch (FileNotFoundException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    /**
     * A method used to read from the selected file.
     *
     * @param stage - a window of the MyCourses app
     */
    public void readFile(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select File To Open");
        chooser.setInitialDirectory(new File("."));
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File selectedFile = chooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                courseList.removeAll();
                lvwDisplay.getItems().clear();
                Scanner input = new Scanner(selectedFile);
                while (input.hasNextLine()) {
                    String lineFromFile = input.nextLine();
                    Course courseInfo = new Course(lineFromFile);
                    courseList.add(courseInfo);
                }
                lvwDisplay.setItems(courseList);
            } catch (FileNotFoundException ex) {
                System.out.println(ex.toString());
            }
            System.out.println(selectedFile.getAbsolutePath());
        } else {
            System.out.println("No file selected!");
        }
    }

    /**
     * A method that creates an alert that will be shown to the user after some of his actions.
     *
     * @param message - a String that represents the message to the user
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error Occurred");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("User clicked OK");
        }
    }

}
