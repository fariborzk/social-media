package controller;

import Database.JDBC;
import enums.Messages;

public class GroupSettingController {
    private static GroupSettingController instance = null;
    private JDBC jdbc = JDBC.getInstance();
    private GroupSettingController(){}
    private static  void setInstance(GroupSettingController groupSettingController){
        GroupSettingController.instance = groupSettingController;
    }
    public static GroupSettingController getInstance(){
        if (GroupSettingController.instance == null)
            setInstance( new GroupSettingController());
        return instance;
    }
    public Messages handleCreateGroup(String groupName, String groupID, String user_id, String imageAddress){
        if (groupName.isEmpty())
            return Messages.GROUP_NAME_CANNOT_BE_EMPTY;
        if (groupID.isEmpty())
            return  Messages.GROUP_ID_CANNOT_BE_EMPTY;
        if (!checkGroupIDFormat(groupID))
            return Messages.INVALID_GROUP_ID;
        if (doesGroupIDExists(groupID))
            return Messages.GROUP_ID_EXISTS;
        try {
            jdbc.addNewGroupToSDL(groupName, groupID, user_id, imageAddress);
            return Messages.SUCCESS;
        }
        catch (Exception e){
            System.out.println(e);
        }
        return Messages.SQL_EXCEPTION;
    }

    private boolean doesGroupIDExists(String groupID){
        return false;
    }
    private boolean checkGroupIDFormat(String groupID){
        return groupID.matches("@.+");
    }
}
