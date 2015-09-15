package com.todo.behtarinhotel;

import com.todo.behtarinhotel.simpleobjects.BookedRoomSO;
import com.todo.behtarinhotel.simpleobjects.UserSO;
import com.todo.behtarinhotel.supportclasses.AppState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class AppStateTest {

    LauncherActivity launcherActivity;

    @Before
    public void setUp(){
        launcherActivity = Robolectric.buildActivity(LauncherActivity.class).create().start().resume().visible().get();

        assertNotNull(launcherActivity);

    }

    @Test
    public void testUserLifecycle() {
        final String firstName = "FirstN";
        final String lastName = "LastN";
        final int userId = 5;
        final String email = "FirstN@gmail.com";
        final String password = "password";
        final String username = "Username";
        final String encodingKey = "encodingKey";
        UserSO testUser = new UserSO(firstName, lastName, userId, email, password, username, encodingKey);

        //Trying to log in
        AppState.userLoggedIn(testUser);
        assertTrue(AppState.isUserLoggedIn());
        //We logged in, getting user
        UserSO userFromAppState = AppState.getLoggedUser();
        assertEquals(firstName, userFromAppState.getFirstName());
        assertEquals(lastName, userFromAppState.getLastName());
        assertEquals(userId, userFromAppState.getUserID());
        assertEquals(email, userFromAppState.getEmail());
        assertEquals(password, userFromAppState.getPassword());
        assertEquals(username, userFromAppState.getUsername());
        //User correct, logout
        AppState.userLoggedOut();
        userFromAppState = AppState.getLoggedUser();
        assertNull(userFromAppState);
        //We logged out
    }

    @Test
    public void testWishlistLifecycle(){
        int testHotelId = 10;
        try{
            AppState.userLoggedIn(generateTestUser());
            AppState.addToWishList(testHotelId);
            assertNotNull(AppState.getWishList());
            assertTrue(AppState.isInWishList(testHotelId));
            AppState.removeFromWishList(testHotelId);
            assertFalse(AppState.isInWishList(testHotelId));
            assertNull(AppState.getWishList());

            //Calling "not expected" methods
            //Removing already removed hotel
            AppState.removeFromWishList(testHotelId);
            //Adding hotel twice
            AppState.addToWishList(testHotelId);
            AppState.addToWishList(testHotelId);

        }finally {
            //Clear wishlist
            AppState.clearWishlist();
            assertNull(AppState.getWishList());
        }
    }

    @Test
    public void testBookedRoomsLifecycle(){
        ArrayList<BookedRoomSO> bookedRooms = generateTestBookedRoomsData();
        try{
            assertEquals(AppState.getBookedRooms().size(), 0);
            AppState.saveBookedRoom(bookedRooms);
            assertEquals(AppState.getBookedRooms().size(), 10);

        }finally {
            AppState.clearBookedRooms();
            assertEquals(AppState.getBookedRooms().size(), 0);
        }
    }

    @Test
    public void testHistoryLifecycle(){
        ArrayList<BookedRoomSO> bookedRooms = generateTestBookedRoomsData();
        try{
            assertEquals(AppState.getHistory().size(), 0);
            AppState.addToHistory(bookedRooms);
            assertEquals(AppState.getHistory().size(), 10);
            AppState.changeRoomHistoryState(bookedRooms.get(3), BookedRoomSO.CANCELLED);
            assertEquals(AppState.getHistory().get(3).getOrderState(), BookedRoomSO.CANCELLED);

        }finally {
            AppState.clearHistory();
            assertEquals(AppState.getHistory().size(), 0);
        }
    }

    private ArrayList<BookedRoomSO> generateTestBookedRoomsData(){
        //Init test data
        String arrivalDate = "09/11/2015";
        String departureDate = "09/11/2015";
        String hotelAddress = "hotel address";
        String hotelName = "hotel name";
        String roomDescription = "room description";
        String cancellationPolicy = "cancellation policy";
        String photoUrl = "www.google.com";
        String roomPrice = "999.99";
        ArrayList<String> valueAdds = new ArrayList<>();
        valueAdds.add("12");
        int orderState = 1;
        String email = "email@gmail.com";
        float sumPrice = 99.99f;
        String currency = "USD";
        int nights = 3;
        String firstName = "FirstName";
        String lastName = "LastName";
        String smokingPreference = "NS";
        int bedType = 14;
        int adult = 3;
        int[] children = {5, 8};

        ArrayList<BookedRoomSO> testBookedRooms = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            BookedRoomSO bookedRoomSO = new BookedRoomSO();
            bookedRoomSO.setArrivalDate(arrivalDate);
            bookedRoomSO.setDepartureDate(departureDate);
            bookedRoomSO.setHotelAddress(hotelAddress);
            bookedRoomSO.setHotelName(hotelName);
            bookedRoomSO.setRoomDescription(roomDescription);
            bookedRoomSO.setCancellationPolicy(cancellationPolicy);
            bookedRoomSO.setPhotoUrl(photoUrl);
            bookedRoomSO.setRoomPrice(roomPrice);
            bookedRoomSO.setItineraryId(i);
            bookedRoomSO.setValueAdds(valueAdds);
            bookedRoomSO.setOrderState(orderState);
            bookedRoomSO.setConfirmationNumber(i);
            bookedRoomSO.setEmail(email);
            bookedRoomSO.setSumPrice(sumPrice);
            bookedRoomSO.setCurrency(currency);
            bookedRoomSO.setNights(nights);
            bookedRoomSO.setUserID(i);
            bookedRoomSO.setHotelID(i);
            bookedRoomSO.setFirstName(firstName);
            bookedRoomSO.setLastName(lastName);
            bookedRoomSO.setSmokingPreference(smokingPreference);
            bookedRoomSO.setBedType(bedType);
            bookedRoomSO.setAdult(adult);
            bookedRoomSO.setChildren(children);

            testBookedRooms.add(bookedRoomSO);

        }
        return testBookedRooms;
    }

    private UserSO generateTestUser(){
        final String firstName = "FirstN";
        final String lastName = "LastN";
        final int userId = 5;
        final String email = "FirstN@gmail.com";
        final String password = "password";
        final String username = "Username";
        final String encodingKey = "encodingKey";
        return new UserSO(firstName, lastName, userId, email, password, username, encodingKey);
    }



}