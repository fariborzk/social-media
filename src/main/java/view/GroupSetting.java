package view;

import Database.JDBC;
import controller.GroupSettingController;
import enums.Messages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.sql.ResultSet;
import java.text.ParseException;


public class GroupSetting extends Menu{
    private static Scene scene;
    public Label groupIDLabel;
    public Label groupNameLabel;
    public TextField groupIDTextField;
    public TextField groupNameTextField;
    public ImageView settingImageView;
    public ImageView profileImageView;
    public ImageView groupSettingImageView;
    public ImageView groupImageView;
    public ImageView directImageView;
    public ImageView exploreImageView;
    public ImageView timeLineImageView;
    public AnchorPane bg;
    public Circle profileImage;
    public Circle profile;
    private String address = null;
    private String groupName = null;
    private String groupID = null;
    private static String user_id ;
    String imageAddress = null;
    private JDBC jdbc = JDBC.getInstance();
    private GroupSettingController groupSettingController = GroupSettingController.getInstance();
    public GroupSetting(String user_id){
        System.out.println(user_id);
        this.user_id = user_id;
    }

    public void lightTheme(){
        bg.setStyle("-fx-background-color: #DAA520");
    }

    public GroupSetting(){

    }
    @FXML
    public void initialize(){
    addIcons();
        if (Menu.Theme) lightTheme();
        setProfileImage();
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
    }
    @Override
    public void run() throws ParseException {
        try {
            if (scene == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/social_media_javafx/groupSetting.fxml"));
                scene = new Scene(fxmlLoader.load());
            }

            Menu.stage.setTitle("SMS");
            Menu.stage.setScene(scene);
            Menu.stage.show();

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }


    public void addProfileImage(ActionEvent event) {
        imageAddress = this.fileChooser(scene);
        Image image = new Image(new File(imageAddress).toURI().toString());
        profile.setFill(new ImagePattern(image));
    }
    private  void getGroupName(){
        this.groupID = groupIDTextField.getText();
        groupIDTextField.clear();
    }
    private void getGroupID(){
        this.groupName = groupNameTextField.getText();
        groupNameTextField.clear();
    }

    public void createNewGroup(ActionEvent event) {
        getGroupName();
        getGroupID();
        Messages messages = groupSettingController.handleCreateGroup(groupName, groupID, user_id, imageAddress);
        if (messages != Messages.SUCCESS){
            if (messages == Messages.GROUP_NAME_CANNOT_BE_EMPTY)
                groupNameLabel.setText(messages.toString());
            else groupIDTextField.setText(messages.toString());
        }
    }
}
