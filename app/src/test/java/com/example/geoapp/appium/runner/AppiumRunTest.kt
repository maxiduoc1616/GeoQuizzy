package com.example.geoapp.appium.runner

import io.cucumber.testng.AbstractTestNGCucumberTests
import io.cucumber.testng.CucumberOptions

@CucumberOptions(
    features = ["classpath:features"],
    glue = ["com.example.geoapp.appium.steps"],
    plugin = ["pretty", "html:target/cucumber-reports.html", "json:target/cucumber.json"],
    monochrome = true
)
class AppiumRunTest : AbstractTestNGCucumberTests() {
    @org.testng.annotations.Test
    fun placeholder() {
        // Keeps the class active for Gradle detection
        org.testng.Assert.assertTrue(true)
    }
}
