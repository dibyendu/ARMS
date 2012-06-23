package arms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author vh4x0r
 */
public class ARMSSolution implements Serializable {

    public ARMSSolution() {
        int row = ARMSManager.getTeachers().size();
        int col = ARMSManager.getSubjects().size();
        int i, j, index, maxRow;
        double priority, seniority;
        teacherIndex = new int[col];
        solutionTable = new double[row][col];
        //fill the solution table
        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                index = ARMSManager.getTeachers().get(i).getSubjectList().indexOf(ARMSManager.getSubjects().get(j));
                if (index == -1) {
                    solutionTable[i][j] = 0.0;
                } else {
                    priority = (double) (col - index) / col;
                    seniority = (double) (row - i) / row;
                    solutionTable[i][j] = ARMSManager.getPSR() * priority + seniority;
                }
            }
        }
        //find out the maximum value in each column
        //assume subjects are more than teachers
        for (j = 0; j < col; j++) {
            maxRow = 0;
            for (i = 1; i < row; i++) {
                if (solutionTable[i][j] > solutionTable[maxRow][j]) {
                    maxRow = i;
                }
            }
            teacherIndex[j] = (solutionTable[maxRow][j] == 0.0) ? -1 : maxRow;
        }
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }
        ARMSSolution other = (ARMSSolution) otherObject;
        if (other.hashCode() == this.hashCode()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Arrays.deepHashCode(this.solutionTable);
        return hash;
    }

    public double[][] getSolutionTable() {
        return solutionTable;
    }

    public int[] getTeacherIndex() {
        return teacherIndex;
    }

    public ArrayList<Integer> getSubjects(int index) {
        int i;
        ArrayList<Integer> subjects = new ArrayList<Integer>();
        for (i = 0; i < teacherIndex.length; i++) {
            if (teacherIndex[i] == index) {
                subjects.add(i);
            }
        }
        return subjects;
    }
    private double[][] solutionTable;
    private int[] teacherIndex;
}
