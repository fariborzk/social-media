package controller;
import Database.JDBC;
import enums.Messages;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import models.Posts;
import view.GroupView;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class GroupController {
    private static GroupController instance;
    private JDBC jdbc = JDBC.getInstance();
    private static void setInstance(GroupController groupController){
        GroupController.instance = groupController;
    }
    private GroupController(){
    }
    public static GroupController getInstance(){
        if (instance == null)
            setInstance(new GroupController());
        return instance;
    }
    public Messages handleAddNewMember(String username, String group_id) {
        if (username.isEmpty())
            return Messages.USER_NAME_CANT_BE_EMPTY;
        String user_id = null;
        user_id = this.doesUserNameExist(username);
        if (user_id == null)
            return Messages.NO_USER_EXIST;
        jdbc.addGroupMember(group_id, user_id);
        return Messages.SUCCESS;
    }
    private String doesUserNameExist(String username){
        ResultSet resultSet = jdbc.findUserNameFromDatabase(username);
       String id = null;
        try {
            while (resultSet.next()){
                if (resultSet.getString("username").equals(username)){
                    return resultSet.getString("id");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
      return  id;
    }
    public void handleShowMessage(String group_id, VBox messagesVBox, ArrayList<VBox> messageBoxs,String user_id) {
        ResultSet resultSet = jdbc.getGroupPost(group_id);
        int n = 0;
        try {
            System.out.println("n " + n);
            while (resultSet.next()){
                String username = "";
                String id = resultSet.getString("id");
                String dateTime = resultSet.getString("created_datetime");
                String address = resultSet.getString("media_location");
                System.out.println("in cont ad : " + address);
                String replied_to = resultSet.getString("replyed_post_id");
                String forwarded_from = resultSet.getString("forwarded_from");
                String type = resultSet.getString("post_type");
                String repliedMessage = null;
                String forwardedUser = null;
                if (replied_to != null){
                 repliedMessage = showRepliedMessage(replied_to);
                }
                if (forwarded_from != null)
                  forwardedUser = showForwardedMessage(forwarded_from);
                String postedUserID = resultSet.getString("posted_user_id");
                ResultSet resultSet1 = jdbc.getUserByID(postedUserID);

                String image = null;

                while (resultSet1.next()) {
                    username = resultSet1.getString("username");
                    image = resultSet1.getString("image");
                }
                if (type.equals("T"))
                    showTextMessage(username, repliedMessage, forwardedUser, address, dateTime, user_id.equals(postedUserID),messagesVBox, messageBoxs, image);
                if(type.equals("I")){
                    System.out.println("in cont : " + address);
                    showImageMessage(username, repliedMessage, forwardedUser, address, dateTime, user_id.equals(postedUserID),messagesVBox, messageBoxs, image);
                }
                else if (type.equals("V"))showVideoMessage(username, repliedMessage, forwardedUser, address, dateTime, user_id.equals(postedUserID),messagesVBox, messageBoxs, image);

            }
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }
    private void handelForward(){
        //hande forward
    }
    private String showForwardedMessage(String forwarded_from) {
        ResultSet resultSet = jdbc.getUserByID(forwarded_from);
        String username = "";
        try {
            while (resultSet.next()){
                username = resultSet.getString("username");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        return username;
    }

    private String showRepliedMessage(String replied_to) {
        String text = "";
      ResultSet resultSet = jdbc.findGroupPostByID(replied_to);
      try {
          while (resultSet.next()){
              String address = resultSet.getString("media_location");
             text =  new Posts().readFirstLine(address);
          }
      }
      catch (Exception e){
          System.out.println(e);
      }
      return text;
    }
    public void handleShowMembers(String group_id) {
        ResultSet resultSet = jdbc.getGroupMembers(group_id);
        try {
            while (resultSet.next()){
             String name = resultSet.getString("username");
                System.out.println(name);
            }
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println(e.getStackTrace());
        }
    }
    public String getAdminName(String admin_id) {
        ResultSet resultSet = jdbc.getUserByID(admin_id);
        try {
            while (resultSet.next())
              return  resultSet.getString("username");
        }
        catch (Exception e){
            System.out.println(e);
            e.fillInStackTrace();
        }
        return null;
    }
    public Messages handleRemoveMember(String username, String group_id) {
        if (username.isEmpty()) return Messages.USER_NAME_CANT_BE_EMPTY;
       ResultSet resultSet = jdbc.findUserINGroup(username, group_id);
       if (resultSet == null) return Messages.NO_USER_EXIST;
       try {
           while (resultSet.next()){
               jdbc.removeGroupMember(group_id , resultSet.getString("user_id"));
           }
       }
       catch (Exception e){
           System.out.println(e);
           e.fillInStackTrace();
       }
       return Messages.SUCCESS;
    }

    public Messages handleChangeName(String name, String id) {
        if (name.isEmpty()) return Messages.GROUP_NAME_CANNOT_BE_EMPTY;
        jdbc.updateGroup(name, id);
        jdbc.updateGroupInfo("group_name", name, id);
        return Messages.SUCCESS;
    }

    public Messages handleChangeID(String new_id, String id) {
        if (new_id.isEmpty()) return Messages.GROUP_ID_CANNOT_BE_EMPTY;
        if (!checkGroupIDFormat(new_id)) return Messages.INVALID_GROUP_ID;
        if (doesGroupIDExist(new_id))  return Messages.GROUP_ID_EXISTS;
        jdbc.updateGroupInfo("group_id",new_id, id);
        return Messages.SUCCESS;
    }
    private boolean checkGroupIDFormat(String groupID){
        return groupID.matches("@.+");
    }

    private boolean doesGroupIDExist(String new_id) {
        ResultSet resultSet =jdbc.getGroupByGroupID(new_id);
        try {
            while (resultSet.next()) return true;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return false;
    }
   public void showAllGroups(String user_id){
      ResultSet resultSet ;
      resultSet = jdbc.findAllGroups(user_id);
       try {
           while (resultSet.next()) {
               String group_name = resultSet.getString("group_name");
               String group_id = resultSet.getString("group_id");
               System.out.println("group_name : " + group_name + " group_id : " + group_id);
           }
       }
       catch (Exception e){
           System.out.println(e);
       }
   }



    public Messages handleBan(String group_id, String username) {
        if (username.isEmpty()) return Messages.USER_NAME_CANT_BE_EMPTY;
        String user_id = doesUserNameExist(username);
        if (user_id == null) return Messages.NO_USER_EXIST;
        if (!isUserAMemberOfAGroup(username, group_id)) return Messages.NOT_A_MEMBER_OF_GROUP;
        jdbc.banFromTheGroup(user_id, group_id);
        return Messages.SUCCESS;
    }

    private boolean isUserAMemberOfAGroup(String username, String group_id) {
        ResultSet resultSet = jdbc.findUserINGroup(username, group_id);
        try {
            while (resultSet.next()) return  true;
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        return  false;
    }

    public Messages handleUnBan(String username, String group_id) {
        if (username.isEmpty()) return Messages.USER_NAME_CANT_BE_EMPTY;
        String user_id = doesUserNameExist(username);
        if (user_id == null) return Messages.NO_USER_EXIST;
        if (!isUserAMemberOfAGroup(username, group_id)) return Messages.NOT_A_MEMBER_OF_GROUP;
        if (!isUserBaned(user_id, group_id)) return Messages.NOT_BEEN_BANED;
        jdbc.deleteBan(user_id, group_id);
               return Messages.SUCCESS;
    }

    private boolean isUserBaned(String user_id, String group_id) {
        ResultSet resultSet = jdbc.findUserIsBaned(user_id, group_id);
        try {
            while (resultSet.next()) return  true;
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        return false;
    }
    public Messages handleSendMessage(String id, String user_id, String replied_id, String forwardID, VBox messageVBox, ArrayList<VBox> boxes, String text) throws IOException {
        if (isUserBaned(user_id, id)) return Messages.BAN;
        String address = new Posts().writeFile(text, id, user_id, replied_id);
        ResultSet resultSet  = jdbc.getUserByID(user_id);
        String image = null;
        try {
            while (resultSet.next()){
                image = resultSet.getString("image");
            }
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        showTextMessage(null, replied_id, forwardID, address,LocalDate.now().toString(), true, messageVBox, boxes, image );
        return Messages.SUCCESS;
    }

    public Messages handleEdit(String user_id, String id, String message_id,String text) {
        ResultSet resultSet = jdbc.getGroupPostByPostID(user_id, id, message_id);
        try {
            while (resultSet.next()){

                new Posts().edit_file(text, resultSet.getString("media_location"));
                return Messages.SUCCESS;
            }
            return Messages.INVALID_MESSAGE_ID;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
    public void imageMessage(String group_id, String userID, String address, String username, VBox messagesVBox, ArrayList<VBox> messageBoxs, String reply, String forward){
     jdbc.addGroupPost(group_id,userID,address,"I", forward, reply);
        String repliedMessage = null;
        String forwardedUser = null;
        if (reply != null){
            repliedMessage = showRepliedMessage(reply);
        }
        if (forward != null)
            forwardedUser = showForwardedMessage(forward);
        ResultSet resultSet  = jdbc.getUserByID(userID);
        String image = null;
        try {
            while (resultSet.next()){
                image = resultSet.getString("image");
            }
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
     showImageMessage(username, repliedMessage, forwardedUser, address, LocalDate.now().toString(), true,messagesVBox, messageBoxs , image);
    }
    public void showImageMessage(String username, String repliedMessage, String forwardedUser, String address, String dateTime, boolean thisUser, VBox messagesVBox, ArrayList<VBox> messageBoxs, String imageStr){
        VBox vBox = new VBox();
        HBox headerHBox = new HBox();
        VBox footerVBox = new VBox();
        VBox headerText = new VBox();
        HBox optionHBox = new HBox();
        Label to_forwardL = new Label("forward");
        Label to_replyL = new Label("reply");
        Label editL = new Label("edit");
       to_forwardL.setOnMouseClicked((e) ->{
            GroupView.forwardID = getPostIDByAddress(address);
            handelForward();
        });
       to_replyL.setOnMouseClicked((e) -> {

           GroupView.reply_id = getPostIDByAddress(address);
       });
        optionHBox.getChildren().addAll(to_replyL, to_forwardL);
        if (!thisUser){
            Circle circle = new Circle();
            circle.setFill(Color.BLACK);
            circle.setRadius(20);
            if (imageStr!= null){
                Image profImage = new Image(new File(imageStr).toURI().toString());
                circle.setFill(new ImagePattern(profImage));

            }
            headerHBox.getChildren().add(circle);
            Label nameL = new Label(username);
            headerText.getChildren().add(nameL);
        }
        else {
            editL.setOnMouseClicked((e)->{
                GroupView.edit_id = getPostIDByAddress(address);
            });
            footerVBox.setBackground(new Background(new BackgroundFill(Color.BLUE,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            optionHBox.getChildren().add(editL);
        }
        System.out.println("address : " + address);
        Image image = new Image(new File(address).toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        Label repliedL = null, forwardL = null;
        Label dateL = new Label(dateTime);
        if (repliedMessage != null) repliedL = new Label(repliedMessage);
        if (forwardedUser != null) forwardL = new Label(forwardedUser);
        if (repliedL != null) headerText.getChildren().add(repliedL);
        if (forwardL != null) headerText.getChildren().add(forwardL);
        headerHBox.getChildren().addAll(headerText);
        footerVBox.getChildren().addAll(imageView, dateL, optionHBox);
        vBox.getChildren().addAll(headerHBox, footerVBox);
        messageBoxs.add(vBox);
        messagesVBox.getChildren().add(vBox);
    }

    private String getPostIDByAddress(String address) {
        ResultSet resultSet = jdbc.getPostIDByAddress(address);
        String id = null;
        try
        {
         while (resultSet.next()) return resultSet.getString("id");
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        return null;
    }

    public void showTextMessage(String username, String repliedMessage, String forwardedUser, String address, String dateTime, boolean thisUser, VBox messagesVBox, ArrayList<VBox> messageBoxs, String image){
        VBox vBox = new VBox();
        HBox headerHBox = new HBox();
        VBox footerVBox = new VBox();
        VBox headerText = new VBox();
        HBox optionHBox = new HBox();
        Label to_forwardL = new Label("forward");
        Label to_replyL = new Label("reply");
        Label editL = new Label("edit");
        to_forwardL.setOnMouseClicked((e) ->{
            GroupView.forwardID = getPostIDByAddress(address);
            handelForward();
        });
        to_replyL.setOnMouseClicked((e) -> {
            GroupView.reply_id = getPostIDByAddress(address);
        });
        optionHBox.getChildren().addAll(to_replyL, to_forwardL);
        if (!thisUser){

            Circle circle = new Circle();
            circle.setFill(Color.BLACK);
            circle.setRadius(20);
            if (image!= null){
                Image profImage = new Image(new File(image).toURI().toString());
                circle.setFill(new ImagePattern(profImage));

            }
            headerHBox.getChildren().add(circle);
            Label nameL = new Label(username);
            headerText.getChildren().add(nameL);

        }
        else {
            editL.setOnMouseClicked((e)->{
                GroupView.edit_id = getPostIDByAddress(address);
            });
            footerVBox.setBackground(new Background(new BackgroundFill(Color.BLUE,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            optionHBox.getChildren().add(editL);

        }
        System.out.println("in show message");
        System.out.println(address);
        String text = new Posts().readFromFile(address);
        Text message = new Text(text);
        Label repliedL = null, forwardL = null;
        Label dateL = new Label(dateTime);
        if (repliedMessage != null) repliedL = new Label(repliedMessage);
        if (forwardedUser != null) forwardL = new Label(forwardedUser);
        if (repliedL != null) headerText.getChildren().add(repliedL);
        if (forwardL != null) headerText.getChildren().add(forwardL);
        headerHBox.getChildren().addAll(headerText);
        footerVBox.getChildren().addAll(message, dateL);
        vBox.getChildren().addAll(headerHBox, footerVBox, optionHBox);
        messageBoxs.add(vBox);
        messagesVBox.getChildren().add(vBox);
    }
    public void showVideoMessage(String username, String repliedMessage, String forwardedUser, String address, String dateTime, boolean thisUser, VBox messagesVBox, ArrayList<VBox> messageBoxs, String image){
        VBox vBox = new VBox();
        HBox headerHBox = new HBox();
        VBox footerVBox = new VBox();
        VBox headerText = new VBox();
        HBox optionHBox = new HBox();
        Label to_forwardL = new Label("forward");
        Label to_replyL = new Label("reply");
        Label editL = new Label("edit");
        to_forwardL.setOnMouseClicked((e) ->{
            GroupView.forwardID = getPostIDByAddress(address);
            handelForward();
        });
        to_replyL.setOnMouseClicked((e) -> {
            GroupView.reply_id = getPostIDByAddress(address);
        });
        optionHBox.getChildren().addAll(to_replyL, to_forwardL);
        if (!thisUser){
            Circle circle = new Circle();
            circle.setFill(Color.BLACK);
            circle.setRadius(20);
            if (image!= null){
                Image profImage = new Image(new File(image).toURI().toString());
                circle.setFill(new ImagePattern(profImage));

            }
            headerHBox.getChildren().add(circle);
            Label nameL = new Label(username);
            headerText.getChildren().add(nameL);
        }
        else {
            editL.setOnMouseClicked((e)->{
                GroupView.edit_id = getPostIDByAddress(address);
            });
            footerVBox.setBackground(new Background(new BackgroundFill(Color.BLUE,
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            optionHBox.getChildren().add(editL);
        }
        System.out.println("media : " + address);
        Media media = new Media (new File(address).toURI().toString());
        MediaPlayer player = new MediaPlayer (media);
        player.setAutoPlay(true);
        MediaView view = new MediaView (player);
        view.setPreserveRatio(true);
        view.setFitHeight(100);
        view.setFitWidth(100);
        Label repliedL = null, forwardL = null;
        Label dateL = new Label(dateTime);
        if (repliedMessage != null) repliedL = new Label(repliedMessage);
        if (forwardedUser != null) forwardL = new Label(forwardedUser);
        if (repliedL != null) headerText.getChildren().add(repliedL);
        if (forwardL != null) headerText.getChildren().add(forwardL);
        headerHBox.getChildren().addAll(headerText);
        footerVBox.getChildren().addAll(view, dateL);
        vBox.getChildren().addAll(headerHBox, footerVBox);
        messageBoxs.add(vBox);
        messagesVBox.getChildren().add(vBox);
    }

    public String getIDBYGroupID(String group_id) {
        ResultSet resultSet = jdbc.findGroupsFromDataBase(group_id);
        String id = null;
        try {
            while (resultSet.next())
                return resultSet.getString("id");
        }
        catch (Exception e){
        }
        return id;
    }

    public void videoMessage(String group_id, String userID, String address, String username, VBox messagesVBox, ArrayList<VBox> messageBoxs, String reply, String forward) {
        jdbc.addGroupPost(group_id,userID,address,"V", forward,reply);
        String repliedMessage = null;
        String forwardedUser = null;
        if (reply != null){
            repliedMessage = showRepliedMessage(reply);
        }
        if (forward != null)
            forwardedUser = showForwardedMessage(forwardedUser);
        ResultSet resultSet  = jdbc.getUserByID(userID);
        String image = null;
        try {
            while (resultSet.next()){
               image = resultSet.getString("image");
            }
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
        showVideoMessage(username, repliedMessage, forwardedUser, address, LocalDate.now().toString(), true,messagesVBox, messageBoxs, image );
    }
}