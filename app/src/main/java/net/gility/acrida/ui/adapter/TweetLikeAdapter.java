package net.gility.acrida.ui.adapter;

import net.gility.acrida.R;
import net.gility.acrida.content.TweetLike;
import net.gility.acrida.ui.cell.TweetLikeCell;

/**
 * 动弹点赞适配器 TweetLikeAdapter.java
 *
 * @author 火蚁(http://my.oschina.net/u/253900)
 * @author Alimy
 * @data 2015-4-10 上午10:19:19
 */
public class TweetLikeAdapter extends BaseCellAdapter<TweetLike, TweetLikeCell> {

    @Override
    protected int getLayoutId() {
        return R.layout.cell_tweet_like;
    }
}
