package com.todo.behtarinhotel;

import com.android.volley.Response;
import com.todo.behtarinhotel.supportclasses.DataLoader;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

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

        System.out.println("Start getting hotel images");
        Response.Listener<JSONObject> listener = mock(Response.Listener.class);
        Response.ErrorListener errorListener = mock(Response.ErrorListener.class);
        DataLoader.makeRequest(DataLoader.getHotelImagesUrl(189771), listener, errorListener);
        System.out.println("Request sent");
    }



}
