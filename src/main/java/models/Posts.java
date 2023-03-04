package models;

import Database.JDBC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Posts {

    private JDBC jdbc = JDBC.getInstance();
    String location;
    String createDateTime;
    int posted_user_id;
    int received_user_id;// fill in if it is a direct message;
    int group_id;// fill in if it is a group post
    // if both of received user_id and group_id is null, then it is  a post in your page
    String post_type; // T if text , V if video, I if image;
    int reply_post_id; //fill in if it is a replied_post
    int forwarded_from_user_id;// fill in if it was forwarded
    //you can use User model fields instead of posted_user_id--> username
    //received_user_id --> username  group_id --> group_id   forwarded_from_user_id-->username
    public String writeFile(String text, String group_id, String user_id, String replied_id) throws IOException {
        String fileName = jdbc.getNumberOfTextMessage() + ".txt";
        String fileAddress = "C:\\Users\\tejan system\\Desktop\\social-media-project\\text_messages\\" + fileName;
        File myObj = new File(fileAddress);
        System.out.println("in file writer");
        try {
            if (myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter(fileAddress);
                System.out.println("text" + text);
                myWriter.write(text);
                myWriter.close();
                jdbc.addGroupPost(group_id, user_id, fileAddress,"T",null, replied_id);
            }
        }
        catch (Exception e){
            System.out.println("an error while creating or writing file..");
            System.out.println(e.getStackTrace());
        }
        return fileAddress;
    }
    public String readFromFile(String address){
        String text = "";
        try {
            File myObj = new File(address);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                text += data + "\n";
            }
            myReader.close();

        }
        catch (FileNotFoundException e) {

            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return text;
    }
    public String readFirstLine(String address){
        String data = "";
        try {
            File myObj = new File(address);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
             data = myReader.nextLine();
                break;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }
    public void edit_file(String text, String address){
        File myObj = new File(address);
        System.out.println(address);
        try {

                FileWriter myWriter = new FileWriter(address);
                myWriter.write(text);
                myWriter.close();

        }
        catch (Exception e){
            System.out.println("an error while creating or writing file..");
            System.out.println(e.getStackTrace());
        }
    }

}
