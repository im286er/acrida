package net.gility.acrida.config;

import net.gility.acrida.content.User;
import net.gility.acrida.utils.CyptoUtils;
import net.gility.acrida.storage.ConfigStoreManager;

import java.util.HashMap;

/**
 * User config
 *
 * @author Alimy
 * @version Created by Alimy on 10/30/15.
 */
public class UserConfig {
    private int loginUid;
    private boolean isLogined;

    /**
     * 保存登录信息
     *
     * @param user 用户信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final User user) {
        this.loginUid = user.getId();
        this.isLogined = true;
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("user.uid", String.valueOf(user.getId()));
        userInfo.put("user.name", user.getName());
        userInfo.put("user.face", user.getPortrait());// 用户头像-文件名
        userInfo.put("user.account", user.getAccount());
        userInfo.put("user.pwd", CyptoUtils.encode("oschinaApp", user.getPwd()));
        userInfo.put("user.location", user.getLocation());
        userInfo.put("user.followers", String.valueOf(user.getFollowers()));
        userInfo.put("user.fans", String.valueOf(user.getFans()));
        userInfo.put("user.score", String.valueOf(user.getScore()));
        userInfo.put("user.favoritecount", String.valueOf(user.getFavoritecount()));
        userInfo.put("user.gender", String.valueOf(user.getGender()));
        userInfo.put("user.isRememberMe", String.valueOf(user.isRememberMe()));// 是否记住我的信息

        ConfigStoreManager.setProperties(userInfo);
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final User user) {
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("user.name", user.getName());
        userInfo.put("user.face", user.getPortrait());// 用户头像-文件名
        userInfo.put("user.followers", String.valueOf(user.getFollowers()));
        userInfo.put("user.fans", String.valueOf(user.getFans()));
        userInfo.put("user.score", String.valueOf(user.getScore()));
        userInfo.put("user.favoritecount", String.valueOf(user.getFavoritecount()));
        userInfo.put("user.gender", String.valueOf(user.getGender()));

        ConfigStoreManager.setProperties(userInfo);
    }

    /**
     * 获得登录用户的信息
     *
     * @return
     */
    public User getLoginUser() {
        User user = new User();
        user.setId(ConfigStoreManager.getInt("user.uid"));
        user.setName(ConfigStoreManager.getProperty("user.name"));
        user.setPortrait(ConfigStoreManager.getProperty("user.face"));
        user.setAccount(ConfigStoreManager.getProperty("user.account"));
        user.setLocation(ConfigStoreManager.getProperty("user.location"));
        user.setFollowers(ConfigStoreManager.getInt("user.followers"));
        user.setFans(ConfigStoreManager.getInt("user.fans"));
        user.setScore(ConfigStoreManager.getInt("user.score"));
        user.setFavoritecount(ConfigStoreManager.getInt("user.favoritecount"));
        user.setRememberMe(ConfigStoreManager.getBoolean("user.isRememberMe"));
        user.setGender(ConfigStoreManager.getProperty("user.gender"));
        return user;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = 0;
        this.isLogined = false;
        ConfigStoreManager.removeProperties("user.uid", "user.name", "user.face", "user.location",
                "user.followers", "user.fans", "user.score",
                "user.isRememberMe", "user.gender", "user.favoritecount");
    }

    public int getLoginUid() {
        return loginUid;
    }

    public boolean isLogined() {
        return isLogined;
    }
}
