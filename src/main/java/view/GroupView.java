package view;

import Database.JDBC;
import controller.GroupController;
import enums.Messages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;

public class GroupView extends Menu {
    public static String forwardID;
    public static String edit_id;
    private static String id;
    private static String admin_id;
    public VBox userVBox;
    public VBox messagesVBox;
    public ImageView timeLineImageView;
    public ImageView exploreImageView;
    public ImageView directImageView;
    public ImageView groupImageView;
    public ImageView groupSettingImageView;
    public ImageView profileImageView;
    public ImageView settingImageView;
    public ImageView imoJImageView;
    public ImageView attachImageView;
    public ImageView sendImageView;
    public ImageView searchImageview;
    public TextField messageTextBox;
    public AnchorPane bg;
    public Circle profileImage;
    public ImageView groupSetImageView;
    GroupController groupController = GroupController.getInstance();
    boolean admin;
    private static String user_id;
    private  static Scene scene;
    private JDBC jdbc = JDBC.getInstance();
    private int n;
    private String currGroup_id = "1";
    private ArrayList<VBox> messageBoxs  = new ArrayList<>();
    private ArrayList<HBox> allBox = new ArrayList<>();
    private ArrayList<Circle> allCircles = new ArrayList<>();
    private ArrayList<Text> allTexts = new ArrayList<>();
    private String text;
    public static String reply_id = null;
    public GroupView(){

    }

    private void setProfileImage() {
        ResultSet resultSet  = jdbc.getUserByID(user_id);
        try {
            while (resultSet.next()){
                String address = resultSet.getString("image");
                System.out.println(address);
                if (address ==  null) return;
                Image image = new Image(new File(address).toURI().toString());
                profileImage.setFill(new ImagePattern(image));
            }
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize(){
        addIcons();
        addGroup();
        detectClickOnGroupHBoxes();
        addGroupMessages();
        setProfileImage();
    }
    public void lightTheme(){
        bg.setStyle("-fx-background-color: #DAA520");
    }
    private void addIcons() {
        Image timeLineImage = new Image("/timeLine.png");
        timeLineImageView.setImage(timeLineImage);
        Image exploreImage = new Image("/explore.png");
        exploreImageView.setImage(exploreImage);
        Image directImage = new Image("/direct.png");
        directImageView.setImage(directImage);
        Image groupImage = new Image("/group.png");
        groupImageView.setImage(groupImage);
        Image groupSettingImage = new Image("/groupSetting.png");
        groupSettingImageView.setImage(groupSettingImage);
        Image profileImage = new Image("/profile.png");
        profileImageView.setImage(profileImage);
        Image settingImage = new Image("/setting.png");
        settingImageView.setImage(settingImage);
        Image sentImage = new Image("/send.png");
        sendImageView.setImage(sentImage);
        Image searchImage = new Image("/search.png");
        searchImageview.setImage(searchImage);
        Image imoJImage = new Image("/imoJ.png");
        imoJImageView.setImage(imoJImage);
        Image attachImage = new Image("/attach.png");
        attachImageView.setImage(attachImage);
        Image setG = new Image("/setting.png");
        groupSetImageView.setImage(setG);
    }

    @FXML
    public void addGroup(){
        ResultSet resultSet1;
        resultSet1 = jdbc.findAllGroups(user_id);
        try {
            while (resultSet1.next()) {
                String group_name = resultSet1.getString("group_name");
                String group_id = resultSet1.getString("group_id");
                String imageStr = resultSet1.getString("image");
                HBox hBox = new HBox();
                Circle circle = new Circle();
                circle.setRadius(23);
                circle.setFill(Color.GRAY);;
                if (imageStr!= null){
                    Image profImage = new Image(new File(imageStr).toURI().toString());
                    circle.setFill(new ImagePattern(profImage));

                }
                hBox.getChildren().addAll(circle, new Label(group_name + " " + group_id));
                userVBox.getChildren().add(hBox);
                allBox.add(hBox);
            }
        }
        catch (Exception e){System.out.println(e + "in group view");
            e.printStackTrace();
        }
    }
    private void addGroupMessages(){
        messagesVBox.getChildren().clear();
        String id = groupController.getIDBYGroupID(currGroup_id);
        groupController.handleShowMessage(id,messagesVBox, messageBoxs, user_id);
    }
    private void detectClickOnGroupHBoxes(){
        for(HBox hBox : allBox){
            HBox currHBox = hBox;
            currHBox.setOnMouseClicked( ( e ) ->
            {
                System.out.println("yes");
                for (Node x : currHBox.getChildren()){
                    if (x instanceof Label){
                        String text = ((Label) x).getText();
                        System.out.println(currGroup_id);
                        currGroup_id = getGroupIDFromLabel(text);
                        id =  groupController.getIDBYGroupID(currGroup_id);
                        addGroupMessages();
                        System.out.println(text);
                    }
                }
            } );
        }
    }
    private String getGroupIDFromLabel(String text){
        String[] data = text.split(" ");
        return data[1];
    }

    public GroupView( String user_id) {
        super();
        this.user_id = user_id;
    }

    @Override
    public void run() throws ParseException {
        try {
            if (scene == null){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/social_media_javafx/groupMenu.fxml"));
                scene = new Scene(fxmlLoader.load());
            }
            Menu.stage.setTitle("SMS");
            Menu.stage.setScene(scene);
            Menu.stage.show();
        }
        catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }
    }


    private void getTextMessage() {
        this.text = messageTextBox.getText();

    }

    public void openFileChooser(ActionEvent event) {
        String address =  this.fileChooser(scene);
        String id = groupController.getIDBYGroupID(currGroup_id);
        groupController.imageMessage(id,user_id, address, user_id,messagesVBox, messageBoxs, reply_id, forwardID);
        reply_id = null;
        forwardID = null;
        System.out.println("here in file chooser");
    }

    public void sendTextMessage(ActionEvent event) throws IOException {
        getTextMessage();
        String id = groupController.getIDBYGroupID(currGroup_id);
        if (edit_id != null){
            System.out.println("edit_id " + edit_id);
            System.out.println("group_id " + id);
            groupController.handleEdit(user_id, id, edit_id, text);
            edit_id = null;
            addGroupMessages();
            return;
        }
        Messages messages = groupController.handleSendMessage(id, user_id, reply_id,forwardID,messagesVBox, messageBoxs, text);
        reply_id = null;
        forwardID = null;
        messageTextBox.clear();
    }

    public void groupSettingMenu(ActionEvent event) throws ParseException {
        new GroupSetting(user_id).run();
    }


    public void sentVideo(ActionEvent event) {
        String address =  this.fileChooser(scene);
        String id = groupController.getIDBYGroupID(currGroup_id);
        groupController.videoMessage(id,user_id, address, user_id,messagesVBox, messageBoxs, reply_id, forwardID);
        reply_id = null;
        forwardID = null;

    }

    public void setting(ActionEvent event) throws ParseException {
        String id = groupController.getIDBYGroupID(currGroup_id);
        new GroupSettingView(currGroup_id, id).run();
    }
}
