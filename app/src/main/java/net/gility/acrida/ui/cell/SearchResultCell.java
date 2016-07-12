package net.gility.acrida.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import net.gility.acrida.content.SearchResult;
import net.gility.acrida.utils.StringUtils;

/**
 * @author Alimy
 */

public class SearchResultCell extends SummaryCell<SearchResult> {
    public SearchResultCell(Context context) {
        super(context);
    }

    public SearchResultCell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchResultCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindTo(SearchResult result) {
        if (result.getDescription() == null || StringUtils.isEmpty(result.getDescription())) {
            description.setVisibility(View.GONE);
        } else {
            description.setVisibility(View.VISIBLE);
            description.setText(result.getDescription());
        }

        if (!StringUtils.isEmpty(result.getAuthor())) {
            source.setText(result.getAuthor());
        } else {
            source.setVisibility(View.GONE);
        }

        if (!StringUtils.isEmpty(result.getPubDate())) {
            time.setText(StringUtils.friendly_time(result.getPubDate()));
        } else {
            time.setVisibility(View.GONE);
        }

        tip.setVisibility(View.GONE);
        comment_count.setVisibility(View.GONE);
    }
}
