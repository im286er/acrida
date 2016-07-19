package net.gility.acrida.dagger;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.android.BaseApplication;
import net.gility.acrida.ui.AppBaseActivity;
import net.gility.acrida.ui.MainActivity;
import net.gility.acrida.ui.fragment.InjectListFragment;
import net.gility.acrida.ui.fragment.RtfBlogFragment;
import net.gility.acrida.ui.fragment.RtfNewsFragment;
import net.gility.acrida.ui.fragment.SettingsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Alimy
 * Created by Michael Li on 7/17/16.
 */

@Singleton
@Component(
        modules = {
                AppModule.class,
                ApiModule.class,
                DataModule.class,
                UiModule.class,
                BindModule.class,
                UtilsModule.class
        }
)
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(ApplicationLoader applicationLoader);
    void inject(BaseApplication baseApplication);
    void inject(SettingsFragment settingsFragment);
    void inject(RtfNewsFragment rtfNewsFragment);
    void inject(RtfBlogFragment rtfBlogFragment);
}
