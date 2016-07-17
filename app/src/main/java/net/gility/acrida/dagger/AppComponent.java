package net.gility.acrida.dagger;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.ui.MainActivity;

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
                UiModule.class
        }
)
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(ApplicationLoader applicationLoader);
}
