package net.gility.acrida.dagger;

import java.security.SecureRandom;
import java.util.Random;

import dagger.Binds;
import dagger.Module;

/**
 * @author Alimy
 * Created by Michael Li on 7/17/16.
 */

@Module
public interface BindModule {

    @Binds Random bindRandom(SecureRandom secureRandom);
}
