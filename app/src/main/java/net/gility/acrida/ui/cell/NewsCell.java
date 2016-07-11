package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.content.News;
import net.gility.acrida.content.NewsList;
import net.gility.acrida.utils.StringUtils;
import net.gility.acrida.utils.ThemeSwitchUtils;

/**
 * @author Alimy
 */

public class NewsCell extends SummaryCell<News> {
    public NewsCell(Context context) {
        super(context);
    }

    public NewsCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(News news) {
        title.setText(news.getTitle());

        if (ApplicationLoader.isOnReadedPostList(NewsList.PREF_READED_NEWS_LIST, news.getId() + "")) {
            title.setTextColor(getResources().getColor(ThemeSwitchUtils.getTitleReadedColor()));
        } else {
            title.setTextColor(getResources().getColor(ThemeSwitchUtils.getTitleUnReadedColor()));
        }

        String description = news.getBody();
        this.description.setVisibility(View.GONE);
        if (description != null && !StringUtils.isEmpty(description)) {
            this.description.setVisibility(View.VISIBLE);
            this.description.setText(description.trim());
        }

        this.source.setText(news.getAuthor());
        if (StringUtils.isToday(news.getPubDate())) {
            this.tip.setVisibility(View.VISIBLE);
        } else {
            this.tip.setVisibility(View.GONE);
        }
        this.time.setText(StringUtils.friendly_time(news.getPubDate()));
        this.comment_count.setText(news.getCommentCount() + "");
    }
}

