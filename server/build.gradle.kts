plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    application
}

group = "dev.afalabarce.wordlechains.api"
version = "1.0.0"
application {
    mainClass.set("dev.afalabarce.wordlechains.api.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.datetime)
    implementation(libs.exposed.migration.r2dbc)
    implementation(libs.exposed.r2dbc)
    implementation(libs.exposed.spring.transaction)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.contentNegotiation)
    implementation(libs.ktor.cors)
    implementation(libs.ktor.openapi)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverResources)
    implementation(libs.ktor.swagger)
    implementation(libs.logback)
    implementation(libs.r2dbc.postgresql)
    implementation(libs.swagger.codegen)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.testJunit)
    testImplementation(libs.ktor.serverTestHost)
}