package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import net.gility.acrida.R;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.Post;
import net.gility.acrida.content.PostList;
import net.gility.acrida.ui.widget.AvatarView;
import net.gility.acrida.utils.HTMLUtil;
import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.utils.ThemeSwitchUtils;

import butterknife.BindView;

/**
 * @author Alimy
 */

public class PostCell extends RelativeCell<Post> {
    @BindView(R.id.tv_title) TextView title;
    @BindView(R.id.tv_description) TextView description;
    @BindView(R.id.tv_author) TextView author;
    @BindView(R.id.tv_date) TextView time;
    @BindView(R.id.tv_count) TextView comment_count;
    @BindView(R.id.iv_face) public AvatarView face;

    public PostCell(Context context) {
        super(context);
    }

    public PostCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(Post post) {
        face.setUserInfo(post.getAuthorId(), post.getAuthor());
        face.setAvatarUrl(post.getPortrait());
        title.setText(post.getTitle());
        String body = post.getBody();
        description.setVisibility(View.GONE);
        if (null != body || !StringUtils.isEmpty(body)) {
            description.setVisibility(View.VISIBLE);
            description.setText(HTMLUtil.replaceTag(post.getBody()).trim());
        }

        if (ApplicationLoader.isOnReadedPostList(PostList.PREF_READED_POST_LIST,
                post.getId() + "")) {
            title.setTextColor(getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleReadedColor()));
        } else {
            title.setTextColor(getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleUnReadedColor()));
        }

        author.setText(post.getAuthor());
        time.setText(StringUtils.friendly_time(post.getPubDate()));
        comment_count.setText(post.getAnswerCount() + "回 / " + post.getViewCount() + "阅");
    }
}
