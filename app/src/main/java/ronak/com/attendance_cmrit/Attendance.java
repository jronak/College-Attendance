package ronak.com.attendance_cmrit;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ronak on 18-04-2015.
 */
public class Attendance implements Serializable {

    String name;
    LinkedList<String> subjects;
    LinkedList<Float> total;
    LinkedList<Float> present;
    LinkedList<Float> percentage;

    public Attendance()
    {
        this.subjects = new LinkedList<>();
        this.total = new LinkedList<>();
        this.present = new LinkedList<>();
        this.percentage = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(LinkedList<String> subjects) {
        this.subjects = subjects;
    }

    public LinkedList<Float> getTotal() {
        return total;
    }

    public void setTotal(LinkedList<Float> total) {
        this.total = total;
    }

    public LinkedList<Float> getPresent() {
        return present;
    }

    public void setPresent(LinkedList<Float> present) {
        this.present = present;
    }

    public LinkedList<Float> getPercentage() {
        return percentage;
    }

    public void setPercentage(LinkedList<Float> percentage) {
        this.percentage = percentage;
    }
}
