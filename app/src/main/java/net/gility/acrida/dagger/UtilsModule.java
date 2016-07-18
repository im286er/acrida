package net.gility.acrida.dagger;

import net.gility.acrida.android.ApplicationLoader;
import net.gility.acrida.manager.AppManager;
import net.gility.acrida.ui.help.DoubleClickExitHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Alimy
 * Created by Michael Li on 7/18/16.
 */

@Module(
        includes = {
                AppModule.class
        }
)
public class UtilsModule {
    @Provides
    DoubleClickExitHelper provideDoubleClickExitHelper(ApplicationLoader applicationLoader) {
        return new DoubleClickExitHelper(applicationLoader.getApplicationContext());
    }
}
