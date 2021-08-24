package com.example.taskmaster;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityRule = new ActivityScenarioRule<MainActivity>(MainActivity.class) ;


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.taskmaster", appContext.getPackageName());
    }

    @Test
    public void testChangingUsername(){
        //click settings button to go to the settings activity
        onView(withId(R.id.imageButtonSettings)).perform(click()) ;
        //type Ahmad in the user name input field
        onView(withId(R.id.editTextUsername)).perform(typeText("Ahmad") , closeSoftKeyboard()) ;
        //click on save button to save the username
        onView(withId(R.id.imageButtonSave)).perform(click());
        //click on home button to go to the main activity
        onView(withId(R.id.goHome)).perform(click());

        onView(withId(R.id.textViewUsernameTasks)).check(matches(withText("Ahmad"))) ;
    }

    @Test
    public void testUIElements(){
        onView(withId(R.id.buttonTasks)).check(matches(withText("TASKS")));
    }

//    @Test
//    public void testRecyclerView(){
//        //go to add task activity
//        onView(withId(R.id.buttonAddTask)).perform(click()) ;
//        //enter the task title
//        onView(withId(R.id.editTextTaskTitle)).perform(typeText("TestTask"));
//        //enter the task description
//        onView(withId(R.id.editTextTaskDescription)).perform(typeText("task from test espresso"));
//        //add the task
//        onView(withId(R.id.buttonAddTask)).perform(click());
////        MyNameActivity activity = activityTestRule.getActivity();
////        onView(withText(R.string.toast_text)).
////                inRoot(withDecorView(not(is(activity.getWindow().getDecorView())))).
////                check(matches(isDisplayed()));
//
//    }


}