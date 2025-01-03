package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileOperations {

//	private String b;
//	private String a;
//	private String c;
//	private String d;
//	public static Credentials cred = new Credentials(a, b, c, d);

    public static void writeToAFile(String email, String password, String name, String isTech) {
        try (FileWriter fw = new FileWriter("Credentials.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(email + "|" + password + "|" + name + "|" + isTech);
            bw.newLine();
            System.out.println("File created successfully");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public static void writeToAFile(String email, String password, String name, String isTech, String phNo,
    		int height, int weight, String address, String gender, String bloodType, Date dob) {
        try (FileWriter fw = new FileWriter("Credentials1.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(email + "|" + password + "|" + name + "|" + isTech+ "|" + phNo + "|" + height + "|" + weight+ "|" + address+ "|" + gender+ "|" + bloodType+ "|" + dob);
            bw.newLine();
            System.out.println("File created successfully");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public static List<Credentials> readFromAFile() {
        List<Credentials> credentials = new ArrayList<>();
        File file = new File("Credentials.txt");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] lines = line.split("\\|");
                    if (lines.length == 8) {
                        credentials.add(new Credentials(lines[0], lines[1], lines[2], lines[3], lines[4], Integer.parseInt(lines[5]), Integer.parseInt(lines[6]), lines[7], lines[8], lines[9], LocalDate.parse(lines[10])));
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file.");
                e.printStackTrace();
            }
        }
        return credentials;
    }

    public static boolean passwordReset(String email, String newPassword) {
        List<Credentials> credentials = readFromAFile();
        boolean isUpdated = false;

        for (Credentials cred : credentials) {
            if (cred.getEmailID().equalsIgnoreCase(email)) {
                cred.setPassword(newPassword);
                isUpdated = true;
                break;
            }
        }

        if (isUpdated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Credentials.txt"))) {
                for (Credentials cred : credentials) {
                    writer.write(cred.getEmailID() + "|" + cred.getPassword() + "|" + cred.getName() + "|" + cred.getIsTech());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    public static void fileLogs(String text) {
        try (FileWriter fw = new FileWriter("Log.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(text + " Clicked");
            bw.newLine();
            System.out.println("File created successfully");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the log file.");
            e.printStackTrace();
        }
    }


}