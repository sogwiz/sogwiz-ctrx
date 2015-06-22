# sogwiz-ctrx
Example test framework using java, webdriver, testng, and maven

Let's just dive right into the code
-----
```
@Test (dataProvider = "webinarCombinations")
	void testAScheduleWebinar(String name, String description) throws Exception{
		app.loginPage().login(configDataReader.getDefaultUsername(), configDataReader.getDefaultPassword());
		
		Date d = AppUtils.getDatePlusDays(configDataReader.getDaysOffset());
		
		String webinarId = app.homePage().scheduleNewWebinar(name, description, d);
		System.out.println("\nCreated webinar with internal id " + webinarId + " \n");
		app.homePage().navigateToMyWebinars();
		
		//format of webinar on My Webinars page is webinar-<webinar_id>
		//call verify webinar with the name, description, date, and id
		Assert.assertTrue(app.homePage().verifyWebinarCreated(name, description, d, webinarId), "webinar should have valid values");
	}
```

So ummm, what's this all do? Let's look line by line!
```app.loginPage().login(configDataReader.getDefaultUsername(), configDataReader.getDefaultPassword());```
* *app.loginPage()* : app is an object that acts as both an entrypoint to the app under test and as a page factory that provides tests with access to the page objects. The app object is instantiated in the @BeforeMethod of the BaseTest and takes in the WebDriver object as its constructor parameter
* *configDataReader.getDefaultUsername()* : configDataReader is a glorified properties reader that grabs property values from the command line (via maven) and from the config/testdata.properties file.  The -Dusername and -Dpassword values must be supplied for the tests to work
```mvn clean install -Dusername=YOUR_USER -Dpassword=YOUR_PASSWORD ....```

```Date d = AppUtils.getDatePlusDays(configDataReader.getDaysOffset());```
* *getDaysOffset()* : This method grabs a configurable field that lets the framework know how many days in advance to schedule the webinar.  It is currently set to 3.  This value is important as it is used by the framework in determining whether or not rollover to the next month has occurred.  This becomes important when using the calendar widget to choose the date of the webinar

```String webinarId = app.homePage().scheduleNewWebinar(name, description, d);```
* *homePage().scheduleNewWebinar(...)* : Ok, so this is where most of the magic happens. The homePage extends the BasePageObject class and provides most of the navigational logic for webinar creation. It also provides the verification logic for verying the correctness / integrity of a previously created webinar

