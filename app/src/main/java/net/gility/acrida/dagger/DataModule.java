package net.gility.acrida.dagger;

import android.content.SharedPreferences;

import net.gility.acrida.android.ApplicationLoader;

import java.security.SecureRandom;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Alimy
 * Created by Michael Li on 7/17/16.
 */

@Module
public class DataModule {
    @Provides @Singleton
    SharedPreferences provideSharedPreferences() {
        return  ApplicationLoader.getPreferences();
    }

    @Provides
    SecureRandom provideSecureRandom() {
        return new SecureRandom();
    }
}
