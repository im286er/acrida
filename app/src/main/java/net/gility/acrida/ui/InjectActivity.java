package net.gility.acrida.ui;

import butterknife.ButterKnife;

/**
 * @author Alimy
 * Created by Michael Li on 7/18/16.
 */

public abstract class InjectActivity extends AppBaseActivity {

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        ButterKnife.bind(this);
    }
}
