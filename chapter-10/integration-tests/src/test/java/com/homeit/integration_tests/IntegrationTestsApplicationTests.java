package com.homeit.integration_tests;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class IntegrationTestsApplicationTests {

	@Test
	void endToEndFlow() {
		CreatedUser createdLandlord = createUserAndToken("landlord");
		CreatedUser createdTenant = createUserAndToken("tenant");
		CreatedProperty createdProperty = landlordCreatesProperty(createdLandlord);

		tenantRetrievesProperty(createdProperty.createdPropertyResponse(), createdTenant.userTokenResponse());
		tenantFailsAtCreatingProperty(createdTenant);
		verifyLandlordTokenIsNotRevoked(createdLandlord.userAccessToken());
		revokeLandlordToken(createdLandlord.userAccessToken());
		landlordFailsAtCreatingProperty(createdLandlord.userId(), createdLandlord.userAccessToken());
	}

	private CreatedUser createUserAndToken(String userType) {
		// First request to localhost:8081
		RestAssured.baseURI = "http://localhost/users";
		RestAssured.port = 8081;
		System.out.println("======================== CREATES USER OF TYPE: " + userType);
		ExtractableResponse<Response> createdUserResponse = createUser(userType);
        ExtractableResponse<Response> userTokenResponse = createToken(createdUserResponse.path("email"));
        return new CreatedUser(createdUserResponse, userTokenResponse);
	}

	private record CreatedUser(ExtractableResponse<Response> userIdResponse, ExtractableResponse<Response> userTokenResponse) {
		String userAccessToken() {
			return userTokenResponse.path("access_token");
		}
		String userId() {
			return userIdResponse.path("id");
		}
	}

	private CreatedProperty landlordCreatesProperty(CreatedUser createdLandlord) {
		System.out.println("======================== LANDLORD CREATES PROPERTY");
		RestAssured.baseURI = "http://localhost/";
		RestAssured.port = 8080;
		ExtractableResponse<Response> createdProperty = createProperty("John Doe Landlord", createdLandlord.userId(),createdLandlord.userAccessToken());
        return new CreatedProperty(createdProperty);
	}

	private record CreatedProperty(ExtractableResponse<Response> createdPropertyResponse) {
	}


	private void tenantRetrievesProperty(ExtractableResponse<Response> createdProperty, ExtractableResponse<Response> tenantTokenResponse) {
		System.out.println("======================== TENANT RETRIEVES PROPERTY");
		ExtractableResponse<Response> retrievedProperty = getProperty(createdProperty.path("id"), tenantTokenResponse.path("access_token"));
	}

	private void tenantFailsAtCreatingProperty(CreatedUser createdTenant) {
		System.out.println("======================== TENANT FAILS AT CREATING PROPERTY");
		failsWhenCreatingProperty("John Doe Landlord", createdTenant.userId(), createdTenant.userAccessToken());
	}

	private void verifyLandlordTokenIsNotRevoked(String landlordToken) {
		System.out.println("======================== ENSURES LANDLORD TOKEN IS NOT REVOKED");
		RestAssured.baseURI = "http://localhost/";
		RestAssured.port = 8082;
		nonRevokedToken(landlordToken);
	}

	private void revokeLandlordToken(String landlordToken) {
		System.out.println("======================== REVOKE LANDLORD TOKEN");
		RestAssured.baseURI = "http://localhost/";
		RestAssured.port = 8082;
		ExtractableResponse<Response> revokedTokenResponse = revokeToken(landlordToken);
	}

	private void landlordFailsAtCreatingProperty(String landlordId, String landlordToken) {
		System.out.println("======================== LANDLORD FAILS AT CREATING PROPERTY");
		RestAssured.baseURI = "http://localhost/";
		RestAssured.port = 8080;
		failsWhenCreatingProperty("John Doe Landlord", landlordId, landlordToken);
	}


	private ExtractableResponse<Response> createUser(String userType) {
		return given()
				.contentType("application/json")
				.body(userPayload(userType)).
			when()
				.log().all()
				.post("/register").
			then()
				.log().all()
				.body("id", notNullValue())
				.body("email", endsWith("user@example.com"))
				.body("user_type", equalTo(userType))
				.extract();
	}

	private ExtractableResponse<Response> revokeToken(String token) {
		return given()
				.header("Authorization", "Basic bXlBcHBJZDpteVNlY3JldA==") // Add the Bearer token here
				.when()
				.log().all()
				.post("/api/revoke-tokens?token="+token).
				then()
				.log().all()
				.body("revoked", equalTo(token))
				.extract();
	}

	private void nonRevokedToken(String token) {
		given()
			.header("Authorization", "Basic bXlBcHBJZDpteVNlY3JldA==") // Add the Bearer token here
			.when()
			.log().all()
			.get("/api/revoke-tokens?token="+token).
			then()
			.log().all()
			.statusCode(is(204));
	}

	private ExtractableResponse<Response> createToken(String email) {
		return given()
				.contentType("application/json")
				.body(tokenPayload(email, "userpass")).
				when()
				.log().all()
				.post("/token").
				then()
				.log().all()
				.body("access_token", notNullValue())
				.body("token_type", equalTo("Bearer"))
				.body("expires_in", equalTo("3600"))
				.extract();
	}

	private ExtractableResponse<Response> createProperty(String landlordName, String landlordId, String landlordToken) {
		return given()
				.contentType("application/json")
				.body(createPropertyPayload(landlordName, landlordId)).
				header("Authorization", "Bearer " + landlordToken) // Add the Bearer token here
				.when()
				.log().all()
				.post("/api/v1/rental-properties").
				then()
				.log().all()
				.body("landlordID", equalTo(landlordId))
				.body("name", equalTo(landlordName))
				.body("address", equalTo("Example st 123"))
				.body("city", equalTo("Strange City"))
				.body("country", equalTo("US"))
				.body("zipCode", equalTo("11111"))
				.body("rent", equalTo(133.33F))
				.extract();
	}

	private void failsWhenCreatingProperty(String landlordName, String landlordId, String landlordToken) {
		given()
				.contentType("application/json")
				.body(createPropertyPayload(landlordName, landlordId)).
				header("Authorization", "Bearer " + landlordToken) // Add the Bearer token here
				.when()
				.log().all()
				.post("/api/v1/rental-properties").
				then()
				.log().all()
				.statusCode(equalTo(401));
	}

	private ExtractableResponse<Response> getProperty(String propertyId, String tenantToken) {
		return given()
				.contentType("application/json")
				.header("Authorization", "Bearer " + tenantToken) // Add the Bearer token here
				.when()
				.log().all()
				.get("/api/v1/rental-properties/" + propertyId).
				then()
				.log().all()
				.body("landlordID", notNullValue())
				.body("name", equalTo("John Doe Landlord"))
				.body("address", equalTo("Example st 123"))
				.body("city", equalTo("Strange City"))
				.body("country", equalTo("US"))
				.body("zipCode", equalTo("11111"))
				.body("rent", equalTo(133.33F))
				.extract();
	}

	private static Map<String, String> createPropertyPayload(String landLordName, String landlordId) {
		return Map.of("landlordID", landlordId,
				"name", landLordName,
				"address", "Example st 123",
				"city", "Strange City",
				"country", "US",
				"zipCode", "11111",
				"rent", "133.33");
	}

	private static Map<String, String> tokenPayload(String username, String password) {
		return Map.of("grant_type", "password",
				"username", username,
				"password", password,
				"client_id", "4ca8f880-0bee-4c24-88ce-3402fe7e37f0",
				"client_secret", "b08cee2b-e79f-472b-a0b9-b210465c8bf3");
	}

	private static Map<String, String> userPayload(String userType) {
		return Map.of("email", new Date().getTime() + "_user@example.com",
			"password", "userpass",
			"user_type", userType);
	}

}
