package view;

import Database.JDBC;
import controller.GroupController;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.File;
import java.sql.ResultSet;
import java.text.ParseException;


public class GroupSettingView extends Menu{
    public ImageView timeLineImageView;
    public ImageView exploreImageView;
    public ImageView directImageView;
    public ImageView groupImageView;
    public ImageView groupSettingImageView;
    public ImageView profileImageView;
    public ImageView settingImageView;
    public ImageView searchImageview;
    private static GroupSettingView instance = null;
    private static String groupID;
    private static String userID;
    private static String chosenUserID;
    public static String id;
    private static String addUserID = null;
    public TextField banTextField;
    public Label banLabel;
    public TextField unbanTextField;
    public Label unBanLabel;
    public Label removeLabel;
    public TextField removeTextField;
    public TextField addTextField;
    public Label addLabel;
    public TextField groupIDTextField;
    public TextField groupNameTextField;
    public Label changeLabel;
    public ImageView backImageView;
    public AnchorPane bg;
    public Circle profileImage;
    private JDBC jdbc = JDBC.getInstance();
    private static Scene scene;
    public VBox settingVBox;
    private GroupController groupController = GroupController.getInstance();
    private void addMembersToVbox(){
        Text t = new Text("Members :");
        settingVBox.getChildren().add(t);
        ResultSet resultSet = jdbc.getGroupMembers(groupID);
        try {
            while (resultSet.next()){
                String name = resultSet.getString("username");
                id = resultSet.getString("id");
                Text text = new Text(name);
                settingVBox.getChildren().add(text);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public static GroupSettingView getInstance(){

        return instance;
    }

    public GroupSettingView(){

    }
    public GroupSettingView(String groupID, String userID){
     GroupSettingView.groupID = groupID;
     GroupSettingView.userID = userID;
    }
    @FXML
    public void initialize(){
        addIcons();
       showGroupInfo();
       if (Menu.Theme) lightTheme();
       setProfileImage();
    }
    private void setProfileImage() {
        ResultSet resultSet  = jdbc.getUserByID(userID);
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
        Image back = new Image("/back.png");
        backImageView.setImage(back);

    }
    @Override
    public void run() throws ParseException {
        try {
            if (scene == null){
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/social_media_javafx/settingGroup.fxml"));
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

    private void showGroupInfo(){
        ResultSet resultSet = jdbc.getGroup(groupID);

        try{
            System.out.println("here in info");
            while (resultSet.next()){
                System.out.println(" in resultset");
                String groupName = resultSet.getString("group_name");
                String group_id = resultSet.getString("group_id");
                String created_date = resultSet.getString("created_time");
                String admin_id = resultSet.getString("admin_id");
                String admin_name = groupController.getAdminName(admin_id);
                Text nameT = new Text("group name : " + groupName);
                Text IDT = new Text("group id : " + group_id);
                Text dateT = new Text("created date : " + created_date);
                Text adminT = new Text("admin: " + admin_name);
                settingVBox.getChildren().addAll(nameT, IDT, dateT, adminT);
                addMembersToVbox();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void addNewMember(ActionEvent event) {
        String username = addTextField.getText();
        Messages message = groupController.handleAddNewMember(username, groupID);
        if (message != Messages.SUCCESS)
            addLabel.setText(message.toString());
    }
    public void removeMember(ActionEvent event) {
        String username = removeTextField.getText();
        Messages messages =groupController.handleRemoveMember(username, groupID);
        System.out.println(messages);
        if (messages != Messages.SUCCESS)
            removeLabel.setText(messages.toString());
    }
    public void banMember(ActionEvent event) {
        String username = banTextField.getText();
        Messages messages = groupController.handleBan(groupID, username);
        if (messages!= Messages.SUCCESS)
            banLabel.setText(messages.toString());
    }

    public void unbanMember(ActionEvent event) {
        String username =unbanTextField.getText();
        Messages messages  =  groupController.handleUnBan(username, groupID);
        if (messages != Messages.SUCCESS)
            unBanLabel.setText(messages.toString());
    }

    public void backToChat(ActionEvent event) throws ParseException {
        new GroupView((userID)).run();
    }

    public void groupSettingMenu(ActionEvent event) {
    }

    public void change(ActionEvent event) {
        String groupName =   groupNameTextField.getText();
        String group_id = groupIDTextField.getText();
        if (!groupName.isEmpty()){
            Messages messages = groupController.handleChangeName(groupName, groupID);
            if (messages != Messages.SUCCESS)
                changeLabel.setText(messages.toString());
        }
        if (!group_id.isEmpty()){
            Messages messages =  groupController.handleChangeID(group_id, groupID);
            if (messages != Messages.SUCCESS)
            {
                changeLabel.setText(messages.toString());
            }

        }
    }
}
