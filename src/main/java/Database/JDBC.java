package Database;



import enums.Type;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class JDBC
{

   private Connection connection;
   private static JDBC instance = null;
    private static void setInstance(JDBC jdbc)
    {
        JDBC.instance = jdbc;
    }
    public static JDBC getInstance(){
        if (JDBC.instance == null)
            setInstance(new JDBC());
        return JDBC.instance;
    }
    private JDBC() {
        try {
           connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/social_media_test1", "root", "2581537299");
           connection.createStatement();
            System.out.println("connection");

        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }
    public void addNewUserToMySql(String first_name, String last_name, String birthday, String email, String phone_number, String join_date, String user_password, String username, String gender
            , String forget_passQ, String forget_passA, Type type, String address) throws SQLException {
        //System.out.println(user_password);
        String sql = " insert into user_profile (first_name, last_name, username, birthday, email, phone_number, join_date, gender,user_password, user_type, image)" + " values (?, ?, ?, ?, ?, ? , ?, ?, ?, ?, ?)";
        String user_type;
        if (type == Type.SIMPLE) user_type = "S";
        else user_type = "B";
        PreparedStatement preparedStmt = connection.prepareStatement(sql);
        preparedStmt.setString (1, first_name);
        preparedStmt.setString (2, last_name);
        preparedStmt.setString (3, username);
        preparedStmt.setString (4, birthday);
        preparedStmt.setString (5, email);
        preparedStmt.setString (6, phone_number);
        preparedStmt.setString (7, join_date);
        preparedStmt.setString (8, gender);
        preparedStmt.setString (9, user_password);
        preparedStmt.setString (10, user_type);
        preparedStmt.setString (11, address);
        preparedStmt.execute();
    }
    public ResultSet findUserNameFromDatabase(String username){
        ResultSet resultSet = null;
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_profile WHERE username=? ");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
        }
        catch (Exception e)
        {
            System.out.println("error while executing...");
            System.out.println(e);
        }
       return resultSet;
    }
    public ResultSet findUserPasswordFromDatabase(String username){
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_password FROM user_profile WHERE username=?  ;");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }
    public void addNewGroupToSDL(String groupName, String groupID, String admin_id, String imageAddress) throws SQLException {
        String sql = " insert into group_profile (group_name, group_id, created_time, admin_id, image)" + " values (?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        String datetime = LocalDate.now().toString() + "T"+ LocalTime.now().toString();
        PreparedStatement preparedStmt = connection.prepareStatement(sql);
        preparedStmt.setString (1, groupName);
        preparedStmt.setString (2, groupID);
        preparedStmt.setString (3, datetime);
        preparedStmt.setString (4, admin_id);
        preparedStmt.setString (5,imageAddress);
        preparedStmt.execute();
        ResultSet resultSet = getGroupByGroupID(groupID);
        try
        {
            while (resultSet.next()){
                String id = resultSet.getString("id");
                addGroupMember(id, admin_id);
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public ResultSet findAllGroups(String user_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM group_profile WHERE id in ( SELECT  group_id FROM  group_membership where user_id = ?);");
            preparedStatement.setString(1, user_id);
            resultSet = preparedStatement.executeQuery();
        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }
   public ResultSet findGroupsFromDataBase(String group_id, String admin_id){
        ResultSet resultSet = null;
       try {
           PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM group_profile WHERE  admin_id = ? AND group_id = ?;");
           preparedStatement.setString(1, admin_id);
           preparedStatement.setString(2, group_id);
           resultSet = preparedStatement.executeQuery();
       }
       catch (Exception e){
           System.out.println("error while executing...");
           System.out.println(e);
       }
       return resultSet;
   }
   public ResultSet getGroup(String id){
       ResultSet resultSet = null;
       try {
           PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM group_profile WHERE id = ?;");
           preparedStatement.setString(1, id);
           resultSet = preparedStatement.executeQuery();
       }
       catch (Exception e){
           System.out.println("error while executing...");
           System.out.println(e);
       }
       return resultSet;

   }
   public ResultSet findGroupsFromDataBase(String group_id){
       ResultSet resultSet = null;
       try {
           PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM group_profile WHERE group_id = ?;");
           preparedStatement.setString(1, group_id);

           resultSet = preparedStatement.executeQuery();
       }
       catch (Exception e){
           System.out.println("error while executing...");
           System.out.println(e);
       }
       return resultSet;
   }
    public ResultSet fillAllSimpleGroups() {
        return null;
    }
    public void addGroupMember(String group_id, String user_id) {
        String sql = " insert into group_membership (user_id, group_id, joined_date)" + " values (?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        String datetime = LocalDate.now().toString() + "T"+ LocalTime.now().toString();
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString (1, user_id);
            preparedStmt.setString (2, group_id);
            preparedStmt.setString (3, datetime);
            preparedStmt.execute();
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
    public void addGroupPost(String group_id, String user_id, String address, String post_type, String forwarded_from, String replied_to){
        String sql = " insert into group_post (posted_user_id, group_id, created_datetime, media_location, post_type, replyed_post_id, forwarded_from)" + " values (?, ?, ?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        String datetime = LocalDate.now().toString() + "T"+ LocalTime.now().toString();
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString (1, user_id);
            preparedStmt.setString (2, group_id);
            preparedStmt.setString (3, datetime);
            preparedStmt.setString (4, address);
            preparedStmt.setString (5, post_type);
            preparedStmt.setString (6, replied_to);
            preparedStmt.setString (7, forwarded_from);
            preparedStmt.execute();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public String getNumberOfGroups(String user_id){
        System.out.println(user_id + " in jdbc");
        String sql = "SELECT COUNT(*) AS num " +
                "FROM group_profile" +
                "WHERE admin_id =?;";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString (1, user_id);
            resultSet = preparedStmt.executeQuery();
        }
        catch (Exception e){
            System.out.println();
            System.out.println(e.getStackTrace());
        }

        try {
            while (resultSet.next()){
                //System.out.println(resultSet.getInt("num"));
                return String.valueOf(resultSet.getInt("num"));
            }
        }
        catch (Exception e){
            System.out.println(e + " in jdbc");
            System.out.println(e.getStackTrace());
        }

        return "0";
    }

    public String getNumberOfTextMessage() {
        String sql = "SELECT COUNT(*) AS num " +
                "FROM group_post " +
                "WHERE post_type =?;";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString (1, "T");
            resultSet = preparedStmt.executeQuery();
        }
        catch (Exception e){
            System.out.println("error in getNumber of textMessages");
            System.out.println(e.getStackTrace());
        }

        try {
            while (resultSet.next()){
                //System.out.println(resultSet.getInt("num"));
                return String.valueOf(resultSet.getInt("num"));
            }
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println(e.getStackTrace());
        }
        return "0";
    }

    public ResultSet findAllJoinedGroups(String user_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM group_profile" +
                    " WHERE id IN ( " +
                    "    SELECT group_id" +
                    "    FROM group_membership" +
                    "    WHERE user_id = ?);");
            preparedStatement.setString(1, user_id);
            resultSet = preparedStatement.executeQuery();
        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }
    public ResultSet getGroupPost(String group_id){
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM group_post" +
                    " WHERE group_id = ?");
            preparedStatement.setString(1, group_id);
            resultSet = preparedStatement.executeQuery();
        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }
    public ResultSet getUserByID(String id){
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM user_profile" +
                    " WHERE id = ?");
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }
    public ResultSet getGroupMembers(String group_id){
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM user_profile" +
                    " WHERE id IN ( " +
                    "    SELECT user_id" +
                    "    FROM group_membership" +
                    "    WHERE group_id = ?);");
            preparedStatement.setString(1, group_id);
            resultSet = preparedStatement.executeQuery();
        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }

    public ResultSet findUserINGroup(String username, String group_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id " +
                    "FROM group_membership" +
                    " WHERE user_id IN ( " +
                    "    SELECT id" +
                    "    FROM user_profile" +
                    "    WHERE username = ?) AND group_id = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, group_id);
            resultSet = preparedStatement.executeQuery();

        } catch (Exception e) {
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }
    public void removeGroupMember(String group_id, String user_id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM  group_membership " +
                    " WHERE user_id= ? AND  group_id = ?;");
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, group_id);
            preparedStatement.execute();
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println(e.fillInStackTrace());
        }
    }

    public void updateGroup(String name, String id) {
    }

    public void updateGroupInfo(String column, String new_str, String group_id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE group_profile\n" +
                    "SET  " + column + " = ?" +
                    "WHERE id = ?;");
            preparedStatement.setString(1, new_str);
            preparedStatement.setString(2, group_id);
            preparedStatement.execute();
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println(e.fillInStackTrace());
        }

    }
    public ResultSet findGroupPostByID(String post_id){
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM group_post " +
                    " WHERE id = ?");
            preparedStatement.setString(1, post_id);
            resultSet = preparedStatement.executeQuery();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }


    public ResultSet getAllViewedAndLikePosts() {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM user_post WHERE post_id in (SELECT DISTINCT post_id FROM advert )");
            resultSet = preparedStatement.executeQuery();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }

    public String getNumberOfBusinessPosts() {
        String sql = "SELECT COUNT(*) AS num " +
                "FROM user_post " +
                "WHERE post_type =?;";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStmt = connection.prepareStatement(sql);
            preparedStmt.setString (1, "T");
            resultSet = preparedStmt.executeQuery();
        }
        catch (Exception e){
            System.out.println("error in getNumber of textMessages");
            System.out.println(e.getStackTrace());
        }

        try {
            while (resultSet.next()){
                System.out.println(resultSet.getInt("num"));
                return String.valueOf(resultSet.getInt("num"));
            }
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println(e.getStackTrace());
        }
        return "0";
    }

    public ResultSet getBusinessPostByPostID() {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM user_post " +
                    " WHERE posted_user_id IN (SELECT id FROM user_profile WHERE user_type = ?);");
            preparedStatement.setString(1, "B");
            resultSet = preparedStatement.executeQuery();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }


    public ResultSet allUsersWithFavNums(String post_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM advert " +
                    " WHERE post_id = ?");
            preparedStatement.setString(1, post_id);
            resultSet = preparedStatement.executeQuery();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }

    public double getFavNum(String curr_post_id, String curr_user_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT fav_num " +
                    "FROM advert " +
                    " WHERE user_id = ? AND  post_id = ?");
            preparedStatement.setString(1, curr_user_id);
            preparedStatement.setString(2, curr_post_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) return Integer.parseInt(resultSet.getString("fav_num"));

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return 0.5;
    }

    public void banFromTheGroup(String user_id, String group_id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(" insert into ban (user_id, group_id,ban_time ) value (?, ?, ?)"
                   );
            LocalDateTime now = LocalDateTime.now();
            String datetime = LocalDate.now().toString() + "T"+ LocalTime.now().toString();
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, group_id);
            preparedStatement.setString(3, datetime);
            preparedStatement.execute();
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println(e.fillInStackTrace());
        }
    }
    public void updateFavNum(String user_id, String post_id, String value){

        try {
            PreparedStatement preparedStatement;
            if (findFavNum(user_id, post_id))
         preparedStatement = connection.prepareStatement(" update  advert set user_id = ?,post_id = ?,fav_num = ?;"
            );
            else
                preparedStatement = connection.prepareStatement(" insert into advert  (user_id,post_id,fav_num ) VALUES (?, ? ,?);"
                );
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, post_id);
            preparedStatement.setString(3, value);
            preparedStatement.execute();
        }
        catch (Exception e){
            System.out.println(e);
            System.out.println(e.fillInStackTrace());
        }
    }

    private boolean findFavNum(String user_id, String post_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM advert " +
                    " WHERE user_id = ? and post_id = ? ");
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, post_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return true;
        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return false;
    }

    public ResultSet findUserIsBaned(String user_id, String group_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  *  FROM  ban WHERE  user_id = ? AND group_id = ?");
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, group_id);
            resultSet = preparedStatement.executeQuery();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }

    public void deleteBan(String user_id, String group_id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE  FROM  ban WHERE  user_id = ? AND group_id = ?");
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, group_id);
            preparedStatement.execute();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
    }

    public ResultSet findAllBanedUser(String group_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  username  FROM  user_profile WHERE  id in ( SELECT  user_id FROM ban WHERE group_id = ?)");
            preparedStatement.setString(1, group_id);
            resultSet = preparedStatement.executeQuery();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }

    public ResultSet getGroupByGroupID(String new_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *  FROM  group_profile WHERE group_id = ?");
            preparedStatement.setString(1,new_id);
            resultSet = preparedStatement.executeQuery();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }

    public void addSecurityQuestion(String toString, String username, String answer) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_profile\n" +
                    "SET forget_password_question = ?, forget_password_answer = ?\n" +
                    "WHERE id in (SELECT id WHERE  username = ?);");
            preparedStatement.setString(1, toString);
            preparedStatement.setString(2, answer);
            preparedStatement.setString(3, username);
            preparedStatement.execute();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
    }

    public ResultSet getSecurityQ(String username) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT forget_password_question, forget_password_answer  FROM  user_profile WHERE id in (SELECT id FROM  user_profile WHERE username = ?)");
            preparedStatement.setString(1,username);
            resultSet = preparedStatement.executeQuery();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }

    public void updatePassword(String username, String newPass) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_profile\n" +
                    "SET user_password = ?\n" +
                    "WHERE id in (SELECT id WHERE  username = ?);");
            preparedStatement.setString(1, newPass);
            preparedStatement.setString(2, username);
            preparedStatement.execute();
        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
    }
    public ResultSet getGroupPostByPostID(String user_id, String id, String message_id) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  *   FROM  group_post WHERE id=? and posted_user_id =? and group_id = ? ORDER BY created_datetime DESC limit 10");
            preparedStatement.setString(1,message_id);
            preparedStatement.setString(2,user_id);
            preparedStatement.setString(3,id);
            resultSet = preparedStatement.executeQuery();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }
    public ResultSet getAllGroups(){
        return null;
    }

    public ResultSet getPostIDByAddress(String address) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT  *   FROM  group_post WHERE media_location = ?");
            preparedStatement.setString(1,address);

            resultSet = preparedStatement.executeQuery();

        }
        catch (Exception e){
            System.out.println("error while executing...");
            System.out.println(e);
        }
        return resultSet;
    }
}