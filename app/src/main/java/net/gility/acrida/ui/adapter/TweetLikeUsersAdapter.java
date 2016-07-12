package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.User;
import net.gility.acrida.ui.cell.TweetLikeUserCell;

/**
 * TweetLikeUsersAdapter.java
 * 
 * @author 火蚁(http://my.oschina.net/u/253900)
 *
 * @author Alimy
 *
 * @data 2015-3-26 下午4:11:25
 */
public class TweetLikeUsersAdapter extends BaseCellAdapter<User, TweetLikeUserCell> {

	@Override
	protected int getLayoutId() {
		return R.layout.cell_tweet_like_user;
	}
}

