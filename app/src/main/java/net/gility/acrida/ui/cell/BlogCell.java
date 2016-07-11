package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import net.gility.acrida.R;
import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.Blog;
import net.gility.acrida.content.BlogList;
import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.utils.ThemeSwitchUtils;

/**
 * @author Alimy
 */

public class BlogCell extends SummaryCell<Blog> {
    public BlogCell(Context context) {
        super(context);
    }

    public BlogCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlogCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(Blog blog) {
        tip.setVisibility(View.VISIBLE);
        if (blog.getDocumenttype() == Blog.DOC_TYPE_ORIGINAL) {
            tip.setImageResource(R.drawable.widget_original_icon);
        } else {
            tip.setImageResource(R.drawable.widget_repaste_icon);
        }

        title.setText(blog.getTitle());

        if (ApplicationLoader.isOnReadedPostList(BlogList.PREF_READED_BLOG_LIST, blog.getId() + "")) {
            title.setTextColor(getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleReadedColor()));
        } else {
            title.setTextColor(getContext().getResources()
                    .getColor(ThemeSwitchUtils.getTitleUnReadedColor()));
        }

        description.setVisibility(View.GONE);
        String description = blog.getBody();
        if (null != description && !StringUtils.isEmpty(description)) {
            this.description.setVisibility(View.VISIBLE);
            this.description.setText(description.trim());
        }

        source.setText(blog.getAuthor());
        time.setText(StringUtils.friendly_time(blog.getPubDate()));
        comment_count.setText(blog.getCommentCount() + "");
    }
}

