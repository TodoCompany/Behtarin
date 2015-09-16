package com.todo.behtarinhotel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Andriy on 9/15/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class RequestsTest {

    LauncherActivity launcherActivity;

    @Before
    public void setUp(){
        launcherActivity = Robolectric.buildActivity(LauncherActivity.class).create().start().resume().visible().get();
        assertNotNull(launcherActivity);

    }


    @Test
    public void testRequestHotelImages() {
    }



}
