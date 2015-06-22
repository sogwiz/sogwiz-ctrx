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
* *app.loginPage()* : app is an object that acts as both an entrypoint to the app under test and as a page factory that provides tests with access to the page objects. The app object is instantiated in the @BeforeMethod of the BaseTest and takes in the WebDriver object as its constructor parameter.  This becomes handy when we want to run tests concurrently in parallel - we don't share WebDriver objects so no clobbering of shared resources will occur.
* *configDataReader.getDefaultUsername()* : configDataReader is a glorified properties reader that grabs property values from the command line (via maven) and from the config/testdata.properties file.  The -Dusername and -Dpassword values must be supplied for the tests to work
```mvn clean install -Dusername=YOUR_USER -Dpassword=YOUR_PASSWORD ....```

```Date d = AppUtils.getDatePlusDays(configDataReader.getDaysOffset());```
* *getDaysOffset()* : This method grabs a configurable field that lets the framework know how many days in advance to schedule the webinar.  It is currently set to 3.  This value is important as it is used by the framework in determining whether or not rollover to the next month has occurred.  This becomes important when using the calendar widget to choose the date of the webinar

```String webinarId = app.homePage().scheduleNewWebinar(name, description, d);```
* *homePage().scheduleNewWebinar(...)* : Ok, so this is where most of the magic happens. The homePage extends the BasePageObject class and provides most of the navigational logic for webinar creation. It also provides the verification logic for verying the correctness / integrity of a previously created webinar

Ok ok, I got it! How do I run the tests?
=====
```
mvn clean install -Dusername=YOUR_USERNAME -Dpassword=YOUR_PASSWORD -Ddriver_path=~/Downloads/chromedriver
```
* The -Dusername and -Dpassword are required parameters.
* The -Ddriver_path points to the location of the chromedriver on your machine. If you choose not to provide this value, it will default to the value set config/testdata.properties
* NOTE: be sure to cd into the directory of the pom.xml file when executing the maven command

Test Output
-----
The output of the tests is written to sogwiz-ctrx/target/surefire-reports

Architecture
-----
* Page Object Model : the framework uses page objects to both provide the navigational facilities for the tests to traverse through the app and the framework provides verification methods that the tests can call assertions on
* Config Driven: the framework is driven by maven command line options that supercede options set in the config/testdata.properties file. You can even define a clientType (eg chrome, firefox, safari)
* Readable Tests: I wanted to make the tests as readable as possible

Limitations
-----
* No separation of page objects with UI element locators: Ideally, the page objects should only be responsible for the navigational actions. They shouldn't be responsible for knowing HOW to access the UI elements. The UI element locators should be defined in a separate data model so as to separate the navigational concerns with the locator details.  Hence, I'd use an xml, json, or even a key value format to store this date (eg xpath, id, css).
* Minimal verifications: Though the framework handles month rollovers just fine, it doesn't verify certain date time components like selection of the AM / PM fields. 
* Testing : At the very minimum, I'd write positive, negative, and boundary tests for each feature. Features include login, cookie management, session handling, input validations, timezone discrepancies, and so much more.
| Feature / Component | 3 Positive Tests | 2 Negative Tests | 2 Boundary Tests |

