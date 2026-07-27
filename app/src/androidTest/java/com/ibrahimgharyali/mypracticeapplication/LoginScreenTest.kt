package com.ibrahimgharyali.mypracticeapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.ibrahimgharyali.mypracticeapplication.presentation.LoginScreen
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule 
    val composeTestRule = createComposeRule()

    @Test
    fun LoginScreen_shows_error_when_invalid_email_input() {
        composeTestRule.setContent {
            LoginScreen(
                emailError = "Invalid email",
                passwordError = null,
                performClick = { _, _ -> })
        }
        composeTestRule.onNodeWithText("Invalid email").assertIsDisplayed()

    }
}
