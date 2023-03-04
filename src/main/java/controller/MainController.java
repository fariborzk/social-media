package controller;

import Database.JDBC;


import enums.Messages;
import view.GroupView;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainController extends Controller {
    private static MainController mainController = null;
    private JDBC jdbc = JDBC.getInstance();
    private MainController(){

    }
    private static void setMainController(MainController mainController){
        MainController.mainController = mainController;
    }
    public static MainController getInstance(){
        if (MainController.mainController == null)
            setMainController(new MainController());
        return MainController.mainController;
    }
/*
    public Messages handleGroup(String group_id, String user_id, boolean admin){
        ResultSet resultSet;
        if (admin)
         resultSet = jdbc.findGroupsFromDataBase(group_id, user_id);
        else
            resultSet = jdbc.findGroupsFromDataBase(group_id);
        try {
            while (resultSet.next()){
                if (resultSet.getString("group_id").equals(group_id))
                {
                    new GroupView( user_id).run();
                    return Messages.SUCCESS;
                }
                else
                    System.out.println(Messages.GROUP_ID_DO_NOT_EXIST);
            }
        }
        catch (Exception e){
            System.out.println(e +"\n" + e.fillInStackTrace());
        }
        return null;
    }


 */
    public void handleAdvert(String user_id) {
        String post_id = getRandomLikedPost(user_id);
        String count = jdbc.getNumberOfBusinessPosts();
        ResultSet resultSet = jdbc.allUsersWithFavNums(post_id);
        if (post_id == null){
            showRandomPost(count);
            return;
        }
        else {
            //String best_match_post_id = calcFavNum(post_id, Integer.parseInt(count), resultSet);
            //showPost(best_match_post_id);
        }

    }

    private void showPost(String best_match_post_id) {
        //show a post by post id
    }

    private String calcFavNum(String post_id, ResultSet allPosts, ResultSet allReactedUsers) {
        Map<String, Integer> currPostFavNum = new HashMap<>();
        fillInAllCurrPostFavNum(post_id, allReactedUsers, currPostFavNum);
        int l = currPostFavNum.size();
        double diff = Integer.MAX_VALUE;
        String best_match_post_id = "0";
        try {
            while (allPosts.next()){
                double curr_diff = 0;
                String curr_post_id = null;
                for (String curr_user_id : currPostFavNum.keySet()){
                    curr_post_id  = allPosts.getString("post_id");
                    double curr_fav = jdbc.getFavNum(curr_post_id, curr_user_id);
                    double this_user_diff = Math.abs(curr_fav - currPostFavNum.get(curr_user_id));
                    curr_diff += this_user_diff;
                }
               if (curr_diff < diff){
                  diff = curr_diff;
                  best_match_post_id = curr_post_id;
               }
            }
        }
       catch (Exception e){
           System.out.println("in calc fav num");
           System.out.println(e);
           e.printStackTrace();
       }

        return best_match_post_id;
    }

    private void fillInAllCurrPostFavNum(String post_id, ResultSet resultSet, Map<String, Integer> currPostFavNum) {
        try {
             while (resultSet.next()) {
                 currPostFavNum.put(resultSet.getString("user_id"), Integer.parseInt(resultSet.getString("fav_num")));
             }
        }
        catch (Exception e){
            System.out.println("error in fillAllCurrPostFavNum");
            System.out.println(e);
        }
    }

    private void showRandomPost(String count) {

        Random random = new Random();
        try{
            Integer rand = random.nextInt(Integer.parseInt(count));
            ResultSet resultSet = jdbc.getBusinessPostByPostID();
            //showPost() with id rand
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
    private String getRandomLikedPost(String user_id) {
        return null;
    }
}
