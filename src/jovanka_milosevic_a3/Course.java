package jovanka_milosevic_a3;

/**
 * This class represents a Course
 *
 * @author Jovanka Milosevic
 */
public class Course {

    private String courseName;
    private double courseGrade;

    /**
     * A constructor that creates a Course using course name and course grade as parameters
     *
     * @param courseName - name of the course
     * @param courseGrade - course grade
     */
    public Course(String courseName, double courseGrade) {
        this.courseName = courseName;
        this.courseGrade = courseGrade;
    }

    /**
     * A constructor that takes in a line from a file as parameter, splits the input using comma delimiter and stores it
     * in a string array, where the course name is the first and the course grade is the second.
     *
     * @param lineFromFile - a line from the selected file
     */
    public Course(String lineFromFile) {
        String[] sentenceParts = lineFromFile.split(",");

        courseName = sentenceParts[0];
        courseGrade = Double.parseDouble(sentenceParts[1]);
    }

    public String getCourseName() {
        return courseName;
    }

    public double getCourseGrade() {
        return courseGrade;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseGrade(double courseGrade) {
        this.courseGrade = courseGrade;
    }

    /**
     * A method that returns string that is formatted to have comma as delimiter between two parts, the course name as
     * the first part, and the course grade as second one.
     *
     * @return - a formatted string
     */
    public String toFile() {
        String format = "%s,%.2f";
        String lineIntoFile = String.format(format, courseName, courseGrade);
        return lineIntoFile;
    }

    /**
     * An overridden toString method that returns string with a course name and grade in it
     *
     * @return String
     */
    @Override
    public String toString() {
        return courseName + " grade: " + courseGrade;
    }

}
