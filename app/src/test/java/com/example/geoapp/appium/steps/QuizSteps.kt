package com.example.geoapp.appium.steps

import io.appium.java_client.AppiumBy
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.options.UiAutomator2Options
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.testng.Assert
import java.net.URL
import java.time.Duration

class QuizSteps {

    private var driver: AndroidDriver? = null

    @Before
    fun setUp() {
        // Configuración de Capabilities
        val options = UiAutomator2Options()
            .setPlatformName("Android")
            .setAutomationName("UiAutomator2")
            //.setDeviceName("Android Emulator")
            .setAppPackage("com.example.geoapp")
            .setAppActivity("com.example.geoapp.LoginActivity")
            .setNoReset(false) // Limpiar estado entre tests para evitar inconsistencias

        // Conectar al servidor Appium local
        driver = AndroidDriver(URL("http://127.0.0.1:4723"), options)
        driver?.manage()?.timeouts()?.implicitlyWait(Duration.ofSeconds(10))
    }


    @After
    fun tearDown() {
        driver?.quit()
    }

    @Given("The app is open")
    fun theAppIsOpen() {
        Assert.assertNotNull(driver)
        
        // Manejo de Login
        try {
            // Buscamos si estamos en la pantalla de Login (buscando el botón de debug)
            val debugLoginBtn = driver?.findElement(AppiumBy.id("com.example.geoapp:id/btn_debug_login"))
            debugLoginBtn?.click()
        } catch (e: Exception) {
            // Si no encuentra el botón, asumimos que ya estamos logueados o en otra pantalla
            // Continuamos
        }
    }

    @When("I tap {string}")
    fun iTap(text: String) {
        val viewId = when (text) {
            "Start Quiz" -> "com.example.geoapp:id/btn_start_quiz"
            "Normal (3 Vidas)" -> "com.example.geoapp:id/btn_medium"
            "Easy (5 Vidas)" -> "com.example.geoapp:id/btn_easy"
            "Hard (1 Vida)" -> "com.example.geoapp:id/btn_hard"
            else -> null // Fallback to text search if not expecting a specific ID
        }

        if (viewId != null) {
             driver?.findElement(AppiumBy.id(viewId))?.click()
        } else {
             // Fallback: This is brittle if language changes!
             driver?.findElement(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"$text\")"))?.click()
        }
    }

    @Then("I should see the Quiz screen")
    fun iShouldSeeTheQuizScreen() {
        // Verificamos algún elemento único del Quiz, ej: tv_question_counter
        val element = driver?.findElement(AppiumBy.id("com.example.geoapp:id/tv_question_counter"))
        Assert.assertNotNull(element)
    }

    @Then("I should see {int} hearts")
    fun iShouldSeeHearts(count: Int) {
        val heartsText = driver?.findElement(AppiumBy.id("com.example.geoapp:id/tv_lives"))?.text
        val expected = "❤".repeat(count)
        Assert.assertEquals(heartsText, expected)
    }

    @When("I wait for the quiz to start")
    fun iWaitForQuizToStart() {
        val questionText = driver?.findElement(AppiumBy.id("com.example.geoapp:id/tv_question"))
        Assert.assertNotNull(questionText)
    }

    @When("I tap the first answer option")
    fun iTapFirstOption() {
        val btnOption1 = driver?.findElement(AppiumBy.id("com.example.geoapp:id/btn_option1"))
        btnOption1?.click()
    }

    @Then("the answer options should become disabled")
    fun optionsShouldBeDisabled() {
        // En QuizActivity.kt, checkAnswer llama a enableButtons(false) inmediatamente.
        val btnOption1 = driver?.findElement(AppiumBy.id("com.example.geoapp:id/btn_option1"))
        val isEnabled = btnOption1?.isEnabled == true
        Assert.assertFalse(isEnabled, "El botón debería estar deshabilitado después de responder")
    }
}
