/*
 * Automatic Routine Management System
 */

/*
 * ARMSManager.java
 */
package arms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author vh4x0r
 */
public class ARMSManager {

    public static void startup() {
        new ARMSManager();
        try {
            File configFile;
            FileInputStream fis;
            ObjectInputStream ois;
            configFile = getConfigFile("tlist.bin");
            if (configFile.length() == 0) {
                teacherList = new ArrayList<Teacher>();
            } else {
                fis = new FileInputStream(configFile);
                ois = new ObjectInputStream(fis);
                teacherList = (ArrayList<Teacher>) ois.readObject();
                ois.close();
                fis.close();
            }
            configFile = getConfigFile("slist.bin");
            if (configFile.length() == 0) {
                subjectList = new ArrayList<Subject>();
            } else {
                fis = new FileInputStream(configFile);
                ois = new ObjectInputStream(fis);
                subjectList = (ArrayList<Subject>) ois.readObject();
                ois.close();
                fis.close();
            }
            configFile = getConfigFile("psr.bin");
            if (configFile.length() == 0) {
                psr = 2.0;
            } else {
                fis = new FileInputStream(configFile);
                ois = new ObjectInputStream(fis);
                psr = ois.readDouble();
                ois.close();
                fis.close();
            }
            configFile = getConfigFile("admin.bin");
            if (configFile.length() == 0) {
                adminPass = "admin".hashCode();
            } else {
                fis = new FileInputStream(configFile);
                ois = new ObjectInputStream(fis);
                adminPass = ois.readLong();
                ois.close();
                fis.close();
            }
            configFile = getConfigFile("user.bin");
            if (configFile.length() == 0) {
                userPass = "user".hashCode();
            } else {
                fis = new FileInputStream(configFile);
                ois = new ObjectInputStream(fis);
                userPass = ois.readLong();
                ois.close();
                fis.close();
            }
            configFile = getConfigFile("solution.bin");
            if (configFile.length() == 0) {
                solution = new ARMSSolution();
            } else {
                fis = new FileInputStream(configFile);
                ois = new ObjectInputStream(fis);
                solution = (ARMSSolution) ois.readObject();
                ois.close();
                fis.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid config file !", "Error", JOptionPane.ERROR_MESSAGE);
            ARMSApp.getApplication().exit();
        }
    }

    public static File getConfigFile(String filename) throws IOException {
        String userHome = System.getProperty("user.home");
        if (userHome.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Cannot find user home !", "Error", JOptionPane.ERROR_MESSAGE);
            ARMSApp.getApplication().exit();
        }
        File home = new File(userHome);
        File settingsDirectory = new File(home, ".ARMS");
        if (!settingsDirectory.exists()) {
            if (!settingsDirectory.mkdir()) {
                JOptionPane.showMessageDialog(null, "Cannot write to home directory !", "Error", JOptionPane.ERROR_MESSAGE);
                ARMSApp.getApplication().exit();
            }
        }
        File settingsFile = new File(settingsDirectory, filename);
        if (!settingsFile.exists()) {
            if (!settingsFile.createNewFile()) {
                JOptionPane.showMessageDialog(null, "Cannot create config file !", "Error", JOptionPane.ERROR_MESSAGE);
                ARMSApp.getApplication().exit();
            }
        }
        return settingsFile;
    }

    public static Object login(String id, String pass, Group group) throws InvalidLoginException {
        switch (group) {
            case User:
                if (id.equals("user") && pass.hashCode() == userPass) {
                    ARMSOutput aRMSOutput = new ARMSOutput(solution, javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                    aRMSOutput.setLocationRelativeTo(ARMSApp.getApplication().getMainFrame());
                    ARMSApp.getApplication().getMainFrame().setVisible(false);
                    aRMSOutput.setVisible(true);
                    return null;
                }
                break;

            case Teacher:
                for (Teacher t : teacherList) {
                    if (t.validate(id, pass)) {
                        return t;
                    }
                }
                break;

            case Administrator:
                if (id.equals("admin") && pass.hashCode() == adminPass) {
                    AdminForm adminForm = new AdminForm(teacherList, subjectList, new ARMSSolution().equals(solution));
                    adminForm.setLocationRelativeTo(ARMSApp.getApplication().getMainFrame());
                    ARMSApp.getApplication().getMainFrame().setVisible(false);
                    adminForm.setVisible(true);
                    return null;
                }
                break;
        }
        throw new InvalidLoginException("Invalid user ID and password combination!");
    }

    public static void shutdown() {
        try {
            File configFile;
            FileOutputStream fos;
            ObjectOutputStream oos;
            ARMSSolution newSolution = new ARMSSolution();
            configFile = getConfigFile("tlist.bin");
            if (!solution.equals(newSolution)) {
                configFile.renameTo(getConfigFile("tlist.bin.bak"));
            }
            fos = new FileOutputStream(configFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(teacherList);
            oos.close();
            fos.close();
            configFile = getConfigFile("slist.bin");
            if (!solution.equals(newSolution)) {
                configFile.renameTo(getConfigFile("slist.bin.bak"));
            }
            fos = new FileOutputStream(configFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(subjectList);
            oos.close();
            fos.close();
            configFile = getConfigFile("psr.bin");
            fos = new FileOutputStream(configFile);
            oos = new ObjectOutputStream(fos);
            oos.writeDouble(psr);
            oos.close();
            fos.close();
            configFile = getConfigFile("admin.bin");
            fos = new FileOutputStream(configFile);
            oos = new ObjectOutputStream(fos);
            oos.writeLong(adminPass);
            oos.close();
            fos.close();
            configFile = getConfigFile("user.bin");
            fos = new FileOutputStream(configFile);
            oos = new ObjectOutputStream(fos);
            oos.writeLong(userPass);
            oos.close();
            fos.close();
            configFile = getConfigFile("solution.bin");
            fos = new FileOutputStream(configFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(solution);
            oos.close();
            fos.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to write config file !", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static ArrayList<Subject> getAvailSubjectList(ArrayList<Subject> selectList) {
        ArrayList<Subject> availSubjectList = new ArrayList<Subject>();
        for (Subject s : subjectList) {
            if (!selectList.contains(s)) {
                availSubjectList.add(s);
            }
        }
        return availSubjectList;
    }

    public static ARMSSolution getSolution() {
        return solution;
    }

    public static ARMSSolution getNewSolution() {
        return new ARMSSolution();
    }

    public static void updateSolution() {
        solution = new ARMSSolution();
        try {
            File configFile;
            configFile = getConfigFile("tlist.bin.bak");
            configFile.delete();
            configFile = getConfigFile("slist.bin.bak");
            configFile.delete();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to write config file !", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void discardSolution() {
        try {
            File configFile;
            FileInputStream fis;
            ObjectInputStream ois;
            configFile = getConfigFile("tlist.bin.bak");
            if (configFile.length() == 0) {
                configFile.delete();
                configFile = getConfigFile("tlist.bin");
                fis = new FileInputStream(configFile);
                ois = new ObjectInputStream(fis);
                teacherList = (ArrayList<Teacher>) ois.readObject();
                ois.close();
                fis.close();
            } else {
                fis = new FileInputStream(configFile);
                ois = new ObjectInputStream(fis);
                teacherList = (ArrayList<Teacher>) ois.readObject();
                ois.close();
                fis.close();
                configFile.delete();
            }
            configFile = getConfigFile("slist.bin.bak");
            if (configFile.length() == 0) {
                configFile.delete();
                configFile = getConfigFile("slist.bin");
                fis = new FileInputStream(configFile);
                ois = new ObjectInputStream(fis);
                subjectList = (ArrayList<Subject>) ois.readObject();
                ois.close();
                fis.close();
            } else {
                fis = new FileInputStream(configFile);
                ois = new ObjectInputStream(fis);
                subjectList = (ArrayList<Subject>) ois.readObject();
                ois.close();
                fis.close();
                configFile.delete();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to read backup config file !", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static ArrayList<Subject> getSubjects() {
        return subjectList;
    }

    public static ArrayList<Teacher> getTeachers() {
        return teacherList;
    }

    public static Double getPSR() {
        return psr;
    }

    public static void setPSR(String d) {
        try {
            psr = Double.valueOf(d);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Bad numerical value !", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void updateUser() {
        String p = JOptionPane.showInputDialog(null, "Enter new user password :", "Password", JOptionPane.QUESTION_MESSAGE);
        if (p == null || p.isEmpty()) {
            return;
        }
        userPass = p.hashCode();
    }

    public static void updateAdmin() {
        String p = JOptionPane.showInputDialog(null, "Enter new administrator password :", "Password", JOptionPane.QUESTION_MESSAGE);
        if (p == null || p.isEmpty()) {
            return;
        }
        adminPass = p.hashCode();
    }
    private static ArrayList<Teacher> teacherList;
    private static ArrayList<Subject> subjectList;
    private static Double psr;
    private static ARMSSolution solution;
    private static long adminPass;
    private static long userPass;
}
